package org.multikoisas.service;

import org.multikoisas.dto.SocialNetworkRequest;
import org.multikoisas.dto.SocialNetworkResponse;

import java.util.List;

public interface SocialNetworkService {

    List<SocialNetworkResponse> getSocialNetworks(String descricao);
    List<SocialNetworkResponse> findAll();
    List<SocialNetworkResponse> findByName(String descricao);
    SocialNetworkResponse save(SocialNetworkRequest request);
    SocialNetworkResponse delete(String descricao);
    SocialNetworkResponse update(String descricao, String link);

}
