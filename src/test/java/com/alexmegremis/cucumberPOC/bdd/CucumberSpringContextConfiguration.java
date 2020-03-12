package com.alexmegremis.cucumberPOC.bdd;

import com.alexmegremis.cucumberPOC.*;
import io.cucumber.java.Before;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.test.context.SpringBootContextLoader;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;

@Slf4j
@SpringBootTest (webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@ContextConfiguration (classes = {CucumberPOCApplication.class, CucumberPOCConfig.class, PersistenceConfigBatch.class, PersistenceConfigApplication.class}, loader = SpringBootContextLoader.class)
//@TestPropertySource (locations = {"classpath:application.yml", "classpath:persistence-multiple-db-boot.yml"})
@ActiveProfiles("INTEGRATION_TEST")
public class CucumberSpringContextConfiguration {

    @Before
    public void setUp() {
        log.info("-------------- Spring Context Initialized For Executing Cucumber Tests --------------");
    }
}
