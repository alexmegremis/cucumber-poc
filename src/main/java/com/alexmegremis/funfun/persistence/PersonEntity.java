package com.alexmegremis.funfun.persistence;

import lombok.*;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;

@Entity(name = "PERSON")
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class PersonEntity {

    @Id
    @Column(name = "ID")
    private Integer id;

    @Column(name = "NAME_FIRST")
    private String nameFirst;

    @Column(name = "NAME_LAST")
    private String nameLast;
}
