package com.alexmegremis.funfun.persistence;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.*;

import javax.persistence.*;
import javax.persistence.Entity;
import java.util.Date;

@Entity (name = "MAP_PRINCIPAL_REPO")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MapPrincipalRepoEntity {

    @Id
    @Column (name = "ID")
    private Integer id;

    @Column (name = "ID_PRINCIPAL")
    private Integer idPrincipal;

    @Column (name = "ID_REPO")
    private Integer idRepo;

    @Column (name = "PERMISSION")
    private String permission;

    @Column (name = "PERMISSION_BITS")
    private Integer permissionBits;

    @Column (name = "DATETIME_CREATED")
    @CreationTimestamp
    private Date datetimeCreated;

    @Column (name = "DATETIME_SUPERSEDED")
    @UpdateTimestamp
    private Date datetimeSuperseded;

    @ManyToOne (fetch = FetchType.EAGER)
    @JoinColumn (name = "ID_REPO", updatable = false, insertable = false)
//    @Filter (name = "ApplicationFilter", condition = "WHERE NAME=:ApplicationFilterParam")
    private RepoEntity repo;

    @JsonBackReference
    @ManyToOne (fetch = FetchType.LAZY)
    @JoinColumn (name = "ID_PRINCIPAL", updatable = false, insertable = false)
    private PrincipalEntity principal;
}
