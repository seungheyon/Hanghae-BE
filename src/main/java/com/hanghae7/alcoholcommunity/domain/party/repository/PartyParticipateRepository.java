package com.hanghae7.alcoholcommunity.domain.party.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.hanghae7.alcoholcommunity.domain.member.entity.Member;
import com.hanghae7.alcoholcommunity.domain.party.dto.response.JoinPartyResponseDto;
import com.hanghae7.alcoholcommunity.domain.party.entity.Party;
import com.hanghae7.alcoholcommunity.domain.party.entity.PartyParticipate;

/**
 * i
 */
public interface PartyParticipateRepository extends JpaRepository<PartyParticipate, Long> {

	@Query("select p from PartyParticipate p join fetch p.member join fetch p.party where p.party = :party and p.member = :member")
	Optional<PartyParticipate> findByPartyAndMember(@Param("party") Party party, @Param("member") Member member);

	@Query("select p.member from PartyParticipate p where p.party.partyId = :partyId and p.host = true")
	Optional<Member> findByPartyIdAndHost(@Param("partyId") Long partyId);

	@Query("select p from PartyParticipate p join fetch p.member where p.party.partyId = :partyId and p.awaiting = false order by p.host desc ")
	List<PartyParticipate> findByPartyId(@Param("partyId") Long partyId);


	@Query("select p from PartyParticipate p join fetch p.party where p.member.memberUniqueId = :memberUniqueId")
	List<PartyParticipate> findByMemberUniqueId(@Param("memberUniqueId") String memberUniqueId);
  
	List<PartyParticipate> findByParty(Party party);

	void deleteByParty(Party party);

	@Query("select p from PartyParticipate p join fetch p.party join fetch p.member where p.member = :member and p.rejected = false and p.host = false order by p.party.partyDate")
	List<PartyParticipate> findByAllParty(@Param("member") Member member);

	@Query("select p from PartyParticipate p join fetch p.party join fetch p.member where p.member = :member and p.awaiting = true and p.rejected = false and p.host = false order by p.party.partyDate")
	List<PartyParticipate> findByJoinParty(@Param("member") Member member);

	@Query("select p from PartyParticipate p join fetch p.party join fetch p.member where p.member = :member and p.awaiting = false and p.rejected = false and p.host = false order by p.party.partyDate")
	List<PartyParticipate> findByAcceptedParty(@Param("member") Member member);

	@Query("SELECT pp FROM PartyParticipate pp join fetch pp.member join fetch pp.party " +
		"WHERE pp.party.partyId IN " +
		"(SELECT p.party.partyId FROM PartyParticipate p " +
		"WHERE p.member = :member AND p.host = true) " +
		"AND pp.host = false and pp.rejected = false")
	List<PartyParticipate> findPartyParticipatesByHostAndMemberId(@Param("member") Member member);

}

