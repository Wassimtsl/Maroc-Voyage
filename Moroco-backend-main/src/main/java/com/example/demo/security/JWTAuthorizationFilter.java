package com.example.demo.security;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.auth0.jwt.interfaces.DecodedJWT;

@Component
public class JWTAuthorizationFilter extends OncePerRequestFilter {

    private final SecParams secParams;

    public JWTAuthorizationFilter(SecParams secParams) {
        this.secParams = secParams;
    }

    // ✅ Exclure les routes publiques du JWT Filter
// ✅ Exclure les routes publiques du JWT Filter
@Override
protected boolean shouldNotFilter(HttpServletRequest request) {
    String path = request.getRequestURI();
    String method = request.getMethod();
    
    System.out.println("🔍 Vérification route: " + method + " " + path);
    
    AntPathRequestMatcher[] publicRoutes = {
        new AntPathRequestMatcher("/api/auth/**"),
        new AntPathRequestMatcher("/api/users", "POST")
    };
    
    boolean skip = Arrays.stream(publicRoutes)
        .anyMatch(matcher -> matcher.matches(request));
    
    System.out.println("✅ shouldNotFilter(" + path + "): " + skip);
    
    return skip;
}

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        String authHeader = request.getHeader(SecParams.HEADER);

        if (authHeader == null || !authHeader.startsWith(SecParams.PREFIX)) {
            filterChain.doFilter(request, response);
            return;
        }

        try {
            String jwt = authHeader.substring(SecParams.PREFIX.length());

            JWTVerifier verifier = JWT.require(
                    Algorithm.HMAC256(secParams.getSecret())
            ).build();

            DecodedJWT decodedJWT = verifier.verify(jwt);

            String username = decodedJWT.getSubject();	
            List<String> roles = decodedJWT.getClaim("roles").asList(String.class);

            Collection<GrantedAuthority> authorities = roles.stream()
                    .map(r -> new SimpleGrantedAuthority("ROLE_" + r))
                    .collect(Collectors.toList());

            UsernamePasswordAuthenticationToken authentication =
                    new UsernamePasswordAuthenticationToken(username, null, authorities);

            SecurityContextHolder.getContext().setAuthentication(authentication);

        } catch (TokenExpiredException e) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("{\"error\":\"Token expired\"}");
             System.err.println("⚠️ Token expiré - mais route peut être publique");
            return;

        } catch (JWTVerificationException e) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("{\"error\":\"Invalid token\"}");
                System.err.println("⚠️ Token invalide - mais route peut être publique");
            return;
        }

        filterChain.doFilter(request, response);
    }
}