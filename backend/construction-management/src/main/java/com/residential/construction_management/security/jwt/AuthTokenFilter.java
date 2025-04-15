package com.residential.construction_management.security.jwt;

import com.residential.construction_management.security.service.UserDetailsServiceImpl;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter; // Correct import

import java.io.IOException;

// This filter intercepts requests, extracts JWT, validates it, and sets Authentication in SecurityContext
public class AuthTokenFilter extends OncePerRequestFilter { // Extend OncePerRequestFilter

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private UserDetailsServiceImpl userDetailsService; // Use the implementation

    private static final Logger logger = LoggerFactory.getLogger(AuthTokenFilter.class);

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        logger.debug("AuthTokenFilter: Processing request for {}", request.getRequestURI()); // ADDED
        try {
            String jwt = parseJwt(request);
            logger.debug("AuthTokenFilter: Parsed JWT: {}", jwt); // ADDED

            if (jwt != null) { // Check jwt is not null before validation
                logger.debug("AuthTokenFilter: JWT is not null, attempting validation..."); // ADDED
                if (jwtUtils.validateJwtToken(jwt)) {
                    logger.debug("AuthTokenFilter: JWT validation successful."); // ADDED
                    String username = jwtUtils.getUserNameFromJwtToken(jwt);
                    logger.debug("AuthTokenFilter: Username from JWT: {}", username); // ADDED

                    UserDetails userDetails = userDetailsService.loadUserByUsername(username);
                    logger.debug("AuthTokenFilter: UserDetails loaded for username: {}", userDetails.getUsername()); // ADDED

                    UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                            userDetails, null, userDetails.getAuthorities());
                    authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                    SecurityContextHolder.getContext().setAuthentication(authentication);
                    logger.debug("AuthTokenFilter: SecurityContext updated for user: {}", username); // ADDED
                } else {
                    logger.warn("AuthTokenFilter: JWT validation failed for token: {}", jwt); // ADDED
                }
            } else {
                logger.debug("AuthTokenFilter: JWT is null or Bearer prefix missing."); // ADDED
            }
        } catch (Exception e) {
            // Log the exception clearly INCLUDING the request URI
            logger.error("AuthTokenFilter: Cannot set user authentication for URI {}: {}", request.getRequestURI(), e.getMessage(), e); // MODIFIED
        }

        logger.debug("AuthTokenFilter: Passing request down the filter chain for {}", request.getRequestURI()); // ADDED
        filterChain.doFilter(request, response);
    }

    private String parseJwt(HttpServletRequest request) {
        String headerAuth = request.getHeader("Authorization");
        logger.debug("AuthTokenFilter: Authorization Header: {}", headerAuth); // ADDED

        if (StringUtils.hasText(headerAuth) && headerAuth.startsWith("Bearer ")) {
            return headerAuth.substring(7); // Extract token part after "Bearer "
        }

        return null;
    }
}

