package com.hanghae7.alcoholcommunity.domain.party.service;

import java.util.Objects;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

	// 모임 참가 신청
	// 대기자 명단에 올리기
	@Transactional
	public ParticipateResponseDto participateParty(Long partyId, Member member, Party title) {
		Party party = partyRepository.findById(partyId).orElseThrow(
			() -> new IllegalArgumentException("존재하지않는 게시글입니다.")
		);

		if(!Objects.equals(member.getMemberId(), party.getMember().getMemberId())) {
			PartyParticipate participate = partyParticipateRepository.findByPartyAndMember(party, member);

			// 참여 신청만 누른 상태로 party 엔티티 상의 currentCount 는 건들지 않음
			// 주최자가 Awaiting 값을 true로 바꿔줄 경우 currentCount 를 올려줄거임
			if (participate == null) {
				PartyParticipate partyParticipate = new PartyParticipate(party, member);
				partyParticipate.setAwaiting(false);
				partyParticipateRepository.save(partyParticipate);
			} else {
				partyParticipateRepository.delete(participate);
			}
		} else {
			throw new IllegalArgumentException("참여자만 가능합니다."); // 주최자는 X
		}
		return new ParticipateResponseDto(member.getMemberId(), partyId, title);
	}

	// 주최자가 참여 여부 판단하기
	@Transactional
	public ResponseEntity<String> acceptParty(Long participateId, Member member){
		PartyParticipate participate = partyParticipateRepository.findById(participateId).orElseThrow(
			() -> new IllegalArgumentException("존재하지않는 참여글입니다.")
		);

		Party party = partyRepository.findById(participate.getParty().getPartyId()).orElseThrow(
			() -> new IllegalArgumentException("존재하지않는 게시글입니다.")
		);

		if(Objects.equals(member.getMemberId(), party.getMember().getMemberId())) {
			if(party.isProcessing()) {
				participate.setAwaiting(true);
				partyParticipateRepository.save(participate);
				party.addCurrentCount();
				// 가득 차면 processing false로 변경
				if(party.getCurrentCount() == party.getTotalCount()) party.setProcessing(false);
			}
			return new ResponseEntity<>("참여 인원이 가득 찼습니다.",HttpStatus.BAD_REQUEST);
		}

		return ResponseEntity.ok(null);
	}

	// 주최자가 대기 인원 중에 삭제하고 싶은 대기 인원 삭제 메서드(승인거부)
	@Transactional
	public ResponseEntity<Void> removeWaiting(Long participateId, Member member){
		PartyParticipate participate = partyParticipateRepository.findById(participateId).orElseThrow(
			() -> new IllegalArgumentException("존재하지않는 참여글입니다.")
		);

		Party party = partyRepository.findById(participate.getParty().getPartyId()).orElseThrow(
			() -> new IllegalArgumentException("존재하지않는 게시글입니다.")
		);

		if(Objects.equals(member.getMemberId(), party.getMember().getMemberId())) {
			// 대기자가 승인되지 않은 false 일때만 삭제 가능하도록!
			if(!participate.isAwaiting()) {
				partyParticipateRepository.delete(participate);
			}
		}

		return ResponseEntity.ok(null);
	}

	// 참여중인 party 리스트를 불러올 때 멤버의 partyParticipate 리스트를 불러와서 true인 경우만 따로 빼내서 응답할 수 있는 메서드 필요


}