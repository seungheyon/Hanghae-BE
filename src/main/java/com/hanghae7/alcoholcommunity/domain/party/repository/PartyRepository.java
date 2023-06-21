package com.hanghae7.alcoholcommunity.domain.party.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

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


	Optional<Party> findByPartyIdOrderByCreatedAtDesc(Long partyId);
	List<Party> findAllByisDeletedFalseOrderByCreatedAtDesc(Pageable pageable);
	List<Party> findAllByisDeletedFalseAndRecruitmentStatusOrderByCreatedAtDesc(boolean status, Pageable pageable);
	List<Party> findAllByPartyDateBefore(LocalDateTime dateTime);
	@Query("select p from Party p where (p.isDeleted=false) and (p.placeName like %:keyword% or p.stationName like %:keyword% or p.placeAddress like %:keyword%) ORDER BY p.partyDate desc")
	List<Party> findAllPartyByKeyword(Pageable pageable, @Param("keyword")String keyword);

	@Query("select p from Party p where p.recruitmentStatus = :status and p.isDeleted = false and (p.placeName like %:keyword% or p.stationName like %:keyword% or p.placeAddress like %:keyword%) ORDER BY p.partyDate desc")
	List<Party> findAllPartyByKeywordRecruitmentStatus(@Param("status") boolean status, Pageable pageable, @Param("keyword")String keyword);

	@Modifying
	@Query("UPDATE Party p SET p.isDeleted = true WHERE p.partyId = :partyId")
	void softDeleteParty(@Param("partyId") Long partyId);


}
