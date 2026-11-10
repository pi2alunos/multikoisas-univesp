package org.multikoisas.entities;

import lombok.*;
import org.multikoisas.enums.LojaImagemTipo;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LojaImagemEntity {

    private Integer id;
    private String imagemUrl;
    private LojaImagemTipo tipo;

}
