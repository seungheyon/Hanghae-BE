package com.hanghae7.alcoholcommunity.domain.sociallogin.naverlogin.client;

import java.net.URI;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

import com.hanghae7.alcoholcommunity.domain.sociallogin.config.FeignConfiguration;
import com.hanghae7.alcoholcommunity.domain.sociallogin.kakaologin.dto.KakaoInfo;
import com.hanghae7.alcoholcommunity.domain.sociallogin.kakaologin.dto.KakaoToken;
import com.hanghae7.alcoholcommunity.domain.sociallogin.naverlogin.dto.NaverInfo;
import com.hanghae7.alcoholcommunity.domain.sociallogin.naverlogin.dto.NaverToken;

/**
 * Please explain the class!!
 *
 * @fileName      : NaverClient
 * @author        : 조우필
 * @since         : 2023-05-22
 */
@FeignClient(name = "naverClient", configuration = FeignConfiguration.class)
public interface NaverClient {

	/**
	 *
	 * @param baseUrl 네이버 토큰 요청 Url
	 * @param grantType 요청 타입
	 * @param ClientId Naver API ClientId
	 * @param SecretKey Naver API ClientSecretkey
	 * @param code 요청을 통해 얻은 1회성 코드
	 * @param state 요청한 state
	 * @return naver에 사용하기위한 Aceesstoken과 refrestoken값을 반환
	 */
	@PostMapping
	NaverToken getNaverToken(URI baseUrl, @RequestParam("grant_type") String grantType,
		@RequestParam("client_id") String ClientId,
		@RequestParam("client_secret") String SecretKey,
		@RequestParam("code") String code,
		@RequestParam("state") String state
		);

	/**
	 *
	 * @param baseUrl 네이버 유저정보 요청을 위한 Url
	 * @param accessToken gettoken을 통해 얻은 AccessToken
	 * @return 인증허가가된 유저의 정보를 반환
	 */
	@PostMapping
	NaverInfo getNaverInfo(URI baseUrl, @RequestHeader("Authorization") String accessToken);
}
