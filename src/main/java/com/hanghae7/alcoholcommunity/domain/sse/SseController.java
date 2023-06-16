//package com.hanghae7.alcoholcommunity.domain.sse;
//
//import com.fasterxml.jackson.databind.ObjectMapper;
//import com.fasterxml.jackson.databind.SerializationFeature;
//import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
//import com.hanghae7.alcoholcommunity.domain.common.security.UserDetailsImplement;
//import com.hanghae7.alcoholcommunity.domain.member.dto.MemberNoticeDto;
//import com.hanghae7.alcoholcommunity.domain.member.entity.Member;
//import com.hanghae7.alcoholcommunity.domain.member.entity.Notice;
//import com.hanghae7.alcoholcommunity.domain.member.repository.NoticeRepository;
//import lombok.RequiredArgsConstructor;
//import org.springframework.security.core.annotation.AuthenticationPrincipal;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
//
//import java.io.IOException;
//import java.util.Iterator;
//import java.util.List;
//import java.util.Map;
//import java.util.concurrent.ConcurrentHashMap;
//
//
///**
// * The type Sse controller.
// * SSE 연결을 요청받는 컨트롤러
// */
//@RestController
//@RequiredArgsConstructor
//@RequestMapping("/sse")
//public class SseController {
//
//    private static Map<String, SseEmitter> emitters = new ConcurrentHashMap<>();
//    private final NoticeRepository noticeRepository;
//    private final ObjectMapper objectMapper = new ObjectMapper();
//
//
//    /**
//     * Stream emitter events sse emitter.
//     *
//     * @param userDetails the user details
//     * @return the sse emitter
//     * @throws IOException the io exception
//     */
//// SSE event 생성, SseEmitter 사용
//    @GetMapping("/stream")
//    public SseEmitter streamEmitterEvents(
//            @AuthenticationPrincipal UserDetailsImplement userDetails
//    ) throws IOException {
//        String memberUniqueId = userDetails.getMember().getMemberUniqueId();
//        SseEmitter sseEmitter = new SseEmitter();
//
//        sseEmitter.onCompletion(() -> emitters.remove(memberUniqueId));
//        sseEmitter.onTimeout(() -> {
//            try{
//                sseEmitter.send(
//                        SseEmitter.event()
//                                .name("reconnect")
//                        .data("reconnect")
//                        .build()
//                );
//            } catch (IOException e) {
//                // 예외처리
//                e.printStackTrace();
//            }
//
//        });
//
//        emitters.put(memberUniqueId, sseEmitter);
//
//        List<Notice> noticeList = userDetails.getMember().getMemberNotice();
//        Iterator<Notice> iterator = noticeList.iterator();
//        while(iterator.hasNext()){
//            Notice notice = iterator.next();
//            if(notice.getIsRead()){
//                continue;
//            }
//            objectMapper.registerModule(new JavaTimeModule());
//            objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
//            String jsonData = objectMapper.writeValueAsString(notice);
//            sseEmitter.send(SseEmitter.event()
//                    .data(jsonData)
//                    .build()
//            );
//            notice.updateRead(true);
//            iterator.remove();
//            //noticeRepository.delete(notice);
//            //noticeRepository.deleteById(notice.getNoticeId());
//        }
////        sseEmitter.send(SseEmitter.event()
////                    .data("testData")
////                    .build()
////            );
//
////        for (Notice notice : noticeList) {
////            String jsonString = notice.getNotice();
////            sseEmitter.send(SseEmitter.event()
////                    .data(jsonString)
////                    .build()
////            );
////            // noticeList 에서 방금 전송한 notice 삭제
////            iterator.remove();
////        }
//
//
//
//        return sseEmitter;
//    }
//
//    /**
//     * Add emitter.
//     * emitter 객체에 접근하기 위한 메서드(sseEmitter 추가)
//     * @param memberUniqueId the member unique id
//     * @param emitter        the emitter
//     */
//    public static void addEmitter(String memberUniqueId, SseEmitter emitter) {
//        emitters.put(memberUniqueId, emitter);
//    }
//
//    /**
//     * Remove emitter.
//     * emitter 객체에 접근하기 위한 메서드(sseEmitter 삭제)
//     * @param memberUniqueId the member unique id
//     */
//    public static void removeEmitter(String memberUniqueId) {
//        emitters.remove(memberUniqueId);
//    }
//
//    /**
//     * Gets emitter.
//     * emitter 객체에 접근하기 위한 메서드(sseEmitter 가져오기)
//     * @param memberUniqueId the member unique id
//     * @return the emitter
//     */
//    public static SseEmitter getEmitter(String memberUniqueId) {
//        return emitters.get(memberUniqueId);
//    }
//}
