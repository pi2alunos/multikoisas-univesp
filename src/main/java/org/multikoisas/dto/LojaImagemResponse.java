package org.multikoisas.dto;


import lombok.*;
import org.multikoisas.enums.LojaImagemTipo;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LojaImagemResponse {

    private Integer id;
    private String imagemUrl;
    private LojaImagemTipo tipo;
}
