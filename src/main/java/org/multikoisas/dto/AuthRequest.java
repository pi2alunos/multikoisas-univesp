package org.multikoisas.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AuthRequest {

    @NotBlank(message = "O campo username e obrigatorio")
    private String username;
    @NotBlank(message = "O campo password e obrigatoria")
    private String password;
}
