
package org.multikoisas.controller;

import java.util.List;
import java.util.Map;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.multikoisas.dto.ProdutoRequest;
import org.multikoisas.dto.ProdutoResponse;
import org.multikoisas.service.ProdutoService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/produto")
@RequiredArgsConstructor
public class ProdutoController {

    private final ProdutoService produtoService;

    @GetMapping
    public ResponseEntity<List<ProdutoResponse>> getProdutos() {
        return ResponseEntity.ok(produtoService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProdutoResponse> getProdutoById(@PathVariable Integer id) {
        return ResponseEntity.ok(produtoService.findById(id));
    }

    @PostMapping
    public ResponseEntity<ProdutoResponse> createProduto(@Valid @RequestBody ProdutoRequest request) {
        ProdutoResponse response = produtoService.save(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProdutoResponse> updateProduto(@PathVariable Integer id,@RequestBody ProdutoRequest request) {
        boolean response = produtoService.update(id, request);
        return response ? ResponseEntity.ok(produtoService.findById(id)) : ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteProduto(@PathVariable Integer id) {
        produtoService.deletarProdutoComDependencias(id);
        return ResponseEntity.ok(Map.of("message", "O produto " + id + " deletado com sucesso."));
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> handleIllegalArgumentException(IllegalArgumentException ex) {
        // Interceta a exceção de negócio e devolve o status 409 com a mensagem exata
        return ResponseEntity.status(HttpStatus.CONFLICT).body(ex.getMessage());
    }
}
