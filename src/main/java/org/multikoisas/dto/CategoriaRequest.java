package org.multikoisas.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CategoriaRequest {

    private Integer id;
    @NotBlank(message = "O campo nome e obrigatorio")
    private String nome;
}
