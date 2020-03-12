package com.alexmegremis.cucumberPOC;

//import org.junit.jupiter.api.Test;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootContextLoader;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

@ContextConfiguration(classes = {CucumberPOCApplication.class, CucumberPOCConfig.class, PersistenceConfigBatch.class, PersistenceConfigApplication.class},
                      loader = SpringBootContextLoader.class)
@RunWith(SpringRunner.class)
@SpringBootTest
public class CucumberPOCApplicationTests {

    @Test
    public void contextLoads() {
    }
}
