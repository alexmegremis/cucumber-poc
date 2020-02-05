package com.alexmegremis.funfun.api;

import lombok.*;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class PersonEntity {
    private Integer id;
    private String nameFirst;
    private String nameLast;
}
