package com.hanghae7.alcoholcommunity.domain.party.repository;

import java.util.Optional;

import org.hibernate.metamodel.model.convert.spi.JpaAttributeConverter;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.RequestParam;

import com.hanghae7.alcoholcommunity.domain.member.entity.Member;
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

	@Query("select p from Party p ORDER BY p.createdAt desc")
	Page<Party> findAll(Pageable pageable);

	@Query("select p from Party p where p.recruitmentStatus = :status ORDER BY p.createdAt desc ")
	Page<Party> findAllPartyRecruitmentStatus(@Param("status") boolean status, Pageable pageable);



}
