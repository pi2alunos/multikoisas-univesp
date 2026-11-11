package org.multikoisas.dto;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateLojaRequest {

    private String descricaoLoja;
    private String endereco;
}
