package com.alexmegremis.cucumberPOC.api;

import com.alexmegremis.cucumberPOC.persistence.batch.BatchJobExecutionEntity;
import com.alexmegremis.cucumberPOC.persistence.batch.BatchJobExecutionRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Slf4j
@RestController
@RequestMapping (value = "/api/v1")
public class IngestionsController {

    @Autowired
    private BatchJobExecutionRepository batchJobExecutionRepository;

    @GetMapping (value = "ingestions/hr/do", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    @ResponseStatus (code = HttpStatus.OK)
    public ResponseEntity<String> ingestHR(@RequestParam (name = "filename", required = false) final String name) {
        log.info(">>> HR ingestion called");
        return ResponseEntity.of(Optional.ofNullable(name));
    }

    @GetMapping (value = "ingestions/status", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    @ResponseStatus (code = HttpStatus.OK)
    public ResponseEntity<List<BatchJobExecutionEntity>> checkIngestionDB() {
        return ResponseEntity.of(Optional.ofNullable(batchJobExecutionRepository.findAll()));
    }
}