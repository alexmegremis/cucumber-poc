package com.alexmegremis.funfun;

import com.alexmegremis.funfun.api.PersonEntity;
import com.alexmegremis.funfun.api.ResponseDTO;
import com.google.gson.Gson;
import cucumber.api.java.en.*;
import org.springframework.http.HttpStatus;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;

public class APIStepDefs extends SpringIntegrationTest {

    @Given ("the client calls /api/v1/hello$")
    public void clientCallsDefaultHello() throws Throwable {
        doGet("/api/v1/hello");
    }

    @Given ("the client calls /api/v1/hello with name (.*)$")
    public void clientCallsDefaultHelloWithParam(final String name) throws Throwable {
        doGet("/api/v1/hello", "name", name);
    }

    @Then ("^the client receives status code of (\\d+)$")
    public void clientReceivesStatusCodeOf(int statusCode) throws Throwable {
        final HttpStatus currentStatusCode = lastResponse.getStatusCode();
        assertThat("status code is incorrect : " + currentStatusCode, currentStatusCode.value(), is(statusCode));
    }

    //
    @And ("^the response says (.*)$")
    public void theClientReceivesTheGreeting(final String message) throws Throwable {
        ResponseDTO response = (ResponseDTO) lastResponse.getBody();
        assertThat("response message was not correct : " + message, response.getMessage(), equalTo(message));
    }

    @Given("^the client calls /api/v1/hello with nameFirst (.*) and nameLast (.*)$")
    public void theClientCallsApiVHelloWithNameFirstAndNameLast(final String nameFirst, final String nameLast) {
        doGet("/api/v1/helloYou", "nameFirst", nameFirst, "nameLast", nameLast);
    }

    @And ("^the person is with nameFirst (.*) and nameLast (.*)$")
    public void thePersonIsWithWithNameFirstAndNameLast(final String nameFirst, final String nameLast) {
        ResponseDTO  response = (ResponseDTO) lastResponse.getBody();
        PersonEntity person   = response.getPerson();
        assertThat("nameFirst was incorrect : " + person.getNameFirst(), person.getNameFirst(), equalTo(nameFirst + "-io"));
        assertThat("nameLast was incorrect : " + person.getNameLast(), person.getNameLast(), equalTo(nameLast));
    }
}
