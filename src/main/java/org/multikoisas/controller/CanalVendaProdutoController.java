package org.multikoisas.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.multikoisas.dto.CanalVendaProdutoRequest;
import org.multikoisas.dto.CanalVendaProdutoResponse;
import org.multikoisas.service.CanalVendaProdutoService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/canalvendaproduto")
@RequiredArgsConstructor
public class CanalVendaProdutoController {

    private final CanalVendaProdutoService canalVendaProdutoService;

    @GetMapping
    public ResponseEntity<?> getCanaisVendaProdutos(
            @RequestParam(required = false) Integer produtoId,
            @RequestParam(required = false) Integer canalVendaId) {
        List<CanalVendaProdutoResponse> response = canalVendaProdutoService.findByProdutoIdOrCanalVendaIdOrAll(produtoId, canalVendaId);

        return response.isEmpty() ? ResponseEntity.status(HttpStatus.NOT_FOUND).body("Canal de venda e/ou produto nao encontrado") : ResponseEntity.ok(response);
    }


    @PostMapping
    public ResponseEntity<Object> save(
            @Valid @RequestBody CanalVendaProdutoRequest request) {

        canalVendaProdutoService.save(request);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(Map.of(
                        "mensagem",
                        "Canal de venda associado ao produto com sucesso."
                ));
    }

    @DeleteMapping
    public ResponseEntity<Object> deleteProdutoAndCanal(
            @Valid @RequestBody CanalVendaProdutoRequest request) {

        boolean response = canalVendaProdutoService.deleteByProdutoIdAndCanalId(
                request.getProdutoId(),
                request.getCanalVendaId()
        );

        return response
                ? ResponseEntity.ok(
                Map.of(
                        "mensagem",
                        "Canal de venda desassociado do produto com sucesso."
                )
        )
                : ResponseEntity.notFound().build();
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationExceptions(
            MethodArgumentNotValidException ex) {

        Map<String, String> errors = new HashMap<>();

        ex.getBindingResult()
                .getFieldErrors()
                .forEach(error ->
                        errors.put(
                                error.getField(),
                                error.getDefaultMessage()
                        )
                );

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(errors);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> handleIllegalArgumentException(
            IllegalArgumentException ex) {

        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(ex.getMessage());
    }


}