package org.multikoisas.entities;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class LojaEntity {

    private Integer id;
    private String nomeLoja;
    private String descricaoLoja;


}
