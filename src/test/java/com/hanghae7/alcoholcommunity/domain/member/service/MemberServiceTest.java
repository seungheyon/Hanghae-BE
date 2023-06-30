package com.hanghae7.alcoholcommunity.domain.member.service;

import static org.junit.jupiter.api.Assertions.*;

import com.hanghae7.alcoholcommunity.domain.common.entity.S3Service;
import com.hanghae7.alcoholcommunity.domain.common.entity.Timestamped;
import com.hanghae7.alcoholcommunity.domain.common.jwt.JwtUtil;
import com.hanghae7.alcoholcommunity.domain.common.jwt.RedisDao;
import com.hanghae7.alcoholcommunity.domain.member.dto.IndividualPageResponseDto;
import com.hanghae7.alcoholcommunity.domain.member.dto.MemberPageUpdateRequestDto;
import com.hanghae7.alcoholcommunity.domain.member.dto.MemberResponseDto;
import com.hanghae7.alcoholcommunity.domain.member.entity.Member;
import com.hanghae7.alcoholcommunity.domain.member.repository.MemberRepository;
import com.hanghae7.alcoholcommunity.domain.common.ResponseDto;
import com.hanghae7.alcoholcommunity.domain.party.repository.PartyParticipateRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

import javax.servlet.http.HttpServletRequest;

class MemberServiceTest extends Timestamped {
	@Mock
	private MemberRepository memberRepository;

	@Mock
	private S3Service s3Service;
	MockMultipartFile image = new MockMultipartFile("image.jpg", "image.jpg", "image/jpeg", new byte[0]);

	@Mock
	private PartyParticipateRepository partyParticipateRepository;

	@Mock
	private JwtUtil jwtUtil;
	@Mock
	private RedisDao redisDao;
	private MemberService memberService;

	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);
		memberService = new MemberService( memberRepository, partyParticipateRepository, redisDao, jwtUtil, null, s3Service);
	}


	@DisplayName("1- 1. 맴퍼 페이지 테스트")
	@Test
	void testMemberPageWithExistingMember() {
		// Arrange
		String memberUniqueId = "0dc4665c-fe9a-4687-ad1b-a7c158c4d5dd";
		String memberEmailId = "kjbcom@naver.com";
		String memberName = "종범";
		int age = 32;
		String gender = "male";
		String profileImage = "https://ssl.pstatic.net/static/pwe/address/img_profile.png";
		String introduce = "Hi";
		String social = "NAVER";
		Member member = new Member();
		member.setMemberEmailId(memberEmailId);
		member.setMemberName(memberName);
		member.setAge(age);
		member.setGender(gender);
		member.setProfileImage(profileImage);
		member.setIntroduce(introduce);
		member.setSocial(social);
		when(memberRepository.findByMemberUniqueId(memberUniqueId)).thenReturn(Optional.of(member));

		// Act
		ResponseEntity<ResponseDto> responseEntity = memberService.memberPage(memberUniqueId);

		// Assert
		assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
		assertEquals(200, responseEntity.getBody().getStatus());
		assertEquals("로그인에 성공하셨습니다.", responseEntity.getBody().getMsg());

		MemberResponseDto memberResponseDto = (MemberResponseDto) responseEntity.getBody().getData();
		assertEquals(memberEmailId, memberResponseDto.getMemberEmailId());
		assertEquals(memberName, memberResponseDto.getMemberName());
		assertEquals(age, memberResponseDto.getAge());
		assertEquals(gender, memberResponseDto.getGender());
		assertEquals(profileImage, memberResponseDto.getProfileImage());
		assertEquals(introduce, memberResponseDto.getIntroduce());
		assertEquals(social, memberResponseDto.getSocial());

		verify(memberRepository, times(1)).findByMemberUniqueId(memberUniqueId);
	}
	@DisplayName("1-2. 멤버 페이지 테스트 case 멤버 존재 x ")
	@Test
	void testMemberPageWithNonExistingMember() {
		// Arrange
		String memberUniqueId = "0dc4665c-fe9a-4687-ad1b-a7c158c4d5dd";
		when(memberRepository.findByMemberUniqueId(memberUniqueId)).thenReturn(Optional.empty());

		// Act
		ResponseEntity<ResponseDto> responseEntity = memberService.memberPage(memberUniqueId);

		// Assert
		assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
		assertEquals(200, responseEntity.getBody().getStatus());
		assertEquals("등록된 사용자가 없습니다.", responseEntity.getBody().getMsg());
		assertEquals(null, responseEntity.getBody().getData());

		verify(memberRepository, times(1)).findByMemberUniqueId(memberUniqueId);
	}
	@DisplayName("2-1. 멤버 페이지 업데이트 테스트")
	@Test
	void testMemberPageUpdate() throws IOException {
		// Arrange
		String memberUniqueId = "0dc4665c-fe9a-4687-ad1b-a7c158c4d5dd";
		String newMemberName = "종범";
		String newIntroduce = "Hi";
		MemberPageUpdateRequestDto updateRequestDto = new MemberPageUpdateRequestDto(newMemberName, newIntroduce);
		Member member = new Member();
		member.setMemberUniqueId(memberUniqueId);
		when(memberRepository.findByMemberUniqueId(memberUniqueId)).thenReturn(Optional.of(member));

		// Act
		ResponseEntity<ResponseDto> responseEntity = memberService.memberPageUpdate(updateRequestDto, member, image);

		// Assert
		assertEquals(200, responseEntity.getStatusCodeValue());
		assertEquals("마이페이지 수정에 성공하셨습니다.", responseEntity.getBody().getMsg());

		verify(memberRepository, times(1)).findByMemberUniqueId(memberUniqueId);
	}


	@DisplayName("3-1. 프로필 보기 ")
	@Test
	void testIndividualPageWithExistingMember() {
		// Arrange
		Long memberId = 1L;
		String memberEmailId = "kjbcom@naver.com";
		String memberName = "종범";
		int age = 32;
		String gender = "male";
		String profileImage = "https://ssl.pstatic.net/static/pwe/address/img_profile.png";
		String introduce = "Hi";

		Member member = new Member();
		member.setMemberEmailId(memberEmailId);
		member.setMemberName(memberName);
		member.setAge(age);
		member.setGender(gender);
		member.setProfileImage(profileImage);
		member.setIntroduce(introduce);
		when(memberRepository.findById(memberId)).thenReturn(Optional.of(member));

		// Act
		ResponseEntity<ResponseDto> responseEntity = memberService.individualPage(memberId);

		// Assert
		assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
		assertEquals(200, responseEntity.getBody().getStatus());
		assertEquals("상대방 프로필 조회 성공.", responseEntity.getBody().getMsg());

		IndividualPageResponseDto individualPageResponseDto = (IndividualPageResponseDto) responseEntity.getBody().getData();
		assertEquals(memberEmailId, individualPageResponseDto.getMemberEmailId());
		assertEquals(memberName, individualPageResponseDto.getMemberName());
		assertEquals(age, individualPageResponseDto.getAge());
		assertEquals(gender, individualPageResponseDto.getGender());
		assertEquals(profileImage, individualPageResponseDto.getProfileImage());
		assertEquals(introduce, individualPageResponseDto.getIntroduce());

		verify(memberRepository, times(1)).findById(memberId);
	}

	@DisplayName("3-1. 프로필 보기 - 유저정보 x ")
	@Test
	void testIndividualPageWithNonExistingMember() {
		// Arrange
		Long memberId = 1L;
		when(memberRepository.findById(memberId)).thenReturn(Optional.empty());

		// Act
		ResponseEntity<ResponseDto> responseEntity = memberService.individualPage(memberId);

		// Assert
		assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
		assertEquals(200, responseEntity.getBody().getStatus());
		assertEquals("등록된 사용자가 없습니다.", responseEntity.getBody().getMsg());
		assertEquals(null, responseEntity.getBody().getData());

		verify(memberRepository, times(1)).findById(memberId);
	}


	@DisplayName("4. 로그아웃 테스트")
	@Test
	void testMemberLogout() {
		// Arrange
		HttpServletRequest request = mock(HttpServletRequest.class);
		when(request.getHeader("Access_Key")).thenReturn("Bearer token123");

		when(jwtUtil.getMemberInfoFromToken(anyString())).thenReturn("memberUniqueId");

		// Act
		ResponseEntity<ResponseDto> response = memberService.memberLogout(request);

		// Assert
		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertEquals("Log OUT!!!!", response.getBody().getMsg());

	}

	@DisplayName("5-1. 이미지 타입 체커, 지원 되는 이미지")
	@Test
	void testImageTypeCheckerWithSupportedImageType() {
		// Arrange
		MockMultipartFile image = new MockMultipartFile(
			"image",
			"image.jpg",
			"image/jpeg",
			"test image".getBytes()
		);

		// Act
		boolean result = memberService.imageTypeChecker(image);

		// Assert
		assertTrue(result);
	}

	@DisplayName("5-1. 이미지 타입 체커, 지원 안되는 이미지")
	@Test
	void testImageTypeCheckerWithUnsupportedImageType() {
		// Arrange
		MockMultipartFile image = new MockMultipartFile(
			"image",
			"image.pdf",
			"application/pdf",
			"test image".getBytes()
		);

		// Act
		boolean result = memberService.imageTypeChecker(image);

		// Assert
		assertFalse(result);
	}
}