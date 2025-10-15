package sv.edu.udb.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import sv.edu.udb.model.User;
import sv.edu.udb.repository.UserRepository;

@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService extends DefaultOAuth2UserService {
    private final UserRepository userRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) {
        OAuth2User oauthUser = super.loadUser(userRequest);
        String email = oauthUser.getAttribute("email");
        String login = oauthUser.getAttribute("login"); // GitHub username
        String name = oauthUser.getAttribute("name");
        // Usa email si existe, sino login como fallback
        String username = (email != null) ? email : login;
        if (username == null) {
            throw new OAuth2AuthenticationException("No se pudo determinar un username único del proveedor OAuth2");
        }
        userRepository.findByUsername(username).orElseGet(() -> {
            User newUser = new User();
            newUser.setUsername(username);
            newUser.setFirstname(name != null ? name : "");
            newUser.setPassword(""); // No se requiere password para OAuth2
            return userRepository.save(newUser);
        });
        return oauthUser;
    }
}
