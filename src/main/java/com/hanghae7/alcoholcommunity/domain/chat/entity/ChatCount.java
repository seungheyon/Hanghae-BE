package com.hanghae7.alcoholcommunity.domain.chat.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import com.hanghae7.alcoholcommunity.domain.party.entity.PartyParticipate;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

/**
 * Please explain the class!!
 *
 * @fileName      : ChatCount
 * @author        : mycom
 * @since         : 2023-06-07
 */

@Getter
@Setter
@Entity
@RequiredArgsConstructor
public class ChatCount {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long Id;

	@ManyToOne
	@JoinColumn(name = "partyParticipate_id")
	private PartyParticipate partyParticipate;

	@ManyToOne
	@JoinColumn(name = "chatMessage_id")
	private ChatMessage chatMessage;

	private boolean readStatus;

	private boolean isDeleted = false;

	public ChatCount(PartyParticipate partyParticipate, ChatMessage chatMessage){
		this.partyParticipate = partyParticipate;
		this.chatMessage = chatMessage;
		this.readStatus = false;
	}

	public void setReadStatus(boolean readStatus){
		this.readStatus = readStatus;
	}

}
