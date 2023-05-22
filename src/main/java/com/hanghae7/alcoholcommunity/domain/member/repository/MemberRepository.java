package com.hanghae7.alcoholcommunity.domain.member.repository;


import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.hanghae7.alcoholcommunity.domain.member.entity.Member;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {

	@Query("SELECT m FROM Member m WHERE m.memberEmailId = :memberEmailId")
	Optional<Member> findByMemberEmailId(@Param("memberEmailId") String memberEmailId);

}
