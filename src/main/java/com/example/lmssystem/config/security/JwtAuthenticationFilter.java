package com.example.lmssystem.config.security;

import com.example.lmssystem.exception.InvalidToken;
import com.example.lmssystem.exception.TokenExpiredException;
import com.example.lmssystem.utils.Constants;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;


import java.io.IOException;

@Component
@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtProvider jwtProvider;
    private final UserDetailsService userDetailsService;

    public JwtAuthenticationFilter(JwtProvider jwtProvider, UserDetailsService userDetailsService) {
        this.jwtProvider = jwtProvider;
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws IOException {
        try {
            if (!requiredBearer(request.getServletPath())) {
                filterChain.doFilter(request, response);
                return;
            }

            final String jwt = getToken(request);

            if (jwt != null) {
                setAuthentication(jwt, request, response);
            }

            filterChain.doFilter(request, response);
        } catch (JwtException | TokenExpiredException | InvalidToken e) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN, e.getMessage());
        } catch (Exception e) {
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Internal server error");
        }
    }

    private void setAuthentication(String jwt, HttpServletRequest request, HttpServletResponse response) throws TokenExpiredException, InvalidToken {
        final String username = jwtProvider.extractUsername(jwt);
        if (username != null) {
            UserDetails userDetails = this.userDetailsService.loadUserByUsername(username);

             jwtProvider.validateToken(jwt, userDetails);

            UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                    userDetails,
                    null,
                    userDetails.getAuthorities()
            );
            authToken.setDetails(
                    new WebAuthenticationDetailsSource().buildDetails(request)
            );
            SecurityContextHolder.getContext().setAuthentication(authToken);
        }

    }

    private String getToken(HttpServletRequest request) {
        final String authorization = request.getHeader("Authorization");
        if (authorization != null && authorization.startsWith("Bearer")) {
            return authorization.substring(7);
        }
        return null;
    }

    private boolean requiredBearer(String url) {

        for (String white : Constants.whiteList) {
            AntPathMatcher matcher = new AntPathMatcher();
            if (matcher.match(white, url)) {
                return false;
            }
        }

        return true;
    }
}