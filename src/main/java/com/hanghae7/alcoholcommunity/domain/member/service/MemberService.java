package com.hanghae7.alcoholcommunity.domain.member.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
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

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.time.temporal.ChronoField;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Please explain the class!!
 * MemberService Class
 * @author : 승현
 * @fileName : MemberService
 * @since : 2023-05-19
 */
@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final PartyParticipateRepository partyParticipateRepository;

    // image part
    private static final String S3_BUCKET_PREFIX = "S3";

    @Value("${cloud.aws.s3.bucket}")
    private String bucketName;
    private final AmazonS3 amazonS3;

    /**
     * 마이페이지 조회
     * @param memberUniqueId the member unique id
     * @return the response entity
     */
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
        int age = member.getAge();
        String gender = member.getGender();
        String profileImage = member.getProfileImage();

        MemberResponseDto memberResponseDto = MemberResponseDto.builder()
                .memberEmailId(memberEmailId)
                .memberName(memberName)
                .profileImage(profileImage)
                .age(age)
                .gender(gender)
                .build();

        return new ResponseEntity<>(new ResponseDto(200, "로그인에 성공하셨습니다.", memberResponseDto), HttpStatus.OK);
    }

    /**
     * Member page update response entity.
     * 마이페이지 수정
     * @param memberPageUpdateRequestDto the member page update request dto
     * @param member                     the member
     * @param image                      the image
     * @return the response entity
     * @throws IOException the io exception
     */
    @Transactional
    public ResponseEntity<ResponseDto> memberPageUpdate(MemberPageUpdateRequestDto memberPageUpdateRequestDto, Member member, MultipartFile image) throws IOException {
        String newMemberName = memberPageUpdateRequestDto.getMemberName();
        int age = memberPageUpdateRequestDto.getAge();
        Optional<Member> updateMember = memberRepository.findByMemberUniqueId(member.getMemberUniqueId());
        // image가 null 일 경우  -> 처리해야 함
        // image 수정 =========================================================
        if (image != null) {

            String imageUrl;
            // 새로 부여한 이미지명
            String newFileName = UUID.randomUUID().toString();
            String fileExtension = '.' + image.getOriginalFilename().substring(image.getOriginalFilename().lastIndexOf('.') + 1);
            String imageName = S3_BUCKET_PREFIX + newFileName + fileExtension;

            // 메타데이터 설정
            ObjectMetadata objectMetadata = new ObjectMetadata();
            objectMetadata.setContentType(image.getContentType());
            objectMetadata.setContentLength(image.getSize());

            InputStream inputStream = image.getInputStream();

            amazonS3.putObject(new PutObjectRequest(bucketName, imageName, inputStream, objectMetadata)
                    .withCannedAcl(CannedAccessControlList.PublicRead)); // 이미지에 대한 접근 권한 '공개' 로 설정
            imageUrl = amazonS3.getUrl(bucketName, imageName).toString();
            updateMember.get().update(newMemberName, age, imageUrl);
        }
        // image 수정 =========================================================
        else{
            updateMember.get().update(newMemberName, age);
        }

        return new ResponseEntity<>(new ResponseDto(200, "마이페이지 수정에 성공하셨습니다."), HttpStatus.OK);
    }

    /**
     * Individual page response entity.
     * 상대페이지 조회
     * @param memberId the member id
     * @return the response entity
     */
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
