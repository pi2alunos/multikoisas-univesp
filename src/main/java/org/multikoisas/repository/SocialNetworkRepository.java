package org.multikoisas.repository;

import org.multikoisas.entities.SocialNetworkEntity;

import java.util.List;

public interface SocialNetworkRepository {

    List<SocialNetworkEntity> findAll();
    boolean existsByName(String descricao);
    List<SocialNetworkEntity> findByIdAndName(Integer id, String descricao);
    List<SocialNetworkEntity> findByName(String descricao);
    List<SocialNetworkEntity> findById(Integer id);
    SocialNetworkEntity save(SocialNetworkEntity entity);
    SocialNetworkEntity delete(Integer id);
    void update(SocialNetworkEntity entity);


}
