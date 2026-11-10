package org.multikoisas.service.impl;


import lombok.RequiredArgsConstructor;
import org.multikoisas.dto.LojaRequest;
import org.multikoisas.dto.LojaResponse;
import org.multikoisas.entities.LojaEntity;
import org.multikoisas.repository.LojaRepository;
import org.multikoisas.service.LojaService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class LojaServiceImpl implements LojaService {

    private final LojaRepository lojaRepository;

    public List<LojaResponse> getLoja(String nomeLoja) {
        List<LojaEntity> entity;
        if (nomeLoja == null) {
            entity = lojaRepository.findAll();
        }else {
            entity = lojaRepository.findByName(nomeLoja);
        }
        return entity.stream().map(this::toResponse).toList();
    }

    public LojaResponse save(LojaRequest request) {
        validar(request);
        LojaEntity entity = new LojaEntity();
        entity.setNomeLoja(request.getNomeLoja().toUpperCase());
        if(!request.getDescricaoLoja().isBlank()){
            entity.setDescricaoLoja(request.getDescricaoLoja());
        }
        return toResponse(lojaRepository.save(entity));
    }

    public LojaResponse update(String nome, String descricao) {
        List<LojaEntity> entity = lojaRepository.findByName(nome);
        if (entity.isEmpty()) {
            throw new NoSuchElementException("Loja nao encontrada.");
        }
        if (!descricao.isBlank()) {
            entity.getFirst().setDescricaoLoja(descricao);
        }else{
            throw new NoSuchElementException("Descricao deve ser preenchida.");
        }
        return toResponse(lojaRepository.update(entity.getFirst()));
    }

    public void delete(String nome){
        List<LojaEntity> entity = lojaRepository.findByName(nome);
        if(entity.isEmpty()){
            throw new NoSuchElementException("Nao existe loja com esse nome para ser deletado");
        }
            lojaRepository.delete(entity.getFirst().getId());
    }

    private LojaResponse toResponse(LojaEntity entity){
        return LojaResponse.builder()
                .id(entity.getId())
                .nomeLoja(entity.getNomeLoja())
                .descricaoLoja(entity.getDescricaoLoja())
                .build();
    }

    private void validar(LojaRequest request) {
        if (request == null || request.getNomeLoja() == null || request.getNomeLoja().isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"O nome da loja e obrigatorio");
        }
    }
}
