package org.multikoisas.service;

import org.multikoisas.dto.SocialNetworkRequest;
import org.multikoisas.dto.SocialNetworkResponse;

import java.util.List;

public interface SocialNetworkService {

    List<SocialNetworkResponse> getSocialNetworks(String name);
    List<SocialNetworkResponse> findAll();
    List<SocialNetworkResponse> findByName(String name);
    SocialNetworkResponse save(SocialNetworkRequest request);
    SocialNetworkResponse delete(String name);
    SocialNetworkResponse update(String description, String link);

}
