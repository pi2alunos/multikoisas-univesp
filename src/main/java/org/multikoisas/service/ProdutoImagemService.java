package org.multikoisas.service;

import org.multikoisas.dto.ProdutoImagemResponse;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ProdutoImagemService {

    List<ProdutoImagemResponse> getProdutos(Integer produtoId);
    ProdutoImagemResponse save(MultipartFile file, Integer produtoId);
    ProdutoImagemResponse delete(Integer id, Integer productId);
    List<ProdutoImagemResponse> findAll();
    List<ProdutoImagemResponse> findByProdutoId(Integer id);
    void deleteAllByProdutoId(Integer produtoId);

}