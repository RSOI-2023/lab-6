package com.github.rsoi.service;

import com.github.rsoi.domain.Greeting;
import com.github.rsoi.dto.CreateGreetingRequest;
import com.github.rsoi.dto.GreetingResponse;
import com.github.rsoi.repository.GreetingRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GreetingServiceImpl implements GreetingService {
    private final GreetingRepository greetingRepository;

    /**
     * Saving our Hello world greeting to the database, @PostConstruct executes only once on context creation
     */
    @PostConstruct
    void init() {
        var greetings = greetingRepository.findAll();
        if (greetings.isEmpty()) {
            var greeting = new Greeting();
            greeting.setText("Hello world from Spring Service Bean!");
            greetingRepository.save(greeting);
            System.out.println("Oh, it's first start of application, I created one for start");
        } else {
            System.out.println("Cool, there are already some greetings in the DB");
        }
    }

    @Override
    public GreetingResponse getGreeting(long id) {
        return GreetingResponse.of(greetingRepository.getReferenceById(id));
    }

    @Override
    public GreetingResponse createGreeting(CreateGreetingRequest request) {
        var greeting = new Greeting();
        greeting.setText(request.getText());
        return GreetingResponse.of(greetingRepository.save(greeting));
    }
}
