package com.alexmegremis.cucumberPOC.persistence.application;

import org.springframework.data.jpa.repository.JpaRepository;

public interface PrincipalRepository extends JpaRepository<PrincipalEntity, Integer> {}
