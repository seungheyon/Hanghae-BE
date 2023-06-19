package com.hanghae7.alcoholcommunity.domain.notification.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.hanghae7.alcoholcommunity.domain.common.ResponseDto;
import com.hanghae7.alcoholcommunity.domain.member.entity.Member;
import com.hanghae7.alcoholcommunity.domain.notification.dto.AbsenceNoticeListDto;
import com.hanghae7.alcoholcommunity.domain.notification.dto.NoticeResponseDto;
import com.hanghae7.alcoholcommunity.domain.notification.entity.Notice;
import com.hanghae7.alcoholcommunity.domain.notification.repository.NoticeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@RequiredArgsConstructor
@Service
public class SseService {

    //private final ObjectMapper objectMapper;
    private final NoticeRepository noticeRepository;

    @Transactional
    public ResponseEntity<ResponseDto> getAbsenceNotice(Member member){
        List<Notice> noticeList = member.getMemberNotice();
        List<NoticeResponseDto> noticeResponseDtoList = new ArrayList<>();

        for(int i=0;i<noticeList.size();i++){
            if(noticeList.get(i).getIsRead()){
                continue;
            }
            else {
                noticeResponseDtoList.add(new NoticeResponseDto(noticeList.get(i)));
                noticeList.get(i).updateRead(true);
            }
        }
//        Iterator<Notice> iterator = noticeList.iterator();
//            while(iterator.hasNext()){
//                Notice notice = iterator.next();
//
//                Notice realNotice = noticeRepository.findByNoticeId(notice.getNoticeId());
//
//                if(realNotice.getIsRead()){
//                    continue;
//                }
//                //objectMapper.registerModule(new JavaTimeModule());
//                //objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
//                //String jsonData = objectMapper.writeValueAsString(notice);
////              sseEmitter.send(SseEmitter.event()
////                        .data(jsonData)
////                        .build()
////              );
//                noticeResponseDtoList.add(new NoticeResponseDto(notice));
//
//                realNotice.updateRead(true);
//
//            //noticeRepository.delete(notice);
//            //noticeRepository.deleteById(notice.getNoticeId());
//            }

        return new ResponseEntity<>(new ResponseDto(200, "부재 중 알림 메세지 응답에 성공하였습니다.", new AbsenceNoticeListDto(noticeResponseDtoList)), HttpStatus.OK);
    }

}
