package com.hanghae7.alcoholcommunity.domain.member.entity;
import java.time.LocalDateTime;
import java.util.List;


import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

import com.hanghae7.alcoholcommunity.domain.common.entity.Timestamped;
import com.hanghae7.alcoholcommunity.domain.member.dto.MemberSignupRequest;

/**
 * The type Member.
 * 멤버 엔티티
 */
@Getter
@NoArgsConstructor
@Entity(name="Member")
public class Member extends Timestamped {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long memberId;

	@Column(nullable = false)
	private String memberEmailId;

	@Column(nullable = false, unique = true)
	private String memberUniqueId;

	@Column(nullable = true)
	private int age;

	@Column(nullable = false)
	private String gender;

	@Column(nullable = false)
	private String memberName;

	private String introduce;

	private int point;

	private double latitude;

	private double longitude;

	private String profileImage;

	private String authority = "user";

	private String social;

	private LocalDateTime createdAt;

	@JsonManagedReference
	@OneToMany(mappedBy = "member", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	private List<Notice> memberNotice;

	private Member(String memberEmailId, String memberUniqueId, int age, String gender, String memberName, String profileImage, String social, LocalDateTime createdAt) {
		this.memberEmailId = memberEmailId;
		this.memberUniqueId = memberUniqueId;
		this.age = age;
		this.gender = gender;
		this.memberName = memberName;
		this.profileImage = profileImage;
		this.social = social;
		this.createdAt = createdAt;
	}

	/**
	 * Member Entity의 무결성을 위해 생성자 주입
	 * @param memberSignupRequest 필수적인 정보만 주입
	 * @return 신규 회원 생성
	 */
	public static Member create(MemberSignupRequest memberSignupRequest) {
		return new Member(
			memberSignupRequest.getMemberEmailId(),
			memberSignupRequest.getMemberUniqueId(),
			memberSignupRequest.getAge(),
			memberSignupRequest.getGender(),
			memberSignupRequest.getMemberName(),
			memberSignupRequest.getProfileImage(),
			memberSignupRequest.getSocial(),
			memberSignupRequest.getCreatedAt()
		);
	}




	/**
	 * Update.
	 * 멤버 업데이트 메서드(파라미터로 이미지가 있을 경우)
	 * @param newMemberName   the new member name
	 * @param newProfileImage the new profile image
	 */
	public void update1(String newMemberName ,String newProfileImage){
		this.memberName = newMemberName;
		this.profileImage = newProfileImage;
	}

	public void setBlock(){
		this.authority ="BLOCK";
	}

	/**
	 * Update.
	 * 멤버 업데이트 메서드(파라미터로 이미지가 없을 경우)
	 * @param newMemberName the new member name
	 */
	public void update(String newMemberName, String newIntroduce){
		this.memberName = newMemberName;
		this.introduce = newIntroduce;
	}



}
