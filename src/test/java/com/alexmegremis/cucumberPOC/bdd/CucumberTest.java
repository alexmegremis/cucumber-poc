package com.alexmegremis.cucumberPOC.bdd;

import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import org.junit.runner.RunWith;
import org.springframework.test.context.TestPropertySource;

@CucumberOptions (features = "classpath:features", plugin = {"pretty", "html:target/cucumber-report.html"}, glue = "com.alexmegremis.cucumberPOC.bdd")
@RunWith (Cucumber.class)
@TestPropertySource (locations = {"classpath:application.properties", "classpath:persistence-multiple-db-boot.properties"})
public class CucumberTest {}
