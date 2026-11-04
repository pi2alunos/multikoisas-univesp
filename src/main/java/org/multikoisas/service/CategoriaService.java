
package org.multikoisas.service;

import java.util.List;

import org.multikoisas.dto.CategoriaRequest;
import org.multikoisas.dto.CategoriaResponse;

public interface CategoriaService {

    List<CategoriaResponse> findAll();
    List<CategoriaResponse> findById(Integer id);
    List<CategoriaResponse> findByName(String nome);
    List<CategoriaResponse> findByCategoriaIdOrCategoriaNomeOrAll (Integer categoriaId, String nome) ;
    CategoriaResponse save(CategoriaRequest request);
    boolean update(Integer id, CategoriaRequest request);
    boolean deleteById(Integer id);
}
