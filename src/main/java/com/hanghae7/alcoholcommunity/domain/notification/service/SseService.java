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

import javax.persistence.EntityManager;
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


        for (Notice notice : noticeList) {
            if (notice.getIsRead()) {
                continue;
            } else {
                // noticeCode = 1 일 경우와 2 일 경우 나눠서 처리

                // if noticeCode == 1 (신청자 알림)
                // 프론트에서 어떻게 받을지 정하고 구현

                // if noticeCode == 2 (승인거절여부 알림)
                noticeResponseDtoList.add(new NoticeResponseDto(notice));
                Notice not = noticeRepository.findByNoticeId(notice.getNoticeId());
                not.updateRead(true);
            }
        }
        return new ResponseEntity<>(new ResponseDto(200, "부재 중 알림 메세지 응답에 성공하였습니다.", new AbsenceNoticeListDto(noticeResponseDtoList)), HttpStatus.OK);
    }

}
