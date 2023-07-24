package com.hanghae7.alcoholcommunity.domain.member.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class Login {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long Id;

	@Column(nullable = false, unique = true)
	private String memberEmailId;

	@Column(nullable = false)
	private String password;

	private Login(String memberEmailId, String password){
		this.memberEmailId =memberEmailId;
		this.password = password;
	}
	public static Login create(String memberEmailId, String password){
		return new Login(
			memberEmailId, password
		);
	}

}
