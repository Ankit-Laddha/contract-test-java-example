package io.ankitladdha.contracts.service;

import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
public class PhraseService {

    public String echo(long timestamp, String greetings){
        return greetings + " sent at: " + timestamp + " worked at: " + Instant.now().getEpochSecond();
    }
}
