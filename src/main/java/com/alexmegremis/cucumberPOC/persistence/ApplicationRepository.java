package com.alexmegremis.cucumberPOC.persistence;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Set;

public interface ApplicationRepository extends JpaRepository<ApplicationEntity, Integer> {

    Set<ApplicationEntity> findAllByIdentifierIn(final Set<String> name);
}
