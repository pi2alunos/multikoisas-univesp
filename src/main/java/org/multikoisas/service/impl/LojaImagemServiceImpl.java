package org.multikoisas.service.impl;

import lombok.RequiredArgsConstructor;
import org.multikoisas.dto.LojaImagemResponse;
import org.multikoisas.entities.LojaImagemEntity;
import org.multikoisas.enums.LojaImagemTipo;
import org.multikoisas.repository.LojaImagemRepository;
import org.multikoisas.service.LojaImagemService;
import org.multikoisas.service.S3StorageService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class LojaImagemServiceImpl implements LojaImagemService {

    private final LojaImagemRepository repository;
    private final S3StorageService s3Service; // Injeção da Interface, não da implementação S3!
    private static final long TAMANHO_MAXIMO = 5 * 1024 * 1024; // 5MB

    @Override
    @Transactional // Garante que se o banco falhar, o processo inteiro aborta
    public LojaImagemResponse save(MultipartFile file, String tipo) {

        validarImagem(file);

        // Valida se o loja existe, caso contrário lança 404
        if (!repository.findByTipo(tipo).isEmpty()) {
            new IllegalArgumentException("Imagem do tipo " + tipo + " ja esta cadastrado.");
        }

        if(tipo.isBlank()){
            new IllegalArgumentException("O tipo nao pode estar em branco.");
        }
        //tratamento da String tipo para uupercase
        String type = tipo.toUpperCase();

        // 1. Envia para a nuvem e recebe a URL (Responsabilidade do StorageService)
        String publicUrl = s3Service.uploadFile(file);

        // 2. Constrói a Entidade com os dados do Request + URL da nuvem
        LojaImagemEntity entity = new LojaImagemEntity();
        entity.setImagemUrl(publicUrl);
        entity.setTipo(LojaImagemTipo.valueOf(type));

        // 3. Salva no banco de dados (Responsabilidade do Repository)
        LojaImagemEntity savedEntity = repository.save(entity);

        // 4. Converte e devolve
        return toResponse(savedEntity);
    }

    @Override
    @Transactional
    public LojaImagemResponse delete(Integer id, String lojaImagemTipo) {
        // 1. Busca a entidade no banco de dados
        List<LojaImagemEntity> entity = repository.findByIdAndTipo(id, lojaImagemTipo);

        // 2. Valida regras de negócio (DDD) - A imagem deve existir e pertencer ao tipo
        if (entity == null || entity.isEmpty()) {
            throw new IllegalArgumentException("Imagem nao encontrada ou nao pertence a este tipo.");
        }

        // 3. Apaga o arquivo físico na nuvem (Infraestrutura S3)
        s3Service.deleteFile(entity.getFirst().getImagemUrl());

        // 4. Apaga o registro no banco de dados (Persistência)
        repository.deleteByIdAndTipo(id, lojaImagemTipo);

        // 5. Retorna os dados da imagem deletada (ou poderia ser void, dependendo do design da sua API)
        return toResponse(entity.getFirst());
    }

    @Override
    public List<LojaImagemResponse> getImagemLojas(Integer id, String lojaImagemTipo) {
        List<LojaImagemEntity> entity;
        if (id == null && lojaImagemTipo == null) {
            entity = repository.findAll();
        } else if (id != null && lojaImagemTipo != null) {
            entity = repository.findByIdAndTipo(id, lojaImagemTipo);
        } else if (lojaImagemTipo != null) {
            entity = repository.findByTipo(lojaImagemTipo);
        } else {
            entity = repository.findById(id);
        }
//        if (entity.isEmpty()) {
//            throw new NoSuchElementException("Nenhuma imagem encontrada.");
//        }
        return entity.stream().map(this::toResponse).toList();
    }

    @Override
    public List<LojaImagemResponse> findAll() {
        return repository.findAll().stream().map(this::toResponse).toList();
    }

    @Override
    public List<LojaImagemResponse> findById(Integer id) {
        return repository.findById(id).stream().map(this::toResponse).toList();
    }


    private void validarImagem(MultipartFile file) {

        Set<String> tiposPermitidos = Set.of(
                "image/jpg",
                "image/jpeg",
                "image/png",
                "image/webp"
        );

        if (file.getSize() > TAMANHO_MAXIMO) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "A imagem deve ter no máximo 5 MB"
            );
        }

        if (!tiposPermitidos.contains(file.getContentType())) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Envie uma imagem JPG/JPEG, PNG ou WebP"
            );
        }
    }

    public LojaImagemResponse toResponse(LojaImagemEntity entity) {
        return LojaImagemResponse.builder()
                .id(entity.getId())
                .imagemUrl(entity.getImagemUrl())
                .tipo(entity.getTipo())
                .build();

    }

}
