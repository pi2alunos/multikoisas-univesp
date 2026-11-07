package org.multikoisas.repository;

import org.multikoisas.entities.CanalVendaProdutoEntity;

import java.util.List;

public interface CanalVendaProdutoRepository {

    List<CanalVendaProdutoEntity> findByProdutoIdAndCanalId(Integer produtoId, Integer canalVendaId);
    List<CanalVendaProdutoEntity> findAll();
    List<CanalVendaProdutoEntity> findByProdutoId(Integer id);
    List<CanalVendaProdutoEntity> findByCanalVendaId(Integer id);
    CanalVendaProdutoEntity save(CanalVendaProdutoEntity entity);
    int deleteByProdutoIdAndCanalId(Integer produtoId, Integer canalVendaId);
    int deleteByProdutoId(Integer produtoId);
    int deleteByCanalId(Integer canalVendaId);
    CanalVendaProdutoEntity updateLink(Integer produtoId, Integer canalVendaId, String link);
    boolean exists(Integer produtoId, Integer canalVendaId);
}
