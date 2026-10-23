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

    public ProdutoImagemResponse toResponse(ProdutoImagemEntity entity) {
        return ProdutoImagemResponse.builder()
                .id(entity.getId())
                .produtoId(entity.getProdutoId())
                .imagemUrl(entity.getImagemUrl())
                .build();

    }

}
