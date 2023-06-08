package com.hanghae7.alcoholcommunity.domain.party.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import com.hanghae7.alcoholcommunity.domain.chat.entity.ChatRoom;
import com.hanghae7.alcoholcommunity.domain.member.entity.Member;
// import com.hanghae7.alcoholcommunity.domain.party.dto.request.PartyJoinRequestDto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Entity
public class PartyParticipate {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(nullable = false)
	private Member member;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(nullable = false)
	private Party party;

	@ManyToOne(fetch = FetchType.EAGER)
	private ChatRoom chatRoom;

	@Column(nullable = false)
	private boolean awaiting = true;

	@Column(nullable = false)
	private boolean rejected = false;

	@Column(nullable = false)
	private boolean host = false;

	private String amountAlcohol;

	private String reason;

	public PartyParticipate(Party party, Member member) { //, PartyJoinRequestDto partyJoinRequestDto
		this.party=party;
		this.member=member;
		// this.reason=partyJoinRequestDto.getReason();
		// this.amountAlcohol=partyJoinRequestDto.getAmountAlcohol();
	}

	public PartyParticipate(Party party, Member member, boolean host, boolean awaiting, ChatRoom chatRoom) {
		this.party=party;
		this.member=member;
		this.host = host;
		this.awaiting = awaiting;
		this.chatRoom = chatRoom;
	}

	public void setAwaiting(boolean awaiting){
		this.awaiting = awaiting;
	}

	public void setRejection(boolean rejected){
		this.rejected = rejected;
	}

	public void setParty(Party party) {
		this.party = party;
	}

	public void setChatRoom(ChatRoom chatRoom){
		this.chatRoom = chatRoom;
	}
	public void deleteCahtRoom(){
		this.chatRoom = null;
	}
}
