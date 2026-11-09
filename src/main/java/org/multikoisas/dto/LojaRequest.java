package org.multikoisas.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LojaRequest {
    @NotBlank(message = "O nome da loja e obrigatorio.")
    private String nomeLoja;
    @NotBlank(message = "A descricao da loja e obrigatoria.")
    private String descricaoLoja;
}
