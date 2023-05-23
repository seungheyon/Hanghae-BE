package com.hanghae7.alcoholcommunity.domain.party.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hanghae7.alcoholcommunity.domain.common.security.UserDetailsImplement;
import com.hanghae7.alcoholcommunity.domain.party.dto.PartyDetailResponseDto;
import com.hanghae7.alcoholcommunity.domain.party.dto.PartyRequestDto;
import com.hanghae7.alcoholcommunity.domain.party.service.PartyService;

import lombok.RequiredArgsConstructor;

/**
 * Please explain the class!!
 *
 * @fileName      : example
 * @author        : mycom
 * @since         : 2023-05-19
 */

@RequiredArgsConstructor
@RestController
@RequestMapping("/api")
public class PartyController {

	private final PartyService partyService;

	// 모임 게시글 등록
	@PostMapping("/party")
	public ResponseEntity<Void> createParty(@RequestBody PartyRequestDto partyRequestDto, @AuthenticationPrincipal UserDetailsImplement userDetails) {
		return partyService.creatParty(partyRequestDto, userDetails.getMember());
	}

	// 모임 게시글 전체 조회


	// 모임 게시글 상세 조회
	@GetMapping("/parties/party/{partyId}") //<-url  수정필요해보임
	public ResponseEntity<PartyDetailResponseDto> getParty(@PathVariable Long partyId, @AuthenticationPrincipal UserDetailsImplement userDetails) {
		return partyService.getParty(partyId, userDetails.getMember());
	}

	// 모임 게시글 수정




	// 모임 게시글 삭제
	@DeleteMapping("/party/{partyId}")
	public ResponseEntity<Void> deleteParty(@PathVariable Long partyId, @AuthenticationPrincipal UserDetailsImplement userDetails) {
		return partyService.deleteParty(partyId, userDetails.getMember());
	}



}
