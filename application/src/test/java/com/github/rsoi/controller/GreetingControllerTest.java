package com.github.rsoi.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.rsoi.dto.CreateGreetingRequest;
import com.github.rsoi.dto.GreetingResponse;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class GreetingControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Test
    void getGreetingById() throws Exception {
        mockMvc
                // making a request "GET http://localhost:8080/greetings?greetingId=1"
                .perform(get("/greetings").param("greetingId", "1"))
                // print some additional information to console (not necessary)
                .andDo(print())
                // expect HTTP status 200
                .andExpect(status().isOk())
                // and also expect "Hello world from Spring Service Bean!" in the response
                .andExpect(content().string(containsString("Hello world from Spring Service Bean!")));
    }

    @Test
    void createGreeting() throws Exception {
        var testGreeting = new CreateGreetingRequest("Hi, my name is Test Greeting");

        // send POST request with body
        MvcResult createdGreetingResponse =
                mockMvc.perform(post("/greetings")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(asJsonString(testGreeting)))
                        .andDo(print())
                        .andExpect(status().isOk())
                        .andReturn();

        // parse response into java object
        GreetingResponse createdGreeting = new ObjectMapper()
                .readerFor(GreetingResponse.class)
                .readValue(createdGreetingResponse.getResponse().getContentAsString());

        // check if created entity actually exists in DB
        mockMvc.perform(get("/greetings").param("greetingId", String.valueOf(createdGreeting.getId())))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(containsString(createdGreeting.getText())));
    }

    // Util method. Just shows how to clean up code
    public static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}