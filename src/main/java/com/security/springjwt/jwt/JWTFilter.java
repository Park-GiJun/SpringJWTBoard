package com.security.springjwt.jwt;

import com.security.springjwt.dto.CustomUserDetails;
import com.security.springjwt.entity.UserEntity;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;

public class JWTFilter extends OncePerRequestFilter {
	private final JWTUtil jwtUtil;

	public JWTFilter(JWTUtil jwtUtil) {
		this.jwtUtil = jwtUtil;
	}

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

		String token = null;

		Cookie[] cookies = request.getCookies();
		if (cookies != null) {
			for (Cookie cookie : cookies) {
				if ("jwtToken".equals(cookie.getName())) {
					token = cookie.getValue();
					break;
				}
			}
		}

		System.out.println ("token: " + token);

		if (token == null || jwtUtil.isExpired(token)) {
			filterChain.doFilter(request, response);
			return;
		}

		if (jwtUtil.isExpired(token)) {
			filterChain.doFilter(request, response);
			return;
		}

		String username = jwtUtil.getUsername(token);
		String role = jwtUtil.getRole(token);

		System.out.println ("username: " + username + " role: " + role);

		UserEntity userEntity = new UserEntity();
		userEntity.setUsername(username);
		userEntity.setPassword("temppassword");  // 임시 비밀번호
		userEntity.setRole(role);

		CustomUserDetails customUserDetails = new CustomUserDetails(userEntity);
		Authentication authToken = new UsernamePasswordAuthenticationToken(customUserDetails, null, customUserDetails.getAuthorities());

		SecurityContextHolder.getContext().setAuthentication(authToken);
		filterChain.doFilter(request, response);
	}
}

