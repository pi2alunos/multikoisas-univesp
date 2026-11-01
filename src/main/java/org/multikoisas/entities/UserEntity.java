package org.multikoisas.entities;


import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class UserEntity {

    private Integer id;
    private String email;
    private String password;
    private String username;
    private String role;
}
