package org.multikoisas.controller;

import lombok.RequiredArgsConstructor;
import org.multikoisas.dto.LojaImagemResponse;
import org.multikoisas.service.LojaImagemService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/imagemloja")
@RequiredArgsConstructor
public class LojaImagemController {

    private final LojaImagemService lojaImagemService;

    @GetMapping
    public ResponseEntity<Object> getImagemLojas(@RequestParam(required = false) Integer id , @RequestParam(required = false) String tipo) {
        List<LojaImagemResponse> response = lojaImagemService.getImagemLojas(id, tipo);
        return response.isEmpty() ? ResponseEntity.status(HttpStatus.NOT_FOUND).body("Nenhuma imagem encontrada") :
                ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<LojaImagemResponse> upload(@RequestPart("file") MultipartFile file,
                                                     @RequestParam("tipo") String tipo) {

        LojaImagemResponse response = lojaImagemService.save(file, tipo);

        if (response == null) {
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_CONTENT, "Erro ao salvar a imagem");
        }

        if (!response.getTipo().name().equalsIgnoreCase(tipo)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Arquivo invalido");
        }

        return ResponseEntity.status(HttpStatus.CREATED).body(response);

    }

    @DeleteMapping("/{tipo}")
    public ResponseEntity<Object> delete(@PathVariable String tipo, @RequestParam Integer id) {
        lojaImagemService.delete(id, tipo);
        return ResponseEntity.status(HttpStatus.OK).body("Imagem deletada com sucesso.");
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> handleIllegalArgumentException(IllegalArgumentException ex) {
        // Interceta a exceção de negócio e devolve o status 409 com a mensagem exata
        return ResponseEntity.status(HttpStatus.CONFLICT).body(ex.getMessage());
    }
}
