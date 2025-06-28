package cl.josbla.sandwichplanet.security.authserver.repository;

import cl.josbla.sandwichplanet.security.authserver.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);
    Optional<User> findByMail(String mail); // Nuevo para validación
    Optional<User> findByRut(String rut);   // Nuevo para validación
    Optional<User> findByTelefono(String telefono); // Nuevo para validación

    boolean existsByMail(String mail);     // Para verificar unicidad
    boolean existsByRut(String rut);       // Para verificar unicidad
    boolean existsByTelefono(String telefono); // Para verificar unicidad
    boolean existsByUsername(String username); // Si decides que username sea único
}