package org.multikoisas.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.List;

@Getter
@AllArgsConstructor // aqui é necessário para que o construtor seja gerado, permitindo a inicialização da lista de authorities
public enum Role {

    ADMIN(
            List.of(
                    new SimpleGrantedAuthority("ROLE_ADMIN"),
                    new SimpleGrantedAuthority("ROLE_USER")
            )
    ),

    USER(
            List.of(
                    new SimpleGrantedAuthority("ROLE_USER")
            )
    );

    private final List<GrantedAuthority> authorities;
}
