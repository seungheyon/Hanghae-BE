package com.hanghae7.alcoholcommunity.domain.sociallogin.naverlogin.service;

import java.net.URI;
import java.net.URISyntaxException;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.hanghae7.alcoholcommunity.domain.sociallogin.kakaologin.client.KakaoClient;
import com.hanghae7.alcoholcommunity.domain.sociallogin.kakaologin.dto.KakaoAccount;
import com.hanghae7.alcoholcommunity.domain.sociallogin.kakaologin.dto.KakaoResponseDto;
import com.hanghae7.alcoholcommunity.domain.sociallogin.kakaologin.dto.KakaoToken;
import com.hanghae7.alcoholcommunity.domain.sociallogin.naverlogin.client.NaverClient;
import com.hanghae7.alcoholcommunity.domain.sociallogin.naverlogin.dto.NaverResponseDto;
import com.hanghae7.alcoholcommunity.domain.sociallogin.naverlogin.dto.NaverToken;
import com.hanghae7.alcoholcommunity.domain.sociallogin.naverlogin.dto.Response;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Please explain the class!!
 *
 * @fileName      : NaverService
 * @author        : mycom
 * @since         : 2023-05-22
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class NaverService {

	private final NaverClient naverClient;

	/**
	 * token을 받기위한 url
	 */
	@Value(value = "${naver.authUrl}")
	private String naverAuthUrl;

	/**
	 * 유저 정보를 얻기위한 url
	 */
	@Value("${naver.userApiUrl}")
	private String naverUserApiUrl;

	/**
	 * clientId key값
	 */
	@Value("${naver.clientID}")
	private String naverClientId;

	/**
	 * clientSecret key
	 */
	@Value("${naver.clientSecret}")
	private String naverClientSecret;

	public ResponseEntity<NaverResponseDto> getNaverInfo(final String code, final String state) {

		final NaverToken token = getNavertoken(code, state);
		try {
			Response naverResponse = naverClient.getNaverInfo(new URI(naverUserApiUrl), token.getTokenType() + " " + token.getAccessToken()).getResponse();
			NaverResponseDto responseDto = new NaverResponseDto(naverResponse, token.getAccessToken());
			log.debug("token = {}", token);
			return ResponseEntity.ok(responseDto);
		} catch (URISyntaxException e) {
			log.error("Invalid URI syntax", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}

	/**
	 *
	 * @param code getInfo메소드에서 입력된 code값을 그대로 받아옴
	 * @return KakaoToken값에 accessToken, refreshToken값 리턴
	 */
	public NaverToken getNavertoken(final String code, final String state) {

		try {
			return naverClient.getNaverToken(new URI(naverAuthUrl),"authorization_code", naverClientId, naverClientSecret, code, state);
		} catch (Exception e) {
			log.error("Something error..", e);
			return NaverToken.fail();
		}
	}

}
