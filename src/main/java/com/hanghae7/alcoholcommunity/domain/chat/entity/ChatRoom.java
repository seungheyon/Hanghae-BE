package com.hanghae7.alcoholcommunity.domain.chat.entity;

import java.util.List;
import java.util.UUID;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;

import com.hanghae7.alcoholcommunity.domain.common.entity.Timestamped;
import com.hanghae7.alcoholcommunity.domain.party.entity.PartyParticipate;

import lombok.Getter;

/**
 */
@Getter
@Entity
public class ChatRoom extends Timestamped {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long chatRoomId;

    private String chatRoomUniqueId;

    private String title;

    private boolean isDeleted = false;

    @OneToMany(mappedBy = "chatRoom")
    @OrderBy("createdAt DESC")
    private List<ChatMessage> messages;

    @OneToMany(mappedBy = "chatRoom")
    private List<PartyParticipate> partyParticipates;
    public static ChatRoom create(String name) {
        ChatRoom chatRoom = new ChatRoom();
        chatRoom.chatRoomUniqueId = UUID.randomUUID().toString();
        chatRoom.title = name;
        return chatRoom;
    }

}
