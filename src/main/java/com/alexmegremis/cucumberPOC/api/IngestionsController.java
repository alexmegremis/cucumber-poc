package com.alexmegremis.cucumberPOC.api;

import com.alexmegremis.cucumberPOC.persistence.batch.BatchJobExecutionEntity;
import com.alexmegremis.cucumberPOC.persistence.batch.BatchJobExecutionRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;

@Slf4j
@RestController
@RequestMapping (value = "/api/v1")
public class IngestionsController {

    @Autowired
    private BatchJobExecutionRepository batchJobExecutionRepository;

    @GetMapping (value = "ingestions/hr/do", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    @ResponseStatus (code = HttpStatus.OK)
    public String ingestHR(@RequestParam (name = "filename", required = false) final String name) {
        log.info(">>> HR ingestion called");
        BatchJobExecutionEntity entity = BatchJobExecutionEntity.builder().status("PENDING").createTime(Timestamp.from(Instant.now())).jobExecutionId(22l).build();
        batchJobExecutionRepository.save(entity);
        return name;
    }

    @GetMapping (value = "ingestions/status", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    @ResponseStatus (code = HttpStatus.OK)
    public List<BatchJobExecutionEntity> checkIngestionDB() {
        return batchJobExecutionRepository.findAll();
    }
}