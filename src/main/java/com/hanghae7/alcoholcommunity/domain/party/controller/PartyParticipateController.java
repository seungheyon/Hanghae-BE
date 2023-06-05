package com.hanghae7.alcoholcommunity.domain.party.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.hanghae7.alcoholcommunity.domain.common.ResponseDto;
import com.hanghae7.alcoholcommunity.domain.common.security.UserDetailsImplement;
import com.hanghae7.alcoholcommunity.domain.party.dto.request.PartyJoinRequestDto;
import com.hanghae7.alcoholcommunity.domain.party.service.PartyParticipateService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
public class PartyParticipateController {

	private final PartyParticipateService partyParticipateService;

	/**
	 * 모임신청 메소드, 신청 save시 기본 awating값은 True 설정
	 * @param partyId FE에서 매개변수로 전달한 Party의 Id
	 * @param userDetails 사용자 정보
	 * @return PartyID와 신청한 Member값 반환
	 */
	@PostMapping("/party/join/{partyId}")
	public ResponseEntity<ResponseDto> participateParty(@PathVariable Long partyId, @RequestBody PartyJoinRequestDto partyJoinRequestDto, @AuthenticationPrincipal UserDetailsImplement userDetails) {
		return partyParticipateService.participateParty(partyId, partyJoinRequestDto, userDetails.getMember());
	}

	/**
	 * 주최자가 승인신청 여부판단, 꽉찬 모임이라면 승인안됨
	 * @param participateId 파티신청 정보의 ID
	 * @param userDetails 사용자 정보
	 * @return 승인여부 리턴
	 */
	@PostMapping("/party/accept/{participateId}")
	public ResponseEntity<ResponseDto> acceptParty(@PathVariable Long participateId, @AuthenticationPrincipal UserDetailsImplement userDetails){
		return partyParticipateService.acceptParty(participateId);
	}

	/**
	 * 주최자가 대기 인원 중에 승인거부하고 싶은 대기 인원 승인 거부
	 * @param participateId 파티신청 정보의 ID
	 * @param userDetails 사용자 정보
	 * @return 승인거절 여부 리턴
	 */
	@DeleteMapping("/party/accept/{participateId}")
	public ResponseEntity<ResponseDto> removeWaiting(@PathVariable Long participateId, @AuthenticationPrincipal UserDetailsImplement userDetails){
		return partyParticipateService.removeWaiting(participateId);
	}

	/**
	 * 모임 리스트 (전체/승인완료된리스트/승인대기중인 리스트)
	 * @param userDetails 사용자 정보
	 * @return approveStatus값에 따른 모임리스트 출력
	 */
	@GetMapping("party/my-party-list")
	public ResponseEntity<ResponseDto> getParticipateList(@AuthenticationPrincipal UserDetailsImplement userDetails){
		return partyParticipateService.getParticipatePartyList(userDetails.getMember());
	}



	/**
	 * 내게 들어온 모임 승인 요청 목록
	 * @param userDetails 사용자 정보
	 * @return 승인 요청 된 멤버 리스트 출력
	 */

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
