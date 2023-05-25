package com.hanghae7.alcoholcommunity.domain.member.service;

import com.hanghae7.alcoholcommunity.domain.common.ResponseDto;
import com.hanghae7.alcoholcommunity.domain.common.jwt.JwtUtil;
import com.hanghae7.alcoholcommunity.domain.member.dto.IndividualPageResponseDto;
import com.hanghae7.alcoholcommunity.domain.member.dto.MemberPagePartyResponseDto;
import com.hanghae7.alcoholcommunity.domain.member.dto.MemberPageUpdateRequestDto;
import com.hanghae7.alcoholcommunity.domain.member.dto.MemberResponseDto;
import com.hanghae7.alcoholcommunity.domain.member.entity.Member;
import com.hanghae7.alcoholcommunity.domain.member.repository.MemberRepository;
import com.hanghae7.alcoholcommunity.domain.party.entity.Party;
import com.hanghae7.alcoholcommunity.domain.party.entity.PartyParticipate;
import com.hanghae7.alcoholcommunity.domain.party.repository.PartyParticipateRepository;

import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final PartyParticipateRepository partyParticipateRepository;

    @Transactional
    public ResponseEntity<ResponseDto> memberPage(String memberUniqueId){
        Member member = new Member();
        try {
            member = memberRepository.findByMemberUniqueId(memberUniqueId).orElseThrow(
                () -> new IllegalArgumentException("등록된 사용자가 없습니다."));
        }
        catch(IllegalArgumentException e){
            return new ResponseEntity<>(new ResponseDto(200, "등록된 사용자가 없습니다."), HttpStatus.OK);
        }

        String memberEmailId = member.getMemberEmailId();
        String memberName = member.getMemberName();
        String profileImage = member.getProfileImage();
        String characteristic = "characteristic";

        List<MemberPagePartyResponseDto> myPartyList = new ArrayList<>();
        List<PartyParticipate> partyParticipateList = partyParticipateRepository.findByMemberUniqueId(memberUniqueId);

        for (PartyParticipate partyParticipate : partyParticipateList) {
            Party party = partyParticipate.getParty();
            Long partyId = party.getPartyId();
            String title = party.getTitle();
            String content = party.getContent();
            String hostName = party.getHostName();
            String address = "address";
            LocalDateTime date = party.getPartyDate();
            int totalCount = party.getTotalCount();
            int currentCount = party.getCurrentCount();
            Boolean processing = false;
            String partyProfileImage = "profileImage";
            LocalDateTime createdAt = party.getCreatedAt();

            MemberPagePartyResponseDto memberPagePartyResponseDto = MemberPagePartyResponseDto.builder()
                    .partyId(partyId)
                    .title(title)
                    .content(content)
                    .memberName(hostName)
                    .address(address)
                    .date(date)
                    .totalCount(totalCount)
                    .currentCount(currentCount)
                    .processing(processing)
                    .profileImage(partyProfileImage)
                    .createdAt(createdAt)
                    .build();

            myPartyList.add(memberPagePartyResponseDto);
        }

        MemberResponseDto memberResponseDto = MemberResponseDto.builder()
                .memberEmailId(memberEmailId)
                .memberName(memberName)
                .profileImage(profileImage)
                .characteristic(characteristic)
                .partyList(myPartyList)
                .build();

        return new ResponseEntity<>(new ResponseDto(200, "로그인에 성공하셨습니다.", memberResponseDto), HttpStatus.OK);
    }

    @Transactional
    public ResponseEntity<ResponseDto> memberPageUpdate(MemberPageUpdateRequestDto memberPageUpdateRequestDto, String memberUniqueId){
        String newMemberName = memberPageUpdateRequestDto.getMemberName();
        String newProfileImage = memberPageUpdateRequestDto.getImage();
        String newCharacteristic = memberPageUpdateRequestDto.getCharacteristic();

        Member member = new Member();
        try {
            member = memberRepository.findByMemberUniqueId(memberUniqueId).orElseThrow(
                () -> new IllegalArgumentException("등록된 사용자가 없습니다."));
        }
        catch(IllegalArgumentException e){
            return new ResponseEntity<>(new ResponseDto(200, "등록된 사용자가 없습니다."), HttpStatus.OK);
        }
        member.update(newMemberName, newProfileImage, newCharacteristic);
        return new ResponseEntity<>(new ResponseDto(200, "마이페이지 수정에 성공하셨습니다."), HttpStatus.OK);
    }

    @Transactional
    public ResponseEntity<ResponseDto> individualPage(Long memberId){

        Member member = new Member();
        try {
            member = memberRepository.findById(memberId).orElseThrow(
                () -> new IllegalArgumentException("등록된 사용자가 없습니다."));
        }
        catch(IllegalArgumentException e){
            return new ResponseEntity<>(new ResponseDto(200, "등록된 사용자가 없습니다."), HttpStatus.OK);
        }
        String emailId = member.getMemberEmailId();
        String name = member.getMemberName();
        int age = member.getAge();
        String gender = member.getGender();
        String profileImage = member.getProfileImage();

        IndividualPageResponseDto individualPageResponseDto = IndividualPageResponseDto.builder()
                .memberEmailId(emailId)
                .memberName(name)
                .age(age)
                .gender(gender)
                .profileImage(profileImage)
                .build();

        return new ResponseEntity<>(new ResponseDto(200, "상대방 프로필 조회 성공.", individualPageResponseDto), HttpStatus.OK);
    }
}
