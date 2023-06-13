package com.hanghae7.alcoholcommunity.domain.common.report;

import java.io.IOException;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.hanghae7.alcoholcommunity.domain.common.ResponseDto;
import com.hanghae7.alcoholcommunity.domain.common.security.UserDetailsImplement;


@RestController
public class ReportController {
	private ReportService reportService;
	@PostMapping("/report")
	public ResponseEntity<ResponseDto> reportMember( @RequestBody ReportRequestDto  reportRequestDto,
													@AuthenticationPrincipal UserDetailsImplement userDetails){
		return reportService.reportMember(reportRequestDto, userDetails.getMember());
	}

	@PutMapping("/blockUser/{userId}")
	public ResponseEntity<ResponseDto> reportMember( @RequestParam String memberUniqueId,@AuthenticationPrincipal UserDetailsImplement userDetails){
		return reportService.blockMember(memberUniqueId, userDetails.getMember());
	}


}
