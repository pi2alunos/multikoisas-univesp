package org.multikoisas.repository;

import org.multikoisas.entities.CategoriaProdutoEntity;

import java.util.List;

public interface CategoriaProdutoRepository {
    List<CategoriaProdutoEntity> findAll();
    List<CategoriaProdutoEntity> findByProdutoId(Integer produtoId);
    List<CategoriaProdutoEntity> findByCategoriaId(Integer categoriaId);
    List<CategoriaProdutoEntity> findByProdutoIdAndCategoriaId (Integer produtoId, Integer categoriaId);
    CategoriaProdutoEntity save(CategoriaProdutoEntity entity);
    int deleteByProdutoIdAndCategoriaId(Integer produtoId, Integer categoriaId);
    int deleteByProdutoId(Integer produtoId);
    int deleteByCategoriaId(Integer categoriaId);
    boolean exists(Integer produtoId, Integer categoriaId);
}
