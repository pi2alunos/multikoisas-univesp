
package org.multikoisas.service.impl;

import java.util.Comparator;
import java.util.List;
import java.util.NoSuchElementException;

import lombok.RequiredArgsConstructor;

import org.multikoisas.dto.CanalVendaRequest;
import org.multikoisas.dto.CanalVendaResponse;
import org.multikoisas.entities.CanalVendaEntity;
import org.multikoisas.entities.CategoriaProdutoEntity;
import org.multikoisas.entities.ProdutoEntity;
import org.multikoisas.repository.CanalVendaRepository;
import org.multikoisas.service.CanalVendaService;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class CanalVendaServiceImpl implements CanalVendaService {

    private final CanalVendaRepository canalVendaRepository;

    @Override
    public List<CanalVendaResponse> findByCanalIdOrByNomeOrAll(Integer id, String nome) {
        List<CanalVendaEntity> entity;
        if (id == null && nome == null) {
            entity = canalVendaRepository.findAll();
            entity.sort(Comparator.comparing(CanalVendaEntity::getId));
        } else if (id != null && nome != null) {
            entity = canalVendaRepository.findByIdAndName(id, nome);
        } else if (id != null) {
            entity = canalVendaRepository.findById(id);
        } else {
            entity = canalVendaRepository.findByName(nome);
        }

        if (entity.isEmpty()) {
            throw new NoSuchElementException("Nenhum canal de venda encontrado.");
        }
        return entity.stream().map(this::toResponse).toList();
    }

    @Override
    public List<CanalVendaResponse> findAll() {
        return canalVendaRepository.findAll()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Override
    public List<CanalVendaResponse> findById(Integer id) {
        return canalVendaRepository.findById(id).stream().map(this::toResponse).toList();
    }

    @Override
    public List<CanalVendaResponse> findByName(String nome) {
        return canalVendaRepository.findByName(nome).stream().map(this::toResponse).toList();
    }

    @Override
    public CanalVendaResponse save(CanalVendaRequest request) {

        if (canalVendaRepository.findByName(request.getNome()).stream().findFirst().isPresent()) {
            throw new IllegalArgumentException("Já existe um canal com esse nome");
        }

        CanalVendaEntity entity = new CanalVendaEntity();
        entity.setNome(request.getNome());

        return toResponse(canalVendaRepository.save(entity));
    }

    @Override
    public boolean update(Integer id, CanalVendaRequest request) {
        if (request.getNome() == null || request.getNome().isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "O nome é obrigatório");
        }

        if (canalVendaRepository.findById(id).isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Canal de venda não encontrado");
        }

        CanalVendaEntity entity = new CanalVendaEntity();
        entity.setId(id);
        entity.setNome(request.getNome());

        return canalVendaRepository.update(id, entity);
    }

    @Override
    public boolean deleteById(Integer id) {
        if (canalVendaRepository.findById(id).isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Canal de venda não encontrado");
        }

        try {
            return canalVendaRepository.deleteById(id);
        } catch (DataIntegrityViolationException e) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "Não é possível excluir este canal de venda porque ela está associada a um ou mais produtos."
            );
        }
    }

    private CanalVendaResponse toResponse(CanalVendaEntity entity) {
        return CanalVendaResponse.builder()
                .id(entity.getId())
                .nome(entity.getNome())
                .build();
    }
}
