package com.hanghae7.alcoholcommunity.domain.sociallogin.kakaologin.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * accessToken, refreshTeken을 저장하기위한 필드
 */
@ToString
@Getter
@NoArgsConstructor
@JsonNaming(value = PropertyNamingStrategies.SnakeCaseStrategy.class)
public class KakaoToken {

    private String tokenType;
    private String accessToken;
    private String refreshToken;
    private Long expiresIn;
    private Long refreshTokenExpiresIn;

    public static KakaoToken fail() {
        return new KakaoToken(null, null);
    }

    /**
     * code값을 통해 들어온 필드값 생성자
     * @param accessToken accessToken
     * @param refreshToken refreshToken
     */
    private KakaoToken(final String accessToken, final String refreshToken) {

        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
    }
}
