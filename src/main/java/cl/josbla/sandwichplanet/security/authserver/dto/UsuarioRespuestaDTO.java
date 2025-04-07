package cl.josbla.sandwichplanet.security.authserver.dto;

import java.util.List;

import lombok.Data;

@Data
public class UsuarioRespuestaDTO {
    private String username;
    private String apellidoPat;
    private String apellidoMat;
    private String rut;
    private String telefono;
    private String mail;
    private List<String> direcciones;
    private String roles; // "USER", "ADMIN", etc.
}
