package com.hanghae7.alcoholcommunity.domain.member.controller;

import com.hanghae7.alcoholcommunity.domain.common.ResponseDto;
import com.hanghae7.alcoholcommunity.domain.common.security.UserDetailsImplement;
import com.hanghae7.alcoholcommunity.domain.member.dto.IndividualPageResponseDto;
import com.hanghae7.alcoholcommunity.domain.member.dto.MemberPageUpdateRequestDto;
import com.hanghae7.alcoholcommunity.domain.member.dto.MemberResponseDto;
import com.hanghae7.alcoholcommunity.domain.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;


@RestController
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    @GetMapping("/member/info")
    public ResponseEntity<ResponseDto> memberPage(@AuthenticationPrincipal UserDetailsImplement userDetails){

        try{
            return memberService.memberPage(userDetails.getMember().getMemberUniqueId());
        }
        catch (IllegalArgumentException e){
            return new ResponseEntity<>(new ResponseDto(400, e.getMessage()), HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/member/update")
    public ResponseEntity<ResponseDto> memberPageUpdate(@RequestBody MemberPageUpdateRequestDto requestDto,
                                                        @RequestParam(value = "image", required = false) MultipartFile image,
                                                        @AuthenticationPrincipal UserDetailsImplement userDetails){
        try{
            return memberService.memberPageUpdate(requestDto, userDetails.getMember().getMemberUniqueId(), image);
        }
        catch (IllegalArgumentException e){
            return new ResponseEntity<>(new ResponseDto(400, e.getMessage()), HttpStatus.BAD_REQUEST);
        }
        catch (IOException e) {
            //e.printStackTrace();  // 예외처리 명확하게 해야 함
            return new ResponseEntity<>(new ResponseDto(400, e.getMessage()), HttpStatus.BAD_REQUEST);
        }
    }



    @GetMapping("/member/{memberId}")
    public ResponseEntity<ResponseDto> individualPage(@PathVariable Long memberId, @AuthenticationPrincipal UserDetailsImplement userDetails){
        try{
            return memberService.individualPage(memberId);
        }
        catch (IllegalArgumentException e){
            return new ResponseEntity<>(new ResponseDto(400, e.getMessage()), HttpStatus.BAD_REQUEST);
        }
    }
}
