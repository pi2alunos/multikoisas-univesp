
package org.multikoisas.service;

import java.util.List;

import org.multikoisas.dto.CategoriaRequest;
import org.multikoisas.dto.CategoriaResponse;

public interface CategoriaService {

    List<CategoriaResponse> findAll();

    CategoriaResponse findById(Integer id);

    CategoriaResponse findByName(String nome);

    CategoriaResponse save(CategoriaRequest request);

    boolean update(Integer id, CategoriaRequest request);

    boolean deleteById(Integer id);
}
