package com.hanghae7.alcoholcommunity.domain.sociallogin.naverlogin.dto;

import lombok.Getter;
import lombok.ToString;

/**
 * Please explain the class!!
 *
 * @fileName      : Response
 * @author        : mycom
 * @since         : 2023-05-22
 */
@Getter
@ToString
public class Response {

	private String nickname;
	private String email;
	private String gender;
	private String birthyear;
	private String profile_image;
}
