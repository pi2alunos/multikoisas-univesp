package org.multikoisas.dto;

import jakarta.validation.constraints.NotBlank;
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
    @NotBlank(message = "o campo link e obrigatorio")
    private String link;
}