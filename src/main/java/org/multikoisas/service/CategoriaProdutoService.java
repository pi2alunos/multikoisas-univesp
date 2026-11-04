package org.multikoisas.service;

import org.multikoisas.dto.CategoriaProdutoRequest;
import org.multikoisas.dto.CategoriaProdutoResponse;

import java.util.List;

public interface CategoriaProdutoService {

    List<CategoriaProdutoResponse> findByProdutoIdOrCategoriaIdOrAll (Integer produtoId, Integer categoriaId);
    List<CategoriaProdutoResponse> findAll();
    List<CategoriaProdutoResponse> findByProdutoId(Integer produtoId);
    List<CategoriaProdutoResponse> findByCategoriaId(Integer categoriaId);
    CategoriaProdutoResponse save(CategoriaProdutoRequest categoriaProdutoRequest);
    boolean deleteByProdutoIdAndCategoriaId(Integer produtoId, Integer categoriaId);
    boolean deleteByProdutoId(Integer produtoId);
    boolean deleteByCategoriaId(Integer categoriaId);

}
