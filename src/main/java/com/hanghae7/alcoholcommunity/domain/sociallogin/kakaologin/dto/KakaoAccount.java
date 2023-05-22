package com.hanghae7.alcoholcommunity.domain.sociallogin.kakaologin.dto;

import lombok.Getter;
import lombok.ToString;

/**
 * Kakao에서 받을 유저저옵를 매칭시키기 위한 Dto
 */
@Getter
@ToString
public class KakaoAccount {
    /**
     * 유저의 정보 : 닉네임, 이미지 포함
     */
    private Profile profile;
    /**
     * 유저의 성별
     */
    private String gender;

    /**
     * 유저의 가입된 이메일
     */
    private String email;
    /**
     * 유저의 연령
     */
    private String age_range;

}
