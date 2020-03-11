package com.alexmegremis.cucumberPOC.bdd.stepdefs;

import com.alexmegremis.cucumberPOC.persistence.application.PersonEntity;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java8.En;
import lombok.extern.slf4j.Slf4j;
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
import java.util.List;
import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;

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
        batchScriptRunner = new ScriptRunner(applicationDBConnection);
    }

    @After
    public void tearDown() throws Throwable {
        super.tearDown();

        applicationDBConnection.close();
        log.info(">>> Connection closed");
        applicationScriptRunner = null;
    }

    public DBStepDefs() {
        Then("^local ([A-Za-z]*) is found that looks like$",
             (final String entityName, final DataTable dataTable) -> doParseLocalExamples(entityName, dataTable));
        Then("^global ([A-Za-z]*) is found that looks like$",
             (final String entityName, final DataTable dataTable) -> doHandleGlobalExamples(entityName, dataTable));

        Given("^the DBs were reset$", () -> {
            runScript("/reset.sql", applicationScriptRunner);
            runScript("/schemaComplete.sql", applicationScriptRunner);
            runScript("/batchH2Schema.sql", batchScriptRunner);
        });

        Given("^the (application|batch) DB has loaded (.*)$", (final String db, final String fileName) -> runScript("/" + fileName + ".sql",
                                                                                                                  db.equals("application") ?
                                                                                                                  applicationScriptRunner :
                                                                                                                  batchScriptRunner));

        Then("^table(?:s?) ([A-Z,]*) (?:have|has) ([a-z]*) ([0-9]*) rows$", (final String tableNames, final String countRule, final Integer count) -> {
            log.info(">>> BDD: Will check for tables " + tableNames);
        });
    }

    private <T> void doParseLocalExamples(final String entityName, final DataTable dataTable) {
        Class<T> clazz = MappingsAware.namedClasses.get(entityName);
        doParseLocalExamples(clazz, dataTable);
    }

    private <T> void doParseLocalExamples(final Class<T> clazz, final DataTable dataTable) {
        List<T> exampleEntities = dataTable.asList(clazz);
        for (T anExampleEntity : exampleEntities) {
            verifyFoundExampleEntity(anExampleEntity, clazz);
        }
    }

    private <T> void doHandleGlobalExamples(final String entityName, final DataTable dataTable) {
        Class<T>      clazz     = MappingsAware.namedClasses.get(entityName);
        final List<T> container = MappingsAware.namedContainers.get(entityName);
        doHandleGlobalExamples(clazz, container, dataTable);
    }

    private <T> void doHandleGlobalExamples(final Class<T> clazz, List<T> globalExamples, final DataTable dataTable) {
        List<Integer> indices = dataTable.asList(Integer.class);
        for (Integer i : indices) {
            final T exampleEntity = globalExamples.get(i);
            verifyFoundExampleEntity(exampleEntity, clazz);
        }
    }

    private <T> void verifyFoundExampleEntity(final T exampleEntity, final Class<T> clazz) {
        final JpaRepository<T, Integer> repository = getRepository(clazz);
        Example<T>                      example    = Example.of(exampleEntity);
        final Optional<T>               result     = repository.findOne(example);
        assertThat(clazz.getSimpleName() + " from example " + exampleEntity.toString() + " was not found", result.isPresent());
    }

    public <T> JpaRepository<T, Integer> getRepository(final Class<T> clazz) {
        Repositories           repositories = new Repositories(applicationContext);
        final Optional<Object> result       = repositories.getRepositoryFor(clazz);
        return (JpaRepository<T, Integer>) result.get();
    }

    public void runScript(final String name, final ScriptRunner scriptRunner) throws IOException {
        InputStream inputStream = this.getClass().getResourceAsStream(name);
        Reader      reader      = new BufferedReader(new InputStreamReader(inputStream));
        scriptRunner.setLogWriter(null);
        scriptRunner.runScript(reader);
    }
}
