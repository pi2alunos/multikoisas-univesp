package org.multikoisas.service;

import org.multikoisas.dto.CanalVendaProdutoRequest;
import org.multikoisas.dto.CanalVendaProdutoResponse;

import java.util.List;

public interface CanalVendaProdutoService {

    List<CanalVendaProdutoResponse> findByProdutoIdOrCanalVendaIdOrAll(Integer produtoId, Integer canalVendaId);
    List<CanalVendaProdutoResponse> findAll();
    List<CanalVendaProdutoResponse> findByProdutoId(Integer produtoId);
    List<CanalVendaProdutoResponse> findByCanalVendaId(Integer canalVendaId);
    CanalVendaProdutoResponse save(CanalVendaProdutoRequest request);
    boolean deleteByProdutoIdAndCanalId(Integer produtoId, Integer canalVendaId);
    boolean deleteByProdutoId(Integer produtoId);
    boolean deleteByCanalId(Integer canalVendaId);


}
