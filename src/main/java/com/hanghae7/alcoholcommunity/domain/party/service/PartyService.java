package com.hanghae7.alcoholcommunity.domain.party.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hanghae7.alcoholcommunity.domain.member.entity.Member;

import com.hanghae7.alcoholcommunity.domain.party.dto.ParticipateInfoDto;
import com.hanghae7.alcoholcommunity.domain.party.dto.PartyDetailResponseDto;
import com.hanghae7.alcoholcommunity.domain.party.dto.PartyRequestDto;
import com.hanghae7.alcoholcommunity.domain.party.dto.PartyResponseDto;
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
	public ResponseEntity<Void> creatParty(PartyRequestDto partyRequestDto, Member member) {

		Party party = new Party(partyRequestDto);
		PartyParticipate partyParticipate = new PartyParticipate(party, member);
		partyParticipate.setHost(true);
		party.addCurrentCount();
		partyRepository.save(party);
		partyParticipateRepository.save(partyParticipate);
		return ResponseEntity.ok(null);
	}

	// 모임 전체조회(전체/모집중/모집마감)
	@Transactional(readOnly = true)
	public ResponseEntity<Page<Party>> findAll(int recruitmentStatus, int page) {

		Page<Party> parties;
		Pageable pageable = PageRequest.of(page, 10);
		if(recruitmentStatus == 0){
			parties = partyRepository.findAllParty(pageable);
		}else if(recruitmentStatus == 1){
			parties = partyRepository.findAllPartyRecruitmentStatus(true, pageable);
		}else{
			parties = partyRepository.findAllPartyRecruitmentStatus(false, pageable);
		}
		return ResponseEntity.ok(parties);
	}

	// 모임 상세조회
	@Transactional
	public ResponseEntity<PartyDetailResponseDto> getParty(Long partyId) {

		Party party = partyRepository.findById(partyId).orElseThrow(
			()-> new IllegalArgumentException("해당 게시글이 존재하지 않습니다."));

		List<PartyParticipate> participates = partyParticipateRepository.findByPartyId(partyId);
		PartyResponseDto partyResponseDto = new PartyResponseDto(party, participates.get(0).getMember().getProfileImage(), participates.get(0).getMember().getMemberName());
		return ResponseEntity.ok(new PartyDetailResponseDto(participates, partyResponseDto));
	}


	// 모임 게시글 수정
	@Transactional
	public ResponseEntity<Void> updateParty(Long partyId, PartyRequestDto partyRequestDto, Member member) {
		Party party = partyRepository.findById(partyId).orElseThrow(
			() -> new IllegalArgumentException("해당 게시글이 존재하지 않습니다.")
		);
		Optional<PartyParticipate> hostMember = partyParticipateRepository.findByPartyIdAndHost(partyId);

		if(!hostMember.equals(member)) {
			throw new IllegalArgumentException("다른 회원이 작성한 게시물입니다.");
		} else {
			party.updateParty(partyRequestDto);
			return ResponseEntity.ok(null);
		}
	}

	// 모임 게시글 삭제
	@Transactional
	public ResponseEntity<Void> deleteParty(Long partyId, Member member) {

		Party party = partyRepository.findById(partyId).orElseThrow(
			() -> new IllegalArgumentException("해당 게시글이 존재하지 않습니다."));
		Optional<PartyParticipate> hostMember = partyParticipateRepository.findByPartyIdAndHost(partyId);

		if(!hostMember.equals(member)) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		} else {
			partyRepository.delete(party);
			return ResponseEntity.ok(null);
		}
	}



}
