package com.alexmegremis.funfun.persistence;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Set;

public interface RepoRepository extends JpaRepository<RepoEntity, Integer> {

    Set<RepoEntity> findAllByApplicationIn(final Set<ApplicationEntity> application);
}
