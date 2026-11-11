package org.multikoisas.service;

import org.multikoisas.dto.SocialNetworkRequest;
import org.multikoisas.dto.SocialNetworkResponse;

import java.util.List;

public interface SocialNetworkService {

    List<SocialNetworkResponse> getSocialNetworks(Integer id, String descricao);
    List<SocialNetworkResponse> findAll();
    //List<SocialNetworkResponse> findByName(String descricao);
    List<SocialNetworkResponse> findById(Integer id);
    SocialNetworkResponse save(SocialNetworkRequest request);
    SocialNetworkResponse delete(Integer id);
    SocialNetworkResponse update(Integer id, String link);

}
