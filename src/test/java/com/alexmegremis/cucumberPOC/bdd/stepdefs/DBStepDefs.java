package com.alexmegremis.cucumberPOC.bdd.stepdefs;

import com.alexmegremis.cucumberPOC.persistence.*;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.*;
import io.cucumber.java.en.*;
import io.cucumber.java8.En;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ArrayUtils;
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
import java.util.function.Consumer;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

@Slf4j
public class DBStepDefs extends SpringIntegrationTest implements En {

    private Long                   rowCount = 0l;
    private Optional<PersonEntity> person;
    private Connection             conn;
    private ScriptRunner           scriptRunner;

    @Autowired
    private ApplicationContext applicationContext;

    public DBStepDefs() {
        Then ("local Person is found that looks like", (final DataTable dataTable) -> doParseLocalExamples(PersonEntity.class, dataTable));
        Then ("local Principal is found that looks like", (final DataTable dataTable) -> doParseLocalExamples(PrincipalEntity.class, dataTable));
        Then("global Person is found that looks like", (final DataTable dataTable) -> doHandleGlobalExamples(PersonEntity.class, globalPersons, dataTable));
    }

    private <T> void doParseLocalExamples(final Class<T> clazz, final DataTable dataTable) {
        List<T> exampleEntities = dataTable.asList(clazz);
        for(T anExampleEntity : exampleEntities) {
            verifyFoundExampleEntity(anExampleEntity, clazz);
        }
    }
    private <T> void doHandleGlobalExamples(final Class<T> clazz, List<T> globalExamples, final DataTable dataTable) {
        List<Integer> indices = dataTable.asList(Integer.class);
        for(Integer i : indices) {
            final T exampleEntity = globalExamples.get(i);
            verifyFoundExampleEntity(exampleEntity, clazz);
        }
    }

    private <T> void verifyFoundExampleEntity(final T exampleEntity, final Class<T> clazz) {
        final JpaRepository<T, Integer> repository = getRepository(clazz);
        Example<T> example = Example.of(exampleEntity);
        final Optional<T> result = repository.findOne(example);
        assertThat(clazz.getSimpleName() + " from example " + exampleEntity.toString() + " was not found", result.isPresent());
    }

    @ParameterType("(.*)")
    public String[] tableNames(final String commaDelimitedTableNames) {
        return ArrayUtils.toArray(commaDelimitedTableNames.split(","));
    }

    @Before
    public void setUp() throws Throwable {
        super.setUp();

        DriverManager.registerDriver(new org.h2.Driver());
        conn = DriverManager.getConnection(url, "sa", null);
        log.info(">>> Connection established");
        scriptRunner = new ScriptRunner(conn);
    }

    @After
    public void tearDown() throws Throwable {
        super.tearDown();

        conn.close();
        log.info(">>> Connection closed");
        scriptRunner = null;
    }

    public <T> JpaRepository<T, Integer> getRepository(final Class<T> clazz) {
        Repositories repositories = new Repositories(applicationContext);
        final Optional<Object> result = repositories.getRepositoryFor(clazz);
        return (JpaRepository<T, Integer>) result.get();
    }

    @Value("${spring.datasource.url}")
    private String url;

    @Given("The client gets a row count$")
    public void clientCallsDefaultHello() throws Throwable {
        final JpaRepository<PersonEntity, Integer> personRepository = getRepository(PersonEntity.class);
        rowCount = personRepository.count();
    }

    @Then("^The row count is non zero$")
    public void theRowCountIsNonZero() throws Throwable {
        assertThat("DB is empty", rowCount > 0);
    }

    @Given("^the DB was reset$")
    public void theDBWasReset() throws Throwable {
        runScript("/reset.sql");
        runScript("/schemaComplete.sql");
    }

    @Given("^the DB has loaded (.*)$")
    public void theDBHasLoadedScript(final String prefix) throws Throwable {

        runScript("/" + prefix + "Data.sql");
    }

    public void runScript(final String name) throws IOException {
        InputStream inputStream = this.getClass().getResourceAsStream(name);
        Reader      reader      = new BufferedReader(new InputStreamReader(inputStream));
        scriptRunner.setLogWriter(null);
        scriptRunner.runScript(reader);
    }

    @When("^The client gets row with ID (.*)$")
    public void theClientGetsRowWithID(final Integer id) {
        person = getRepository(PersonEntity.class).findById(id);
    }

    @Then("^a row is returned$")
    public void aRowIsReturned() {
        assertThat("person was not found ", person.isPresent());
    }

    @Then("^a row is not returned$")
    public void aRowIsNotReturned() {
        assertThat("person was not found ", !person.isPresent());
    }

    @And("^The person has nameFirst (.*) and nameLast (.*)$")
    public void thePersonHasNameFirsAndNameLast(final String nameFirst, final String nameLast) {
        assertThat("nameFirst was not " + nameFirst, person.get().getNameFirst(), is(nameFirst));
        assertThat("nameLast was not " + nameLast, person.get().getNameLast(), is(nameLast));
    }

//    @Then ("table(s) {tableNames} (?:have|has) {countRule} {int} rows")
    @Then ("^table(?:s?) ([A-Z,]*) (?:have|has) ([a-z]*) ([0-9]*) rows$")
    public void tablesHaveRows(final String tableNames, final String countRule, final Integer count) {
        System.out.println(">>> Will check for tables " + tableNames);
    }
}
