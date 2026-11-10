package org.multikoisas.repository;

import org.multikoisas.entities.LojaImagemEntity;

import java.util.List;

public interface LojaImagemRepository {

    List<LojaImagemEntity> findAll();
    List<LojaImagemEntity> findByIdAndTipo(Integer id, String lojaImagemTipo);
    List<LojaImagemEntity> findById(Integer id);
    List<LojaImagemEntity> findByTipo(String lojaImagemTipo);
    LojaImagemEntity save(LojaImagemEntity entity);
    void deleteByIdAndTipo(Integer id, String lojaImagemTipo);
}
