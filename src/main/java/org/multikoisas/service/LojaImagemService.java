package org.multikoisas.service;

import org.multikoisas.dto.LojaImagemResponse;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface LojaImagemService {
    LojaImagemResponse save(MultipartFile file, String lojaImagemTipo);
    LojaImagemResponse delete(Integer id, String tipo);
    List<LojaImagemResponse> getImagemLojas(Integer id, String lojaImagemTipo);
    List<LojaImagemResponse> findAll();
    List<LojaImagemResponse> findById(Integer id);

}
