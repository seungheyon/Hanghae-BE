package com.hanghae7.alcoholcommunity.domain.sociallogin.naverlogin.service;

import java.net.URI;
import java.net.URISyntaxException;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.hanghae7.alcoholcommunity.domain.common.ResponseDto;
import com.hanghae7.alcoholcommunity.domain.common.jwt.JwtUtil;
import com.hanghae7.alcoholcommunity.domain.common.jwt.RedisDao;
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
	private final RedisDao redisDao;
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
	 * 소스는 카카오와 다를바없으므로 주석은 카카오부분 참고
	 * @param code 프론트에서 전달한 코드값
	 * @param state 확인을 위한 state
	 * @return 프론트에 정보전달
	 */
	public ResponseEntity<ResponseDto> getNaverInfo(final String code, final String state, final HttpServletResponse response) {

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
						.age(((currentYear - Integer.parseInt(naverResponse.getBirthyear()) + 1)/10)*10)
						.gender(gender)
						.memberName(naverResponse.getNickname())
						.profileImage(naverResponse.getProfile_image())
						.social("NAVER")
						.createdAt(LocalDateTime.now())
						.build();
						Member newMember = Member.create(signupRequest);
						memberRepository.save(newMember);
						// 새로운 멤버를 `member` 변수에 할당
						member = Optional.of(newMember);
				}
				else{
					return new ResponseEntity<>(new ResponseDto(401, "성인만 저희 서비스를 이용할 수 있습니다."), HttpStatus.BAD_REQUEST);
				}
			}
			TokenDto tokenDto = jwtUtil.createAllToken(member.get().getMemberUniqueId());
			response.addHeader(JwtUtil.ACCESS_KEY, tokenDto.getAccessToken());
			response.addHeader(JwtUtil.REFRESH_KEY, tokenDto.getRefreshToken());
			redisDao.setValues(member.get().getMemberUniqueId(), tokenDto.getRefreshToken(), Duration.ofMillis(14 * 24 * 60 * 60 * 1000L));


			NaverResponseDto responseDto = NaverResponseDto.builder()
				.memberId(member.get().getMemberId())
				.memberUniqueId(member.get().getMemberUniqueId())
				.memberName(member.get().getMemberName())
				.profileImage(member.get().getProfileImage())
				.build();

			log.debug("token = {}", token);
			return new ResponseEntity<>(new ResponseDto(200, "로그인에 성공하셨습니다.", responseDto), HttpStatus.OK);
		} catch (URISyntaxException e) {
			log.error("Invalid URI syntax", e);
			return new ResponseEntity<>(new ResponseDto(401, "NAVER API 요청 에러"), HttpStatus.BAD_REQUEST);
		}
	}

	/**
	 *
	 * @param code getInfo메소드에서 입력된 code값을 그대로 받아옴
	 * @param state naver는 state가 필수값으로 지정됨, FE에서 그대로 받아온 값 사용하면 문제없음
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
