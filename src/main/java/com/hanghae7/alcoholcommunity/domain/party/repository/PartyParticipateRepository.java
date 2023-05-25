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

/**
 * i
 */
public interface PartyParticipateRepository extends JpaRepository<PartyParticipate, Long> {

	@Query("select p from PartyParticipate p join fetch p.member join fetch p.party where p.party = :party and p.member = :member")
	Optional<PartyParticipate> findByPartyAndMember(@Param("party") Party party, @Param("member") Member member);

	@Query("select p.member from PartyParticipate p where p.party.partyId = :partyId and p.host = true")
	Optional<Member> findByPartyIdAndHost(@Param("partyId") Long partyId);

	@Query("select p from PartyParticipate p join fetch p.member where p.party.partyId = :partyId and p.awaiting = false order by p.host")
	List<PartyParticipate> findByPartyId(@Param("partyId") Long partyId);


	@Query("select p from PartyParticipate p join fetch p.party where p.member.memberUniqueId = :memberUniqueId")
	List<PartyParticipate> findByMemberUniqueId(@Param("memberUniqueId") String memberUniqueId);
  
	List<PartyParticipate> findByParty(Party party);

	@Query("select p.party from PartyParticipate p where p.member = :member and p.awaiting = true and p.rejected = false")
	List<Party> findByJoinParty(@Param("member") Member member);

	@Query("select p.party from PartyParticipate p where p.member = :member and p.awaiting = false and p.rejected = false")
	List<Party> findByAcceptedParty(@Param("member") Member member);

/*	@Query("select new com.hanghae7.alcoholcommunity.domain.party.dto.response.JoinPartyResponseDto(pp) from PartyParticipate pp where pp.member = :member and pp.awaiting = false")
	List<JoinPartyResponseDto> getAllParticipateParty(@Param("member") Member member);*/

}

