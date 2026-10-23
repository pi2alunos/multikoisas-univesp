
package org.multikoisas.controller;

import java.util.List;

import jakarta.validation.Valid;
import org.multikoisas.dto.CanalVendaRequest;
import org.multikoisas.dto.CanalVendaResponse;
import org.multikoisas.service.CanalVendaService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/canalvenda")
public class CanalVendaController {

    private final CanalVendaService canalVendaService;

    public CanalVendaController(CanalVendaService canalVendaService) {
        this.canalVendaService = canalVendaService;
    }

    @GetMapping
    public ResponseEntity<List<CanalVendaResponse>> getCanaisVenda() {
        return ResponseEntity.ok(canalVendaService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<CanalVendaResponse> getCanalVendaById(
            @PathVariable Integer id) {
        return ResponseEntity.ok(canalVendaService.findById(id));
    }

    @GetMapping(params = "nome")
    public ResponseEntity<CanalVendaResponse> getCanalVendaByName(
            @RequestParam("nome") String nome) {
        return ResponseEntity.ok(canalVendaService.findByName(nome));
    }

    @PostMapping
    public ResponseEntity<CanalVendaResponse> createCanalVenda(@Valid @RequestBody CanalVendaRequest request) {
        CanalVendaResponse response = canalVendaService.save(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> updateCanalVenda(
            @PathVariable Integer id,
            @RequestBody CanalVendaRequest request) {

        canalVendaService.update(id, request);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCanalVenda(
            @PathVariable Integer id) {

        canalVendaService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> handleIllegalArgumentException(IllegalArgumentException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(ex.getMessage());
    }
}
