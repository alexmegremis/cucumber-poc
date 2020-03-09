package com.alexmegremis.cucumberPOC.bdd.stepdefs;

import com.alexmegremis.cucumberPOC.persistence.PersonEntity;
import com.alexmegremis.cucumberPOC.persistence.PrincipalEntity;
import io.cucumber.datatable.DataTable;
import io.cucumber.java8.En;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.util.*;
import java.util.function.Consumer;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.fail;

@Slf4j
public class IngestionStepDefs extends SpringIntegrationTest implements En {

    public static final Map<String, String> endpoints = new HashMap<>();

    static {
        endpoints.put("HR", "/api/v1/ingestions/hr/do");
        endpoints.put("GITHUB ENTERPRISE USER", "/api/v1/ingestions/gith/user/do");
    }

    private Map.Entry<String, String> endpoint;

    private String fileName;
    private File   file;

    public IngestionStepDefs() {

        Given("global Person entities", (final DataTable personsDataTable) -> parseDataTable(PersonEntity.class, SpringIntegrationTest :: setGlobalPersons, personsDataTable));
        Given("global Principal entities", (final DataTable principalsDataTable) -> parseDataTable(PrincipalEntity.class, SpringIntegrationTest :: setGlobalPrincipals, principalsDataTable));

        Given("^(.*) ingestion$", (final String ingestionType) -> {
            endpoint = endpoints.entrySet().stream().filter(e -> e.getKey().equals(ingestionType.toUpperCase())).findFirst().get();
        });

        Given("^file (.*)$", (final String fileDetail) -> {
            if (fileDetail.equalsIgnoreCase("default")) {
                return;
            }

            File file = new File(fileDetail);
            if (! file.exists()) {
                fail("Invalid ingestion file " + file.getPath() + " : " + file.getAbsolutePath());
            } else {
                this.file = file;
                this.fileName = fileDetail;
            }
        });

        When("ingestion is triggered", () -> {
            if (file == null) {
                doGet(String.class, endpoint.getValue());
            } else {
                doGet(String.class, endpoint.getValue(), "filename", fileName);
            }
        });

        Then("ingestion is successful", () -> {
            String message = (String) lastResponse.getBody();
            if (file != null) {
                assertThat("Ingestion response says " + message, message.endsWith(fileName));
            }
            // The above is rubish - monitor DB for actual ingestion completion
        });
    }

    private <T> void parseDataTable(final Class<T> clazz, Consumer<List<T>> consumer, final DataTable dataTable) {
        List<T> result = dataTable.asList(clazz);
        result = Collections.unmodifiableList(result);
        consumer.accept(result);
        log.info(">>> Parsed {} {}", result.size(), clazz.getName());
    }
}
