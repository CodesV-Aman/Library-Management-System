package com.library.jwt;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.library.repository.UserRepository;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private JwtService jwtService;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		// TODO Auto-generated method stub

		final String authHeader = request.getHeader("Authorization");
		final String jwtToken;
		final String username;

		if (authHeader == null || !authHeader.startsWith("Bearer ")) {
			filterChain.doFilter(request, response);
			return;
		}

		// extract jwt token from header
		jwtToken = authHeader.substring(7);
		username = jwtService.extractUsername(jwtToken);

		// check if have a user and no authentication exists

		if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
			var userdetails = userRepository.findByUsername(username)
					.orElseThrow(() -> new RuntimeException("User not found"));

			// validate the token
			if (jwtService.isTokenValid(jwtToken, userdetails)) {

				// create the authentication with roles
				List<SimpleGrantedAuthority> authority = userdetails.getRoles().stream()
						.map(SimpleGrantedAuthority::new).collect(Collectors.toList());

				UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(userdetails,
						null, authority);

				// set the authentication details
				authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

				// update the security context with authentication
				SecurityContextHolder.getContext().setAuthentication(authToken);

			}
		}
		filterChain.doFilter(request, response);

	}

}
