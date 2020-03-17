package com.alexmegremis.cucumberPOC.bdd.stepdefs;

import com.alexmegremis.cucumberPOC.persistence.application.PersonEntity;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java8.En;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.jdbc.ScriptRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.data.domain.Example;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.support.Repositories;

import java.io.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.*;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

@Slf4j
public class DBStepDefs extends SpringIntegrationTest implements En {

    private Long                   rowCount = 0l;
    private Optional<PersonEntity> person;
    private Connection             applicationDBConnection;
    private Connection             batchDBConnection;
    private ScriptRunner           applicationScriptRunner;
    private ScriptRunner           batchScriptRunner;

    @Autowired
    private ApplicationContext applicationContext;

    @Value("${application.datasource.jdbcUrl}")
    private String applicationDataSourceURL;
    @Value("${application.datasource.username}")
    private String applicationDataSourceUsername;
    @Value("${batch.datasource.jdbcUrl}")
    private String batchDataSourceURL;
    @Value("${batch.datasource.username}")
    private String batchDataSourceUsername;

    @Before
    public void setUp() throws Throwable {
        super.setUp();

        DriverManager.registerDriver(new org.h2.Driver());
        applicationDBConnection = DriverManager.getConnection(applicationDataSourceURL, applicationDataSourceUsername, null);
        log.info(">>> DBB: Connection established to {}", applicationDataSourceURL);
        batchDBConnection = DriverManager.getConnection(batchDataSourceURL, batchDataSourceUsername, null);
        log.info(">>> DBB: Connection established to {}", batchDataSourceURL);
        applicationScriptRunner = new ScriptRunner(applicationDBConnection);
        batchScriptRunner = new ScriptRunner(batchDBConnection);
    }

    @After
    public void tearDown() throws Throwable {
        super.tearDown();

        applicationDBConnection.close();
        log.info(">>> Connection closed");
        applicationScriptRunner = null;
    }

    public DBStepDefs() {
        Then("^local ([A-Za-z]*) is( not)? found that looks like$",
             (final String entityName, final String negativeCondition, final DataTable dataTable) -> handleLocalExamples(entityName, StringUtils.isEmpty(negativeCondition), dataTable));
        Then("^global ([A-Za-z]*) is( not)? found that looks like$",
             (final String entityName, final String negativeCondition, final DataTable dataTable) -> handleGlobalExamples(entityName, StringUtils.isEmpty(negativeCondition), dataTable));

        Given("^the DBs were reset$", () -> {
            runScript("/reset.sql", applicationScriptRunner);
            runScript("/schemaFull.sql", applicationScriptRunner);
            runScript("/batchH2Schema.sql", batchScriptRunner);
        });

        Given("^the (application|batch) DB has loaded (.*)$", (final String db, final String fileName) -> runScript("/" + fileName + ".sql",
                                                                                                                    db.equals("application") ?
                                                                                                                    applicationScriptRunner :
                                                                                                                    batchScriptRunner));

        Then("^table(?:s?) ([A-Z, ]*) (?:have|has) (exactly|under|over) ([0-9]*) row(?:s?)$", (final String concatenatedTableNames, final String countRule, final Integer count) -> {
            log.info(">>> BDD: Will check for tables " + concatenatedTableNames);
            Arrays.asList(concatenatedTableNames.replaceAll(" ", "").split(",")).forEach(t -> verifyPopulatedTable(t, countRule, count));
        });
    }

    private void verifyPopulatedTable(final String tableName, final String countRule, final Integer expectedCount) {
        final Class entityClass = MappingsAware.getClassByNameIgnoreCase(tableName);
        final JpaRepository repository = getRepository(entityClass);
        final long actualCount = repository.count();

        String message = String.format(">>> Table %s had %d rows. It was expected to have %s %d.", tableName, actualCount, countRule, actualCount);

        switch (countRule){
            case "exactly" :
                assertTrue(message,actualCount == expectedCount);
                break;
            case "under" :
                assertTrue(message,actualCount < expectedCount);
                break;
            case "over" :
                assertTrue(message,actualCount > expectedCount);
                break;
        }

        log.info(message);
    }

    private <T> void handleLocalExamples(final String entityName, final Boolean mustHave, final DataTable dataTable) {
        Class<T> clazz = MappingsAware.getClassByNameIgnoreCase(entityName);
        doHandleLocalExamples(clazz,mustHave, dataTable);
    }

    private <T> void doHandleLocalExamples(final Class<T> clazz, final Boolean mustHave, final DataTable dataTable) {
        List<T> exampleEntities = dataTable.asList(clazz);
        for (T anExampleEntity : exampleEntities) {
            verifyFoundExampleEntity(anExampleEntity, clazz, mustHave);
        }
    }

    private <T> void handleGlobalExamples(final String entityName, final Boolean mustHave, final DataTable dataTable) {
        Class<T>      clazz     = MappingsAware.getClassByNameIgnoreCase(entityName);
        final List<T> container = MappingsAware.getContainerByNameIgnoreCase(entityName);
        doHandleGlobalExamples(clazz, mustHave, container, dataTable);
    }

    private <T> void doHandleGlobalExamples(final Class<T> clazz, final Boolean mustHave, List<T> globalExamples, final DataTable dataTable) {
        List<Integer> indices = dataTable.asList(Integer.class);
        for (Integer i : indices) {
            final T exampleEntity = globalExamples.get(i);
            verifyFoundExampleEntity(exampleEntity, clazz, mustHave);
        }
    }

    private <T> void verifyFoundExampleEntity(final T exampleEntity, final Class<T> clazz, final Boolean mustHave) {
        final JpaRepository<T, Integer> repository = getRepository(clazz);
        Example<T>    example = Example.of(exampleEntity);
        final List<T> result  = repository.findAll(example);

        String message = String.format(">>> Was expecting to find %sinstance(s) of %s that looked like %s. Found exactly %d.", mustHave ? "at least 1" : "",
                                       clazz.getSimpleName(), example.toString(), result.size());

        assertThat(message, result.isEmpty() == !mustHave);

        log.info(message);
    }

    public <T> JpaRepository<T, Integer> getRepository(final Class<T> clazz) {
        Repositories           repositories = new Repositories(applicationContext);
        final Optional<Object> result       = repositories.getRepositoryFor(clazz);
        return (JpaRepository<T, Integer>) result.get();
    }

    public void runScript(final String name, final ScriptRunner scriptRunner) throws IOException {
        InputStream inputStream = this.getClass().getResourceAsStream(name);
        if (inputStream == null) {
            fail("input file not found : " + name);
        }
        Reader reader = new BufferedReader(new InputStreamReader(inputStream));
        scriptRunner.setLogWriter(null);
        scriptRunner.runScript(reader);
    }
}
