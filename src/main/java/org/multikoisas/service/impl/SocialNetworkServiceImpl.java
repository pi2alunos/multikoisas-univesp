package org.multikoisas.service.impl;

import lombok.RequiredArgsConstructor;
import org.multikoisas.dto.SocialNetworkRequest;
import org.multikoisas.dto.SocialNetworkResponse;
import org.multikoisas.entities.SocialNetworkEntity;
import org.multikoisas.repository.SocialNetworkRepository;
import org.multikoisas.service.SocialNetworkService;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SocialNetworkServiceImpl implements SocialNetworkService {

    private final SocialNetworkRepository socialRepository;

    public List<SocialNetworkResponse> getSocialNetworks(String name) {
        List<SocialNetworkEntity> socialNetworks;
        if (name == null || name.trim().isEmpty()) {
            socialNetworks = socialRepository.findAll();
            socialNetworks.sort(Comparator.comparing(SocialNetworkEntity::getId));
        } else {
            socialNetworks = socialRepository.findByName(name);
        }
        return socialNetworks.isEmpty() ? List.of() : socialNetworks.stream().map(this::toResponse).toList();
    }

    @Override
    public List<SocialNetworkResponse> findAll() {
        return socialRepository.findAll().stream()
                .map(this::toResponse)
                .toList();
    }

//    @Override
//    public List<SocialNetworkResponse> findByName(String name) {
//        return socialRepository.findByName(name).stream().map(this::toResponse).toList();
//    }

    @Override
    public SocialNetworkResponse save(SocialNetworkRequest request) {

        if (socialRepository.existsByName(request.getDescricao())) {
            throw new IllegalArgumentException("Esta rede social ja esta cadastrada.");
        }

        SocialNetworkEntity entityToSave = toEntity(request);
        SocialNetworkEntity savedEntity = socialRepository.save(entityToSave);

        return toResponse(savedEntity);
    }

    @Override
    public SocialNetworkResponse delete(String descricao) {

        SocialNetworkEntity entityDeleted = socialRepository.delete(descricao);

        if (entityDeleted == null) {
            return null;
        }
        System.out.println("SERVICE: " + descricao);
        return toResponse(entityDeleted);
    }

    @Override
    public SocialNetworkResponse update(String descricao, String link) {

        SocialNetworkEntity socialNetworkToUpdate = socialRepository.findByName(descricao).stream().findFirst().isPresent()
                ? socialRepository.findByName(descricao).stream().findFirst().get()
                : null;

        if (socialNetworkToUpdate == null) {
            return null;
        }

        socialNetworkToUpdate.setLink(link);

        // Gravação e Retorno
        socialRepository.update(socialNetworkToUpdate);
        return toResponse(socialNetworkToUpdate);

    }

    private SocialNetworkResponse toResponse(SocialNetworkEntity socialNetworkEntity)
    {
        return SocialNetworkResponse.builder()
                .id(socialNetworkEntity.getId())
                .descricao(socialNetworkEntity.getDescricao())
                .link(socialNetworkEntity.getLink())
                .build();

    }

    private SocialNetworkEntity toEntity(SocialNetworkRequest socialNetworkRequest)
    {
        return SocialNetworkEntity.builder()
                .descricao(socialNetworkRequest.getDescricao())
                .link(socialNetworkRequest.getLink())
                .build();
    }
}
