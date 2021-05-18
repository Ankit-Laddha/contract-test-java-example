package io.ankitladdha.contracts.controller;

import io.ankitladdha.contracts.model.EchoRequest;
import io.ankitladdha.contracts.model.EchoResponse;
import io.ankitladdha.contracts.service.PhraseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/")
public class PhraseController {

    @Autowired
    PhraseService phraseService;

    @PostMapping("/echo")
    public ResponseEntity<EchoResponse> echoMessage(@RequestBody EchoRequest echoRequest) {
        String phrase = phraseService.echo(echoRequest.getTimestamp(), echoRequest.getGreetings());
        return ResponseEntity.ok(new EchoResponse(phrase));
    }

    @GetMapping("/sayHi")
    public ResponseEntity<String> sayHi() {
        return ResponseEntity.ok("This is valid request!");
    }
}
