package org.multikoisas.dto;

import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CategoriaProdutoRequest {

    @NotNull(message = "categoriaId e obrigatorio")
    private Integer categoriaId;

    @NotNull(message = "produtoId e obrigatorio")
    private Integer produtoId;
}
