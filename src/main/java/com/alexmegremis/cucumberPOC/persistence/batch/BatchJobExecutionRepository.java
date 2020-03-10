package com.alexmegremis.cucumberPOC.persistence.batch;

import org.springframework.data.jpa.repository.JpaRepository;

public interface BatchJobExecutionRepository extends JpaRepository<BatchJobExecutionEntity, Integer> {}
