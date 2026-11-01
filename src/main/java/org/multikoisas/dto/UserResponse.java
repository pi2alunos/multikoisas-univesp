package org.multikoisas.dto;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor

public class UserResponse {

    private Integer id;
    private String email;
    private String username;
    private String role;

}
