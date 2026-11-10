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
    public ResponseEntity<Object> getLoja(@RequestParam(required = false) String nome){
        List<LojaResponse> response = lojaService.getLoja(nome);
        return response.isEmpty() ? ResponseEntity.status(HttpStatus.NOT_FOUND).body("Descricao e nome da loja nao encontrada") :
                ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PostMapping
    public ResponseEntity<Object> createLoja(@Valid @RequestBody LojaRequest lojaRequest){
        LojaResponse response = lojaService.save(lojaRequest);
        return response != null ? ResponseEntity.status(HttpStatus.CREATED).body(Map.of("message","Loja e descricao registrada.", "dados", response)) :
                ResponseEntity.status(HttpStatus.UNPROCESSABLE_CONTENT).body("Nao foi possivel fazer o registro");
    }

    @PutMapping("/{nomeLoja}")
    public ResponseEntity<Object> updateLoja(@Valid @PathVariable String nomeLoja, @RequestBody UpdateLojaRequest updateLojaRequest){
        LojaResponse response = lojaService.update(nomeLoja, updateLojaRequest.getDescricaoLoja());
        return response != null ? ResponseEntity.status(HttpStatus.OK).body(Map.of("message","Descricao da loja " + nomeLoja + " atualizada com sucesso.", "dados", response)):
                ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("message", "Nao encontramos registros para " + nomeLoja + "."));
    }

    @DeleteMapping
    public ResponseEntity<Object> deleteLoja(@RequestParam String nome){
        lojaService.delete(nome);
        return ResponseEntity.status(HttpStatus.OK).body("Nome e descricao da loja " + nome + " deletado com sucesso.");
    }

    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity<String> handleNoSuchElementException(
            NoSuchElementException ex) {

        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(ex.getMessage());
    }
}
