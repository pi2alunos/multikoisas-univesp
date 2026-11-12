package org.multikoisas.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.multikoisas.dto.LojaRequest;
import org.multikoisas.dto.LojaResponse;
import org.multikoisas.dto.UpdateLojaRequest;
import org.multikoisas.service.LojaService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("/loja")
@RequiredArgsConstructor
public class LojaController {

    private final LojaService lojaService;

    @GetMapping
    public ResponseEntity<Object> getLoja(@RequestParam(required = false) Integer id, @RequestParam(required = false) String nomeLoja){
        List<LojaResponse> response = lojaService.getLoja(id, nomeLoja);
        return response.isEmpty() ? ResponseEntity.status(HttpStatus.NOT_FOUND).body("Descricao e nome da loja nao encontrada") :
                ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PostMapping
    public ResponseEntity<Object> createLoja(@Valid @RequestBody LojaRequest lojaRequest){
        LojaResponse response = lojaService.save(lojaRequest);
        return response != null ? ResponseEntity.status(HttpStatus.CREATED).body(Map.of("message","Loja cadastradda com sucesso.", "dados", response)) :
                ResponseEntity.status(HttpStatus.UNPROCESSABLE_CONTENT).body("Nao foi possivel fazer o registro");
    }

    @PutMapping("/{id}")
    public ResponseEntity<Object> updateLoja(@Valid @PathVariable Integer id, @RequestBody UpdateLojaRequest request){
        LojaResponse response = lojaService.update(id, request);
        return response != null ? ResponseEntity.status(HttpStatus.OK).body(Map.of("message","Descricao da loja " + response.getNomeLoja() + " atualizada com sucesso.", "dados", response)):
                ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("message", "Nao encontramos registros para essa loja."));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteLoja(@PathVariable Integer id){
        lojaService.delete(id);
        return ResponseEntity.status(HttpStatus.OK).body("Loja deletada com sucesso.");
    }

    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity<String> handleNoSuchElementException(
            NoSuchElementException ex) {

        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(ex.getMessage());
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> handlerIllegalArgumentException(
            IllegalArgumentException ex) {

        return ResponseEntity
                .status(HttpStatus.UNPROCESSABLE_CONTENT)
                .body(ex.getMessage());
    }
}
