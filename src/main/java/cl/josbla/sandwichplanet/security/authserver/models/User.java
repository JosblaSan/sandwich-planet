package cl.josbla.sandwichplanet.security.authserver.models;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter @Setter
@Table(name = "users",
        uniqueConstraints = {
            @UniqueConstraint(columnNames = "mail", name = "UK_users_mail"),
            @UniqueConstraint(columnNames = "rut", name = "UK_users_rut"),
            @UniqueConstraint(columnNames = "telefono", name = "UK_users_telefono")
        })
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false, unique = false) // Considera 'unique = true' si 'username' debe ser único
    private String username;
    @Column(nullable = false, unique = false)
    private String apellidoPat;
    private String apellidoMat;
    @Column(nullable = false, unique = true)
    private String rut;
    @Column(unique = true) // Si el teléfono es opcional pero único cuando está presente
    private String telefono;
    @Column(nullable = false, unique = true)
    private String mail;
    @ElementCollection(fetch = FetchType.EAGER)
    private List<String> direcciones;
    @Column(nullable = false)
    private String password;
    @Column(nullable = false)
    private String roles; // Ejemplo: "USER", "ADMIN"

    @CreatedDate
    @Column(updatable = false)
    private LocalDateTime fechaCreacion;

    @LastModifiedDate
    private LocalDateTime fechamodificacion;

    // Métodos de UserDetails
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_" + roles));
    }
    @Override public String getPassword() { return this.password; }
    @Override public String getUsername() { return this.username; }
    @Override public boolean isAccountNonExpired() { return true; }
    @Override public boolean isAccountNonLocked() { return true; }
    @Override public boolean isCredentialsNonExpired() { return true; }
    @Override public boolean isEnabled() { return true; }
}