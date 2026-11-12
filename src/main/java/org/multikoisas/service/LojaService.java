package org.multikoisas.service;

import org.multikoisas.dto.LojaRequest;
import org.multikoisas.dto.LojaResponse;
import org.multikoisas.dto.UpdateLojaRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface LojaService {

    List<LojaResponse> getLoja(Integer id, String nomeLoja);
    LojaResponse save(LojaRequest request);
    LojaResponse update(Integer id, UpdateLojaRequest request);
    void delete(Integer id);
}
