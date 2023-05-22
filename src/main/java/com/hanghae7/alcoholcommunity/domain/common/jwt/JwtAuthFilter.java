package com.hanghae7.alcoholcommunity.domain.common.jwt;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hanghae7.alcoholcommunity.domain.member.dto.MemberResponseDto;
import com.hanghae7.alcoholcommunity.domain.member.entity.Member;
import com.hanghae7.alcoholcommunity.domain.member.repository.MemberRepository;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

import static com.hanghae7.alcoholcommunity.domain.common.jwt.JwtUtil.ACCESS_KEY;
import static com.hanghae7.alcoholcommunity.domain.common.jwt.JwtUtil.REFRESH_KEY;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtAuthFilter extends OncePerRequestFilter {

	private final JwtUtil jwtUtil;
	private final MemberRepository memberRepository;
	@Override
	protected void doFilterInternal(javax.servlet.http.HttpServletRequest request, javax.servlet.http.HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
		String access_token = jwtUtil.resolveToken(request, ACCESS_KEY);
		String refresh_token = jwtUtil.resolveToken(request, REFRESH_KEY);

		if(access_token != null) {
			if(jwtUtil.validateToken(access_token)) {
				setAuthentication(jwtUtil.getMemberInfoFromToken(access_token));
			}
			else if (refresh_token != null && jwtUtil.refreshTokenValidation(refresh_token)) {
				String memberEmailId = jwtUtil.getMemberInfoFromToken(refresh_token);
				Member member = memberRepository.findByMemberEmailId(memberEmailId).get();
				String newAccessToken = jwtUtil.createToken(memberEmailId, "Access");
				jwtUtil.setHeaderAccessToken(response, newAccessToken);
				setAuthentication(memberEmailId);
			} else if (refresh_token == null) {
				jwtExceptionHandler(response, "AccessToken has Expired. Please send your RefreshToken together.", HttpStatus.BAD_REQUEST.value());
			} else {
				jwtExceptionHandler(response, "RefreshToken Expired", HttpStatus.BAD_REQUEST.value());
				return;
			}
		}
		filterChain.doFilter(request, response);
	}

	private void jwtExceptionHandler(javax.servlet.http.HttpServletResponse response, String msg, int statusCode) {
		response.setStatus(statusCode);
		response.setContentType("application/json");
		try {
			String json = new ObjectMapper().writeValueAsString(new MemberResponseDto(msg, HttpStatus.UNAUTHORIZED));
			response.getWriter().write(json);
		} catch (Exception e){
			log.error(e.getMessage());
		}
	}

	public void setAuthentication(String username) {
		SecurityContext context = SecurityContextHolder.createEmptyContext();
		Authentication authentication = jwtUtil.createAuthentication(username);
		context.setAuthentication(authentication);
		SecurityContextHolder.setContext(context);
	}

}
