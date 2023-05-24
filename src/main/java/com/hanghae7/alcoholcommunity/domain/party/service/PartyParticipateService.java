package com.hanghae7.alcoholcommunity.domain.party.service;

import java.util.Objects;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hanghae7.alcoholcommunity.domain.member.entity.Member;
import com.hanghae7.alcoholcommunity.domain.party.dto.ParticipateResponseDto;
import com.hanghae7.alcoholcommunity.domain.party.entity.Party;
import com.hanghae7.alcoholcommunity.domain.party.entity.PartyParticipate;
import com.hanghae7.alcoholcommunity.domain.party.repository.PartyParticipateRepository;
import com.hanghae7.alcoholcommunity.domain.party.repository.PartyRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class PartyParticipateService {

	private final PartyParticipateRepository partyParticipateRepository;
	private final PartyRepository partyRepository;

	/**
	 * 모임신청 메소드, 신청 save시 기본 awating값은 True 설정
	 * @param partyId FE에서 매개변수로 전달한 Party의 Id
	 * @param member token을 통해 얻은 Member
	 * @return PartyID와 신청한 Member값 반환
	 */
	@Transactional
	public ParticipateResponseDto participateParty(Long partyId, Member member) {

		Party party = partyRepository.findById(partyId).orElseThrow(
			() -> new IllegalArgumentException("존재하지않는 게시글입니다.")
		);

		Optional<PartyParticipate> participate = partyParticipateRepository.findByPartyAndMember(party, member);
		if(participate.isEmpty()){
			partyParticipateRepository.save(new PartyParticipate(party, member));
		}
		else if(participate.get().isRejected()){
			return null; // 거절된 사람이라는 리턴값 필요
		}else{
			partyParticipateRepository.delete(participate.get());
		}
		return new ParticipateResponseDto(member.getMemberId(), partyId);
	}

	/**
	 * 승인신청 여부, 꽉찬 모임이라면 승인안됨
	 * @param participateId 파티신청 정보의 ID
	 * @return 승인여부 리턴
	 */
	// 주최자가 참여 여부 판단하기
	@Transactional
	public ResponseEntity<String> acceptParty(Long participateId){
		PartyParticipate participate = partyParticipateRepository.findById(participateId).orElseThrow(
			() -> new IllegalArgumentException("존재하지않는 참여자입니다.")
		);

		Party party = partyRepository.findById(participate.getParty().getPartyId()).orElseThrow(
			() -> new IllegalArgumentException("존재하지않는 게시글입니다.")
		);

		if(party.isRecruitmentStatus()){
			participate.setAwaiting(false);
			party.addCurrentCount();
			//채팅방에 추가해주는 로직추가되야함
			if(party.getCurrentCount() == party.getTotalCount()){
				party.setRecruitmentStatus(false);
			}
		}
		else{
			return new ResponseEntity<>("참여 인원이 가득 찼습니다.",HttpStatus.BAD_REQUEST);
		}
		return ResponseEntity.ok(null);
	}

	// 주최자가 대기 인원 중에 삭제하고 싶은 대기 인원 삭제 메서드(승인거부)
	@Transactional
	public ResponseEntity<Void> removeWaiting(Long participateId){
		PartyParticipate participate = partyParticipateRepository.findById(participateId).orElseThrow(
			() -> new IllegalArgumentException("존재하지않는 참여글입니다.")
		);

		participate.setRejected(true);

		return ResponseEntity.ok(null);
	}

	// 참여중인 party 리스트를 불러올 때 멤버의 partyParticipate 리스트를 불러와서 true인 경우만 따로 빼내서 응답할 수 있는 메서드 필요


}