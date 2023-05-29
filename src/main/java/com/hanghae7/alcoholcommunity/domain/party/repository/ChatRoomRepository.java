package com.hanghae7.alcoholcommunity.domain.party.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.hanghae7.alcoholcommunity.domain.chat.entity.ChatRoom;

@Repository
public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long> {

	void deleteByChatRoomId(Long ChatroomId);
}
