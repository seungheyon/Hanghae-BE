package com.hanghae7.alcoholcommunity.domain.notification.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hanghae7.alcoholcommunity.domain.common.ResponseDto;
import com.hanghae7.alcoholcommunity.domain.member.entity.Member;
import com.hanghae7.alcoholcommunity.domain.notification.dto.NoticeResponseDto;
import com.hanghae7.alcoholcommunity.domain.notification.entity.Notice;
import com.hanghae7.alcoholcommunity.domain.notification.repository.NoticeRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class NoticeService {

	private final NoticeRepository noticeRepository;

	@Transactional
	public ResponseEntity<ResponseDto> checkNotice(Member member){
		List<Notice> noticeList = noticeRepository.findAllByMemberAndIsRead(member, false);
		List<NoticeResponseDto> noticeResponseDtoList = new ArrayList<>();

		for(Notice notice : noticeList){
			noticeResponseDtoList.add(new NoticeResponseDto(notice.getPartyId(), notice.getPartyTitle(), notice.getAccepted()));
			notice.updateRead(true);
		}

		return new ResponseEntity<>(new ResponseDto(200, "알림 조회에 성공했습니다.", noticeResponseDtoList), HttpStatus.OK);
	}
}
