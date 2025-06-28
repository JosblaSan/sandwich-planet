package cl.josbla.sandwichplanet.security.authserver.dto;

import lombok.Data;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.util.List;

@Data
public class UsuarioCreacionDTO {
    @NotBlank(message = "El nombre de usuario es requerido")
    @Size(min = 3, max = 50, message = "El nombre de usuario debe tener entre 3 y 50 caracteres")
    private String username;

    @NotBlank(message = "El apellido paterno es requerido")
    private String apellidoPat;

    private String apellidoMat;

    @NotBlank(message = "El RUT es requerido")
    private String rut;

    private String telefono;

    @NotBlank(message = "El correo es requerido")
    @Email(message = "El formato del correo es inválido")
    private String mail;

    private List<String> direcciones; // Si envías direcciones en el registro inicial

    @NotBlank(message = "La contraseña es requerida")
    @Size(min = 6, max = 100, message = "La contraseña debe tener al menos 6 caracteres")
    private String password;

    // NO INCLUIR 'roles' aquí para que el cliente no pueda definir su propio rol
}