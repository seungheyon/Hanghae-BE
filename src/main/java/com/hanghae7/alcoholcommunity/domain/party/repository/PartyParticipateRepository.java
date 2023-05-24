package com.hanghae7.alcoholcommunity.domain.party.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.hanghae7.alcoholcommunity.domain.member.entity.Member;
import com.hanghae7.alcoholcommunity.domain.party.entity.Party;
import com.hanghae7.alcoholcommunity.domain.party.entity.PartyParticipate;

public interface PartyParticipateRepository extends JpaRepository<PartyParticipate, Long> {

	Optional<PartyParticipate> findByPartyAndMember(Party party, Member member);

	@Query("select p.member from PartyParticipate p where p.party.partyId = :partyId and p.host = :true")
	Optional<PartyParticipate> findByPartyIdAndHost(@Param("partyId")Long partyId);

	@Query("select p.member.memberName, p.member.profileImage from PartyParticipate p where p.party.partyId = :partyId order by p.host")
	List<PartyParticipate> findByPartyId(@Param("partyId")Long partyId);
}
