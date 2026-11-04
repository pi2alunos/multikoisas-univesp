package org.multikoisas.repository;

import java.util.List;

import org.multikoisas.entities.CategoriaEntity;

public interface CategoriaRepository {
    List<CategoriaEntity> findAll();
    List<CategoriaEntity> findById(Integer id);
    List<CategoriaEntity> findByName(String nome);
    List<CategoriaEntity> findByCategoriaIdAndCategoriaNome(Integer categoriaId, String nome);
    CategoriaEntity save(CategoriaEntity entity);
    boolean update(Integer id, CategoriaEntity entity);
    boolean deleteById(Integer id);
}