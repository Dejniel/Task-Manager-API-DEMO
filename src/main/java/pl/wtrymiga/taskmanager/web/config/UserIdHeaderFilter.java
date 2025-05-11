package pl.wtrymiga.taskmanager.web.config;

import java.io.IOException;

import org.springframework.http.HttpStatus;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.server.ResponseStatusException;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public final class UserIdHeaderFilter extends OncePerRequestFilter {
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
			throws ServletException, IOException {
		String userId = request.getHeader("X-User-Id");
		if (userId == null || userId.isBlank())
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "missing X-User-Id");
		chain.doFilter(request, response);
	}
}
