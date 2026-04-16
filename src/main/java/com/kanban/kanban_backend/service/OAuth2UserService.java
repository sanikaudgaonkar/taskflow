package com.kanban.kanban_backend.service;

import com.kanban.kanban_backend.entity.User;
import com.kanban.kanban_backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class OAuth2UserService extends DefaultOAuth2UserService {

    private final UserRepository userRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest request) {
        OAuth2User oAuth2User = super.loadUser(request);
        Map<String, Object> attributes = oAuth2User.getAttributes();

        String provider = request.getClientRegistration().getRegistrationId();
        String email = null;
        String name = null;

        if (provider.equals("google")) {
            email = (String) attributes.get("email");
            name = (String) attributes.get("name");
        } else if (provider.equals("github")) {
            email = (String) attributes.get("email");
            name = (String) attributes.get("name");
            if (email == null) {
                email = attributes.get("login") + "@github.com";
            }
            if (name == null) {
                name = (String) attributes.get("login");
            }
        }

        // Create user if not exists
        if (email != null) {
            String finalEmail = email;
            String finalName = name;
            userRepository.findByEmail(email).orElseGet(() -> {
                User newUser = new User();
                newUser.setEmail(finalEmail);
                newUser.setName(finalName != null ? finalName : finalEmail);
                newUser.setPassword("OAUTH2_USER");
                return userRepository.save(newUser);
            });
        }

        return oAuth2User;
    }
}