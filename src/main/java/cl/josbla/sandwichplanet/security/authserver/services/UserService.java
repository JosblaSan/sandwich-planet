package cl.josbla.sandwichplanet.security.authserver.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.Collections; // Para direcciones

import cl.josbla.sandwichplanet.security.authserver.dto.UsuarioCreacionDTO;
import cl.josbla.sandwichplanet.security.authserver.dto.UsuarioRespuestaDTO;
import cl.josbla.sandwichplanet.security.authserver.models.User;
import cl.josbla.sandwichplanet.security.authserver.repository.UserRepository;
import cl.josbla.sandwichplanet.security.authserver.exceptions.EmailAlreadyExistsException;
import cl.josbla.sandwichplanet.security.authserver.exceptions.RutAlreadyExistsException;
import cl.josbla.sandwichplanet.security.authserver.exceptions.PhoneAlreadyExistsException;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Transactional
    public UsuarioRespuestaDTO crearUsuario(UsuarioCreacionDTO request){
        if(userRepository.existsByMail(request.getMail())){
            throw new EmailAlreadyExistsException(request.getMail());
        }
        if (userRepository.existsByRut(request.getRut())) {
            throw new RutAlreadyExistsException(request.getRut());
        }
        if (request.getTelefono() != null && !request.getTelefono().isEmpty() && userRepository.existsByTelefono(request.getTelefono())) {
            throw new PhoneAlreadyExistsException(request.getTelefono());
        }
        // Si tu username debe ser único, añadir:
        // if (userRepository.existsByUsername(request.getUsername())) {
        //    throw new ResourceAlreadyExistsException("El nombre de usuario '" + request.getUsername() + "' ya está en uso.");
        // }

        User user = new User();
        user.setUsername(request.getUsername());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setApellidoPat(request.getApellidoPat());
        user.setApellidoMat(request.getApellidoMat());
        user.setRut(request.getRut());
        user.setTelefono(request.getTelefono());
        user.setMail(request.getMail());
        user.setDirecciones(request.getDirecciones() != null ? request.getDirecciones() : Collections.emptyList());
        
        // ¡ASIGNAR EL ROL POR DEFECTO AQUÍ, NO DEL CLIENTE!
        user.setRoles("USER"); // O el rol por defecto para nuevos usuarios

        userRepository.save(user);

        // Construye la respuesta DTO para el cliente
        UsuarioRespuestaDTO respuesta = new UsuarioRespuestaDTO();
        respuesta.setUsername(user.getUsername());
        respuesta.setApellidoPat(user.getApellidoPat());
        respuesta.setApellidoMat(user.getApellidoMat());
        respuesta.setRut(user.getRut());
        respuesta.setTelefono(user.getTelefono());
        respuesta.setMail(user.getMail());
        respuesta.setDirecciones(user.getDirecciones());
        respuesta.setRoles(user.getRoles());
        return respuesta;
    }
}