package com.alexmegremis.funfun.persistence;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.*;

import javax.persistence.*;
import javax.persistence.Entity;
import java.util.*;

@Entity (name = "PRINCIPAL")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PrincipalEntity {

    @Id
    @Column (name = "ID")
    private Integer id;

    @Column (name = "NAME")
    private String name;

    @Column (name = "ID_PERSON_OWNER")
    private Integer idPersonOwner;

    @Column (name = "DATETIME_CREATED")
    @CreationTimestamp
    private Date datetimeCreated;

    @Column (name = "DATETIME_SUPERSEDED")
    @UpdateTimestamp
    private Date datetimeSuperseded;

    @JsonBackReference
    @ManyToOne (fetch = FetchType.LAZY)
    @JoinColumn (name = "ID_PERSON_OWNER", insertable = false, updatable = false)
    private PersonEntity owner;


//    @Filter (name = "ApplicationFilter", condition = "WHERE NAME=:ApplicationFilterParam")
    @OneToMany (fetch = FetchType.LAZY, mappedBy = "principal")
    private Set<MapPrincipalRepoEntity> repoMappings;

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final PrincipalEntity that = (PrincipalEntity) o;
        return getName().equals(that.getName()) && getIdPersonOwner().equals(that.getIdPersonOwner());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getName(), getIdPersonOwner());
    }
}
