package com.hanghae7.alcoholcommunity.domain.notification.repository;

import java.util.List;

import com.hanghae7.alcoholcommunity.domain.member.entity.Member;
import com.hanghae7.alcoholcommunity.domain.notification.entity.Notice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NoticeRepository extends JpaRepository<Notice, Long> {
	List<com.hanghae7.alcoholcommunity.domain.notification.entity.Notice>findAllByMemberAndIsRead(Member member, Boolean isRead);
}
