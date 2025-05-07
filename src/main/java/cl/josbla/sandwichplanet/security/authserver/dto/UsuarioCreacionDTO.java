package cl.josbla.sandwichplanet.security.authserver.dto;

import java.util.List;

import lombok.Data;

@Data
public class UsuarioCreacionDTO {
    private String username;
    private String password;
    private String apellidoPat;
    private String apellidoMat;
    private String rut;
    private String telefono;
    private String mail;
    private List<String> direcciones;
    private String roles; // "USER", "ADMIN", etc.
}
