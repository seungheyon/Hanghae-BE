package com.hanghae7.alcoholcommunity.domain.party.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
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


	@Query("select p from Party p  ORDER BY p.createdAt desc")
	Optional<Party> findByPartyId(Long partyId);

	/**
	 * 생성된 모든 파티를 생성된 날짜순으로 얻기위한 쿼리
	 * @param pageable
	 * @return 모든 파티를 날짜순으로 리턴
	 */
	@Query("select p from Party p ORDER BY p.createdAt desc")
	List<Party> findAllParty(Pageable pageable);

	/**
	 * status 정보에 따라 모집중, 모집완료된 모임정보를 얻기위한 쿼리
	 * @param status
	 * @param pageable
	 * @return 모집중, 모집완료된 모임정보를 각각의 status에 따라 리턴
	 */
	@Query("select p from Party p where p.recruitmentStatus = :status ORDER BY p.createdAt desc")
	List<Party> findAllPartyRecruitmentStatus(@Param("status") boolean status, Pageable pageable);

	@Query("select p from Party p where p.partyDate < :Date")
	List<Party> findTimeoverParty(@Param("Date")LocalDateTime dateTime);

}
