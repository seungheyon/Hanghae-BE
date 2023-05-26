package com.hanghae7.alcoholcommunity.domain.party.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
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

	// 모임 신청 대기 목록 (승인대기중)
	@GetMapping("party/my-party-list")
	public ResponseEntity<ResponseDto> getParticipateList(@RequestParam int approveStatus, @AuthenticationPrincipal UserDetailsImplement userDetails){
		return partyParticipateService.getParticipatePartyList(approveStatus ,userDetails.getMember());
	}

	@GetMapping("/party/approve")
	public ResponseEntity<ResponseDto> getApproveList(@AuthenticationPrincipal UserDetailsImplement userDetails){
		return partyParticipateService.getApproveList(userDetails.getMember());
	}

	/**
	 * 회원이 호스트인 파티리스트 출력
	 * @param userDetails 로그인된 유저정보
	 * @return 호스트인 파티리스트 출력
	 */
	@GetMapping("party/host-party-list")
	public ResponseEntity<ResponseDto> getHostPartyList(@AuthenticationPrincipal UserDetailsImplement userDetails){
		return partyParticipateService.getHostPartyList(userDetails.getMember());
	}



}
