package com.hanghae7.alcoholcommunity.domain.sociallogin.naverlogin.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * Please explain the class!!
 *
 * @fileName      : NaverInfo
 * @author        : mycom
 * @since         : 2023-05-22
 */

@ToString
@Getter
@NoArgsConstructor
@JsonNaming(value = PropertyNamingStrategies.SnakeCaseStrategy.class)
public class NaverInfo {
	private Response response;

	/**
	 * @return 값이 없을시 null 값저장
	 */
	public static NaverInfo fail() {
		return null;
	}
}
