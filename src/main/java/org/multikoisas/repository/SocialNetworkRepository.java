package org.multikoisas.repository;

import org.multikoisas.entities.SocialNetworkEntity;

import java.util.List;

public interface SocialNetworkRepository {

    List<SocialNetworkEntity> findAll();

    List<SocialNetworkEntity> findByName(String descricao);

    SocialNetworkEntity save(SocialNetworkEntity entity);

    SocialNetworkEntity delete(String descricao);

    void update(SocialNetworkEntity entity);


}
