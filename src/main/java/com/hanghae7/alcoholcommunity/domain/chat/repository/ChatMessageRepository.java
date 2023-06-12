package com.hanghae7.alcoholcommunity.domain.chat.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.hanghae7.alcoholcommunity.domain.chat.entity.ChatMessage;

@Repository
public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long> {

	@Modifying
	@Query("update ChatMessage set isDeleted = true where chatRoomUniqueId = :chatRoomUniqueId ")
	void softDeleteByChatRoomUniqueId(@Param("chatRoomUniqueId") String chatRoomUniqueId);

	@Modifying
	@Query("UPDATE ChatMessage SET memberProfileImage = :profileImage, sender = :memberName WHERE memberId = :memberId")
	void updateChatMessageProfileAndMemberInfo(@Param("memberId") Long memberId, @Param("profileImage") String profileImage, @Param("memberName") String memberName);

	@Modifying
	@Query("UPDATE ChatMessage SET sender = :memberName WHERE memberId = :memberId")
	void updateChatMessageMemberInfo(@Param("memberId") Long memberId, @Param("memberName") String memberName);

}



