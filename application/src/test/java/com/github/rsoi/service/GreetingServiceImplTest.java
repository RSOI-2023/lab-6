package com.github.rsoi.service;

import com.github.rsoi.domain.Greeting;
import com.github.rsoi.dto.CreateGreetingRequest;
import com.github.rsoi.repository.GreetingRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;

@SpringBootTest
class GreetingServiceImplTest {

    @InjectMocks
    private GreetingServiceImpl greetingService;

    @Mock
    private GreetingRepository greetingRepository;

    private Greeting greeting;
    @BeforeEach
    public void setUp() {
        greeting = new Greeting();
        greeting.setId(42L);
        greeting.setText("Service Test greeting");
        Mockito.when(greetingRepository.getReferenceById(42L))
                .thenReturn(greeting);
    }

    @Test
    void getGreeting() {
        assertEquals(greeting.getText(),
                greetingService.getGreeting(greeting.getId()).getText());
    }

    @Test
    void createGreeting() {
        // given - precondition or setup
//        given(greetingRepository.getReferenceById(greeting.getId()))
//                .willReturn(null);
//
//        given(greetingRepository.save(greeting)).willReturn(greeting);
//
//        // when -  action or the behaviour that we are going test
//        var greetingResponse = greetingService.createGreeting(new CreateGreetingRequest(greeting.getText()));
//
//        // then - verify the output
//        assertThat(greetingResponse).isNotNull();

    }
}