package org.multikoisas.service.impl;

import lombok.RequiredArgsConstructor;
import org.multikoisas.dto.ProdutoImagemRequest;
import org.multikoisas.dto.ProdutoImagemResponse;
import org.multikoisas.entities.ProdutoImagemEntity;
import org.multikoisas.repository.ProdutoImagemRepository;
import org.multikoisas.service.ProdutoImagemService;
import org.multikoisas.service.S3StorageService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProdutoImagemServiceImpl implements ProdutoImagemService {

    private final ProdutoImagemRepository repository;
    private final S3StorageService s3Service; // Injeção da Interface, não da implementação S3!

    @Override
    @Transactional // Garante que se o banco falhar, o processo inteiro aborta
    public ProdutoImagemResponse save(ProdutoImagemRequest request) {

        // 1. Envia para a nuvem e recebe a URL (Responsabilidade do StorageService)
        String publicUrl = s3Service.uploadFile(request.getFile());

        // 2. Constrói a Entidade com os dados do Request + URL da nuvem
        ProdutoImagemEntity entity = new ProdutoImagemEntity();
        entity.setProdutoId(request.getProdutoId());
        entity.setImagemUrl(publicUrl);

        // 3. Salva no banco de dados (Responsabilidade do Repository)
        ProdutoImagemEntity savedEntity = repository.save(entity);

        // 4. Converte e devolve
        return toResponse(savedEntity);
    }

    @Override
    @Transactional
    public ProdutoImagemResponse delete(Integer id, Integer productId) {
        // 1. Busca a entidade no banco de dados
        ProdutoImagemEntity entity = repository.findById(id);

        // 2. Valida regras de negócio (DDD) - A imagem deve existir e pertencer ao produto
        if (entity == null || !entity.getProdutoId().equals(productId)) {
            throw new IllegalArgumentException("Imagem não encontrada ou não pertence a este produto.");
        }

        // 3. Apaga o arquivo físico na nuvem (Infraestrutura S3)
        s3Service.deleteFile(entity.getImagemUrl());

        // 4. Apaga o registro no banco de dados (Persistência)
        repository.deleteById(id);

        // 5. Retorna os dados da imagem deletada (ou poderia ser void, dependendo do design da sua API)
        return toResponse(entity);
    }

    @Override
    public List<ProdutoImagemResponse> findAll() {
        return repository.findAll().stream().map(this::toResponse).toList();
    }

    @Override
    public List<ProdutoImagemResponse> findByProdutoId(Integer produtoId) {
        return repository.findByProdutoId(produtoId).stream().map(this::toResponse).toList();
    }

    @Override
    @Transactional
    public void deleteAllByProdutoId(Integer produtoId) {
        // 1. Busca todas as imagens no banco de dados
        List<ProdutoImagemResponse> imagens = this.findByProdutoId(produtoId);

        // 2. Extrai apenas as URLs para a memória antes de deletar do banco
        List<String> urlsParaDeletar = imagens.stream()
                .map(ProdutoImagemResponse::getImagemUrl)
                .toList();

        // 3. Apaga os registros do banco de dados (Persistência)
        for (ProdutoImagemResponse imagem : imagens) {
            repository.deleteById(imagem.getId());
        }

        // 4. Registra a exclusão na nuvem para ocorrer SOMENTE APÓS o commit do banco
        TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {
            @Override
            public void afterCommit() {
                // Este bloco roda de forma isolada, apenas se a exclusão no banco for um sucesso.
                for (String url : urlsParaDeletar) {
                    try {
                        s3Service.deleteFile(url);
                    } catch (Exception e) {
                        // O banco já comitou com sucesso. Se o S3 falhar por rede, logamos o erro.
                        // O sistema se mantém íntegro, evitando reverter o banco por falha externa.
                        System.err.println("Aviso: Falha ao deletar arquivo órfão no S3: " + url);
                    }
                }
            }
        });
    }

    public ProdutoImagemResponse toResponse(ProdutoImagemEntity entity) {
        return ProdutoImagemResponse.builder()
                .id(entity.getId())
                .produtoId(entity.getProdutoId())
                .imagemUrl(entity.getImagemUrl())
                .build();

    }

}
