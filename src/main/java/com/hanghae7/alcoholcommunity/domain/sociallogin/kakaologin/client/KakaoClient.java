package com.hanghae7.alcoholcommunity.domain.sociallogin.kakaologin.client;

import java.net.URI;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

import com.hanghae7.alcoholcommunity.domain.sociallogin.config.FeignConfiguration;
import com.hanghae7.alcoholcommunity.domain.sociallogin.kakaologin.dto.KakaoInfo;
import com.hanghae7.alcoholcommunity.domain.sociallogin.kakaologin.dto.KakaoToken;

/**
 * @author  조우필
 * kakaodevelper로 api요청을 보내기위한 Client구성
 */
@FeignClient(name = "kakaoClient", configuration = FeignConfiguration.class)
public interface KakaoClient {

    /**
     *
     * @param baseUrl 토큰을 받기위해 보내는 api url
     * @param restApiKey develper의 RestApi key
     * @param redirectUrl 해당 요청에대한 응답을 받을 url
     * @param code 첫 api요청을 통해 들어온 code(일회성 코드로 token을 요청한다)
     * @param grantType 받을 code의 타입
     * @return Kakao요청에 사용하기위한 Aceesstoken과 refrestoken값을 반환
     */
    @PostMapping
    KakaoToken getKakaoToken(URI baseUrl, @RequestParam("client_id") String restApiKey,
        @RequestParam("redirect_uri") String redirectUrl,
        @RequestParam("code") String code,
        @RequestParam("grant_type") String grantType);

    /**
     *
     * @param baseUrl UserInfo를 요청하기위한 Url주소
     * @param accessToken code값을 통해 받은 accessToken을 헤더에 해당 Param으로 보냄
     * @return 인증허가가된 유저의 정보를 반환
     */
    @PostMapping
    KakaoInfo getKakaoInfo(URI baseUrl, @RequestHeader("Authorization") String accessToken);

    /**
     *
     * @param baseUrl 카카오 api로 로그아웃을 하기위하 Url주소
     * @param accessToken 로그아웃을 하기위한 accessToken
     */
    @PostMapping
    void logout(URI baseUrl, @RequestHeader("Authorization") String accessToken);

}
