package com.hanghae7.alcoholcommunity.domain.party.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.hanghae7.alcoholcommunity.domain.party.dto.response.RecruitingPartyResponseDto;
import com.hanghae7.alcoholcommunity.domain.party.entity.Party;

/**
 * Please explain the class!!
 *
 * @fileName      : example
 * @author        : mycom
 * @since         : 2023-05-19
 */
@Repository
public interface PartyRepository extends JpaRepository<Party, Long> {


	@Query("select p from Party p  ORDER BY p.createdAt desc")
	Optional<Party> findByPartyId(Long partyId);

	@Query("select p from Party p ORDER BY p.createdAt desc")
	List<Party> findAllParty(Pageable pageable);

	@Query("select p from Party p where p.recruitmentStatus = :status ORDER BY p.createdAt desc")
	List<Party> findAllPartyRecruitmentStatus(@Param("status") boolean status, Pageable pageable);

	@Query("select new com.hanghae7.alcoholcommunity.domain.party.dto.response.RecruitingPartyResponseDto(p, m) " +
		"from Party p " +
		"join p.partyParticipates pp " +
		"join pp.member m " +
		"where p.recruitmentStatus = true " +
		"and pp.host = true")
	List<RecruitingPartyResponseDto> getAllJoinParty();


}
