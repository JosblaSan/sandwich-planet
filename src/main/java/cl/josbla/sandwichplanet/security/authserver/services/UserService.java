package cl.josbla.sandwichplanet.security.authserver.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import cl.josbla.sandwichplanet.security.authserver.dto.UsuarioCreacionDTO;
import cl.josbla.sandwichplanet.security.authserver.dto.UsuarioRespuestaDTO;
import cl.josbla.sandwichplanet.security.authserver.models.User;
import cl.josbla.sandwichplanet.security.authserver.repository.UserRepository;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    public UsuarioRespuestaDTO crearUsuario(UsuarioCreacionDTO request){
        if(userRepository.existsByMail(request.getMail())){
            throw new RuntimeException("El correo ya está registrado");
        }
        if (userRepository.existsByRut(request.getRut())) {
            throw new RuntimeException("El RUT ya está registrado");
        }
        if (userRepository.existsByTelefono(request.getTelefono())) {
            throw new RuntimeException("El telefono ya está registrado");
        }

        User user = new User();
        user.setUsername(request.getUsername());
        user.setPassword(passwordEncoder.encode(request.getPassword())); // Encripta!
        user.setApellidoPat(request.getApellidoPat());
        user.setApellidoMat(request.getApellidoMat());
        user.setRut(request.getRut());
        user.setTelefono(request.getTelefono());
        user.setMail(request.getMail());
        user.setDirecciones(request.getDirecciones());
        user.setRoles(request.getRoles());
        userRepository.save(user);

        UsuarioRespuestaDTO respuesta = new UsuarioRespuestaDTO();
        respuesta.setUsername(request.getUsername());
        respuesta.setApellidoPat(request.getApellidoPat());
        respuesta.setApellidoMat(request.getApellidoMat());
        respuesta.setRut(request.getRut());
        respuesta.setTelefono(request.getTelefono());
        respuesta.setMail(request.getMail());
        respuesta.setDirecciones(request.getDirecciones());
        respuesta.setRoles(request.getRoles());

        return respuesta;
    }
}
