package org.multikoisas.service.impl;


import lombok.RequiredArgsConstructor;
import org.multikoisas.dto.LojaRequest;
import org.multikoisas.dto.LojaResponse;
import org.multikoisas.dto.UpdateLojaRequest;
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

    @Override
    public List<LojaResponse> getLoja(Integer id, String nomeLoja) {
        List<LojaEntity> entity;
        if (id == null && nomeLoja == null) {
            entity = lojaRepository.findAll();
        }else if (nomeLoja != null) {
            entity = lojaRepository.findByName(nomeLoja);
        }else {
            entity = lojaRepository.findById(id);
        }
        return entity.stream().map(this::toResponse).toList();
    }

    @Override
    public LojaResponse save(LojaRequest request) {
        validar(request);
        List<LojaEntity> search = lojaRepository.findByName(request.getNomeLoja());
        if (!search.isEmpty()) {
            throw new IllegalArgumentException("ja existe uma loja com esse nome");
        }
        LojaEntity entity = new LojaEntity();
        entity.setNomeLoja(request.getNomeLoja());
        if(!request.getDescricaoLoja().isBlank()){
            entity.setDescricaoLoja(request.getDescricaoLoja());
        }
        if(!request.getEndereco().isBlank()){
            entity.setEndereco(request.getEndereco());
        }
        if(!request.getCorDestaque().isBlank()){
            entity.setCorDestaque(request.getCorDestaque());
        }
        if(request.getColunaCarrossel() >= 1){
            entity.setColunaCarrossel(request.getColunaCarrossel());
        }
        return toResponse(lojaRepository.save(entity));
    }

    public LojaResponse update(Integer id, UpdateLojaRequest request) {
        List<LojaEntity> entity = lojaRepository.findById(id);
        if (entity.isEmpty()) {
            throw new NoSuchElementException("Loja nao encontrada.");
        }
        if (!request.getDescricaoLoja().isBlank()) {
            entity.getFirst().setDescricaoLoja(request.getDescricaoLoja());
        }
        if (!request.getEndereco().isBlank()) {
            entity.getFirst().setEndereco(request.getEndereco());
        }
        if (!request.getCorDestaque().isBlank())
        {
            entity.getFirst().setCorDestaque(request.getCorDestaque());
        }
        if (request.getColunaCarrossel() >= 1){
            entity.getFirst().setColunaCarrossel(request.getColunaCarrossel());
        }
        return toResponse(lojaRepository.update(entity.getFirst()));
    }

    @Override
    public void delete(Integer id){
        List<LojaEntity> entity = lojaRepository.findById(id);
        if(entity.isEmpty()){
            throw new NoSuchElementException("Nao existe essa loja para ser deletado");
        }
            lojaRepository.delete(entity.getFirst().getId());
    }

    private LojaResponse toResponse(LojaEntity entity){
        return LojaResponse.builder()
                .id(entity.getId())
                .nomeLoja(entity.getNomeLoja())
                .descricaoLoja(entity.getDescricaoLoja())
                .endereco(entity.getEndereco())
                .corDestaque(entity.getCorDestaque())
                .colunaCarrossel(entity.getColunaCarrossel())
                .build();
    }

    private void validar(LojaRequest request) {
        if (request == null || request.getNomeLoja() == null || request.getNomeLoja().isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"O nome da loja e obrigatorio");
        }
    }
}
