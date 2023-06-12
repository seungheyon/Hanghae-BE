package com.hanghae7.alcoholcommunity.domain.chat.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.hanghae7.alcoholcommunity.domain.chat.entity.ChatCount;
import com.hanghae7.alcoholcommunity.domain.party.entity.PartyParticipate;

/**
 * Please explain the class!!
 *
 * @fileName      : ChatCountRepository
 * @author        : mycom
 * @since         : 2023-06-07
 */
public interface ChatCountRepository extends JpaRepository<ChatCount, Long> {

	@Query("select c from ChatCount c where c.partyParticipate = :partyParticipate and c.readStatus = false")
	List<ChatCount> findChatCountByPartyParticipate(@Param("partyParticipate") PartyParticipate partyParticipate);

	@Query("select count(c) from ChatCount c where c.partyParticipate = :partyParticipate and c.readStatus = false")
	long countBychatCount(@Param("partyParticipate") PartyParticipate partyParticipate);

}
