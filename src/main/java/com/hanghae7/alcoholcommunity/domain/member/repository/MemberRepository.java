package com.hanghae7.alcoholcommunity.domain.member.repository;


import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.hanghae7.alcoholcommunity.domain.member.entity.Member;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {

	@Query("SELECT m FROM Member m WHERE m.memberEmailId = :memberUniqueId")
	Optional<Member> findByMemberUniqueId(@Param("memberUniqueId") String memberUniqueId);

	@Query("SELECT m FROM Member m WHERE m.memberEmailId = :memberEmailId and m.social = :social")
	Optional<Member> findByMemberEmailIdAndSocial(@Param("memberEmailId") String memberEmailId, @Param("social") String social);

	Optional<Member> findById(Long memberId);



}
