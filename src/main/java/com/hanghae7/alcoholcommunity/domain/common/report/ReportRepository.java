package com.hanghae7.alcoholcommunity.domain.common.report;

import java.util.Optional;

import javax.swing.text.html.Option;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.hanghae7.alcoholcommunity.domain.member.entity.Member;

@Repository
public interface ReportRepository extends JpaRepository<Report, Long> {

	Optional<Report> findByReporterIdAndReportedId(String ReporterId, Long ReportedId);
}
