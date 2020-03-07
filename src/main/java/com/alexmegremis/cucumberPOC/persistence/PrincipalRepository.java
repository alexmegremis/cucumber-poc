package com.alexmegremis.cucumberPOC.persistence;

import org.springframework.data.jpa.repository.JpaRepository;

public interface PrincipalRepository extends JpaRepository<PrincipalEntity, Integer> {}
