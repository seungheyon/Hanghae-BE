package com.hanghae7.alcoholcommunity.domain.notification.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hanghae7.alcoholcommunity.domain.common.ResponseDto;
import com.hanghae7.alcoholcommunity.domain.common.security.UserDetailsImplement;
import com.hanghae7.alcoholcommunity.domain.notification.service.NoticeService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class NoticeController {

	private final NoticeService noticeService;

	@PostMapping("/notice")
	public ResponseEntity<ResponseDto> checkNotice(
		@AuthenticationPrincipal UserDetailsImplement userDetails
	){
		return noticeService.checkNotice(userDetails.getMember());
	}

}
