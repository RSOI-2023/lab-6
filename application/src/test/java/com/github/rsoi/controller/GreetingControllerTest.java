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
                // посылаем запрос вида "GET http://localhost:8080/greetings?greetingId=1"
                .perform(get("/greetings").param("greetingId", "1"))
                // выводим информацию о запросе и ответе в консоль (можно не выводить, это сделано для красоты)
                .andDo(print())
                // ожидаем HTTP код 200
                .andExpect(status().isOk())
                // и также ожидаем сообщение "Hello world from Spring Service Bean!" в ответе
                .andExpect(content().string(containsString("Hello world from Spring Service Bean!")));
    }

    @Test
    void createGreeting() throws Exception {
        var testGreeting = new CreateGreetingRequest("Hi, my name is Test Greeting");

        // отправляем POST запрос с объектом, который нужно создать
        MvcResult createdGreetingResponse =
                mockMvc.perform(post("/greetings")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(asJsonString(testGreeting)))
                        .andDo(print())
                        .andExpect(status().isOk())
                        .andReturn();

        // контроллер возвращает json, который нужно превратить в java-объект
        GreetingResponse createdGreeting = new ObjectMapper()
                .readerFor(GreetingResponse.class)
                .readValue(createdGreetingResponse.getResponse().getContentAsString());

        // Проверяем, что созданный объект действительно создался и мы можем его получить GET-запросом
        mockMvc.perform(get("/greetings").param("greetingId", String.valueOf(createdGreeting.getId())))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(containsString(createdGreeting.getText())));
    }

    @Test
    void getNotFoundException() throws Exception {
        mockMvc
                // пытаемся получить несуществующую запись
                .perform(get("/greetings").param("greetingId", "9999999"))
                .andDo(print())
                .andExpect(status().isNotFound());
    }
    @Test
    void getInternalServerError() throws Exception {
        mockMvc
                // посылаем некорректный запрос
                .perform(get("/greetings").param("greetingId", "a"))
                .andDo(print())
                .andExpect(status().isInternalServerError());
    }
    // вспомогательный метод (util method)
    public static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}