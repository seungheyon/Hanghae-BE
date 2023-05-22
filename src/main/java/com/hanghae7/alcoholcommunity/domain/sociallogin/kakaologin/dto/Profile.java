package com.hanghae7.alcoholcommunity.domain.sociallogin.kakaologin.dto;

import lombok.Getter;
import lombok.ToString;

/**
 * 유저 프로필 정보 : 닉네임, 썸네일 img, 프로필 img
 */
@Getter
@ToString
public class Profile {

    private String nickname;
    /**
     * kakao api자체에서 snake방식으로 전달하여 필드명 설정
     */
    private String thumbnail_image_url;
    /**
     * kakao api자체에서 snake방식으로 전달하여 필드명 설정
     */
    private String profile_image_url;
}
