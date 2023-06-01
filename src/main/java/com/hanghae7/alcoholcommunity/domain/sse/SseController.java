package com.hanghae7.alcoholcommunity.domain.sse;

import com.hanghae7.alcoholcommunity.domain.common.security.UserDetailsImplement;
import lombok.RequiredArgsConstructor;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import reactor.core.publisher.*;

import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@RestController
@RequiredArgsConstructor
@RequestMapping("/sse")
public class SseController {

    private static Map<String, SseEmitter> emitters = new ConcurrentHashMap<>();

    // SSE event 생성
    @GetMapping("/stream")
    public Flux<ServerSentEvent<String>> streamEvents(){


        String memberUniqueId = "UniqueId";
        Flux<ServerSentEvent<String>> sseStream = Flux.just("Initial data")
                .map(data -> ServerSentEvent.<String>builder()
                        .id(memberUniqueId)
                        .event("first-event")
                        .data("data "+data)
                        .build());
                //.concatWith(Flux.never()); // 데이터 전송 후 스트림 유지

        //sseStream 을 사용해서 한번 더 데이터를 전송하고 싶다
        Flux<ServerSentEvent<String>> updateStream = Flux.just("Second data")
                .map(data -> ServerSentEvent.<String>builder()
                        .id(memberUniqueId)
                        .event("second-event")
                        .data(data)
                        .build());


        return sseStream;
    }

    // SseEmitter 사용
    @GetMapping("/emitterStream")
    public SseEmitter streamEmitterEvents(@AuthenticationPrincipal UserDetailsImplement userDetails) throws IOException {
        String memberUniqueId = userDetails.getMember().getMemberUniqueId();
        SseEmitter sseEmitter = new SseEmitter();

        sseEmitter.onCompletion(() -> emitters.remove(memberUniqueId));
        sseEmitter.onTimeout(() -> {
            try{
                sseEmitter.send(SseEmitter.event()
                                .name("reconnect")
                        .data("Initial data")
                        .build());
            } catch (IOException e){
                // 예외처리
            }
        });

        emitters.put(memberUniqueId, sseEmitter);

        sseEmitter.send(SseEmitter.event()
                .data("Initial data")
                .build()
        );

        return sseEmitter;
    }

    @GetMapping("/streamSec")
    public Flux<ServerSentEvent<String>> streamEvents2(){

        String memberUniqueId = "UniqueId";

        // 최초 연결 시에만 데이터를 전송하는 Flux 생성
        Flux<ServerSentEvent<String>> initialData = Flux.just("Initial data")
                .map(data -> ServerSentEvent.<String>builder()
                        .id(memberUniqueId)
                        .event("first-event")
                        .data(data)
                        .build());

        initialData.map(data -> ServerSentEvent.<String>builder()
                .event("second-event")
                .data("Second data")
                .build());

        // 이후에는 데이터를 전송하지 않고 스트림 유지
        Flux<ServerSentEvent<String>> keepAlive = Flux.interval(Duration.ofSeconds(1))
                .map(tick -> ServerSentEvent.<String>builder()
                        .event("keep-alive")
                        .build());

        // 최초 데이터 전송 후 스트림 유지를 위해 concatWith 사용
        Flux<ServerSentEvent<String>> sseStream = initialData.concatWith(keepAlive);

        // 다른 데이터를 전송하는 부분을 추가
        Flux<ServerSentEvent<String>> updateStream = sseStream.concatWith(Flux.just("Updated data")
                .map(data -> ServerSentEvent.<String>builder()
                        .id(memberUniqueId)
                        .event("update-event")
                        .data(data)
                        .build())
        );

        return updateStream;
    }

    public static void addEmitter(String memberUniqueId, SseEmitter emitter) {
        emitters.put(memberUniqueId, emitter);
    }

    public static void removeEmitter(String memberUniqueId) {
        emitters.remove(memberUniqueId);
    }

    public static SseEmitter getEmitter(String memberUniqueId) {
        return emitters.get(memberUniqueId);
    }
}
