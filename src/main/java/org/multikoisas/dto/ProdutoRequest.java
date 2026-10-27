package org.multikoisas.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProdutoRequest {

    @NotBlank(message = "o campo nome e obrigatorio")
    private String nome;
    private String descricaoProduto;
}