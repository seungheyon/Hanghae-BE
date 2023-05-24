package com.hanghae7.alcoholcommunity.domain.member.controller;

import com.hanghae7.alcoholcommunity.domain.common.ResponseDto;
import com.hanghae7.alcoholcommunity.domain.common.security.UserDetailsImplement;
import com.hanghae7.alcoholcommunity.domain.member.dto.IndividualPageResponseDto;
import com.hanghae7.alcoholcommunity.domain.member.dto.MemberPageUpdateRequestDto;
import com.hanghae7.alcoholcommunity.domain.member.dto.MemberResponseDto;
import com.hanghae7.alcoholcommunity.domain.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;


@RestController
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    @GetMapping("/member/info")
    public ResponseDto<MemberResponseDto> memberPage(@AuthenticationPrincipal UserDetailsImplement userDetails){

        try{
            return memberService.memberPage(userDetails.getMember().getMemberUniqueId());
        }
        catch (IllegalArgumentException e){
            return new ResponseDto<>(HttpStatus.BAD_REQUEST.value(), e.getMessage());
        }
    }

    @PutMapping("/member/update")
    public ResponseDto<?> memberPageUpdate(@RequestBody MemberPageUpdateRequestDto requestDto, @AuthenticationPrincipal UserDetailsImplement userDetails){
        try{
            return memberService.memberPageUpdate(requestDto, userDetails.getMember().getMemberUniqueId());
        }
        catch (IllegalArgumentException e){
            return new ResponseDto<>(HttpStatus.BAD_REQUEST.value(), e.getMessage());
        }
    }


    @GetMapping("/member/{memberId}")
    public ResponseDto<IndividualPageResponseDto> individualPage(@PathVariable Long memberId, @AuthenticationPrincipal UserDetailsImplement userDetails){
        try{
            return memberService.individualPage(memberId);
        }
        catch (IllegalArgumentException e){
            return new ResponseDto<>(HttpStatus.BAD_REQUEST.value(), e.getMessage());
        }
    }
}
