package com.alexmegremis.cucumberPOC.api;

import lombok.extern.slf4j.Slf4j;
import org.junit.*;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.not;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class HelloControllerTest {

    @Autowired
    private MockMvc         mockMvc;
    @Autowired
    private HelloController controller;

    @Before
    public void setUp() throws Exception {
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void contexLoads() {
        assertThat(controller).isNotNull();
    }

    @Test
    public void hello() throws Exception {
        mockMvc.perform(get("/api/v1/hello")).andDo(print()).andExpect(status().isOk()).andExpect(content().string(containsString("Hello world")));
    }

    @Test
    public void hello_name() throws Exception {
        mockMvc.perform(get("/api/v1/hello").param("name", "Alex"))
               .andDo(print())
               .andExpect(status().isOk())
               .andExpect(content().string(containsString("Hello Alex")));
        mockMvc.perform(get("/api/v1/hello").param("name", "George"))
               .andDo(print())
               .andExpect(status().isOk())
               .andExpect(content().string(not(containsString("Hello Alex"))));
    }

    @Test
    public void helloYou_name() throws Exception {
        mockMvc.perform(get("/api/v1/helloYou").param("nameFirst", "Alex").param("nameLast", "Megremis"))
               .andDo(print())
               .andExpect(status().isOk())
               .andExpect(content().string(containsString("Hello Alex")));
        mockMvc.perform(get("/api/v1/helloYou").param("nameFirst", "Thomas").param("nameLast", "Megremis"))
               .andDo(print())
               .andExpect(status().isOk())
               .andExpect(content().string(containsString("Hello Thomas")))
               .andExpect(content().string(containsString("Thomas-io")));
    }
}