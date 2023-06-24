 package com.hanghae7.alcoholcommunity.domain.notification.controller;

 import com.fasterxml.jackson.databind.ObjectMapper;
 import com.fasterxml.jackson.databind.SerializationFeature;
 import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
 import com.hanghae7.alcoholcommunity.domain.common.ResponseDto;
 import com.hanghae7.alcoholcommunity.domain.common.security.UserDetailsImplement;
 import com.hanghae7.alcoholcommunity.domain.member.entity.Member;
 import com.hanghae7.alcoholcommunity.domain.notification.entity.Notice;
 import com.hanghae7.alcoholcommunity.domain.notification.repository.NoticeRepository;
 import com.hanghae7.alcoholcommunity.domain.notification.service.SseService;
 import lombok.RequiredArgsConstructor;
 import org.springframework.http.ResponseEntity;
 import org.springframework.security.core.annotation.AuthenticationPrincipal;
 import org.springframework.web.bind.annotation.*;
 import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

 import java.io.IOException;
 import java.util.Iterator;
 import java.util.List;
 import java.util.Map;
 import java.util.concurrent.ConcurrentHashMap;


 /**
  * The type Sse controller.
  * SSE 연결을 요청받는 컨트롤러
  */
 @RestController
 @RequiredArgsConstructor
 @RequestMapping("/sse")
 public class SseController {

     private static Map<String, SseEmitter> emitters = new ConcurrentHashMap<>();
     private final NoticeRepository noticeRepository;
     private final ObjectMapper objectMapper = new ObjectMapper();
     private final SseService sseService;


     /**
      * Stream emitter events sse emitter.
      *
      * @param userDetails the user details
      * @return the sse emitter
      * @throws IOException the io exception
      */
     // SSE event 생성, SseEmitter 사용
     @GetMapping("/stream")
     public SseEmitter streamEmitterEvents(
         @AuthenticationPrincipal UserDetailsImplement userDetails
     ) throws IOException {
         String memberUniqueId = userDetails.getMember().getMemberUniqueId();
         Long sseTimeout = 5*60*1000L;
         SseEmitter sseEmitter = new SseEmitter(sseTimeout);

         sseEmitter.onCompletion(() -> emitters.remove(memberUniqueId));
         sseEmitter.onTimeout(() -> emitters.remove(memberUniqueId));
         sseEmitter.send(SseEmitter.event()
                   .data("connection is open for "+sseTimeout/1000L+"sec")
                   .build()
         );
         emitters.put(memberUniqueId, sseEmitter);

         return sseEmitter;
     }


     /**
      * Get AbsenceNotice
      * @param userDetails
      * @return
      */
     @GetMapping("/notice")
     public ResponseEntity<ResponseDto> getAllNotice(
             @AuthenticationPrincipal UserDetailsImplement userDetails
     ){
         return sseService.getAllNotice(userDetails.getMember());
     }


     /**
      * Update ReadState
      * @param noticeId
      * @return
      */
     @PostMapping("/read/{noticeId}")
     public ResponseEntity<ResponseDto> updateReadState( @PathVariable Long noticeId){
         return sseService.updateReadState(noticeId);
     }


     /**
      *  Delete Notice
      * @param noticeId
      * @return
      */
     @DeleteMapping("/notice/{noticeId}")
     public ResponseEntity<ResponseDto> deleteNotice(@PathVariable Long noticeId){
         return sseService.deleteNotice(noticeId);
     }


     /**
      * Add emitter.
      * emitter 객체에 접근하기 위한 메서드(sseEmitter 추가)
      * @param memberUniqueId the member unique id
      * @param emitter        the emitter
      */
     public static void addEmitter(String memberUniqueId, SseEmitter emitter) {
         emitters.put(memberUniqueId, emitter);
     }

     /**
      * Remove emitter.
      * emitter 객체에 접근하기 위한 메서드(sseEmitter 삭제)
      * @param memberUniqueId the member unique id
      */
     public static void removeEmitter(String memberUniqueId) {
         emitters.remove(memberUniqueId);
     }

     /**
      * Gets emitter.
      * emitter 객체에 접근하기 위한 메서드(sseEmitter 가져오기)
      * @param memberUniqueId the member unique id
      * @return the emitter
      */
     public static SseEmitter getEmitter(String memberUniqueId) {
         return emitters.get(memberUniqueId);
     }

 }
