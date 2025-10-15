package sv.edu.udb.service;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import sv.edu.udb.model.User;
import sv.edu.udb.repository.UserRepository;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class OAuth2AuthenticationSuccessHandler implements AuthenticationSuccessHandler {
    private final JwtService jwtService;
    private final UserRepository userRepository;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse
                                                response,
                                        Authentication authentication) throws IOException {
        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();
        // GitHub a veces no da el email, por eso usamos login como fallback
        String username = oAuth2User.getAttribute("email");
        if (username == null) {
            username = oAuth2User.getAttribute("login"); // GitHub username
        }
        if (username == null) {
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "No  se pudo obtener el email ni el login");
            return;
        }
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        String jwt = jwtService.generateToken(user);
        // OPCIÓN A: Redirige con el token como parámetro (para apps web frontend como React)
        response.sendRedirect("http://localhost:3000/oauth2/success?token=" + jwt);
        // OPCIÓN B: Respuesta JSON directa (para clientes REST, móviles, Postman, etc.)
 /*
 response.setContentType("application/json");
 response.setCharacterEncoding("UTF-8");
 response.getWriter().write("{\"token\": \"" + jwt + "\"}");
 */
    }
}