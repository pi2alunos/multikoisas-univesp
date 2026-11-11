package org.multikoisas.service;

import org.multikoisas.dto.LojaRequest;
import org.multikoisas.dto.LojaResponse;
import org.multikoisas.dto.UpdateLojaRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface LojaService {

    List<LojaResponse> getLoja(String nomeLoja);
    LojaResponse save(LojaRequest request);
    LojaResponse update(String nome, UpdateLojaRequest request);
    void delete(String nome);
}
