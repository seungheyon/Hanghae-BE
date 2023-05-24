package com.hanghae7.alcoholcommunity.domain.common.security;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotBlank;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RefreshToken {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long refreshTokenId;
	@NotBlank
	private String refreshToken;
	@NotBlank
	private String memberUniqueId;

	public RefreshToken(String refreshToken, String memberUniqueId){
		this.refreshToken = refreshToken;
		this.memberUniqueId = memberUniqueId;
	}

	public RefreshToken updateToken(String refreshToken){
		this.refreshToken = refreshToken;
		return this;
	}
}
