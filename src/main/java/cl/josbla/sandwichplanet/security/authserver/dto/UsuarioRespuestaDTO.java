package cl.josbla.sandwichplanet.security.authserver.dto;

import lombok.Data;
import java.util.List;

@Data
public class UsuarioRespuestaDTO {
    private String username;
    private String apellidoPat;
    private String apellidoMat;
    private String rut;
    private String telefono;
    private String mail;
    private List<String> direcciones;
    private String roles; // Muestra el rol asignado por el backend

    private String message; // Para mensajes de error controlados

    public UsuarioRespuestaDTO() {}
    public UsuarioRespuestaDTO(String message) { this.message = message; }
}