package org.multikoisas.service.impl;

import lombok.RequiredArgsConstructor;
import org.multikoisas.dto.CanalVendaProdutoRequest;
import org.multikoisas.dto.CanalVendaProdutoResponse;
import org.multikoisas.entities.CanalVendaProdutoEntity;
import org.multikoisas.repository.CanalVendaProdutoRepository;
import org.multikoisas.service.CanalVendaProdutoService;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class CanalVendaProdutoServiceImpl implements CanalVendaProdutoService {

    private final CanalVendaProdutoRepository canalVendaProdutoRepository;

    @Override
    public List<CanalVendaProdutoResponse> findAll() {
        return canalVendaProdutoRepository.findAll()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Override
    public List<CanalVendaProdutoResponse> findByProdutoIdOrCanalVendaIdOrAll(Integer produtoId, Integer canalVendaId) {
        List<CanalVendaProdutoEntity> entity;
        if (produtoId != null && canalVendaId != null) {
            entity = canalVendaProdutoRepository.findByProdutoIdAndCanalId(produtoId, canalVendaId);
            validarLista(entity, "Produto e/ou canal de venda inexistentes. Verifique os IDs informados.");
        } else if (produtoId != null) {
            entity = canalVendaProdutoRepository.findByProdutoId(produtoId);
            validarLista(entity, "Produto inexistente. Verifique o ID informado.");
        } else if (canalVendaId != null) {
            entity = canalVendaProdutoRepository.findByCanalVendaId(canalVendaId);
            validarLista(entity, "Canal de venda inexistente. Verifique o ID informado.");
        } else {
            entity = canalVendaProdutoRepository.findAll();
        }

        return entity.stream()
                .map(this::toResponse)
                .toList();
    }

    @Override
    public List<CanalVendaProdutoResponse> findByProdutoId(Integer produtoId) {
        return canalVendaProdutoRepository.findByProdutoId(produtoId)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Override
    public List<CanalVendaProdutoResponse> findByCanalVendaId(Integer canalVendaId) {
        return canalVendaProdutoRepository.findByCanalVendaId(canalVendaId)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Override
    public CanalVendaProdutoResponse save(CanalVendaProdutoRequest request) {

        if (canalVendaProdutoRepository.exists(
                request.getProdutoId(),
                request.getCanalVendaId())) {

            throw new IllegalArgumentException(
                    "Este produto já está associado a este canal de venda."
            );
        }

        CanalVendaProdutoEntity entity = new CanalVendaProdutoEntity();

        entity.setProdutoId(request.getProdutoId());
        entity.setCanalVendaId(request.getCanalVendaId());
        entity.setLink(request.getLink());

        try {
            canalVendaProdutoRepository.save(entity);
        } catch (DataIntegrityViolationException e) {
            throw new IllegalArgumentException(
                    "Produto ou canal de venda inexistente. Verifique os IDs informados."
            );
        }

        return toResponse(entity);
    }

    @Override
    public boolean deleteByProdutoIdAndCanalId(Integer produtoId, Integer canalVendaId) {

        int linhasAfetadas =
                canalVendaProdutoRepository.deleteByProdutoIdAndCanalId(produtoId, canalVendaId);

        if (linhasAfetadas == 0) {
            throw new IllegalArgumentException(
                    "O vínculo entre este produto e canal de venda não existe."
            );
        }

        return true;
    }

    public boolean deleteByProdutoId(Integer produtoId) {

        int linhasAfetadas =
                canalVendaProdutoRepository.deleteByProdutoId(produtoId);

        if (linhasAfetadas == 0) {
            throw new IllegalArgumentException(
                    "Nao existe vinculo com o produto."
            );
        }

        return true;
    }

    @Override
    public boolean deleteByCanalId(Integer canalVendaId) {
        int linhasAfetadas =
                canalVendaProdutoRepository.deleteByCanalId(canalVendaId);

        if (linhasAfetadas == 0) {
            throw new IllegalArgumentException(
                    "Nao existe vinculo com o canal de venda."
            );
        }

        return true;
    }

    public CanalVendaProdutoResponse updateLink(Integer produtoId, Integer canalVendaId, String link) {

        if (!canalVendaProdutoRepository.exists(produtoId, canalVendaId)) {
            throw new IllegalArgumentException(
                    "O vínculo entre este produto e canal de venda não existe."
            );
        }

        CanalVendaProdutoEntity updatedEntity = canalVendaProdutoRepository.updateLink(produtoId, canalVendaId, link);

        CanalVendaProdutoEntity entity = new CanalVendaProdutoEntity();
        entity.setProdutoId(updatedEntity.getProdutoId());
        entity.setCanalVendaId(updatedEntity.getCanalVendaId());
        entity.setLink(updatedEntity.getLink());

        return toResponse(entity);
    }

    private CanalVendaProdutoResponse toResponse(
            CanalVendaProdutoEntity canalVendaProdutoEntity) {

        return CanalVendaProdutoResponse.builder()
                .produtoId(canalVendaProdutoEntity.getProdutoId())
                .canalVendaId(canalVendaProdutoEntity.getCanalVendaId())
                .link(canalVendaProdutoEntity.getLink())
                .build();
    }

    private <T> List<T> validarLista(List<T> lista, String mensagem) {
        if (lista.isEmpty()) {
            throw new NoSuchElementException(mensagem);
        }
        return lista;
    }
}
