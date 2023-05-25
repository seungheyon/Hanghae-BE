package com.hanghae7.alcoholcommunity.domain.party.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hanghae7.alcoholcommunity.domain.common.ResponseDto;
import com.hanghae7.alcoholcommunity.domain.common.security.UserDetailsImplement;
import com.hanghae7.alcoholcommunity.domain.party.dto.response.JoinPartyResponseDto;
import com.hanghae7.alcoholcommunity.domain.party.dto.response.RecruitingPartyResponseDto;
import com.hanghae7.alcoholcommunity.domain.party.service.PartyParticipateService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
public class PartyParticipateController {

	private final PartyParticipateService partyParticipateService;

	// 모임 참가
	// 참여자만 접근 가능
	@PostMapping("/party/join/{partyId}")
	public ResponseEntity<ResponseDto> participateParty(@PathVariable Long partyId, @AuthenticationPrincipal UserDetailsImplement userDetails) {
		return partyParticipateService.participateParty(partyId, userDetails.getMember());
	}

	// 주최자만 접근 가능
	@PostMapping("/party/accept/{participateId}")
	public ResponseEntity<ResponseDto> acceptParty(@PathVariable Long participateId, @AuthenticationPrincipal UserDetailsImplement userDetails){
		return partyParticipateService.acceptParty(participateId);
	}

	@DeleteMapping("/party/accept/{participateId}")
	public ResponseEntity<ResponseDto> removeWaiting(@PathVariable Long participateId, @AuthenticationPrincipal UserDetailsImplement userDetails){
		return partyParticipateService.removeWaiting(participateId);
	}

	// 참여중인 party 리스트 (채팅방까지 들어간 모임)
	@GetMapping("/party/my-party-list/accepted")
	public ResponseEntity<List<RecruitingPartyResponseDto>> getJoinPartyList(){
		return partyParticipateService.getJoinPartyList();
	}

	// 모임 신청 대기 목록 (승인대기중)
	@GetMapping("party/my-party-list/await")
	public ResponseEntity<List<JoinPartyResponseDto>> getParticipateList(@AuthenticationPrincipal UserDetailsImplement userDetails){
		return partyParticipateService.getParticipatePartyList(userDetails.getMember());
	}
}
