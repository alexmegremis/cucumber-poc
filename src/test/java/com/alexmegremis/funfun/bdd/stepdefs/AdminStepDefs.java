package com.alexmegremis.funfun.bdd.stepdefs;

import io.cucumber.java.en.Given;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class AdminStepDefs extends SpringIntegrationTest {

    @Given("The context restarts")
    public void bounce() {
        log.info(">>> CONTEXT RESTARTING");
    }
}
