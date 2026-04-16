package com.kanban.kanban_backend.security;

import java.io.IOException;
import java.util.Map;

import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import com.kanban.kanban_backend.entity.User;
import com.kanban.kanban_backend.repository.UserRepository;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class OAuth2SuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final JwtUtil jwtUtil;
    private final UserRepository userRepository;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication) throws IOException {

        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();
        Map<String, Object> attributes = oAuth2User.getAttributes();

        String email = (String) attributes.get("email");
        if (email == null) {
            Object login = attributes.get("login");
            if (login != null) {
                email = login + "@github.com";
            }
        }

        if (email != null) {
            User user = userRepository.findByEmail(email).orElse(null);
            if (user != null) {
                String token = jwtUtil.generateToken(email);
                String name = user.getName();
                // Redirect to frontend with token
                String redirectUrl = "http://localhost:3000/oauth2/callback" +
                        "?token=" + token +
                        "&name=" + name +
                        "&email=" + email;
                getRedirectStrategy().sendRedirect(request, response, redirectUrl);
                return;
            }
        }

        getRedirectStrategy().sendRedirect(request, response, "http://localhost:3000/login?error=true");
    }
}