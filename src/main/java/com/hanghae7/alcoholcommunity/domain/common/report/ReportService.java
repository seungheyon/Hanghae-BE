package com.hanghae7.alcoholcommunity.domain.common.report;

import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hanghae7.alcoholcommunity.domain.common.report.ReportRepository;
import com.hanghae7.alcoholcommunity.domain.common.ResponseDto;
import com.hanghae7.alcoholcommunity.domain.member.entity.Member;
import com.hanghae7.alcoholcommunity.domain.member.repository.MemberRepository;
import com.hanghae7.alcoholcommunity.domain.party.repository.PartyParticipateRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@EnableScheduling
@Service
public class ReportService {
	private final ReportRepository reportRepository;
	private final MemberRepository memberRepository;
	private final PartyParticipateRepository partyParticipateRepository;
	@Transactional
	public ResponseEntity<ResponseDto> reportMember(ReportRequestDto reportRequestDto, Member member) {
		Report report = new Report(member, reportRequestDto);
		reportRepository.save(report);
		return new ResponseEntity<>(new ResponseDto(200, "신고가 정상적으로 접수되었습니다."), HttpStatus.OK);
	}

	public ResponseEntity<ResponseDto> blockMember(String memberUniqueId, Member member) {

		Optional<Member> reportedMember = memberRepository.findByMemberUniqueId(memberUniqueId);
		if(!reportedMember.get().getAuthority().equals("BLOCK")){
			reportedMember.get().setBlock();
		}

		partyParticipateRepository.deleteByMemberMemberId(reportedMember.get().getMemberId());

		return new ResponseEntity<>(new ResponseDto(200, "정지 처리가 정상적으로 이루어졌습니다."), HttpStatus.OK);
	}
}
