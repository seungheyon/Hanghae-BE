package com.hanghae7.alcoholcommunity.domain.sociallogin.kakaologin.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * KakaoAccount에 저장된 필드를 가져온 정보를 자동으로 Snake방식으로 json맵핑시켜 저장
 */
@ToString
@Getter
@NoArgsConstructor
@JsonNaming(value = PropertyNamingStrategies.SnakeCaseStrategy.class)
public class KakaoInfo {

    private KakaoAccount kakaoAccount;

    /**
     * @return 값이 없을시 null 값저장
     */
    public static KakaoInfo fail() {
        return null;
    }
}
