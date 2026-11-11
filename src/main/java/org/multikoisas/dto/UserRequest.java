package org.multikoisas.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.multikoisas.enums.Role;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor

public class UserRequest {

    @NotBlank(message = "O campo email e obrigatorio")
    private String email;
    @NotBlank(message = "O campo username e obrigatorio")
    private String username;
    @NotBlank(message = "O campo password e obrigatoria")
    private String password;
    @NotNull(message="o campo role e obrigatorio")
    private Role role;

}
