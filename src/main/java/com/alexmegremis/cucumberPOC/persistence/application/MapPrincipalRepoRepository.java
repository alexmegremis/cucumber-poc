package com.alexmegremis.cucumberPOC.persistence.application;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Set;

public interface MapPrincipalRepoRepository extends JpaRepository<MapPrincipalRepoEntity, Integer> {

    Set<MapPrincipalRepoEntity> findAllByRepoInAndPrincipalIn(final Set<RepoEntity> repo, final Set<PrincipalEntity> principal);
}
