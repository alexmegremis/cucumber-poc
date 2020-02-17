package com.alexmegremis.funfun.bdd;

import cucumber.api.CucumberOptions;
import cucumber.api.junit.Cucumber;
import org.junit.runner.RunWith;
import org.springframework.test.annotation.DirtiesContext;

@CucumberOptions (features = "classpath:features", plugin = {"pretty", "html:target/cucumber-report.html"})
@RunWith (Cucumber.class)
public class CucumberTest {}
