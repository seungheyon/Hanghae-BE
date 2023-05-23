package com.hanghae7.alcoholcommunity.domain.party.service;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hanghae7.alcoholcommunity.domain.member.entity.Member;
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


	// 모임 참가
	@Transactional
	public ResponseEntity<Void> participateParty(Long partyId, Member member) {
		Party party = partyRepository.findById(partyId).orElseThrow(
			() -> new IllegalArgumentException("존재하지않는 게시글입니다.")
		);

		party.addCurrentCount();
		partyRepository.save(party);
		PartyParticipate partyParticipate = new PartyParticipate(party, member);
		partyParticipateRepository.save(partyParticipate);

		return ResponseEntity.ok(null);
	}



}
