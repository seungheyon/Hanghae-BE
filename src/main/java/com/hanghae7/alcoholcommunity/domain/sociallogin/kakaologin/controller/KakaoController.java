package com.hanghae7.alcoholcommunity.domain.sociallogin.kakaologin.controller;

import java.time.LocalDateTime;
import java.util.Date;

import javax.servlet.http.HttpServletResponse;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.hanghae7.alcoholcommunity.domain.common.ResponseDto;
import com.hanghae7.alcoholcommunity.domain.sociallogin.kakaologin.dto.KakaoResponseDto;
import com.hanghae7.alcoholcommunity.domain.sociallogin.kakaologin.service.KakaoService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@RestController
public class KakaoController {

    private final KakaoService kakaoService;
    /**
     * callback을 통해 받은 code를 이용하여 token발급과, UserInfo를 받음
     */
    @PostMapping("/kakao/callback")
    public ResponseEntity<ResponseDto> getKakaoAccount(@RequestParam("code") String code, final HttpServletResponse response) {
        log.debug("code = {}", code);
        return kakaoService.getKakaoInfo(code, response);
    }

}
