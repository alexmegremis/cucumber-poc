package com.alexmegremis.funfun.bdd;

import com.alexmegremis.funfun.FunfunApplication;
import cucumber.api.java.Before;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.test.context.SpringBootContextLoader;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;

@Slf4j
@SpringBootTest (webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@ContextConfiguration (classes = FunfunApplication.class, loader = SpringBootContextLoader.class)
@TestPropertySource (locations = "classpath:application.properties")
//@DirtiesContext (classMode = DirtiesContext.ClassMode.AFTER_CLASS )
public class CucumberSpringContextConfiguration {

    @Before
    public void setUp() {
        log.info("-------------- Spring Context Initialized For Executing Cucumber Tests --------------");
    }
}
