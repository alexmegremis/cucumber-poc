package com.alexmegremis.cucumberPOC.bdd;

import com.alexmegremis.cucumberPOC.CucumberPOCApplication;
import io.cucumber.java.Before;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.test.context.SpringBootContextLoader;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.*;

@Slf4j
@SpringBootTest (webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@ContextConfiguration (classes = CucumberPOCApplication.class, loader = SpringBootContextLoader.class)
@TestPropertySource (locations = "classpath:application.properties")
@ActiveProfiles("INTEGRATION_TEST")
public class CucumberSpringContextConfiguration {

    @Before
    public void setUp() {
        log.info("-------------- Spring Context Initialized For Executing Cucumber Tests --------------");
    }
}
