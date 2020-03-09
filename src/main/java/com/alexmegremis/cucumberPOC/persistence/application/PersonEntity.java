package com.alexmegremis.cucumberPOC.persistence.application;

import lombok.*;

import javax.persistence.*;
import java.util.Objects;
import java.util.Set;

@Entity(name = "PERSON")
@Data
@Builder
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

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "owner")
    private Set<PrincipalEntity> principals;

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final PersonEntity that = (PersonEntity) o;
        return getNameFirst().equals(that.getNameFirst()) && getNameLast().equals(that.getNameLast());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getNameFirst(), getNameLast());
    }
}
