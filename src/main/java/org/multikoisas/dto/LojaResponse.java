package org.multikoisas.dto;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LojaResponse {

    private Integer id;
    private String nomeLoja;
    private String descricaoLoja;
    private String endereco;
}
