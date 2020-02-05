package com.alexmegremis.funfun;

import com.alexmegremis.funfun.api.ResponseDTO;
import cucumber.api.java.After;
import cucumber.api.java.Before;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.util.Arrays;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Slf4j
@SpringBootTest (classes = FunfunApplication.class, webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@ContextConfiguration
public class SpringIntegrationTest {

    protected ResponseEntity lastResponse;

    @Value("${server.port}")
    private Integer port;

    @Autowired
    protected RestTemplate restTemplate;

    public void doGet(final String path, final String...variables) {
        UriComponentsBuilder uriComponentsBuilder = UriComponentsBuilder.newInstance().scheme("http").host("localhost").port(port).path(path);

        if(!Arrays.isNullOrEmpty(variables)) {
            for(int i = 0; i < variables.length; i++) {
                uriComponentsBuilder.queryParam(variables[i], variables[++i]);
            }
        }

        log.info(">>> About to call : " + uriComponentsBuilder.toUriString());
        lastResponse = restTemplate.getForEntity(uriComponentsBuilder.build().toUri(),ResponseDTO.class );
    }

    @Before
    private void setUp() {
        lastResponse = null;
        log.info(">>> In before");
    }

    @After
    private void tearDown() {
        lastResponse = null;
        log.info(">>> In after");
    }
}