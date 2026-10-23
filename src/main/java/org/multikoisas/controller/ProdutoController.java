
package org.multikoisas.controller;

import java.util.List;

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
    public ResponseEntity<Void> updateProduto(@PathVariable Integer id,@RequestBody ProdutoRequest request) {
        produtoService.update(id, request);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduto(@PathVariable Integer id) {
        produtoService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> handleIllegalArgumentException(IllegalArgumentException ex) {
        // Interceta a exceção de negócio e devolve o status 409 com a mensagem exata
        return ResponseEntity.status(HttpStatus.CONFLICT).body(ex.getMessage());
    }
}
