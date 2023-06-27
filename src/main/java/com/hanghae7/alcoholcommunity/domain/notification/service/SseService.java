package com.hanghae7.alcoholcommunity.domain.notification.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.hanghae7.alcoholcommunity.domain.common.ResponseDto;
import com.hanghae7.alcoholcommunity.domain.member.entity.Member;
import com.hanghae7.alcoholcommunity.domain.member.repository.MemberRepository;
import com.hanghae7.alcoholcommunity.domain.notification.dto.AbsenceNoticeDto;
import com.hanghae7.alcoholcommunity.domain.notification.dto.AbsenceNoticeListDto;
import com.hanghae7.alcoholcommunity.domain.notification.dto.NoticeParticipantResponseDto;
import com.hanghae7.alcoholcommunity.domain.notification.dto.NoticeResponseDto;
import com.hanghae7.alcoholcommunity.domain.notification.entity.Notice;
import com.hanghae7.alcoholcommunity.domain.notification.repository.NoticeRepository;
import com.hanghae7.alcoholcommunity.domain.party.entity.Party;
import com.hanghae7.alcoholcommunity.domain.party.entity.PartyParticipate;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import org.webjars.NotFoundException;

import javax.persistence.EntityManager;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@EnableScheduling
@Service
public class SseService {
    //private final ObjectMapper objectMapper;
    private final NoticeRepository noticeRepository;
    private final MemberRepository memberRepository;


    @Transactional
    public ResponseEntity<ResponseDto> getAllNotice(Member member){
        List<Notice> noticeList = member.getMemberNotice();
        List<AbsenceNoticeDto> absenceNoticeDtoList = new ArrayList<>();

        for (Notice notice : noticeList) {
            // if noticeCode == 1 (호스트 알림(누가 신청했는지))
           if(notice.getNoticeCode()==1){
               Optional<Member> participant = memberRepository.findById(notice.getParticipantsId());
               Boolean participatedIs;
               if(notice.getAccepted()){
                   participatedIs = true;
               }
               else{
                   participatedIs = false;
               }
               NoticeParticipantResponseDto noticeParticipantResponseDto = new NoticeParticipantResponseDto(
                       notice,
                       participant.get().getProfileImage(),
                       participant.get().getMemberName(),
                       notice.getParticipantsId(),
                       participatedIs
               );
               absenceNoticeDtoList.add(noticeParticipantResponseDto);
           }
           // if noticeCode == 2 (승인거절여부 알림)
           else if(notice.getNoticeCode()==2){
               NoticeResponseDto noticeResponseDto = new NoticeResponseDto(notice);
               absenceNoticeDtoList.add(noticeResponseDto);
           }

        }
        return new ResponseEntity<>(new ResponseDto(200, "부재 중 알림 메세지 응답에 성공하였습니다.", new AbsenceNoticeListDto(absenceNoticeDtoList)), HttpStatus.OK);
    }


    @Transactional
    public ResponseEntity<ResponseDto> updateReadState(Long noticeId){
        Notice notice = noticeRepository.findByNoticeId(noticeId);
        notice.updateRead(true);
        return new ResponseEntity<>(new ResponseDto(200, "알림 읽음 처리 완료"), HttpStatus.OK);
    }


//    @Transactional
//    public ResponseEntity<ResponseDto> deleteNotice(Long noticeId){
//        try{
//            //noticeRepository.deleteById(noticeId);
//            noticeRepository.deleteNoticeByNoticeId(noticeId);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return new ResponseEntity<>(new ResponseDto(200, "알림 삭제 완료"), HttpStatus.OK);
//    }

    //@Transactional
    public ResponseEntity<ResponseDto> deleteNotice(Long noticeId){
        try {
            // Notice 엔티티 조회
            Notice notice = noticeRepository.findById(noticeId)
                    .orElseThrow(() -> new NotFoundException("알림을 찾을 수 없습니다."));

            // Notice 엔티티 삭제
            noticeRepository.deleteById(noticeId);


            return new ResponseEntity<>(new ResponseDto(200, "알림 삭제 완료"), HttpStatus.OK);
        } catch (NotFoundException e) {
            // 알림을 찾을 수 없는 경우
            return new ResponseEntity<>(new ResponseDto(404, "알림을 찾을 수 없습니다."), HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            // 예외 발생 시
            return new ResponseEntity<>(new ResponseDto(500, "서버 오류"), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }




    @Scheduled(fixedRate  = 60*1000*60*24)
    @Transactional
    public void deleteOldNotice(){
        LocalDateTime timenow = LocalDateTime.now().plusHours(9);
        LocalDateTime result = timenow.minusMonths(1);
        List<Notice> noticeList = noticeRepository.findAllByCreatedAtBefore(result);
        for(Notice notice:noticeList){
            noticeRepository.deleteById(notice.getNoticeId());
        }
    }


}
