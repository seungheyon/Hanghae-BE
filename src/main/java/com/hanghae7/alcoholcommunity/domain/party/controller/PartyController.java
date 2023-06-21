package com.hanghae7.alcoholcommunity.domain.party.controller;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;

import com.hanghae7.alcoholcommunity.domain.party.dto.request.PartyRequestDto;
import com.hanghae7.alcoholcommunity.domain.party.service.PartyService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.hanghae7.alcoholcommunity.domain.common.ResponseDto;
import com.hanghae7.alcoholcommunity.domain.common.security.UserDetailsImplement;

import lombok.Getter;
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

	/**
	 * 모임 게시글 등록
	 * @param partyRequestDto 유저 입력값
	 * @param userDetails 사용자 정보
	 * @return 모임 생성 유무
	 */
	@PostMapping("/party/new-party")

	public ResponseEntity<ResponseDto> createParty( @RequestPart(value = "data") PartyRequestDto partyRequestDto,
													@RequestPart(value ="image", required = false) MultipartFile image,
													@AuthenticationPrincipal UserDetailsImplement userDetails) throws
		IOException {
		return partyService.createParty(partyRequestDto, userDetails.getMember(), image);

	}

	/**
	 *  모임 전체조회(전체/모집중/모집마감)
	 * @param recruitmentStatus  0: 전체 리스트 / 1: 승인완료된 모임리스트 / 2: 승인 대기중인 모임 리스트
	 * @param page 요청한 페이지 번호
	 * @param request 토큰값을 확인하기 위한 정보
	 * @return 각 리스트 출력
	 */
	@GetMapping("/parties/test")
	public ResponseEntity<ResponseDto> findAll(@RequestParam(defaultValue = "500")double radius, @RequestParam(defaultValue = "127.027616")double longitude, @RequestParam(defaultValue = "37.497967") double latitude, @RequestParam int recruitmentStatus,
		@RequestParam int page, HttpServletRequest request) {

		return partyService.findAll(radius, longitude, latitude, page,  recruitmentStatus, request);
	}


	/**
	 *  모임 전체조회(전체/모집중/모집마감)
	 * @param recruitmentStatus  0: 전체 리스트 / 1: 승인완료된 모임리스트 / 2: 승인 대기중인 모임 리스트
	 * @param page 요청한 페이지 번호
	 * @param request 토큰값을 확인하기 위한 정보
	 * @return 각 리스트 출력
	 */
	@GetMapping("/parties/search")
	public ResponseEntity<ResponseDto> findAllSearch(@RequestParam(defaultValue = "500")double radius, @RequestParam(defaultValue = "127.027616")double longitude, @RequestParam(defaultValue = "37.497967") double latitude, @RequestParam int recruitmentStatus,
		@RequestParam int page, HttpServletRequest request, @RequestParam String keyword) {

		return partyService.findAllSearch(radius, longitude, latitude, page,  recruitmentStatus, request, keyword);
	}

	/**
	 * 모임 상세조회
	 * @param partyId FE에서 매개변수로 전달한 Party의 Id
	 * @param userDetails 사용자 정보
	 * @return 모임 게시글에 속한 모든 내용
	 */
	@GetMapping("/party/{partyId}")
	public ResponseEntity<ResponseDto> getParty(@PathVariable Long partyId, @AuthenticationPrincipal UserDetailsImplement userDetails) {
		return partyService.getParty(partyId, userDetails.getMember());
	}

	/**
	 * 모임 게시글 수정
	 * @param partyId FE에서 매개변수로 전달한 Party의 Id
	 * @param partyRequestDto 유저 입력값
	 * @param userDetails 사용자 정보
	 * @return 수정 성공 유무
	 */
	@PutMapping("/party/{partyId}")
	public ResponseEntity<ResponseDto> updateParty( @PathVariable Long partyId,
													@RequestPart(value = "data") PartyRequestDto partyRequestDto,
													@RequestPart(value ="image", required = false) MultipartFile image,
													@AuthenticationPrincipal UserDetailsImplement userDetails)
													throws IOException{
		return partyService.updateParty(partyId, partyRequestDto, userDetails.getMember(), image);
	}

	/**
	 * 모임 게시글 삭제
	 * @param partyId FE에서 매개변수로 전달한 Party의 Id
	 * @param userDetails 사용자 정보
	 * @return 삭제 성공 유무
	 */
	@DeleteMapping("/party/{partyId}")
	public ResponseEntity<ResponseDto> deleteParty(@PathVariable Long partyId, @AuthenticationPrincipal UserDetailsImplement userDetails) {
		return partyService.deleteParty(partyId, userDetails.getMember());
	}

	@GetMapping("/test")
	public ResponseEntity<Void> forTest(){
		return partyService.forTest();
	}

}
