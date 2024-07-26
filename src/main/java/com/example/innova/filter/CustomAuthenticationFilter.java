package com.example.innova.filter;

import com.example.innova.service.CustomUserDetailsService;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class CustomAuthenticationFilter extends OncePerRequestFilter {

    private final CustomUserDetailsService userDetailsService;

    public CustomAuthenticationFilter(CustomUserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws IOException, ServletException {
        String email = request.getHeader("email");
        String password = request.getHeader("password");

        if (email != null && password != null) {
            try {
                UserDetails userDetails = userDetailsService.loadUserByUsername(email);
                if (userDetails != null && userDetails.getPassword().equals(password)) {
                    // Başarıyla doğrulandı
                    Authentication auth = new CustomAuthenticationToken(userDetails);
                    SecurityContextHolder.getContext().setAuthentication(auth);
                } else {
                    // Doğrulama başarısız
                    response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Invalid Credentials");
                    return;
                }
            } catch (UsernameNotFoundException e) {
                // Kullanıcı bulunamadı
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Invalid Credentials");
                return;
            }
        }

        filterChain.doFilter(request, response);
    }
}
