package com.hanghae7.alcoholcommunity.domain.party.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.hanghae7.alcoholcommunity.domain.chat.entity.ChatMessage;

@Repository
public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long> {

	@Query("delete from ChatMessage cm where cm.chatRoomUniqueId = :chatroomUniqueId")
	void deleteByChatRoomId(@Param("chatRoomUniqueId") String chatRoomUniqueId);
}



