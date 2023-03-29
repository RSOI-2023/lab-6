package com.github.rsoi.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.github.rsoi.domain.Greeting;
import lombok.AllArgsConstructor;
import lombok.Value;

@Value
public class GreetingResponse {
    long id;
    String text;

    @JsonCreator
    public GreetingResponse(@JsonProperty("id") long id,
                            @JsonProperty("text") String text) {
        this.id = id;
        this.text = text;
    }

    public static GreetingResponse of(Greeting greeting) {
        return new GreetingResponse(greeting.getId(), greeting.getText());
    }
}
