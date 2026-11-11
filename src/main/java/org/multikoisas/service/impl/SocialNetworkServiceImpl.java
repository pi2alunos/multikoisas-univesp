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

    public List<SocialNetworkResponse> getSocialNetworks(Integer id, String descricao) {
        List<SocialNetworkEntity> socialNetworks;
        if (id == null && descricao == null) {
            socialNetworks = socialRepository.findAll();
            socialNetworks.sort(Comparator.comparing(SocialNetworkEntity::getId));
        } else if (id != null && descricao != null){
            socialNetworks = socialRepository.findByIdAndName(id, descricao);
        } else if (descricao != null) {
            socialNetworks = socialRepository.findByName(descricao);
        } else {
            socialNetworks = socialRepository.findById(id);
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
    public List<SocialNetworkResponse> findById(Integer id) {
        List<SocialNetworkEntity> socialNetwork = socialRepository.findById(id);
        if  (socialNetwork.isEmpty()) {
            return null;
        }
        return socialNetwork.stream().map(this::toResponse).toList();
    }

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
    public SocialNetworkResponse delete(Integer id) {

        SocialNetworkEntity entityDeleted = socialRepository.delete(id);

        if (entityDeleted == null) {
            return null;
        }
        return toResponse(entityDeleted);
    }

    @Override
    public SocialNetworkResponse update(Integer id, String link) {

        List<SocialNetworkEntity> result = socialRepository.findById(id);

        if (result == null || result.isEmpty()) {
            throw new IllegalArgumentException("Essa rede social nao existe.");
        }
        SocialNetworkEntity socialNetworkToUpdate = result.getFirst();
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
