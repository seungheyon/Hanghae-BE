package com.hanghae7.alcoholcommunity.domain.party.entity;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.hanghae7.alcoholcommunity.domain.member.entity.Member;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@NoArgsConstructor
@Entity
public class PartyParticipate {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.DETACH)
	@JoinColumn(nullable = true)
	private Member member;

	@ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.DETACH)
	@JoinColumn(nullable = true)
	private Party party;

	@Column(nullable = false)
	private boolean awaiting = true;

	@Column(nullable = false)
	private boolean rejected = false;

	@Column(nullable = false)
	private boolean host = false;

	public PartyParticipate(Party party, Member member) {
		this.party=party;
		this.member=member;
	}

	public PartyParticipate(Party party, Member member, boolean host, boolean awaiting) {
		this.party=party;
		this.member=member;
		this.host = host;
		this.awaiting = awaiting;
	}

	public void setAwaite(boolean awaiting){
		this.awaiting = awaiting;
	}

	public void setRejection(boolean rejected){
		this.rejected = rejected;
	}

	public void setParty(Party party) {
		this.party = party;
	}
}
