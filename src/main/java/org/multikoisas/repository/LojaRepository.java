package org.multikoisas.repository;

import org.multikoisas.entities.LojaEntity;

import java.util.List;

public interface LojaRepository {
    List<LojaEntity> findAll();
    List<LojaEntity> findById(Integer id);
    List<LojaEntity> findByName(String nome);
    List<LojaEntity> findByIdAndName(Integer id, String nome);
    LojaEntity save(LojaEntity entity);
    LojaEntity update(LojaEntity entity);
    void delete(Integer id);
}
