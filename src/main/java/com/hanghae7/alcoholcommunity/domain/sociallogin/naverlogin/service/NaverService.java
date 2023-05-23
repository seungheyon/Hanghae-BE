package com.hanghae7.alcoholcommunity.domain.sociallogin.naverlogin.service;

import java.net.URI;
import java.net.URISyntaxException;
import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.hanghae7.alcoholcommunity.domain.common.jwt.JwtUtil;
import com.hanghae7.alcoholcommunity.domain.common.jwt.TokenDto;
import com.hanghae7.alcoholcommunity.domain.member.dto.MemberSignupRequest;
import com.hanghae7.alcoholcommunity.domain.member.entity.Member;
import com.hanghae7.alcoholcommunity.domain.member.repository.MemberRepository;
import com.hanghae7.alcoholcommunity.domain.sociallogin.kakaologin.client.KakaoClient;
import com.hanghae7.alcoholcommunity.domain.sociallogin.kakaologin.dto.KakaoAccount;
import com.hanghae7.alcoholcommunity.domain.sociallogin.kakaologin.dto.KakaoResponseDto;
import com.hanghae7.alcoholcommunity.domain.sociallogin.kakaologin.dto.KakaoToken;
import com.hanghae7.alcoholcommunity.domain.sociallogin.naverlogin.client.NaverClient;
import com.hanghae7.alcoholcommunity.domain.sociallogin.naverlogin.dto.NaverResponseDto;
import com.hanghae7.alcoholcommunity.domain.sociallogin.naverlogin.dto.NaverToken;
import com.hanghae7.alcoholcommunity.domain.sociallogin.naverlogin.dto.Response;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Please explain the class!!
 *
 * @fileName      : NaverService
 * @author        : mycom
 * @since         : 2023-05-22
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class NaverService {

	private final NaverClient naverClient;
	private final MemberRepository memberRepository;
	private final JwtUtil jwtUtil;

	/**
	 * token을 받기위한 url
	 */
	@Value(value = "${naver.authUrl}")
	private String naverAuthUrl;

	/**
	 * 유저 정보를 얻기위한 url
	 */
	@Value("${naver.userApiUrl}")
	private String naverUserApiUrl;

	/**
	 * clientId key값
	 */
	@Value("${naver.clientID}")
	private String naverClientId;

	/**
	 * clientSecret key
	 */
	@Value("${naver.clientSecret}")
	private String naverClientSecret;

	/**
	 * 첫 번째 로그인일시에 회원가입진행
	 * @param code 프론트에서 전달한 코드값
	 * @param state 확인을 위한 state
	 * @return 프론트에 정보전달
	 */
	public ResponseEntity<NaverResponseDto> getNaverInfo(final String code, final String state, final HttpServletResponse response) {

		final NaverToken token = getNavertoken(code, state);
		try {
			Response naverResponse = naverClient.getNaverInfo(new URI(naverUserApiUrl), token.getTokenType() + " " + token.getAccessToken()).getResponse();
			Optional<Member> member = memberRepository.findByMemberEmailIdAndSocial(naverResponse.getEmail(), "NAVER");
			//로그인했는데 첫 로그인일시 회원가입
			if(member.isEmpty()){
				String gender;
				if(naverResponse.getGender().equals("m") || naverResponse.getGender().equals("M")){gender = "male";} else{gender="female";}
				int currentYear = LocalDate.now().getYear();
				if(naverResponse.getBirthyear() != null && (currentYear - Integer.parseInt(naverResponse.getBirthyear()) + 1) >= 20) {
					MemberSignupRequest signupRequest = MemberSignupRequest.builder()
						.memberEmailId(naverResponse.getEmail())
						.memberUniqueId(UUID.randomUUID().toString())
						.gender(gender)
						.memberName(naverResponse.getNickname())
						.profileImage(naverResponse.getProfile_image())
						.social("NAVER")
						.build();
						Member newMember = Member.create(signupRequest);
						memberRepository.save(newMember);
						// 새로운 멤버를 `member` 변수에 할당
						member = Optional.of(newMember);
				}
				else{
					System.out.println("에러 예외처리 넣기");
				}
			}
			TokenDto tokenDto = jwtUtil.createAllToken(member.get().getMemberUniqueId());
			response.addHeader(JwtUtil.ACCESS_KEY, tokenDto.getAccessToken());
			response.addHeader(JwtUtil.REFRESH_KEY, tokenDto.getRefreshToken());
			NaverResponseDto responseDto = NaverResponseDto.builder()
				.memberId(member.get().getMemberId())
				.memberUniqueId(member.get().getMemberUniqueId())
				.memberName(member.get().getMemberName())
				.profileImage(member.get().getProfileImage())
				.build();

			log.debug("token = {}", token);
			return ResponseEntity.ok(responseDto);
		} catch (URISyntaxException e) {
			log.error("Invalid URI syntax", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}

	/**
	 *
	 * @param code getInfo메소드에서 입력된 code값을 그대로 받아옴
	 * @return KakaoToken값에 accessToken, refreshToken값 리턴
	 */
	public NaverToken getNavertoken(final String code, final String state) {

		try {
			return naverClient.getNaverToken(new URI(naverAuthUrl),"authorization_code", naverClientId, naverClientSecret, code, state);
		} catch (Exception e) {
			log.error("Something error..", e);
			return NaverToken.fail();
		}
	}

}
