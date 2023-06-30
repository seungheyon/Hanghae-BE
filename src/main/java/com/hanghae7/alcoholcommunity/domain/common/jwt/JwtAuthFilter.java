package com.hanghae7.alcoholcommunity.domain.common.jwt;

import static com.hanghae7.alcoholcommunity.domain.common.jwt.JwtUtil.*;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hanghae7.alcoholcommunity.domain.member.repository.MemberRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtAuthFilter extends OncePerRequestFilter {

	private final JwtUtil jwtUtil;
	private final MemberRepository memberRepository;
	private final RedisDao redisDao;

	@Override
	protected void doFilterInternal(javax.servlet.http.HttpServletRequest request, javax.servlet.http.HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
		String access_token = jwtUtil.resolveToken(request, ACCESS_KEY);
		String refresh_token = jwtUtil.resolveToken(request, REFRESH_KEY);

		if(access_token != null) {
			if(jwtUtil.validateToken(access_token)) {
				if (redisDao.getValues(request.getHeader("Access_key").substring(7))!= null) {
					jwtExceptionHandler(response, "410 : This token already Logged Out ", 410);
					return;
				}
				setAuthentication(jwtUtil.getMemberInfoFromToken(access_token));
			// 	String memberUniqueId = jwtUtil.getMemberInfoFromToken(refresh_token);
			// 	Member member = memberRepository.findByMemberUniqueId(memberUniqueId).get();
			// 	String newAccessToken = jwtUtil.createToken(memberUniqueId, "Access");
			// 	jwtUtil.setHeaderAccessToken(response, newAccessToken);
			// 	setAuthentication(memberUniqueId);
			// } else if (refresh_token == null) {
			// 	jwtExceptionHandler(response, "AccessToken has Expired. Please send your RefreshToken together.", HttpStatus.OK.value());
			} else {
				jwtExceptionHandler(response, "403 : Wrong token", HttpStatus.FORBIDDEN.value());
				return;
			}
		}
		filterChain.doFilter(request, response);
	}

	private void jwtExceptionHandler(javax.servlet.http.HttpServletResponse response, String msg, int statusCode) {
		response.setStatus(statusCode);
		response.setContentType("application/json");
		try {
			String json = new ObjectMapper().writeValueAsString(new TokenMessageDto(msg, statusCode));
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
