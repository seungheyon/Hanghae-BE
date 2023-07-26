package com.hanghae7.alcoholcommunity.domain.member.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.hanghae7.alcoholcommunity.domain.chat.repository.ChatMessageRepository;
import com.hanghae7.alcoholcommunity.domain.common.ResponseDto;
import com.hanghae7.alcoholcommunity.domain.common.entity.S3Service;
import com.hanghae7.alcoholcommunity.domain.common.jwt.JwtUtil;
import com.hanghae7.alcoholcommunity.domain.common.jwt.RedisDao;
import com.hanghae7.alcoholcommunity.domain.common.jwt.TokenDto;
import com.hanghae7.alcoholcommunity.domain.common.security.UserDetailsImplement;
import com.hanghae7.alcoholcommunity.domain.member.dto.IndividualPageResponseDto;
import com.hanghae7.alcoholcommunity.domain.member.dto.LoginDto;
import com.hanghae7.alcoholcommunity.domain.member.dto.MemberPageUpdateRequestDto;
import com.hanghae7.alcoholcommunity.domain.member.dto.MemberResponseDto;
import com.hanghae7.alcoholcommunity.domain.member.dto.MemberSignupRequest;
import com.hanghae7.alcoholcommunity.domain.member.dto.SignupDto;
import com.hanghae7.alcoholcommunity.domain.member.entity.Login;
import com.hanghae7.alcoholcommunity.domain.member.entity.Member;
import com.hanghae7.alcoholcommunity.domain.member.repository.LoginRepository;
import com.hanghae7.alcoholcommunity.domain.member.repository.MemberRepository;
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
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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
    private final RedisDao redisDao;
    private final JwtUtil jwtUtil;
    private final ChatMessageRepository chatMessageRepository;
    private final LoginRepository loginRepository;
    // image part
    private static final String S3_BUCKET_PREFIX = "S3";

    @Value("${cloud.aws.s3.bucket}")
    private String bucketName;
    private final S3Service s3Service;

    /**
     * 마이페이지 조회
     * @param memberUniqueId the member unique id
     * @return the response entity
     */
    @Transactional
    public ResponseEntity<ResponseDto> memberPage(String memberUniqueId) {
        Member member = new Member();
        try {
            member = memberRepository.findByMemberUniqueId(memberUniqueId).orElseThrow(
                () -> new IllegalArgumentException("등록된 사용자가 없습니다."));
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(new ResponseDto(200, "등록된 사용자가 없습니다."), HttpStatus.OK);
        }

        String memberEmailId = member.getMemberEmailId();
        String memberName = member.getMemberName();
        int age = member.getAge();
        String gender = member.getGender();
        String profileImage = member.getProfileImage();
        String introduce = member.getIntroduce();
        String social = member.getSocial();

        MemberResponseDto memberResponseDto = MemberResponseDto.builder()
            .memberEmailId(memberEmailId)
            .memberName(memberName)
            .profileImage(profileImage)
            .age(age)
            .gender(gender)
            .introduce(introduce)
            .social(social)
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
    public ResponseEntity<ResponseDto> memberPageUpdate(MemberPageUpdateRequestDto memberPageUpdateRequestDto,
        Member member, MultipartFile image) throws IOException {
        String newMemberName = memberPageUpdateRequestDto.getMemberName();
        String newIntroduce = memberPageUpdateRequestDto.getIntroduce();
        Optional<Member> updateMember = memberRepository.findByMemberUniqueId(member.getMemberUniqueId());
        // image가 null 일 경우  -> 처리해야 함
        // image 수정 =========================================================
        if (image != null) {
            if (!imageTypeChecker(image)) {
                return new ResponseEntity<>(
                    new ResponseDto(400, "허용되지 않는 이미지 확장자입니다. 허용되는 확장자 : JPG, JPEG, PNG, GIF, BMP, WEBP, SVG"),
                    HttpStatus.BAD_REQUEST);
            }

            String imageUrl = s3Service.upload((image));
            member.setProfileImage(imageUrl);
            // String imageUrl;
            // // 새로 부여한 이미지명
            // String newFileName = UUID.randomUUID().toString();
            // String fileExtension = '.' + image.getOriginalFilename().substring(image.getOriginalFilename().lastIndexOf('.') + 1);
            // String imageName = S3_BUCKET_PREFIX + newFileName + fileExtension;
            //
            // // 메타데이터 설정
            // ObjectMetadata objectMetadata = new ObjectMetadata();
            // objectMetadata.setContentType(image.getContentType());
            // objectMetadata.setContentLength(image.getSize());
            //
            // InputStream inputStream = image.getInputStream();
            //
            // amazonS3.putObject(new PutObjectRequest(bucketName, imageName, inputStream, objectMetadata)
            //         .withCannedAcl(CannedAccessControlList.PublicRead)); // 이미지에 대한 접근 권한 '공개' 로 설정


            updateMember.get().update1(newMemberName, imageUrl);
            chatMessageRepository.updateChatMessageProfileAndMemberInfo(member.getMemberId(), imageUrl, newMemberName);

        }
        // image 수정 =========================================================
        else {
            updateMember.get().update(newMemberName, newIntroduce);
            chatMessageRepository.updateChatMessageMemberInfo(member.getMemberId(), newMemberName);
        }

        return new ResponseEntity<>(new ResponseDto(200, "마이페이지 수정에 성공하셨습니다."), HttpStatus.OK);
    }


    private boolean imageTypeChecker(MultipartFile image) {
        String imageType [] = {"jpg", "jpeg", "png", "gif", "bmp", "webp", "svg"};
        for(String type : imageType){
            if(image.getContentType().contains(type)){

                return true;
            }
        }
        return false;
    }

    /**
     * Individual page response entity.
     * 상대페이지 조회
     * @param memberId the member id
     * @return the response entity
     */
    @Transactional
    public ResponseEntity<ResponseDto> individualPage(Long memberId) {

        Member member = new Member();
        try {
            member = memberRepository.findById(memberId).orElseThrow(
                () -> new IllegalArgumentException("등록된 사용자가 없습니다."));
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(new ResponseDto(200, "등록된 사용자가 없습니다."), HttpStatus.OK);
        }
        String emailId = member.getMemberEmailId();
        String name = member.getMemberName();
        int age = member.getAge();
        String gender = member.getGender();
        String profileImage = member.getProfileImage();
        String introduce = member.getIntroduce();

        IndividualPageResponseDto individualPageResponseDto = IndividualPageResponseDto.builder()
            .memberEmailId(emailId)
            .memberName(name)
            .age(age)
            .gender(gender)
            .profileImage(profileImage)
            .introduce(introduce)
            .build();

        return new ResponseEntity<>(new ResponseDto(200, "상대방 프로필 조회 성공.", individualPageResponseDto), HttpStatus.OK);
    }

    public ResponseEntity<ResponseDto> memberLogout(HttpServletRequest request) {

        String accessKey = request.getHeader("Access_Key").substring(7);
        redisDao.setValues(accessKey, "blackList", Duration.ofMillis(5400000L));
        String memberUniqueId = jwtUtil.getMemberInfoFromToken(accessKey);
        redisDao.deleteValues(memberUniqueId);
        return new ResponseEntity<>(new ResponseDto(200, "Log OUT!!!!"), HttpStatus.OK);
    }

    @Transactional
    public void signup4(SignupDto signupdto) {
        Optional<Member> member = memberRepository.findByMemberEmailIdAndSocial(signupdto.getMemberEmailId(), "KAKAO");
        if (member.isEmpty()) {
            System.out.println("여기 지나써?1");
            MemberSignupRequest signupRequest = MemberSignupRequest.builder()
                .memberEmailId(signupdto.getMemberEmailId())
                .memberUniqueId(UUID.randomUUID().toString())
                .age(Integer.parseInt(String.valueOf(signupdto.getAge())))
                .gender(signupdto.getGender())
                .memberName(signupdto.getMemberName())
                .social("KAKAO")
                .createdAt(LocalDateTime.now())
                .build();
            Member newMember = Member.create(signupRequest);
            memberRepository.save(newMember);
            System.out.println("여기 지나써?2");
            Login login = Login.create(signupdto.getMemberEmailId(),signupdto.getPassword());
            loginRepository.save(login);
        }
    }

    public void login4(LoginDto loginDto, HttpServletResponse response) {
        Optional<Login> login = loginRepository.findByMemberEmailId(loginDto.getMemberEmailId());
        if (login.isPresent()) {
            if (login.get().getPassword().equals(loginDto.getPassword())) {
                // 토큰줘라
                Optional<Member> member = memberRepository.findByMemberEmailIdAndSocial(loginDto.getMemberEmailId(), "KAKAO");
                TokenDto tokenDto = jwtUtil.createAllToken(member.get().getMemberUniqueId());
                response.addHeader(JwtUtil.ACCESS_KEY, tokenDto.getAccessToken());
                response.addHeader(JwtUtil.REFRESH_KEY, tokenDto.getRefreshToken());
                redisDao.setValues(member.get().getMemberUniqueId(), tokenDto.getRefreshToken());
                response.setStatus(200, "잘됨");
            }
            else {
                response.setStatus(411, "패스워드 잘못 됨");

            }
        }else{
            response.setStatus(412, "회원 가입 안됨");

        }


    }
}
