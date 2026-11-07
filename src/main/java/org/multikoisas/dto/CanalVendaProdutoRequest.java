package org.multikoisas.dto;

import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CanalVendaProdutoRequest {

    @NotNull(message = "o campo produtoId e obrigatorio")
    private Integer produtoId;
    @NotNull(message = "o campo canalVendaId e obrigatorio")
    private Integer canalVendaId;
    private String link;
}