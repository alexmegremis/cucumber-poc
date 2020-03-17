package com.alexmegremis.cucumberPOC.bdd.stepdefs;

import com.alexmegremis.cucumberPOC.persistence.application.PersonEntity;
import com.alexmegremis.cucumberPOC.persistence.application.PrincipalEntity;

import java.util.*;

public abstract class MappingsAware {

    private static final List<PersonEntity>    globalPersons    = new ArrayList<>();
    private static final List<PrincipalEntity> globalPrincipals = new ArrayList<>();

    private static final Map<String, Class> namedClasses    = new HashMap<>();
    private static final Map<String, List>  namedContainers = new HashMap<>();

    static {
        namedClasses.put("PERSON", PersonEntity.class);
        namedClasses.put("PRINCIPAL", PrincipalEntity.class);
        namedContainers.put("PERSON", globalPersons);
        namedContainers.put("PRINCIPAL", globalPrincipals);
    }

    static Class getClassByNameIgnoreCase(final String name) {
        return namedClasses.get(name.toUpperCase());
    }

    static List getContainerByNameIgnoreCase(final String name) {
        return namedContainers.get(name.toUpperCase());
    }
}
