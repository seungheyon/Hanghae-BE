package com.hanghae7.alcoholcommunity.domain.party.service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hanghae7.alcoholcommunity.domain.common.ResponseDto;
import com.hanghae7.alcoholcommunity.domain.member.entity.Member;

import com.hanghae7.alcoholcommunity.domain.party.dto.request.PartyRequestDto;
import com.hanghae7.alcoholcommunity.domain.party.dto.response.PartyListResponse;
import com.hanghae7.alcoholcommunity.domain.party.dto.response.PartyListResponseDto;
import com.hanghae7.alcoholcommunity.domain.party.dto.response.PartyResponseDto;
import com.hanghae7.alcoholcommunity.domain.party.entity.Party;

import com.hanghae7.alcoholcommunity.domain.party.entity.PartyParticipate;
import com.hanghae7.alcoholcommunity.domain.party.repository.PartyParticipateRepository;
import com.hanghae7.alcoholcommunity.domain.party.repository.PartyRepository;

import lombok.RequiredArgsConstructor;

/**
 * Please explain the class!!
 *
 * @fileName      : example
 * @author        : mycom
 * @since         : 2023-05-19
 */

@RequiredArgsConstructor
@Service
public class PartyService {

	private final PartyRepository partyRepository;
	private final PartyParticipateRepository partyParticipateRepository;

	// 모임 게시글 등록
	@Transactional
	public ResponseEntity<ResponseDto> creatParty(PartyRequestDto partyRequestDto, Member member) {

		Party party = new Party(partyRequestDto, member.getMemberName());
		PartyParticipate partyParticipate = new PartyParticipate(party, member, true, false);
		party.addCurrentCount();
		partyRepository.save(party);
		partyParticipateRepository.save(partyParticipate);
		return new ResponseEntity<>(new ResponseDto(200, "모임 생성에 성공했습니다."), HttpStatus.OK);
	}

	// 모임 전체조회(전체/모집중/모집마감)
	@Transactional(readOnly = true)
	public ResponseEntity<ResponseDto> findAll(int recruitmentStatus, int page) {

		List<Party> parties;
		Pageable pageable = PageRequest.of(page, 10);
		if(recruitmentStatus == 0){
			parties = partyRepository.findAllParty(pageable);
		}else if(recruitmentStatus == 1){
			parties = partyRepository.findAllPartyRecruitmentStatus(true, pageable);
		}else{
			parties = partyRepository.findAllPartyRecruitmentStatus(false, pageable);
		}

		List<PartyListResponse> partyList = new ArrayList<>();
		for (Party party : parties) {
			PartyListResponse partyResponse = new PartyListResponse(party);
			partyResponse.getparticipateMembers(party.getPartyParticipates().stream()
				.map(PartyParticipate::getMember)
				.collect(Collectors.toList()));
			partyList.add(partyResponse);
		}

		return new ResponseEntity<>(new ResponseDto(200, "모임 조회에 성공했습니다.", new PartyListResponseDto(partyList, page, partyList.size())), HttpStatus.OK);
	}

	// 모임 상세조회
	@Transactional
	public ResponseEntity<ResponseDto> getParty(Long partyId) {

		Party party = partyRepository.findByPartyId(partyId).orElseThrow(
			()-> new IllegalArgumentException("해당 게시글이 존재하지 않습니다."));

		List<Member> partyMember = partyParticipateRepository.findByPartyId(partyId);
		PartyResponseDto partyResponseDto = new PartyResponseDto(party);
		partyResponseDto.getparticipateMembers(partyMember);
		return new ResponseEntity<>(new ResponseDto(200, "모임 상세 조회에 성공하였습니다.", partyResponseDto), HttpStatus.OK);
	}

	// 모임 게시글 수정
	@Transactional
	public ResponseEntity<ResponseDto> updateParty(Long partyId, PartyRequestDto partyRequestDto, Member member) {

		Party party = partyRepository.findByPartyId(partyId).orElseThrow(
			() -> new IllegalArgumentException("해당 게시글이 존재하지 않습니다.")
		);
		Member hostMember = partyParticipateRepository.findByPartyIdAndHost(partyId).orElseThrow(
			()-> new IllegalArgumentException("없는파티임")
		);

		if(!hostMember.getMemberUniqueId().equals(member.getMemberUniqueId())) {
			throw new IllegalArgumentException("다른 회원이 작성한 게시물입니다.");
		} else {
			party.updateParty(partyRequestDto);
			return new ResponseEntity<>(new ResponseDto(200, "모임을 수정하였습니다."), HttpStatus.OK);
		}
	}

	// 모임 게시글 삭제
	@Transactional
	public ResponseEntity<ResponseDto> deleteParty(Long partyId, Member member) {

		try {
			Party party = partyRepository.findById(partyId).orElseThrow(
				() -> new IllegalArgumentException("해당 게시글이 존재하지 않습니다."));

			Member hostMember = partyParticipateRepository.findByPartyIdAndHost(partyId).orElseThrow(
				()-> new IllegalArgumentException("없는파티임")
			);
			if(!hostMember.getMemberUniqueId().equals(member.getMemberUniqueId())) {
				return new ResponseEntity<>(new ResponseDto(400, "해당 사용자가 아닙니다."), HttpStatus.BAD_REQUEST);
			} else {
				partyRepository.delete(party);
				return new ResponseEntity<>(new ResponseDto(200, "모임을 삭제하였습니다."), HttpStatus.OK);
			}
		}
		catch (IllegalArgumentException e){
			return new ResponseEntity<>(new ResponseDto(400, "해당 게시글이 존재하지 않거나, 없는 모임입니다."), HttpStatus.BAD_REQUEST);
		}
	}
}
