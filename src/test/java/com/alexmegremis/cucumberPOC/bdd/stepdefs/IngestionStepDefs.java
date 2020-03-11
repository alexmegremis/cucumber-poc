package com.alexmegremis.cucumberPOC.bdd.stepdefs;

import com.alexmegremis.cucumberPOC.persistence.batch.*;
import io.cucumber.datatable.DataTable;
import io.cucumber.java8.En;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.io.File;
import java.util.*;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.fail;

@Slf4j
public class IngestionStepDefs extends SpringIntegrationTest implements En {

    @Autowired
    private BatchJobExecutionRepository batchJobExecutionRepository;
    @Autowired
    private BatchJobInstanceRepository batchJobInstanceRepository;

    public static final Map<String, String> INGESTION_ENDPOINTS = new HashMap<>();

    static {
        INGESTION_ENDPOINTS.put("HR", "/api/v1/ingestions/hr/do");
        INGESTION_ENDPOINTS.put("GITHUB ENTERPRISE USER", "/api/v1/ingestions/gith/user/do");
    }

    private Map.Entry<String, String> endpoint;

    private String ingestionFileName;
    private File   file;

    public IngestionStepDefs() {

        // e.g. global Person entities
        Given("^global ([A-Za-z]*) entities$", (final String entityName, final DataTable personsDataTable) -> parseDataTable(entityName, personsDataTable));
        // e.g. HR ingestion
        Given("^(.*) ingestion$", (final String ingestionType) -> setIngestion(ingestionType));
        // file testData/HR_CADEV-237.csv
        Given("^file (.*)$", (final String fileName) -> setFile(fileName));
        When("^ingestion is triggered$", () -> {

            log.info(">>> BDD: Batch DB reset");
            batchJobExecutionRepository.deleteAll();
            batchJobInstanceRepository.deleteAll();

            ResponseEntity<String> ingestionResponse;

            if (file != null && file.exists()) {
                ingestionResponse = doGet(String.class, endpoint.getValue(), "filename", ingestionFileName);
            } else {
                ingestionResponse = doGet(String.class, endpoint.getValue());
            }

            assertThat("Ingestion request to " + endpoint.getValue() + " responded with code " + ingestionResponse.getStatusCode(),
                       ingestionResponse.getStatusCode().equals(HttpStatus.OK));
//            assertThat("Ingestion response was " + ingestionResponse.getBody(), ingestionResponse.getBody().endsWith(ingestionFileName));
        });

        Then("ingestion is successful", () -> {

           BatchJobExecutionEntity batchJobExecutionEntity = BatchJobExecutionEntity.builder().status("COMPLETE").build();
           Example<BatchJobExecutionEntity> example = Example.of(batchJobExecutionEntity);
           Boolean ingestionComplete = true;
           Boolean previousStatus = true;
           Integer loopCount = 0;

            do {
                loopCount++;
                final Optional<BatchJobExecutionEntity> statusCompleted = batchJobExecutionRepository.findOne(example);
                ingestionComplete = statusCompleted.isPresent();
                if(!ingestionComplete) {
                    if(!previousStatus) {
                        log.info(">>> BDD: Ingestion still running. Halt and wait.");
                        previousStatus = ingestionComplete;
                    }

                    if(loopCount % 40 == 0) {
                        log.info(">>> BDD: Still waiting for ingestion completion.");
                    }
                } else {
                    log.info(">>> BDD: Ingestion completed. Proceeding.");
                }
                if(!ingestionComplete) {
                    Thread.sleep(250L);
                }
            } while(!ingestionComplete);

        });
    }

    private void setIngestion(final String ingestionName) {
        final Optional<Map.Entry<String, String>> first = INGESTION_ENDPOINTS.entrySet().stream().filter(e -> e.getKey().equals(ingestionName.toUpperCase())).findFirst();
        if (! first.isPresent()) {
            fail("Invalid ingestion name " + ingestionName);
        }
        endpoint = first.get();
    }

    private void setFile(final String fileName) throws Exception {
        if (fileName.equalsIgnoreCase("default")) {
            return;
        }

        File        file        = new File(fileName);
        File foo = new File(this.getClass().getResource(fileName).toURI());
        if (! file.exists()) {
            fail("Invalid ingestion file " + file.getPath() + " : " + file.getAbsolutePath());
        } else {
            this.file = file;
            this.ingestionFileName = fileName;
        }
    }

    private <T> void parseDataTable(final String entityName, final DataTable dataTable) {
        Class<T>      clazz      = MappingsAware.namedClasses.get(entityName);
        final List<T> collection = MappingsAware.namedContainers.get(entityName);
        List<T>       result     = dataTable.asList(clazz);
        collection.clear();
        collection.addAll(result);
        log.info(">>> BDD: Parsed {} {}", result.size(), clazz.getName());
    }
}
