package com.github.jekattack.cornergame.security;

import com.github.jekattack.cornergame.userdata.CGUser;
import com.github.jekattack.cornergame.userdata.CGUserService;
import io.jsonwebtoken.Claims;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@Component
public class JwtAuthFilter extends OncePerRequestFilter {

    private final JWTService jwtService;
    private final CGUserService cgUserService;

    public JwtAuthFilter(JWTService jwtService, CGUserService userService) {
        this.jwtService = jwtService;
        this.cgUserService = userService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        String token = getToken(request);
        if (token != null && !token.isBlank()) {
            try {
                Claims claims =  jwtService.extractAllClaims(token);
                setSecurityContext(claims);
            } catch (Exception e) {
                response.setStatus(401);
            }
        }

        filterChain.doFilter(request, response);
    }

    private String getToken(HttpServletRequest request) {
        String authorization = request.getHeader("Authorization");
        if (authorization != null) {
            return authorization.replace("Bearer", "").trim();
        }
        return null;
    }

    private void setSecurityContext(Claims claims) {
        List<SimpleGrantedAuthority> grantedAuthorities =
                List.of(new SimpleGrantedAuthority("ROLE_" + claims.get("role")));

        CGUser user = cgUserService.findById(claims.getSubject()).orElseThrow();

        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(user.getUsername(), "", grantedAuthorities);
        SecurityContextHolder.getContext().setAuthentication(token);
    }

}
