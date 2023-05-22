package com.hanghae7.alcoholcommunity.domain.sociallogin.kakaologin.service;

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

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class KakaoService {

    private final KakaoClient client;

    /**
     * token을 받기위한 url
     */
    @Value(value = "${kakao.authUrl}")
    private String kakaoAuthUrl;

    /**
     * 유저 정보를 얻기위한 url
     */
    @Value("${kakao.userApiUrl}")
    private String kakaoUserApiUrl;

    /**
     * 로그아웃을 하기위한 url 구현중
     */
    @Value("${kakao.logoutUrl}")
    private String kakaKologoutUrl;

    /**
     * RestApiKey
     */
    @Value("${kakao.restapiKey}")
    private String restapiKey;

    /**
     * 코드값을 callback받기위한 주소
     */
    @Value("${kakao.redirectUrl}")
    private String redirectUrl;

    /**
     *
     * @param code api를 통해 요청된 코드값
     * @see KakaoResponseDto
     * @return KakaoResponseDto값 리턴
     */
    public ResponseEntity<KakaoResponseDto> getKakaoInfo(final String code) {

        final KakaoToken token = getKakaoToken(code);
        try {
            KakaoAccount kakaoAccount = client.getKakaoInfo(new URI(kakaoUserApiUrl), token.getTokenType() + " " + token.getAccessToken()).getKakaoAccount();
            KakaoResponseDto responseDto = new KakaoResponseDto(kakaoAccount, token.getAccessToken());
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
    public KakaoToken getKakaoToken(final String code) {

        try {
            return client.getKakaoToken(new URI(kakaoAuthUrl), restapiKey, redirectUrl, code, "authorization_code");
        } catch (Exception e) {
            log.error("Something error..", e);
            return KakaoToken.fail();
        }
    }

}
