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
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;

import javax.servlet.http.HttpServletRequest;
import javax.xml.transform.sax.SAXResult;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final JwtUtil jwtUtil;
    private final Member member;

    @Transactional
    public ResponseDto<MemberResponseDto> memberPage(HttpServletRequest httpServletRequest){

        String accessToken = httpServletRequest.getHeader("Authorization").substring(7);
        String memberId = jwtUtil.getMemberInfoFromToken(accessToken);  // 사용자의 UniqueId 가져옴 -> 머지 후 수정

        Member member = memberRepository.findByMemberEmailId(memberId).orElseThrow(
                () -> new IllegalArgumentException("등록된 사용자가 없습니다.")
        );

        String memberEmailId = member.getMemberEmailId();
        String memberName = member.getMemberName();
        String profileImage = member.getProfileImage();
        String characteristic = "characteristic";

        List<MemberPagePartyResponseDto> list = new ArrayList<>();
        List<PartyParticipate> partyParticipateList = member.getPartyParticipates();

        for (PartyParticipate partyParticipate : partyParticipateList) {
            Party party = partyParticipate.getParty();
            Long partyId = party.getPartyId();
            String title = party.getTitle();
            String content = party.getContent();
            String masterName = party.getMember().getMemberName();
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
                    .memberName(masterName)
                    .address(address)
                    .date(date)
                    .totalCount(totalCount)
                    .currentCount(currentCount)
                    .processing(processing)
                    .profileImage(partyProfileImage)
                    .createdAt(createdAt)
                    .build();

            list.add(memberPagePartyResponseDto);
        }

        MemberResponseDto memberResponseDto = MemberResponseDto.builder()
                .memberEmailId(memberEmailId)
                .memberName(memberName)
                .profileImage(profileImage)
                .characteristic(characteristic)
                .partyList(list)
                .build();

        return new ResponseDto<>(200, "성공", memberResponseDto);
    }

    @Transactional
    public ResponseDto<?> memberPageUpdate(MemberPageUpdateRequestDto memberPageUpdateRequestDto){
        String newMemberName = memberPageUpdateRequestDto.getMemberName();
        String newProfileImage = memberPageUpdateRequestDto.getImage();
        String newCharacteristic = memberPageUpdateRequestDto.getCharacteristic();

        member.update(newMemberName, newProfileImage, newCharacteristic);

        return new ResponseDto<>(200, "성공");
    }

    @Transactional
    public ResponseDto<IndividualPageResponseDto> individualPage(HttpServletRequest httpServletRequest){
        String accessToken = httpServletRequest.getHeader("Authorization").substring(7);
        String memberId = jwtUtil.getMemberInfoFromToken(accessToken);  // 사용자의 UniqueId 가져옴 -> 머지 후 수정

        Member member = memberRepository.findByMemberEmailId(memberId).orElseThrow(
                () -> new IllegalArgumentException("등록된 사용자가 없습니다.")
        );

        String emailId = member.getMemberEmailId();
        String name = member.getMemberName();
        int age = 96;
        String gender = member.getGender();
        String profileImage = member.getProfileImage();

        IndividualPageResponseDto individualPageResponseDto = IndividualPageResponseDto.builder()
                .memberEmailId(emailId)
                .memberName(name)
                .age(age)
                .gender(gender)
                .profileImage(profileImage)
                .build();

        return new ResponseDto<>(200, "성공", individualPageResponseDto);
    }

}
