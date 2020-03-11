package com.alexmegremis.cucumberPOC.persistence.batch;

import org.springframework.data.jpa.repository.JpaRepository;

public interface BatchJobInstanceRepository extends JpaRepository<BatchJobInstanceEntity, Long> {}
