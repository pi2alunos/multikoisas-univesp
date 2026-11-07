package org.multikoisas.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.multikoisas.dto.ProdutoImagemDeleteRequest;
import org.multikoisas.dto.ProdutoImagemResponse;
import org.multikoisas.service.ProdutoImagemService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/imagem")
@RequiredArgsConstructor
public class ProdutoImagemController {

    private final ProdutoImagemService service;

    @GetMapping
    public ResponseEntity<List<ProdutoImagemResponse>> getProdutos(@RequestParam(required = false) Integer produtoId) {
        List<ProdutoImagemResponse> response = service.getProdutos(produtoId);
        return response.isEmpty() ? ResponseEntity.notFound().build() : ResponseEntity.ok(response);
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ProdutoImagemResponse> upload(@RequestPart("file") MultipartFile file,
                                                        @RequestParam("produtoId") Integer produtoId) {

        ProdutoImagemResponse response = service.save(file, produtoId);

        if (response == null) {
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_CONTENT, "Erro ao salvar a imagem");
        }

        if (!Objects.equals(response.getProdutoId(), produtoId)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Produto invalido");
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