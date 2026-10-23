package org.multikoisas.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor

public class UserRequest {

    @NotBlank(message = "O campo email e obrigatorio")
    private String email;
    @NotBlank(message = "O campo password e obrigatoria")
    private String password;
    @NotBlank(message = "O campo firstName e obrigatorio")
    private String firstName;
    private String lastName;
    @NotBlank(message = "O campo username e obrigatorio")
    private String username;

}
