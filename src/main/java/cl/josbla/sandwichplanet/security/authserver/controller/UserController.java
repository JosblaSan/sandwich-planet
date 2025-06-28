package cl.josbla.sandwichplanet.security.authserver.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.CrossOrigin; // Importar

import cl.josbla.sandwichplanet.security.authserver.dto.UsuarioCreacionDTO;
import cl.josbla.sandwichplanet.security.authserver.dto.UsuarioRespuestaDTO;
import cl.josbla.sandwichplanet.security.authserver.services.UserService;
import jakarta.validation.Valid; // Importar para @Valid
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
@RequestMapping("api/usuarios")
@CrossOrigin(origins = "http://localhost:4200") // Permitir peticiones desde tu Angular
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/registro")
    public ResponseEntity<UsuarioRespuestaDTO> RegistrarUsuario(@Valid @RequestBody UsuarioCreacionDTO entity) {
        UsuarioRespuestaDTO usuario = userService.crearUsuario(entity);
        return new ResponseEntity<>(usuario, HttpStatus.CREATED); // 201 Created
    }
}
