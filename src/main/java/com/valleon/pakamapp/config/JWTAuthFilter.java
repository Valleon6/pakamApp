package com.valleon.pakamapp.config;

import com.valleon.pakamapp.exception.Codes;
import com.valleon.pakamapp.exception.Message;
import com.valleon.pakamapp.exception.ResourceNotFoundException;
import com.valleon.pakamapp.modules.authentication.entity.CustomUserDetails;
import com.valleon.pakamapp.modules.authentication.security.CustomUserDetailsService;
import com.valleon.pakamapp.modules.customer.repository.CustomerRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.time.LocalDateTime;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;

@Component
@RequiredArgsConstructor
public class JWTAuthFilter extends OncePerRequestFilter {

    private final CustomerRepository customerRepository;
    private final CustomUserDetailsService customUserDetailsService;


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        final String authHeader = request.getHeader(AUTHORIZATION);
        final String userEmail;
        final String jwtToken;

        if (authHeader == null || !authHeader.startsWith("Bearer")) {
            filterChain.doFilter(request, response);
            return;
        }
        jwtToken = authHeader.substring(7);
        userEmail = JwtUtil.extractUsername(jwtToken);
        if (userEmail != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            CustomUserDetails user = customUserDetailsService.loadUserByUsername(userEmail);
            if (user == null) {
                LocalDateTime time = LocalDateTime.now();
                throw new ResourceNotFoundException(time, Codes.USER_NOT_FOUND, Message.USER_NOT_FOUND);
            }

            if (JwtUtil.isTokenValid(jwtToken, user)) {
                UsernamePasswordAuthenticationToken authToken =
                        new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }
        filterChain.doFilter(request, response);

    }
}
