//package com.hanghae7.alcoholcommunity.domain.sse;
//
//import com.hanghae7.alcoholcommunity.domain.common.security.UserDetailsImplement;
//import lombok.RequiredArgsConstructor;
//import org.springframework.security.core.annotation.AuthenticationPrincipal;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
//
//import java.io.IOException;
//
//import static com.hanghae7.alcoholcommunity.domain.sse.SseController.getEmitter;
//
//
//@RestController
//@RequiredArgsConstructor
//@RequestMapping("/sse")
//public class SseTestController {
//
//    @PostMapping("/emitterPost")
//    public void streamEmitterEvents(@AuthenticationPrincipal UserDetailsImplement userDetails){
//        String uniqueId = userDetails.getMember().getMemberUniqueId();
//        try{
//            getEmitter(uniqueId).send(SseEmitter.event()
//                    .data("푸시알림 데이터")
//                    .build()
//            );
//        } catch (IOException e){
//            e.printStackTrace();
//        }
//
//
//    }
//
//
//}
