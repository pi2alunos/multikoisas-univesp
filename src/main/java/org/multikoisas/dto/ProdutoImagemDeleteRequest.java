package org.multikoisas.dto;

import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProdutoImagemDeleteRequest {

    @NotNull(message = "O campo id e obrigatorio")
    private Integer id;

    @NotNull(message = "O campo produtoId e obrigatorio")
    private Integer produtoId;

}
