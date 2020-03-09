package com.alexmegremis.cucumberPOC.persistence.application;

import lombok.*;
import org.hibernate.annotations.*;

import javax.persistence.*;
import javax.persistence.Entity;
import java.util.Objects;

@Entity (name = "REPO")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FilterDef(name = "ApplicationFilter", parameters = @ParamDef(name = "ApplicationFilterParam", type = "string"))
public class RepoEntity {

    @Id
    @Column (name = "ID")
    private Integer id;

    @Column (name = "NAME")
    private String name;

    @Column (name = "ID_APPLICATION")
    private Integer idApplication;

    @ManyToOne (fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_APPLICATION", insertable = false, updatable = false)
//    @Filter(name = "ApplicationFilter", condition = "WHERE NAME=:ApplicationFilterParam")
    private ApplicationEntity application;

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final RepoEntity that = (RepoEntity) o;
        return getName().equals(that.getName()) && getIdApplication().equals(that.getIdApplication());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getName(), getIdApplication());
    }
}
