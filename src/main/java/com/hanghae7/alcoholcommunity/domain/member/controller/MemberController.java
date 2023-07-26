package com.hanghae7.alcoholcommunity.domain.member.controller;

import com.hanghae7.alcoholcommunity.domain.common.ResponseDto;
import com.hanghae7.alcoholcommunity.domain.common.jwt.JwtUtil;
import com.hanghae7.alcoholcommunity.domain.common.jwt.RedisDao;
import com.hanghae7.alcoholcommunity.domain.common.jwt.TokenDto;
import com.hanghae7.alcoholcommunity.domain.common.security.UserDetailsImplement;
import com.hanghae7.alcoholcommunity.domain.member.dto.LoginDto;
import com.hanghae7.alcoholcommunity.domain.member.dto.MemberPageUpdateRequestDto;
import com.hanghae7.alcoholcommunity.domain.member.dto.SignupDto;
import com.hanghae7.alcoholcommunity.domain.member.entity.Member;
import com.hanghae7.alcoholcommunity.domain.member.repository.MemberRepository;
import com.hanghae7.alcoholcommunity.domain.member.service.MemberService;
import com.hanghae7.alcoholcommunity.domain.sociallogin.kakaologin.dto.KakaoResponseDto;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Please explain the class!!
 * MemberController Class
 * @fileName      : MemberController
 * @author        : 승현
 * @since         : 2023-05-19
 */
@RestController
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;
    private final JwtUtil jwtUtil;
    private final RedisDao redisDao;
    private final MemberRepository memberRepository;

    @PostMapping("/member/signup")
    public ResponseEntity<ResponseDto> signup3(@RequestBody SignupDto signupDto) {
        System.out.println("여긴 왔어?");
        memberService.signup4(signupDto);
        return new ResponseEntity<>(new ResponseDto(200, "회원가입에 성공했습니다."), HttpStatus.OK);
    }

    @PostMapping("/member/login")
    public ResponseEntity<ResponseDto> login3(@RequestBody LoginDto loginDto, final HttpServletResponse response) {
        memberService.login4(loginDto, response);
        Optional<Member> member = memberRepository.findByMemberEmailIdAndSocial(loginDto.getMemberEmailId(),"KAKAO");
        KakaoResponseDto responseDto = KakaoResponseDto.builder()
            .memberId(member.get().getMemberId())
            .memberUniqueId(member.get().getMemberUniqueId())
            .memberName(member.get().getMemberName())
            .profileImage(member.get().getProfileImage())
            .build();
        //여기서 케이스를 나눠서

        if(response.getStatus() == 411) {
            return new ResponseEntity<>(new ResponseDto(411, "패스워드 틀림"), HttpStatus.OK);
        }else if(response.getStatus() == 412){
            return new ResponseEntity<>(new ResponseDto(412, "회원 가입이 안됨"), HttpStatus.OK);
        }
        return new ResponseEntity<>(new ResponseDto(200, "로그인에 성공했습니다.", responseDto), HttpStatus.OK);
    }

    /**
     * 마이페이지 조회 (로그인 된 사람의 마이페이지)
     * @param userDetails
     * @return the response entity
     */
    @GetMapping("/member/info")
    public ResponseEntity<ResponseDto> memberPage(@AuthenticationPrincipal UserDetailsImplement userDetails){

        try{
            return memberService.memberPage(userDetails.getMember().getMemberUniqueId());
        }
        catch (IllegalArgumentException e){
            return new ResponseEntity<>(new ResponseDto(400, e.getMessage()), HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * Member page update response entity.
     * 마이페이지 수정
     * @param requestDto  the request dto
     * @param image       the image
     * @param userDetails the user details
     * @return the response entity
     */
    @PutMapping(value ="/member/update", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ResponseDto> memberPageUpdate(@RequestPart(value = "data") MemberPageUpdateRequestDto requestDto,
                                                        @RequestPart(value = "image", required = false) MultipartFile image,
                                                        @AuthenticationPrincipal UserDetailsImplement userDetails) {

        try {
            return memberService.memberPageUpdate(requestDto, userDetails.getMember(), image);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(new ResponseDto(400, e.getMessage()), HttpStatus.BAD_REQUEST);
        } catch (IOException e) {
            //e.printStackTrace();  // 예외처리 명확하게 해야 함
            return new ResponseEntity<>(new ResponseDto(400, e.getMessage()), HttpStatus.BAD_REQUEST);
        }
    }


    /**
     * Individual page response entity.
     * 상대 페이지 조회
     * @param memberId    the member id
     * @param userDetails the user details
     * @return the response entity
     */
    @GetMapping("/member/{memberId}")
    public ResponseEntity<ResponseDto> individualPage(@PathVariable Long memberId, @AuthenticationPrincipal UserDetailsImplement userDetails){
        try{
            return memberService.individualPage(memberId);
        }
        catch (IllegalArgumentException e){
            return new ResponseEntity<>(new ResponseDto(400, e.getMessage()), HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/reissue")
    public ResponseEntity<ResponseDto> reissue(HttpServletRequest request, HttpServletResponse response){


        String refreshKey = request.getHeader("Refresh_Key").substring(7);

        if(refreshKey.length() != 172) return new ResponseEntity<>(new ResponseDto(400, "토큰 오류!"), HttpStatus.BAD_REQUEST);

        String memberUniqueId = jwtUtil.getMemberInfoFromToken(refreshKey);
        if(redisDao.getValues(memberUniqueId).substring(7).equals(refreshKey)) {
            TokenDto tokenDto = new TokenDto(jwtUtil.createToken(memberUniqueId, "Access"), null);
            response.addHeader(JwtUtil.ACCESS_KEY, tokenDto.getAccessToken());
            return new ResponseEntity<>(new ResponseDto(200, "access key 재 발급 완료"), HttpStatus.OK);
        }
        else{
            return new ResponseEntity<>(new ResponseDto(400, "토큰 오류!"), HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/member/logout")
    public ResponseEntity<ResponseDto> memberLogout(HttpServletRequest request){

        return memberService.memberLogout(request);
    }

}
