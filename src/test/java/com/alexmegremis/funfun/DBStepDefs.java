package com.alexmegremis.funfun;

import com.alexmegremis.funfun.persistence.PersonEntity;
import com.alexmegremis.funfun.persistence.PersonRepository;
import cucumber.api.CucumberOptions;
import cucumber.api.java.en.*;
import cucumber.api.junit.Cucumber;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.jdbc.ScriptRunner;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import java.io.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

@Slf4j
//@RunWith (Cucumber.class)
@CucumberOptions (features = "src/test/resources/DB.feature")
public class DBStepDefs {//extends SpringIntegrationTest {

    private Long                   rowCount = 0l;
    private Optional<PersonEntity> person;


    @Autowired
    private PersonRepository personRepository;

    @Value ("${spring.datasource.url}")
    private String url;

    @Given ("The client gets a row count$")
    public void clientCallsDefaultHello() throws Throwable {
        rowCount = personRepository.count();
    }

    @Then ("^The row count is non zero$")
    public void theRowCountIsNonZero() throws Throwable {
        assertThat("DB is empty", rowCount > 0);
    }

    @Given ("^The DB has loaded (.*) and (.*)$")
    public void theDBHasLoadedScripts(final String schemaFile, final String dataFile) throws Throwable {
        DriverManager.registerDriver(new org.h2.Driver());
        Connection conn = DriverManager.getConnection(url, "sa", null);
        log.info(">>> Connection established");
        ScriptRunner sc = new ScriptRunner(conn);

        Reader schemaReader = new BufferedReader(new FileReader("/Volumes/Development/Projects/funfun/src/test/resources/" + schemaFile));
        Reader dataReader   = new BufferedReader(new FileReader("/Volumes/Development/Projects/funfun/src/test/resources/" + dataFile));

        sc.runScript(schemaReader);
        sc.runScript(dataReader);
    }

    @When ("^The client gets row with ID (.*)$")
    public void theClientGetsRowWithID(final Integer id) {
        person = personRepository.findById(id);
    }

    @Then ("^a row is returned$")
    public void aRowIsReturned() {
        assertThat("person was not found ", person.isPresent());
    }

    @And ("^The person has nameFirst (.*) and nameLast (.*)$")
    public void thePersonHasNameFirsAndNameLast(final String nameFirst, final String nameLast) {
        assertThat("nameFirst was not " + nameFirst, person.get().getNameFirst(), is(nameFirst));
        assertThat("nameLast was not " + nameLast, person.get().getNameLast(), is(nameLast));
    }
}
