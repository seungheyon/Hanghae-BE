package com.hanghae7.alcoholcommunity.domain.sociallogin.naverlogin.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.hanghae7.alcoholcommunity.domain.sociallogin.kakaologin.dto.KakaoResponseDto;
import com.hanghae7.alcoholcommunity.domain.sociallogin.naverlogin.dto.NaverResponseDto;
import com.hanghae7.alcoholcommunity.domain.sociallogin.naverlogin.service.NaverService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Please explain the class!!
 *
 * @fileName      : NaverController
 * @author        : mycom
 * @since         : 2023-05-22
 */
@Slf4j
@RequiredArgsConstructor
@RestController
public class NaverController {

	private final NaverService naverService;

	/**
	 * callback을 통해 받은 code를 이용하여 token발급과, UserInfo를 받음
	 */
	@GetMapping("/naver/callback")
	public ResponseEntity<NaverResponseDto> getNaverAccount(@RequestParam("code") String code, @RequestParam("state") String state) {
		log.debug("code = {}", code);
		return naverService.getNaverInfo(code, state);
	}

}
