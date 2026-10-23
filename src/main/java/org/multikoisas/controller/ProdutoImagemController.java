package org.multikoisas.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.multikoisas.dto.ProdutoImagemDeleteRequest;
import org.multikoisas.dto.ProdutoImagemRequest;
import org.multikoisas.dto.ProdutoImagemResponse;
import org.multikoisas.service.ProdutoImagemService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/imagem")
@RequiredArgsConstructor
public class ProdutoImagemController {

    private final ProdutoImagemService service;

    @GetMapping
    public ResponseEntity<List<ProdutoImagemResponse>> findAll() {
        List<ProdutoImagemResponse> response = service.findAll();
        return response != null ? ResponseEntity.ok(response) : ResponseEntity.noContent().build();
    }

    @GetMapping("/{produtoId}")
    public ResponseEntity<List<ProdutoImagemResponse>> findByProdutoId(@PathVariable Integer produtoId) {
        List<ProdutoImagemResponse> response = service.findByProdutoId(produtoId);

        return response != null ? ResponseEntity.ok(response) : ResponseEntity.noContent().build();
    }

    @PostMapping(consumes = {"multipart/form-data"})
    public ResponseEntity<ProdutoImagemResponse> upload(@Valid @ModelAttribute ProdutoImagemRequest request) {

        ProdutoImagemResponse response = service.save(request);

        if (response == null){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }

        return ResponseEntity.status(HttpStatus.CREATED).body(response);

    }

    @DeleteMapping
    public ResponseEntity<Void> delete(@Valid @RequestBody ProdutoImagemDeleteRequest deleteRequest) {

        service.delete(deleteRequest.getId(), deleteRequest.getProdutoId());

        // Retorna 204 No Content, que é o padrão HTTP ideal para deleções bem-sucedidas
        return ResponseEntity.noContent().build();
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> handleIllegalArgumentException(IllegalArgumentException ex) {
        // Interceta a exceção de negócio e devolve o status 409 com a mensagem exata
        return ResponseEntity.status(HttpStatus.CONFLICT).body(ex.getMessage());
    }
}