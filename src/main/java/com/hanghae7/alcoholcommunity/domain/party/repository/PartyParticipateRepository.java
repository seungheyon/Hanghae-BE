package com.hanghae7.alcoholcommunity.domain.party.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.hanghae7.alcoholcommunity.domain.member.entity.Member;
import com.hanghae7.alcoholcommunity.domain.party.dto.response.JoinPartyResponseDto;
import com.hanghae7.alcoholcommunity.domain.party.entity.Party;
import com.hanghae7.alcoholcommunity.domain.party.entity.PartyParticipate;

public interface PartyParticipateRepository extends JpaRepository<PartyParticipate, Long> {

	Optional<PartyParticipate> findByPartyAndMember(Party party, Member member);

	@Query("select p.member from PartyParticipate p where p.party.partyId = :partyId and p.host = true")
	Optional<Member> findByPartyIdAndHost(@Param("partyId")Long partyId);

	@Query("select p.member from PartyParticipate p where p.party.partyId = :partyId")
	List<Member> findByPartyId(@Param("partyId")Long partyId);

	@Query("select p from PartyParticipate p where p.member.memberUniqueId = :memberUniqueId")
	List<PartyParticipate> findByMemberUniqueId(@Param("memberUniqueId") String memberUniqueId);

	@Query("select new com.hanghae7.alcoholcommunity.domain.party.dto.response.JoinPartyResponseDto(pp) from PartyParticipate pp where pp.member = :member and pp.awaiting = false")
	List<JoinPartyResponseDto> getAllParticipateParty(@Param("member") Member member);
}

