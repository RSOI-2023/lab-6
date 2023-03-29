package com.github.rsoi.controller;

import com.github.rsoi.dto.CreateGreetingRequest;
import com.github.rsoi.dto.GreetingResponse;
import com.github.rsoi.service.GreetingService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/greetings")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class GreetingController {
    private final GreetingService greetingService;

    @GetMapping
    public GreetingResponse getGreetingById(@RequestParam Long greetingId) {
        return greetingService.getGreeting(greetingId);
    }

    @PostMapping
    public GreetingResponse createGreeting(@RequestBody CreateGreetingRequest request) {
        return greetingService.createGreeting(request);
    }
}
