
package org.multikoisas.service;

import java.util.List;

import org.multikoisas.dto.CanalVendaRequest;
import org.multikoisas.dto.CanalVendaResponse;

public interface CanalVendaService {

    List<CanalVendaResponse> findAll();

    List<CanalVendaResponse> findByCanalIdOrByNomeOrAll(Integer canalVendaId, String nome);

    List<CanalVendaResponse> findById(Integer id);

    List<CanalVendaResponse> findByName(String nome);

    CanalVendaResponse save(CanalVendaRequest request);

    boolean update(Integer id, CanalVendaRequest request);

    boolean deleteById(Integer id);
}
