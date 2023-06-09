package com.hanghae7.alcoholcommunity.domain.party.repository;

import java.util.List;
import java.util.Optional;

import com.hanghae7.alcoholcommunity.domain.party.entity.PartyParticipate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.hanghae7.alcoholcommunity.domain.member.entity.Member;
import com.hanghae7.alcoholcommunity.domain.party.entity.Party;

/**
 * i
 */
public interface PartyParticipateRepository extends JpaRepository<PartyParticipate, Long> {

	/**
	 * 해당 필드는 join fetch를 안하는게 더빠를거라 판단함
	 * @return 해당 멤버가 해당 모임에 포함된 partyParticipate를 리턴
	 */
	@Query("select p from PartyParticipate p where p.party = :party and p.member = :member")
	Optional<PartyParticipate> findByPartyAndMember(@Param("party") Party party, @Param("member") Member member);

	@Query("select p from PartyParticipate p where p.party.partyId = :partyId and p.member = :member")
	Optional<PartyParticipate> findByPartyIdAndMember(@Param("partyId") Long partyId, @Param("member") Member member);

	/**
	 * 해당 모임의 호스트를 찾기위한 쿼리
	 * @param partyId
	 * @return 입력된 파티의 호스트를 출력
	 */
	@Query("select p.member from PartyParticipate p where p.party.partyId = :partyId and p.host = true")
	Optional<Member> findByPartyIdAndHost(@Param("partyId") Long partyId);

	/**
	 * 파티에 참여한 참여자 정보를 리턴하기위한 쿼리
	 * @param partyId
	 * @return 파티에 참여한 참여자 정보를 리턴, 호스트 순으로 리턴하여 0번째리스트는 호스트의 정보를 반환
	 */
	@Query("select p from PartyParticipate p join fetch p.member where p.party.partyId = :partyId and p.awaiting = false order by p.host desc ")
	List<PartyParticipate> findByPartyId(@Param("partyId") Long partyId);


	@Query("select p from PartyParticipate p join fetch p.party where p.member.memberUniqueId = :memberUniqueId")
	List<PartyParticipate> findByMemberUniqueId(@Param("memberUniqueId") String memberUniqueId);

	@Query("select p from PartyParticipate p where p.party = :party and p.host = true")
	PartyParticipate findByParty(@Param("party") Party party);

	/**
	 * 해당 모임정보를 가진 참여정보를 지우기위한 쿼리
	 * @param party
	 */
	void deleteByParty(Party party);

	/**
	 * 내가 참여신청한 모임 리스트를 얻기위한 쿼리
	 * @param member
	 * @return 해당 멤버가 참여신청한 참여정보를 리턴
	 */
	@Query("select p from PartyParticipate p join fetch p.party join fetch p.member where p.member = :member and p.host = false order by p.party.partyDate")
	List<PartyParticipate> findByAllParty(@Param("member") Member member);

	/**
	 * 내가 참여 신청한 모임 리스트중 승인대기중인 리스트를 얻기위한 쿼리
	 * @param member
	 * @return 해당 멤버가 참여신청하고 승인대기중인 참여정보를 리턴
	 */
	@Query("select p from PartyParticipate p join fetch p.party join fetch p.member where p.member = :member and p.awaiting = true and p.rejected = false and p.host = false order by p.party.partyDate")
	List<PartyParticipate> findByJoinParty(@Param("member") Member member);

	/**
	 * 내가 참여 신청한 모임 리스트중 승인대기중인 리스트를 얻기위한 쿼리
	 * @param member
	 * @return 해당 멤버가 참여신청하고 승인된 참여정보를 리턴
	 */
	@Query("select p from PartyParticipate p join fetch p.party join fetch p.member where p.member = :member and p.awaiting = false and p.rejected = false and p.host = false order by p.party.partyDate")
	List<PartyParticipate> findByAcceptedParty(@Param("member") Member member);

	/**
	 * @param member 토큰에서 얻은 멤버
	 * @return member 사용자에게 승인요청이 들어온 리스트를 출력하기위한 PartyParticipate을 List로 출력
	 */
	@Query("SELECT pp FROM PartyParticipate pp join fetch pp.member join fetch pp.party " +
		"WHERE pp.awaiting = true and pp.party.partyId IN " +
		"(SELECT p.party.partyId FROM PartyParticipate p " +
		"WHERE p.member = :member AND p.host = true) " +
		"AND pp.host = false and pp.rejected = false")
	List<PartyParticipate> findPartyParticipatesByHostAndMemberId(@Param("member") Member member);

	/**
	 * 해당 멤버가 호스트인 파티정보를 얻기위한 쿼리
	 * @param member
	 * @return 해당 멤버가 호스트인 파티의 참여정보를 리턴
	 */
	@Query("select p from PartyParticipate p join fetch p.member join fetch p.party where p.member = :member and p.host = true order by p.party.partyDate")
	List<PartyParticipate> findPartyParticipateByHost(@Param("member") Member member);


}

