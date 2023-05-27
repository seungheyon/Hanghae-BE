package com.hanghae7.alcoholcommunity.domain.member.repository;


import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.hanghae7.alcoholcommunity.domain.member.entity.Member;

/**
 * The interface Member repository.
 */
@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {

	/**
	 * Find by member unique id optional.
	 * MemberUniqueId로 멤버 검색
	 * @param memberUniqueId the member unique id
	 * @return the optional
	 */
	@Query("SELECT m FROM Member m WHERE m.memberUniqueId = :memberUniqueId")
	Optional<Member> findByMemberUniqueId(@Param("memberUniqueId") String memberUniqueId);

	/**
	 * Find by member email id and social optional.
	 * EmailId 와 Social 롤 멤버 검색
	 * @param memberEmailId the member email id
	 * @param social        the social
	 * @return the optional
	 */
	@Query("SELECT m FROM Member m WHERE m.memberEmailId = :memberEmailId and m.social = :social")
	Optional<Member> findByMemberEmailIdAndSocial(@Param("memberEmailId") String memberEmailId, @Param("social") String social);

	Optional<Member> findById(Long memberId);



}
