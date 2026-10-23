package org.multikoisas.service;


import org.multikoisas.dto.ProdutoImagemRequest;
import org.multikoisas.dto.ProdutoImagemResponse;

import java.util.List;

public interface ProdutoImagemService {

    ProdutoImagemResponse save(ProdutoImagemRequest request);

    ProdutoImagemResponse delete(Integer id, Integer productId);

    List<ProdutoImagemResponse> findAll();

    List<ProdutoImagemResponse> findByProdutoId(Integer id);

}