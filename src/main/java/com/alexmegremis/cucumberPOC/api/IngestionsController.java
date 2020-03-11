package com.alexmegremis.cucumberPOC.api;

import com.alexmegremis.cucumberPOC.persistence.batch.*;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Slf4j
@RestController
@RequestMapping (value = "/api/v1/ingestions")
public class IngestionsController {

    @Autowired
    private BatchJobExecutionRepository batchJobExecutionRepository;
    @Autowired
    private BatchJobInstanceRepository batchJobInstanceRepository;

    @GetMapping (value = "hr/do", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    @ResponseStatus (code = HttpStatus.OK)
    public String ingestHR(@RequestParam (name = "filename", required = false) final String name) {
        log.info(">>> HR ingestion called");

        BatchJobInstanceEntity instanceEntity = BatchJobInstanceEntity.builder().jobKey("HR").jobName("HRIngest" + Instant.now().toEpochMilli()).version(1l).build();
        instanceEntity = batchJobInstanceRepository.save(instanceEntity);
        log.info(">>> Saved {}", instanceEntity.toString());

        BatchJobExecutionEntity executionEntity = BatchJobExecutionEntity.builder().status("PENDING").createTime(Timestamp.from(Instant.now())).jobInstanceId(instanceEntity.getJobInstanceId()).build();
        executionEntity = batchJobExecutionRepository.save(executionEntity);
        log.info(">>> Saved {}", executionEntity.toString());

        log.info(">>> batch execution {} recorded PENDING", executionEntity.getJobExecutionId());

        final BatchJobExecutionEntity finalExecutionEntity = executionEntity;
        Runnable updater = new Runnable() {
            @SneakyThrows
            @Override
            public void run() {
                final Integer random = getRandomNumberInRange(1500, 5000);
                Thread.sleep(random);
                finalExecutionEntity.setStatus("COMPLETE");
                batchJobExecutionRepository.save(finalExecutionEntity);

                log.info(">>> batch execution {} recorded COMPLETED {}ms later", finalExecutionEntity.getJobExecutionId(), random);
            }
        };

        ExecutorService executorService = Executors.newSingleThreadExecutor();
        executorService.submit(updater);

        return name;
    }

    @GetMapping (value = "status", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    @ResponseStatus (code = HttpStatus.OK)
    public List<BatchJobExecutionEntity> checkIngestionDB() {
        return batchJobExecutionRepository.findAll();
    }

    private static Integer getRandomNumberInRange(final Integer min, final Integer max) {

        if (min >= max) {
            throw new IllegalArgumentException("max must be greater than min");
        }

        Random r = new Random();
        return r.nextInt((max - min) + 1) + min;
    }
}