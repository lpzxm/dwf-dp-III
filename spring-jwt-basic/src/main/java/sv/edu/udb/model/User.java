package sv.edu.udb.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;

@Data
@Entity
@Table(name = "user")
@NoArgsConstructor
@AllArgsConstructor
public class User implements UserDetails {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(nullable = false)
    private Integer idUser;
    @Column(unique = true, nullable = false)
    private String username;
    private String firstname;
    private String lastname;
    private Integer age;
    @Column(nullable = false)
    private String password;

    // Implementación completa de UserDetails
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER"));
    }

    @Override
    public String getPassword() {
        return this.password; // Retorna el campo password
    }

    @Override
    public String getUsername() {
        return this.username; // Retorna el campo username
    }

    @Override
    public boolean isAccountNonExpired() {
        return true; // Cuenta nunca expira
    }

    @Override
    public boolean isAccountNonLocked() {
        return true; // Cuenta nunca se bloquea
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true; // Credenciales nunca expiran
    }

    @Override
    public boolean isEnabled() {
        return true; // Cuenta siempre activa
    }
}
