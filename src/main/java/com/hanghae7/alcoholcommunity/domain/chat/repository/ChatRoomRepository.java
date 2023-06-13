package com.hanghae7.alcoholcommunity.domain.chat.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.hanghae7.alcoholcommunity.domain.chat.entity.ChatRoom;

@Repository
public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long> {

	void deleteByChatRoomId(Long ChatroomId);

	@Modifying
	@Query("UPDATE ChatRoom SET isDeleted = true WHERE chatRoomId = :chatRoomId")
	void softDeleteChatRoom(@Param("chatRoomId") Long chatRoomId);

	@Modifying
	@Query("UPDATE ChatRoom SET title = :title WHERE chatRoomUniqueId = :chatRoomUniqueId")
	void updateChatRoomTitle(@Param("chatRoomUniqueId") String chatRoomUniqueId, @Param("title") String title);
}
