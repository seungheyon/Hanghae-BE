package com.hanghae7.alcoholcommunity.domain.notification.repository;

import java.time.LocalDateTime;
import java.util.List;

import com.hanghae7.alcoholcommunity.domain.member.entity.Member;
import com.hanghae7.alcoholcommunity.domain.notification.entity.Notice;
import com.hanghae7.alcoholcommunity.domain.party.entity.Party;
import feign.template.Literal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface NoticeRepository extends JpaRepository<Notice, Long> {
	List<Notice>findAllByMemberAndIsRead(Member member, Boolean isRead);
	Notice findByNoticeId(Long Id);
	void deleteAllByPartyId(Long partyId);

	List<Notice> findAllByCreatedAtBefore(LocalDateTime dateTime);
}
