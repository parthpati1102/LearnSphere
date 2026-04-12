package com.learnsphere.Learnsphere.config;

import com.learnsphere.Learnsphere.service.JwtService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

@Component
public class JwtFilter extends OncePerRequestFilter {

    @Autowired
    private JwtService jwtService;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        final String authHeader = request.getHeader("Authorization");
        final String jwt;
        final String userEmail;

        // Debug: Check if the header is even reaching the filter
        if (authHeader != null) {
            System.out.println("DEBUG: Incoming Authorization Header: " + authHeader);
        }

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        jwt = authHeader.substring(7);

        try {
            userEmail = jwtService.extractUsername(jwt);
            String role = jwtService.extractRole(jwt).toUpperCase();

            if (userEmail != null && SecurityContextHolder.getContext().getAuthentication() == null) {

                // Construct the authority with ROLE_ prefix
                List<SimpleGrantedAuthority> authorities =
                        Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + role));

                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                        userEmail,
                        null,
                        authorities
                );

                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                // The "Gatekeeper" moment
                SecurityContextHolder.getContext().setAuthentication(authToken);

                // --- THE TRUTH TEST LOGS ---
                System.out.println("DEBUG: User Email from JWT: " + userEmail);
                System.out.println("DEBUG: Raw Role from JWT: " + role);
                System.out.println("DEBUG: Final Authorities assigned: " + authToken.getAuthorities());
                // ---------------------------
            }
        } catch (Exception e) {
            System.err.println("DEBUG ERROR: Authentication failed: " + e.getMessage());
            logger.error("Could not set user authentication", e);
        }

        filterChain.doFilter(request, response);
    }
}