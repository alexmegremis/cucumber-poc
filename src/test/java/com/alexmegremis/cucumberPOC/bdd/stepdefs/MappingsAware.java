package com.alexmegremis.cucumberPOC.bdd.stepdefs;

import com.alexmegremis.cucumberPOC.persistence.application.PersonEntity;
import com.alexmegremis.cucumberPOC.persistence.application.PrincipalEntity;
import lombok.Getter;

import java.util.*;

public abstract class MappingsAware {

    @Getter
    protected static final List<PersonEntity>    globalPersons = new ArrayList<>();
    @Getter
    protected static final List<PrincipalEntity> globalPrincipals = new ArrayList<>();

    @Getter
    protected static final Map<String, Class> namedClasses = new HashMap<>();
    @Getter
    protected static final Map<String, List> namedContainers = new HashMap<>();

    static {
        namedClasses.put("Person", PersonEntity.class);
        namedClasses.put("Principal", PrincipalEntity.class);
        namedContainers.put("Person", globalPersons);
        namedContainers.put("Principal", globalPrincipals);
    }
}
