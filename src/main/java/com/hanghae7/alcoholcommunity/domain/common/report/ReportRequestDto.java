package com.hanghae7.alcoholcommunity.domain.common.report;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class ReportRequestDto {
	private String reportContent;
	private String reportType;
	private Long reportedId;
}
