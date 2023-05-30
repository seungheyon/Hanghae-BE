package com.hanghae7.alcoholcommunity.domain.sse;

import lombok.RequiredArgsConstructor;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

import java.time.Duration;
import java.time.LocalDateTime;

@RestController
@RequiredArgsConstructor
@RequestMapping("/sse")
public class SseController {

    // SSE event 생성
    @GetMapping("/stream")
    public Flux<ServerSentEvent<String>> streamEvents(){
        return Flux.interval(Duration.ofSeconds(1))
                .map(sequence -> ServerSentEvent.<String>builder()
                        .id(String.valueOf(sequence))
                        .event("event-name")
                        .data("Current time: " + LocalDateTime.now())
                        .build());
    }

    // SSE 컨트롤러 연결

}
