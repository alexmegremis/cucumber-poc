package com.alexmegremis.funfun.bdd.stepdefs;

import com.alexmegremis.funfun.api.ResponseDTO;
import com.alexmegremis.funfun.persistence.PersonEntity;
import cucumber.api.java.en.*;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.util.Arrays;
import org.springframework.http.HttpStatus;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;

@Slf4j
public class APIStepDefs extends SpringIntegrationTest {

    @Given ("the client calls /api/v1/hello$")
    public void clientCallsDefaultHello() throws Throwable {
        doGet(ResponseDTO.class,"/api/v1/hello");
    }

    @Given ("the client calls /api/v1/hello with name (.*)$")
    public void clientCallsDefaultHelloWithParam(final String name) throws Throwable {
        doGet(ResponseDTO.class, "/api/v1/hello", "name", name);
    }

    @Then ("^the client receives status code of (\\d+)$")
    public void clientReceivesStatusCodeOf(int statusCode) throws Throwable {
        final HttpStatus currentStatusCode = lastResponse.getStatusCode();
        assertThat("status code is incorrect : " + currentStatusCode, currentStatusCode.value(), is(statusCode));
    }

    @And ("^the response says (.*)$")
    public void theClientReceivesTheGreeting(final String message) throws Throwable {
        ResponseDTO response = (ResponseDTO) lastResponse.getBody();
        assertThat("response message was not correct : " + message, response.getMessage(), equalTo(message));
    }

    @Given ("^the client calls (.*) with nameFirst (.*) and nameLast (.*)$")
    public void theClientCallsApiWithNameFirstAndNameLast(final String path, final String nameFirst, final String nameLast) {
        doGet(PersonEntity[].class, path, "nameFirst", nameFirst, "nameLast", nameLast);
    }

    @And ("^the person is with nameFirst (.*) and nameLast (.*)$")
    public void thePersonIsWithWithNameFirstAndNameLast(final String nameFirst, final String nameLast) {
        PersonEntity[]  response = (PersonEntity[]) lastResponse.getBody();
        assertThat("person was not found", !Arrays.isNullOrEmpty(response));
        assertThat("non-unique result was found", response.length == 1);
        PersonEntity person = response[0];

        assertThat("nameFirst was incorrect : " + person.getNameFirst(), person.getNameFirst(), equalTo(nameFirst));
        assertThat("nameLast was incorrect : " + person.getNameLast(), person.getNameLast(), equalTo(nameLast));
    }
}
