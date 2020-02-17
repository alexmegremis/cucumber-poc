package com.alexmegremis.funfun.bdd.stepdefs;

import lombok.extern.slf4j.Slf4j;
import org.assertj.core.util.Arrays;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Slf4j
public abstract class SpringIntegrationTest {

    protected ResponseEntity lastResponse;

    @Value ("${server.port}")
    private Integer port;

    @Autowired
    protected RestTemplate restTemplate;

    public void doGet(final Class responseType, final String path, final String... variables) {
        UriComponentsBuilder uriComponentsBuilder = UriComponentsBuilder.newInstance().scheme("http").host("localhost").port(port).path(path);

        if (! Arrays.isNullOrEmpty(variables)) {
            for (int i = 0; i < variables.length; i++) {
                uriComponentsBuilder.queryParam(variables[i], variables[++ i]);
            }
        }

        log.info(">>> About to call : " + uriComponentsBuilder.toUriString());
        lastResponse = restTemplate.getForEntity(uriComponentsBuilder.build().toUri(), responseType);
    }

    public void setUp() throws Throwable {
        log.info(">>> In before");
        lastResponse = null;
    }

    public void tearDown() throws Throwable {
        log.info(">>> In after");
        lastResponse = null;
    }
}