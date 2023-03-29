package com.github.rsoi.service;

import com.github.rsoi.dto.CreateGreetingRequest;
import com.github.rsoi.dto.GreetingResponse;

public interface GreetingService {
    GreetingResponse getGreeting(long id);
    GreetingResponse createGreeting(CreateGreetingRequest request);
}
