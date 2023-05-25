package com.hanghae7.alcoholcommunity.domain.party.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.hanghae7.alcoholcommunity.domain.common.ResponseDto;
import com.hanghae7.alcoholcommunity.domain.common.security.UserDetailsImplement;
import com.hanghae7.alcoholcommunity.domain.party.dto.request.PartyRequestDto;
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
public class PartyController {

	private final PartyService partyService;

	// 모임 게시글 등록
	@PostMapping("/party/new-party")
	public ResponseEntity<ResponseDto> createParty(@RequestBody PartyRequestDto partyRequestDto, @AuthenticationPrincipal UserDetailsImplement userDetails) {
		return partyService.creatParty(partyRequestDto, userDetails.getMember());
	}

	// 모임 게시글 전체 조회
	@GetMapping("/parties")
	public ResponseEntity<ResponseDto> findAll(@RequestParam int recruitmentStatus, @RequestParam int page) {

		return partyService.findAll(recruitmentStatus, page);
	}

	// 모임 게시글 상세 조회
	@GetMapping("/party/{partyId}")
	public ResponseEntity<ResponseDto> getParty(@PathVariable Long partyId, @AuthenticationPrincipal UserDetailsImplement userDetails) {
		return partyService.getParty(partyId);
	}

	// 모임 게시글 수정
	@PutMapping("/party/{partyId}")
	public ResponseEntity<ResponseDto> updateParty(@PathVariable Long partyId, @RequestBody PartyRequestDto partyRequestDto, @AuthenticationPrincipal UserDetailsImplement userDetails) {
		return partyService.updateParty(partyId, partyRequestDto, userDetails.getMember());
	}

	// 모임 게시글 삭제
	@DeleteMapping("/party/{partyId}")
	public ResponseEntity<ResponseDto> deleteParty(@PathVariable Long partyId, @AuthenticationPrincipal UserDetailsImplement userDetails) {
		return partyService.deleteParty(partyId, userDetails.getMember());
	}



}
