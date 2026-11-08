
package org.multikoisas.controller;

import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.multikoisas.dto.CanalVendaRequest;
import org.multikoisas.dto.CanalVendaResponse;
import org.multikoisas.service.CanalVendaService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/canalvenda")
@RequiredArgsConstructor
public class CanalVendaController {

    private final CanalVendaService canalVendaService;

    @GetMapping
    public ResponseEntity<List<CanalVendaResponse>> getCanaisVendaByIdOrByNomeOrAll(
            @RequestParam(required = false) Integer id,
            @RequestParam(required = false) String nome) {
        List<CanalVendaResponse> response = canalVendaService.findByCanalIdOrByNomeOrAll(id, nome);
        return response.isEmpty() ? ResponseEntity.notFound().build() : ResponseEntity.ok(response);
    }

    @PostMapping
    public ResponseEntity<CanalVendaResponse> createCanalVenda(@Valid @RequestBody CanalVendaRequest request) {
        CanalVendaResponse response = canalVendaService.save(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Object> updateCanalVenda(@PathVariable Integer id, @RequestBody CanalVendaRequest request) {
        CanalVendaResponse response = canalVendaService.update(id, request);
        return ResponseEntity.status(HttpStatus.OK).body(Map.of("message", "Canal de venda atualizado com sucesso.", "dados", response));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteCanalVenda(@PathVariable Integer id) {
        canalVendaService.deleteById(id);
        return ResponseEntity.status(HttpStatus.OK).body("Canal de venda excluido com sucesso.");
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> handleIllegalArgumentException(IllegalArgumentException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(ex.getMessage());
    }
    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity<String> handleNoSuchElementException(
            NoSuchElementException ex) {

        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(ex.getMessage());
    }
}
