package com.hanghae7.alcoholcommunity.domain.member.entity;

import java.util.ArrayList;
import java.util.List;

import com.hanghae7.alcoholcommunity.domain.common.entity.Timestamped;
import com.hanghae7.alcoholcommunity.domain.party.entity.PartyParticipate;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@NoArgsConstructor
@Entity(name="member")
public class Member extends Timestamped {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long memberId;

	@Column(nullable = false, unique = true)
	private String memberEmailId;

	@Column(nullable = false)
	private String gender;

	@Column(nullable = false)
	private String memberName;

	private String introduce;

	private int point;

	private double latitude;

	private double longitude;

	private String profileImage;

	@OneToMany(mappedBy = "member", cascade = CascadeType.ALL)
	List<PartyParticipate> partyParticipates = new ArrayList<>();

}