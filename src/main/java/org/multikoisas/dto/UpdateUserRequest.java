package org.multikoisas.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.*;
import org.multikoisas.enums.Role;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UpdateUserRequest {

    private String email;
    @NotBlank(message = "O campo password e obrigatoria")
    private String password;
    @NotBlank(message="o campo role e obrigatorio")
    private Role role;
}
