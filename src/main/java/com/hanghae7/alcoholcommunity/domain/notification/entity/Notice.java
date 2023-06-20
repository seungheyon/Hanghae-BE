package com.hanghae7.alcoholcommunity.domain.notification.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.hanghae7.alcoholcommunity.domain.member.entity.Member;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity(name="Notice")
public class Notice {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long noticeId;

	@Column
	private Integer noticeCode;

	@Column(nullable = false)
	private Long partyId;

	@Column(nullable = false)
	private String partyTitle;

	@Column(nullable = false)
	private Boolean accepted;

	@Column(nullable = false)
	private Boolean isRead;

	// 알림이 전송되는 target
	@JsonBackReference
	@ManyToOne(fetch = FetchType.EAGER)
	private Member member;

	@Column
	private String participantsUniqueId;

	public Notice(Integer noticeCode, Long partyId, String partyTitle, Boolean accepted, Boolean isRead, Member member, String participantsUniqueId) {
		this.noticeCode = noticeCode;
		this.partyId = partyId;
		this.partyTitle = partyTitle;
		this.accepted = accepted;
		this.isRead = isRead;
		this.member = member;
		this.participantsUniqueId = participantsUniqueId;
	}

	public void updateRead(Boolean isRead){
		this.isRead = isRead;
	}
}