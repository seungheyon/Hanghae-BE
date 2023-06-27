package com.hanghae7.alcoholcommunity.domain.notification.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.hanghae7.alcoholcommunity.domain.common.entity.Timestamped;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.hanghae7.alcoholcommunity.domain.member.entity.Member;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity(name="Notice")
public class Notice extends Timestamped {

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
	// noticeCode = 1 -> 승인/거절 여부
	// noticeCode = 2 -> 참가신청/신청취소 여부

	@Column(nullable = false)
	private Boolean isRead;

	// 알림이 전송되는 target
	@JsonBackReference
	@ManyToOne(fetch = FetchType.EAGER)
	private Member member;

	@Column
	private Long participantsId;

	@JsonFormat(shape = JsonFormat.Shape.STRING,pattern = "yyyy-MM-dd HH:mm:ss",timezone = "Asia/Seoul")
	private LocalDateTime createdAt;

	public Notice(Integer noticeCode, Long partyId, String partyTitle, Boolean accepted, Boolean isRead, Member member, Long participantsId) {
		this.noticeCode = noticeCode;
		this.partyId = partyId;
		this.partyTitle = partyTitle;
		this.accepted = accepted;
		this.isRead = isRead;
		this.member = member;
		this.participantsId = participantsId;
	}

	public void updateRead(Boolean isRead){
		this.isRead = isRead;
	}
}