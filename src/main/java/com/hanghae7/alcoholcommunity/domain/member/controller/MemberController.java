package com.hanghae7.alcoholcommunity.domain.member.controller;

import com.hanghae7.alcoholcommunity.domain.common.ResponseDto;
import com.hanghae7.alcoholcommunity.domain.member.dto.IndividualPageResponseDto;
import com.hanghae7.alcoholcommunity.domain.member.dto.MemberPageUpdateRequestDto;
import com.hanghae7.alcoholcommunity.domain.member.dto.MemberResponseDto;
import com.hanghae7.alcoholcommunity.domain.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;


@RestController
@RequiredArgsConstructor
//@RequestMapping("/api")
public class MemberController {

    private final MemberService memberService;

    @GetMapping("/member/info")
    public ResponseDto<MemberResponseDto>  memberPage(HttpServletRequest httpServletRequest){
        try{
            return memberService.memberPage(httpServletRequest);
        }
        catch (IllegalArgumentException e){
            return new ResponseDto<>(HttpStatus.BAD_REQUEST.value(), e.getMessage());
        }
    }

    @PutMapping("/member/update")
    public ResponseDto<?> memberPageUpdate(@RequestBody MemberPageUpdateRequestDto requestDto){
        return memberService.memberPageUpdate(requestDto);
    }


    public ResponseDto<IndividualPageResponseDto> individualPage(HttpServletRequest httpServletRequest){
        try{
            return memberService.individualPage(httpServletRequest);
        }
        catch (IllegalArgumentException e){
            return new ResponseDto<>(HttpStatus.BAD_REQUEST.value(), e.getMessage());
        }
    }
}
