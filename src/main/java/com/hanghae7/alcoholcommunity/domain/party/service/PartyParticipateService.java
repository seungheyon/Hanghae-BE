package com.hanghae7.alcoholcommunity.domain.party.service;

// import static com.hanghae7.alcoholcommunity.domain.sse.SseController.*;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import com.fasterxml.jackson.databind.ObjectMapper;

import com.hanghae7.alcoholcommunity.domain.notification.dto.NoticeResponseDto;
import com.hanghae7.alcoholcommunity.domain.notification.sse.SseSend;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import com.hanghae7.alcoholcommunity.domain.chat.entity.ChatMessage;
import com.hanghae7.alcoholcommunity.domain.chat.repository.ChatMessageRepository;
import com.hanghae7.alcoholcommunity.domain.common.ResponseDto;
import com.hanghae7.alcoholcommunity.domain.member.entity.Member;
import com.hanghae7.alcoholcommunity.domain.notification.entity.Notice;
import com.hanghae7.alcoholcommunity.domain.notification.repository.NoticeRepository;
import com.hanghae7.alcoholcommunity.domain.party.dto.request.PartyJoinRequestDto;
import com.hanghae7.alcoholcommunity.domain.party.dto.response.ApproveListDto;
import com.hanghae7.alcoholcommunity.domain.party.dto.response.PartyListResponse;
import com.hanghae7.alcoholcommunity.domain.party.entity.Party;
import com.hanghae7.alcoholcommunity.domain.party.entity.PartyParticipate;
import com.hanghae7.alcoholcommunity.domain.party.repository.PartyParticipateRepository;
import com.hanghae7.alcoholcommunity.domain.party.repository.PartyRepository;

import lombok.RequiredArgsConstructor;

import static com.hanghae7.alcoholcommunity.domain.notification.controller.SseController.getEmitter;

@RequiredArgsConstructor
@Service
public class PartyParticipateService {

	private final PartyParticipateRepository partyParticipateRepository;
	private final PartyRepository partyRepository;
	private final NoticeRepository noticeRepository;
	private final ChatMessageRepository chatMessageRepository;
	private final ObjectMapper objectMapper;
	private final SseSend sseSend;

	/**
	 * 모임신청 메소드, 신청 save시 기본 awating값은 True 설정
	 * @param partyId FE에서 매개변수로 전달한 Party의 Id
	 * @param member token을 통해 얻은 Member
	 * @return PartyID와 신청한 Member값 반환
	 */
	@Transactional
	public ResponseEntity<ResponseDto> participateParty(Long partyId, PartyJoinRequestDto partyJoinRequestDto, Member member) {
		if(member.getAuthority().equals("BLOCK")){
			return new ResponseEntity<>(new ResponseDto(400, "정지된 아이디 입니다."), HttpStatus.OK);
		}
		Party party = new Party();
		try {
			party = partyRepository.findById(partyId).orElseThrow(
				() -> new IllegalArgumentException("존재하지 않는 모임 입니다."));
		} catch (IllegalArgumentException e) {
			return new ResponseEntity<>(new ResponseDto(400, "존재하지 않는 모임 입니다."), HttpStatus.OK);
		}

		Optional<PartyParticipate> participate = partyParticipateRepository.findByisDeletedFalseAndPartyAndMember(party, member);
		if (party.isRecruitmentStatus() == true) {
			if (participate.isEmpty()) {
				if (partyJoinRequestDto.getAmountAlcohol() == null && partyJoinRequestDto.getReason() == null) {
					return new ResponseEntity<>(new ResponseDto(400, "주량과 모임신청사유를 적어주세요!"), HttpStatus.OK);
				} else if (partyJoinRequestDto.getAmountAlcohol() == null) {
					return new ResponseEntity<>(new ResponseDto(400, "주량을 적어주세요!"), HttpStatus.OK);
				} else if (partyJoinRequestDto.getReason() == null) {
					return new ResponseEntity<>(new ResponseDto(400, "모임신청사유를 적어주세요!"), HttpStatus.OK);
				}
				partyParticipateRepository.save(new PartyParticipate(party, member, partyJoinRequestDto));
				return new ResponseEntity<>(new ResponseDto(200, "모임 신청에 성공했습니다."), HttpStatus.OK);
			} else if (participate.get().isHost()) {
				return new ResponseEntity<>(new ResponseDto(200, "이미 호스트인 모임입니다."), HttpStatus.OK);
			} else if (participate.get().isRejected()) {
				return new ResponseEntity<>(new ResponseDto(200, "거절 된 모임입니다."), HttpStatus.OK);
			} else if (participate.get().isAwaiting()) {
				partyParticipateRepository.softDeletePartyParticipate(participate.get().getId());

				return new ResponseEntity<>(new ResponseDto(200, "모임 신청이 성공적으로 취소되었습니다."), HttpStatus.OK);
			} else {
				partyParticipateRepository.softDeletePartyParticipate(participate.get().getId());
				party.subCurrentCount();
				return new ResponseEntity<>(new ResponseDto(200, "모임 신청이 성공적으로 취소되었습니다."), HttpStatus.OK);
			}
		}
		else {
			if (participate.isEmpty()) {
				return new ResponseEntity<>(new ResponseDto(200, "모집이 마감된 모임입니다."), HttpStatus.OK);
			}
			else{
				partyParticipateRepository.softDeletePartyParticipate(participate.get().getId());
				party.subCurrentCount();
				party.setRecruitmentStatus(true);
				return new ResponseEntity<>(new ResponseDto(200, "모임에서 탈퇴하였습니다."), HttpStatus.OK);
			}
		}
	}
	/**
	 * 주최자가 승인신청 여부판단, 꽉찬 모임이라면 승인안됨
	 * @param participateId 파티신청 정보의 ID
	 * @return 승인여부 리턴
	 */
	@Transactional
	public ResponseEntity<ResponseDto> acceptParty(Long participateId) {

		PartyParticipate participate = new PartyParticipate();
		try {
			participate = partyParticipateRepository.findById(participateId).orElseThrow(
				() -> new IllegalArgumentException("존재하지않는 참여자입니다."));
		} catch (IllegalArgumentException e) {
			return new ResponseEntity<>(new ResponseDto(400, "존재하지 않는 참여자 입니다."), HttpStatus.OK);
		}
		Party party = new Party();
		try {
			party = partyRepository.findById(participate.getParty().getPartyId()).orElseThrow(
				() -> new IllegalArgumentException("존재하지 않는 모임 입니다."));
		} catch (IllegalArgumentException e) {
			return new ResponseEntity<>(new ResponseDto(400, "존재하지 않는 모임 입니다."), HttpStatus.OK);
		}

		if (party.isRecruitmentStatus()) {
			participate.setAwaiting(false);
			PartyParticipate partyParticipate = partyParticipateRepository.findByisDeletedFalseAndHostTrueAndParty(party);
			participate.setChatRoom(partyParticipate.getChatRoom());
			party.addCurrentCount();
			ChatMessage chatMessage = new ChatMessage(ChatMessage.MessageType.ENTER, partyParticipate.getChatRoom().getChatRoomUniqueId(), participate.getMember(), participate.getMember().getMemberName()+" 님이 채팅에 참여하였습니다", LocalDateTime.now(),  partyParticipate.getChatRoom());
			chatMessageRepository.save(chatMessage);
			//채팅방에 추가해주는 로직추가되야함
			if (party.getCurrentCount() == party.getTotalCount()) {
				party.setRecruitmentStatus(false);
			}

			// 파티 참가승인 알림 메세지 생성
//			Notice notice = new Notice(party.getPartyId(), party.getTitle(), true, false, participate.getMember());
//			noticeRepository.save(notice);
			// 파티 참가승인 알림 전송 파트
			String  participantId = participate.getMember().getMemberUniqueId();
			try{
				SseEmitter emitter = getEmitter(participantId);
				if(emitter!=null){
					// 연결되어 있는 사용자의 sseStream으로 알림데이터 전송
					Notice notice = new Notice(party.getPartyId(), party.getTitle(),true, false, participate.getMember());
					NoticeResponseDto noticeResponseDto = new NoticeResponseDto(notice);
					String jsonData = objectMapper.writeValueAsString(noticeResponseDto);
					try{
						sseSend.sseSend(emitter, jsonData);
					} catch (IOException e) {
						e.printStackTrace();
					}
					notice.updateRead(true);	// sse 전송 후 메세지의 읽음 상태를 변경
					noticeRepository.save(notice);	// 메세지를 리포지토리에 저장
				}
				else{
					// DB에 멤버별로 부재중 알림 저장
					Notice notice = new Notice(party.getPartyId(), party.getTitle(),true, false, participate.getMember());
					noticeRepository.save(notice);
					participate.getMember().getMemberNotice().add(notice);
				}
			} catch (IOException e) {
				return new ResponseEntity<>(new ResponseDto(400, e.getMessage()), HttpStatus.OK);
			}
			// -> 파티 참가승인 알림 전송 파트

		} else {
			return new ResponseEntity<>(new ResponseDto(200, "이미 꽉찬 모임방 입니다."), HttpStatus.OK);
		}
		return new ResponseEntity<>(new ResponseDto(200, "해당 유저를 승인하였습니다."), HttpStatus.OK);
	}

	/**
	 * 주최자가 대기 인원 중에 승인거부하고 싶은 대기 인원 승인 거부
	 * @param participateId 파티신청 정보의 ID
	 * @return 승인거절 여부 리턴
	 */
	@Transactional
	public ResponseEntity<ResponseDto> removeWaiting(Long participateId) {
		PartyParticipate participate = new PartyParticipate();
		try {
			participate = partyParticipateRepository.findById(participateId).orElseThrow(
				() -> new IllegalArgumentException("존재하지않는 참여자입니다."));
		} catch (IllegalArgumentException e) {
			return new ResponseEntity<>(new ResponseDto(400, "존재하지 않는 참여자 입니다."), HttpStatus.OK);
		}

		participate.setRejection(true);

		Party party = new Party();
		try {
			party = partyRepository.findById(participate.getParty().getPartyId()).orElseThrow(
					() -> new IllegalArgumentException("존재하지 않는 모임 입니다."));
		} catch (IllegalArgumentException e) {
			return new ResponseEntity<>(new ResponseDto(400, "존재하지 않는 모임 입니다."), HttpStatus.OK);
		}


		// 파티 참가거절 알림 메세지 생성
//		Notice notice = new Notice(party.getPartyId(), party.getTitle(), false, false, participate.getMember());
//		noticeRepository.save(notice);
		// 파티 참가승인 알림 전송 파트
		String  participantId = participate.getMember().getMemberUniqueId();
		try{
			SseEmitter emitter = getEmitter(participantId);
			if(emitter!=null){
				// 연결되어 있는 사용자의 sseStream으로 알림데이터 전송
				Notice notice = new Notice(party.getPartyId(), party.getTitle(),false, false, participate.getMember());
				NoticeResponseDto noticeResponseDto = new NoticeResponseDto(notice);
				String jsonData = objectMapper.writeValueAsString(noticeResponseDto);
				try{
					sseSend.sseSend(emitter, jsonData);
				} catch (IOException e) {
					e.printStackTrace();
				}
				notice.updateRead(true);	// sse 전송 후 메세지의 읽음 상태를 변경
				noticeRepository.save(notice);	// 메세지를 리포지토리에 저장
			}
			else{
				// DB에 멤버별로 부재중 알림 저장
				Notice notice = new Notice(party.getPartyId(), party.getTitle(),false, false, participate.getMember());
				noticeRepository.save(notice);
				participate.getMember().getMemberNotice().add(notice);
			}
		} catch (IOException e) {
			return new ResponseEntity<>(new ResponseDto(400, e.getMessage()), HttpStatus.OK);
		}
		// -> 파티 참가승인 알림 전송 파트

		return new ResponseEntity<>(new ResponseDto(200, "해당 유저를 승인 거절 하였습니다."), HttpStatus.OK);
	}

	/**
	 * 모임 리스트 (전체/승인완료된리스트/승인대기중인 리스트)
	 * @param member token을 통해 얻은 Member
	 * @return approveStatus값에 따른 모임리스트 출력
	 */
	@Transactional(readOnly = true)
	public ResponseEntity<ResponseDto> getParticipatePartyList(Member member) {
		if(member.getAuthority().equals("BLOCK")){
			return new ResponseEntity<>(new ResponseDto(400, "정지된 아이디 입니다."), HttpStatus.OK);
		}
		List<PartyParticipate> parties;
		parties = partyParticipateRepository.findByisDeletedFalseAndHostFalseAndMemberOrderByPartyPartyDate(member);
		List<PartyListResponse> partyList = new ArrayList<>();
		for (PartyParticipate party : parties) {
			int state = getState(party);
			PartyListResponse partyResponse = new PartyListResponse(party.getParty(), state);
			List<PartyParticipate> partyParticipates = partyParticipateRepository.findByisDeletedFalseAndAwaitingFalseAndPartyPartyIdOrderByHostDesc(
				party.getParty().getPartyId());
			partyResponse.getparticipateMembers(partyParticipates.stream()
				.map(PartyParticipate::getMember)
				.collect(Collectors.toList()));
			partyList.add(partyResponse);
		}
		return new ResponseEntity<>(new ResponseDto(200, "모임 조회에 성공했습니다.", partyList), HttpStatus.OK);
	}

	public int getState(PartyParticipate partyParticipate) {
		if (partyParticipate.isRejected()) {
			return 3;
		} else if (partyParticipate.isAwaiting()) {
			return 2;
		} else {
			return 1;
		}
	}

	/**
	 * 내게 들어온 모임 승인 요청 목록
	 * @param member token을 통해 얻은 Member
	 * @return 승인 요청 된 멤버 리스트 출력
	 */
	public ResponseEntity<ResponseDto> getApproveList(Member member) {
		if(member.getAuthority().equals("BLOCK")){
			return new ResponseEntity<>(new ResponseDto(400, "정지된 아이디 입니다."), HttpStatus.OK);
		}
		List<PartyParticipate> parties = partyParticipateRepository.findPartyParticipatesByHostAndMemberId(member);
		Map<Party, List<ApproveListDto>> partyMap = new HashMap<>();

		for (PartyParticipate partyParticipate : parties) {
			Party party = partyParticipate.getParty();
			ApproveListDto approveListDto = new ApproveListDto(partyParticipate);

			if (partyMap.containsKey(party)) {
				partyMap.get(party).add(approveListDto);
			} else {
				List<ApproveListDto> approveListDtos = new ArrayList<>();
				approveListDtos.add(approveListDto);
				partyMap.put(party, approveListDtos);
			}
		}
		List<List<ApproveListDto>> partyLists = new ArrayList<>(partyMap.values());
		return new ResponseEntity<>(new ResponseDto(200, "승인요청멤버 조회에 성공했습니다.", partyLists), HttpStatus.OK);
	}

	public ResponseEntity<ResponseDto> getHostPartyList(Member member) {
		if(member.getAuthority().equals("BLOCK")){
			return new ResponseEntity<>(new ResponseDto(400, "정지된 아이디 입니다."), HttpStatus.OK);
		}
		List<PartyParticipate> parties = partyParticipateRepository.findByisDeletedFalseAndHostTrueAndMemberOrderByPartyPartyDate(member);
		List<PartyListResponse> partyList = new ArrayList<>();
		for (PartyParticipate party : parties) {
			PartyListResponse partyResponse = new PartyListResponse(party.getParty(), 1);
			List<PartyParticipate> partyParticipates = partyParticipateRepository.findByisDeletedFalseAndAwaitingFalseAndPartyPartyIdOrderByHostDesc(party.getParty().getPartyId());
			partyResponse.getparticipateMembers(partyParticipates.stream()
				.map(PartyParticipate::getMember)
				.collect(Collectors.toList()));
			partyList.add(partyResponse);
		}
		return new ResponseEntity<>(new ResponseDto(200, "회원이 호스트인 모임 조회에 성공했습니다.", partyList), HttpStatus.OK);
	}
}