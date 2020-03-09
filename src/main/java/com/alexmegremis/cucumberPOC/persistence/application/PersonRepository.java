package com.alexmegremis.cucumberPOC.persistence.application;

import org.springframework.data.jpa.repository.JpaRepository;

public interface PersonRepository extends JpaRepository<PersonEntity, Integer> {}
