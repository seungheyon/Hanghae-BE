package com.hanghae7.alcoholcommunity.domain.sociallogin.kakaologin.service;

import java.net.URI;
import java.net.URISyntaxException;
import java.time.Duration;
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
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class KakaoService {

    private final KakaoClient client;
    private final MemberRepository memberRepository;
    private final JwtUtil jwtUtil;
    private final RedisDao redisDao;

    /**
     * token을 받기위한 url
     */
    @Value(value = "${kakao.authUrl}")
    private String kakaoAuthUrl;

    /**
     * 유저 정보를 얻기위한 url
     */
    @Value("${kakao.userApiUrl}")
    private String kakaoUserApiUrl;

    /**
     * 로그아웃을 하기위한 url 구현중
     */
    @Value("${kakao.logoutUrl}")
    private String kakaKologoutUrl;

    /**
     * RestApiKey
     */
    @Value("${kakao.restapiKey}")
    private String restapiKey;

    /**
     * 코드값을 callback받기위한 주소
     */
    @Value("${kakao.redirectUrl}")
    private String redirectUrl;

    /**
     * 첫번째 로그인일시 회원가입 진행
     * @param code api를 통해 요청된 코드값
     * @param response 헤더에 accestoken 포함하기 위한 값
     * @see KakaoResponseDto
     * @return KakaoResponseDto값 리턴
     */
    public ResponseEntity<ResponseDto> getKakaoInfo(final String code, final HttpServletResponse response) {

        //code를 이용해서 Kakao Api용 Accesstoekn과 Refresh토큰을 받아옴
        final KakaoToken token = getKakaoToken(code);
        try {
            //해당 토큰을 이용해서 유저정보를 요청하는 API요청을 보낸다
            KakaoAccount kakaoAccount = client.getKakaoInfo(new URI(kakaoUserApiUrl), token.getTokenType() + " " + token.getAccessToken()).getKakaoAccount();
           // 가져온 유저정보로 Email과 소셜로그인 정보로 이미 회원가입이 된 유저인지 확인
            Optional<Member> member = memberRepository.findByMemberEmailIdAndSocial(kakaoAccount.getEmail(), "KAKAO");
            //로그인했는데 첫 로그인일시 회원가입
            if(member.isEmpty()){
                //회원가입이 안된유저라면 성인인지 여부를 판단하고 회원가입 진행
                if(kakaoAccount.getAge_range() != null && Integer.parseInt(kakaoAccount.getAge_range().substring(0, 2)) >= 20) {
                    MemberSignupRequest signupRequest = MemberSignupRequest.builder()
                        .memberEmailId(kakaoAccount.getEmail())
                        .memberUniqueId(UUID.randomUUID().toString())
                        .age(Integer.parseInt(kakaoAccount.getAge_range().substring(0, 2)))
                        .gender(kakaoAccount.getGender())
                        .memberName(kakaoAccount.getProfile().getNickname())
                        .profileImage(kakaoAccount.getProfile().getProfile_image_url())
                        .social("KAKAO")
                        .createdAt(LocalDateTime.now())
                        .build();
                        Member newMember = Member.create(signupRequest);
                        memberRepository.save(newMember);
                        // 새로운 멤버를 `member` 변수에 할당
                        member = Optional.of(newMember);
                    } else{
                    return new ResponseEntity<>(new ResponseDto(401, "성인만 저희 서비스를 이용할 수 있습니다."), HttpStatus.BAD_REQUEST);
                }
            }
            //UniqueId로 Soolo 서비스이용가능한 Token 발급
            TokenDto tokenDto = jwtUtil.createAllToken(member.get().getMemberUniqueId());
            response.addHeader(JwtUtil.ACCESS_KEY, tokenDto.getAccessToken());
            response.addHeader(JwtUtil.REFRESH_KEY, tokenDto.getRefreshToken());
            redisDao.setValues(member.get().getMemberUniqueId(),tokenDto.getRefreshToken(),Duration.ofMillis(14243600000L));
            // .setValues(.get().getMemberUniqueId(), tokenDto.getRefreshToken(), Duration.ofMillis(14 * 24 * 60 * 60 * 1000L));

            KakaoResponseDto responseDto = KakaoResponseDto.builder()
                .memberId(member.get().getMemberId())
                .memberUniqueId(member.get().getMemberUniqueId())
                .memberName(member.get().getMemberName())
                .profileImage(member.get().getProfileImage())
                .build();

            log.debug("token = {}", token);
            return new ResponseEntity<>(new ResponseDto(200, "로그인에 성공하셨습니다.", responseDto), HttpStatus.OK);
        } catch (URISyntaxException e) {
            log.error("Invalid URI syntax", e);
            return new ResponseEntity<>(new ResponseDto(401, "KAKAO API 에러"), HttpStatus.BAD_REQUEST);
        }
    }

    /**
     *
     * @param code getInfo메소드에서 입력된 code값을 그대로 받아옴
     * @return KakaoToken값에 accessToken, refreshToken값 리턴
     */
    public KakaoToken getKakaoToken(final String code) {

        try {
            //code를 이용하여 Token발급 API요청
            return client.getKakaoToken(new URI(kakaoAuthUrl), restapiKey, redirectUrl, code, "authorization_code");
        } catch (Exception e) {
            log.error("Something error..", e);
            return KakaoToken.fail();
        }
    }

}
