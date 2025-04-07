package cl.josbla.sandwichplanet.security.authserver.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import cl.josbla.sandwichplanet.security.authserver.models.User;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long>{
    Optional<User> findByMail(String mail);

    boolean existsByMail(String mail);

    boolean existsByRut(String rut);

    boolean existsByTelefono(String telefono);
    
}
