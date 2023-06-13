package com.hanghae7.alcoholcommunity.domain.common.report;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.hanghae7.alcoholcommunity.domain.common.entity.Timestamped;
import com.hanghae7.alcoholcommunity.domain.member.entity.Member;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class Report extends Timestamped {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long reportId;

	@Column(nullable = false)
	private String reporterId;

	@Column(nullable = false)
	private String reportedId;

	@Column(nullable = false)
	private String reportType;

	@Column(nullable = false)
	private String reportContent;

	@JsonFormat(shape = JsonFormat.Shape.STRING,pattern = "yyyy-MM-dd HH:mm:ss",timezone = "Asia/Seoul")
	private LocalDateTime reportedAt;

	public Report(Member member, ReportRequestDto reportRequestDto){
		this.reporterId = member.getMemberUniqueId();
		this.reportedId = reportRequestDto.getReportedId();
		this.reportType = reportRequestDto.getReportType();
		this.reportContent = reportRequestDto.getReportContent();
		this.reportedAt = LocalDateTime.now();

	}

}
