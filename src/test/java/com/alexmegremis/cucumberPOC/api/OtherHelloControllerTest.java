package com.alexmegremis.cucumberPOC.api;

import com.alexmegremis.cucumberPOC.PropertyPopulator;
import com.alexmegremis.cucumberPOC.persistence.batch.BatchJobExecutionRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.*;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@Slf4j
//@RunWith(SpringRunner.class)
//@RunWith(MockitoJUnitRunner.class)
//@RunWith(JUnitPlatform.class)
//@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
//@ContextConfiguration(classes = {CucumberPOCApplication.class, CucumberPOCConfig.class})
//@TestPropertySource({"classpath:application.yml", "classpath:persistence-multiple-db-boot.yml"})
//@SpringBootConfiguration
//@SpringBootTest(classes = {CucumberPOCApplication.class, CucumberPOCConfig.class})
//@AutoConfigureMockMvc
//@ExtendWith({MockitoExtension.class, SpringExtension.class, EnvironmentE})
//@SpringJUnitConfig
@SpringBootTest//(classes = {CucumberPOCConfig.class})//, PersistenceConfigBatch.class, PersistenceConfigApplication.class})
//@SpringBootTest
public class OtherHelloControllerTest {

    @Value("${file}")
    String file;
    @Value("${level.file}")
    String levelFile;

    @Autowired
    private ApplicationContext context;

    @Autowired
    private PropertyPopulator propertyPopulator;

    @Mock
    BatchJobExecutionRepository mockRepo;

    @InjectMocks
    HelloController controller;

    @BeforeEach
    public void setUp() throws Exception {
        propertyPopulator.populateProperties(controller);
    }

    @AfterEach
    public void tearDown() throws Exception {
    }

    @Test
    void dependenciesTest() {
        assertNotNull(controller.batchJobExecutionRepository);
        assertNotNull(controller.file);
        assertNotNull(controller.levelFile);
        assertNotNull(controller.doDebug);
        assertFalse(controller.doDebug);
    }
}