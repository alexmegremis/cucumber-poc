package com.alexmegremis.cucumberPOC.bdd.stepdefs;

import com.alexmegremis.cucumberPOC.persistence.PersonEntity;
import com.alexmegremis.cucumberPOC.persistence.PrincipalEntity;
import io.cucumber.datatable.DataTable;
import io.cucumber.java8.En;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.*;

import static org.junit.Assert.fail;

public class IngestionStepDefs implements En {

    public static final Map<String, String> endpoints = new HashMap<>();

    static {
        endpoints.put("HR", "/api/v1/ingestions/hr/do");
        endpoints.put("GITHUB ENTERPRISE USER", "/api/v1/ingestions/gith/user/do");
    }

    private Map.Entry<String, String> endpoint;

    private List<PersonEntity> persons;
    private List<PrincipalEntity> principals;

    private String fileName;
    private File   file;

    public IngestionStepDefs() {

        Given("Person entities", (final DataTable personsDataTable) -> {
            persons = personsDataTable.asList(PersonEntity.class);
            System.out.println("parsed " + persons.size() + " persons");
        });

        Given("Principal entities", (final DataTable principalsDataTable) -> {
            principals = principalsDataTable.asList(PrincipalEntity.class);
            System.out.println("parsed " + principals.size() + " principals");
        });

        Given("^(.*) ingestion$", (final String ingestionType) -> {
            endpoint = endpoints.entrySet().stream().filter(e -> e.getKey().equals(ingestionType.toUpperCase())).findFirst().get();
        });

        Given("^file (.*)$", (final String fileDetail) -> {
            if (fileDetail.equalsIgnoreCase("default")) {
                return;
            }

            File file = new File(fileDetail);
            if (! file.exists()) {
                fail("Invalid ingestion file " + file.getPath());
            } else {
                this.file = file;
                this.fileName = fileDetail;
            }
        });

        When("ingestion is done", () -> {

        });

        DataTableType((Map<String, String> row) ->
            PersonEntity.builder()
                        .id(Integer.valueOf(row.get("id")))
                        .nameFirst(row.get("nameFirst"))
                        .nameLast(row.get("nameLast"))
                        .build()
        );

        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);

        DataTableType((Map<String, String> row) -> {
            PrincipalEntity.PrincipalEntityBuilder builder = PrincipalEntity.builder()
                                                                                  .id(Integer.valueOf(row.get("id")))
                                                                                  .name(row.get("name"))
                                                                                  .idPersonOwner(Integer.valueOf(row.get("idPersonOwner")))
                                                                                  .datetimeCreated(formatter.parse(row.get("datetimeCreated")));

            if (StringUtils.isNotBlank(row.get("datetimeSuperseded"))) {
                System.out.println(">>> Adding superseded date");
                builder.datetimeSuperseded(formatter.parse(row.get("datetimeSuperseded")));
            } else {
                System.out.println(">>> Skipping superseded date");
            }

            return builder.build();
        });
    }
}
