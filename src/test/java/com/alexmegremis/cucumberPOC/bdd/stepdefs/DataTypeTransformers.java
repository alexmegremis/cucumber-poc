package com.alexmegremis.cucumberPOC.bdd.stepdefs;

import com.alexmegremis.cucumberPOC.persistence.PersonEntity;
import com.alexmegremis.cucumberPOC.persistence.PrincipalEntity;
import io.cucumber.java8.En;

import java.util.Map;

import static com.alexmegremis.cucumberPOC.bdd.stepdefs.BuilderSettersHelper.*;

public class DataTypeTransformers implements En {

    public DataTypeTransformers() {

        DataTableType((Map<String, String> row) -> {
            final PersonEntity.PersonEntityBuilder builder = PersonEntity.builder();

            setIntegerIfNotNull(row.get("id"), builder :: id);
            setStringIfNotNull(row.get("nameFirst"), builder :: nameFirst);
            setStringIfNotNull(row.get("nameLast"), builder :: nameLast);

            return builder.build();
        });

        DataTableType((Map<String, String> row) -> {
            final PrincipalEntity.PrincipalEntityBuilder builder = PrincipalEntity.builder();

            setIntegerIfNotNull(row.get("id"), builder :: id);
            setStringIfNotNull(row.get("name"), builder :: name);
            setIntegerIfNotNull(row.get("idPersonOwner"), builder :: idPersonOwner);
            setDateIfNotNull(row.get("datetimeSuperseded"), builder :: datetimeSuperseded);
            setDateIfNotNull(row.get("datetimeCreated"), builder :: datetimeCreated);

            return builder.build();
        });
    }
}
