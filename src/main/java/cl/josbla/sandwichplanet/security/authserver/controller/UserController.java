package cl.josbla.sandwichplanet.security.authserver.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import cl.josbla.sandwichplanet.security.authserver.dto.UsuarioCreacionDTO;
import cl.josbla.sandwichplanet.security.authserver.dto.UsuarioRespuestaDTO;
import cl.josbla.sandwichplanet.security.authserver.services.UserService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
@RequestMapping("api/usuarios")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/registro")
    public ResponseEntity<UsuarioRespuestaDTO> RegistrarUsuario(@RequestBody UsuarioCreacionDTO entity) {
        
        UsuarioRespuestaDTO usuario = userService.crearUsuario(entity);
        
        return ResponseEntity.ok(usuario);
    }
    
}
