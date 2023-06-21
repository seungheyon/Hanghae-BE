package com.hanghae7.alcoholcommunity.domain.notification.sse;

import com.hanghae7.alcoholcommunity.domain.common.ResponseDto;
import com.hanghae7.alcoholcommunity.domain.notification.entity.Notice;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;

@Configuration
public class SseSend {

    public void sseSend(SseEmitter emitter, String jsonData) throws IOException {

        emitter.send(SseEmitter.event()
                        .data(jsonData)
                        .build()
        );

    }
}
