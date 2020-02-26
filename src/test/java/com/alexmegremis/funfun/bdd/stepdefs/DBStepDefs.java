package com.alexmegremis.funfun.bdd.stepdefs;

import com.alexmegremis.funfun.persistence.PersonEntity;
import com.alexmegremis.funfun.persistence.PersonRepository;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.en.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.jdbc.ScriptRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import java.io.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

@Slf4j
public class DBStepDefs extends SpringIntegrationTest {

    private Long                   rowCount = 0l;
    private Optional<PersonEntity> person;
    private Connection             conn;
    private ScriptRunner           scriptRunner;

    @Autowired
    private PersonRepository personRepository;

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

    @Value("${spring.datasource.url}")
    private String url;

    @Given("The client gets a row count$")
    public void clientCallsDefaultHello() throws Throwable {
        rowCount = personRepository.count();
    }

    @Then("^The row count is non zero$")
    public void theRowCountIsNonZero() throws Throwable {
        assertThat("DB is empty", rowCount > 0);
    }

    @Given("^The DB was reset$")
    public void theDBWasReset() throws Throwable {
        runScript("/reset.sql");
        runScript("/schemaComplete.sql");
    }

    @Given("^The DB has loaded (.*)$")
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
        person = personRepository.findById(id);
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
}
