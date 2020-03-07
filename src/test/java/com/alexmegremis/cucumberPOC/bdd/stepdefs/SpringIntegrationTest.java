package com.alexmegremis.cucumberPOC.bdd.stepdefs;

import lombok.extern.slf4j.Slf4j;
import org.assertj.core.util.Arrays;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.*;
import reactor.core.publisher.Mono;

@Slf4j
public abstract class SpringIntegrationTest {

    private static ConfigurableApplicationContext context;

    protected ResponseEntity lastResponse;

    @Value ("${server.port}")
    private Integer port;

    @Autowired
    protected RestTemplate restTemplate;

    public <T> void doGet(final Class<T> responseType, final String path, final String... variables) {

        UriBuilderFactory uriBuilderFactory = new DefaultUriBuilderFactory();
        UriBuilder        uriBuilder        = uriBuilderFactory.builder();
        uriBuilder.scheme("http").host("localhost").port(port).path(path);

        if (! Arrays.isNullOrEmpty(variables)) {
            for (int i = 0; i < variables.length; i++) {
                uriBuilder.queryParam(variables[i], variables[++ i]);
            }
        }

        log.info(">>> About to call : " + uriBuilderFactory.toString());
        System.out.println(">>> About to call : " + uriBuilderFactory.toString());

        final Mono<ResponseEntity<T>> mono = WebClient.create().get().uri(uriBuilder.build()).exchange().flatMap(r -> r.toEntity(responseType));

        lastResponse = mono.block();
        System.out.println(">>> foo");
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