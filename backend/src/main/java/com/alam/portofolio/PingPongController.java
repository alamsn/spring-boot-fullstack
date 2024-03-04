package com.alam.portofolio;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PingPongController {

    private record PingPong(String result) {}

    @GetMapping("/ping")
    private PingPong getPingPong() {
        return new PingPong("Pongz");
    }
}
