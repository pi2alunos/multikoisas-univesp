package org.multikoisas.repository;

import java.util.List;

import org.multikoisas.entities.CanalVendaEntity;

public interface CanalVendaRepository {
    List<CanalVendaEntity> findAll();
    List<CanalVendaEntity> findById(Integer id);
    List<CanalVendaEntity> findByName(String nome);
    CanalVendaEntity save(CanalVendaEntity entity);
    boolean update(Integer id, CanalVendaEntity entity);
    boolean deleteById(Integer id);
}