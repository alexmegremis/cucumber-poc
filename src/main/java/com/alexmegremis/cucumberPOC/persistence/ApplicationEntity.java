package com.alexmegremis.cucumberPOC.persistence;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

import javax.persistence.*;
import java.util.Objects;
import java.util.Set;

@Entity (name = "APPLICATION")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ApplicationEntity {

    @Id
    @Column (name = "ID")
    private Integer id;

    @Column (name = "NAME")
    private String name;

    @Column (name = "IDENTIFIER")
    private String identifier;

    @JsonBackReference
    @JsonIgnoreProperties ({"hibernateLazyInitializer", "handler"})
    @OneToMany(mappedBy = "application", fetch = FetchType.LAZY)
    private Set<RepoEntity> repos;

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final ApplicationEntity that = (ApplicationEntity) o;
        return getName().equals(that.getName()) && getIdentifier().equals(that.getIdentifier());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getName(), getIdentifier());
    }
}
