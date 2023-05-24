package com.hanghae7.alcoholcommunity.domain.party.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hanghae7.alcoholcommunity.domain.common.security.UserDetailsImplement;
import com.hanghae7.alcoholcommunity.domain.party.dto.ParticipateResponseDto;
import com.hanghae7.alcoholcommunity.domain.party.service.PartyParticipateService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
public class PartyParticipateController {

	private final PartyParticipateService partyParticipateService;

	// 모임 참가
	// 참여자만 접근 가능
	@PostMapping("/party/join/{partyId}")
	public ParticipateResponseDto participateParty(@PathVariable Long partyId, @AuthenticationPrincipal UserDetailsImplement userDetails) {
		return partyParticipateService.participateParty(partyId, userDetails.getMember());
	}

	// 주최자만 접근 가능
	@PostMapping("/party/accept/{participateId}")
	public ResponseEntity<String> acceptParty(@PathVariable Long participateId, @AuthenticationPrincipal UserDetailsImplement userDetails){
		return partyParticipateService.acceptParty(participateId, userDetails.getMember());
	}

	@DeleteMapping("/party/accept/{participateId}")
	public ResponseEntity<Void> removeWaiting(@PathVariable Long participateId, @AuthenticationPrincipal UserDetailsImplement userDetails){
		return partyParticipateService.removeWaiting(participateId);
	}

	// 참여중인 party 리스트 불러오기
}
