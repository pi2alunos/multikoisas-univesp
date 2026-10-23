package org.multikoisas.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor

public class SocialNetworkRequest
{
    @NotBlank(message = "O campo descricao e obrigatoria")
    private String descricao;
    @NotBlank(message = "O campo link e obrigatorio")
    private String link;
}
