
package org.multikoisas.service.impl;

import java.util.Comparator;
import java.util.List;

import lombok.RequiredArgsConstructor;

import org.multikoisas.dto.ProdutoRequest;
import org.multikoisas.dto.ProdutoResponse;
import org.multikoisas.entities.ProdutoEntity;
import org.multikoisas.repository.ProdutoRepository;
import org.multikoisas.service.*;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class ProdutoServiceImpl implements ProdutoService {

    private final ProdutoRepository produtoRepository;
    private final ProdutoImagemService produtoImagemService;
    private final CanalVendaProdutoService canalVendaProdutoService;
    private final CategoriaProdutoService categoriaProdutoService;

    private void validar(ProdutoRequest request) {
        if (request == null || request.getNome() == null || request.getNome().isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"O nome do produto é obrigatório");
        }
    }

    @Override
    public List<ProdutoResponse> getProdutos(Integer id) {
        List<ProdutoEntity> entity;
        if (id == null) {
            entity = produtoRepository.findAll();
            entity.sort(Comparator.comparing(ProdutoEntity::getId));
        } else if(produtoRepository.findById(id).isPresent()) {
            entity = List.of(produtoRepository.findById(id).get());
        } else {
            entity = List.of();
        }
        return entity.stream().map(this::toResponse).toList();
    }

    @Override
    public List<ProdutoResponse> findAll() {
        return produtoRepository.findAll()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Override
    public ProdutoResponse findById(Integer id) {
        ProdutoEntity entity = produtoRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Produto não encontrado"));

        return toResponse(entity);
    }

    @Override
    public ProdutoResponse save(ProdutoRequest request) {
        validar(request);

        ProdutoEntity entity = new ProdutoEntity();
        entity.setNome(request.getNome());
        if(!request.getDescricaoProduto().isEmpty()){
            entity.setDescricaoProduto(request.getDescricaoProduto());
        }
        return toResponse(produtoRepository.save(entity));
    }

    @Override
    public boolean update(Integer id, ProdutoRequest request) {
        validar(request);

        if (produtoRepository.findById(id).isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Produto nao encontrado");
        }

        ProdutoEntity entity = new ProdutoEntity();
        entity.setNome(request.getNome());
        entity.setDescricaoProduto(request.getDescricaoProduto());

        return produtoRepository.update(id, entity);
    }

//    @Override
//    public boolean deleteById(Integer id) {
//
//
//        try {
//            return produtoRepository.deleteById(id);
//        } catch (DataIntegrityViolationException e) {
//            throw new ResponseStatusException(HttpStatus.CONFLICT,"Não é possível excluir este produto porque ele está associado a outras informações.");
//        }
//    }

    @Transactional
    public void deletarProdutoComDependencias(Integer produtoId) {
        if (produtoRepository.findById(produtoId).isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Esse produto nao existe e nao pode ser deletado");
        }

        if (!produtoImagemService.findByProdutoId(produtoId).isEmpty()) {
            System.out.println("1 - Removendo imagem(ns)");
            produtoImagemService.deleteAllByProdutoId(produtoId);
        }
        if (!canalVendaProdutoService.findByProdutoId(produtoId).isEmpty()) {
            System.out.println("2 - Removendo canal venda");
            canalVendaProdutoService.deleteByProdutoId(produtoId);
        }
        if (!categoriaProdutoService.findByProdutoId(produtoId).isEmpty()) {
            System.out.println("3 - Removendo categorias");
            categoriaProdutoService.deleteByProdutoId(produtoId);
        }

        System.out.println("4 - Removendo produto");
        produtoRepository.deleteById(produtoId);
    }

    private ProdutoResponse toResponse(ProdutoEntity entity) {
        return ProdutoResponse.builder()
                .id(entity.getId())
                .nome(entity.getNome())
                .descricaoProduto(entity.getDescricaoProduto())
                .build();
    }
}
