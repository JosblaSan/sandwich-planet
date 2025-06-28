package cl.josbla.sandwichplanet.security.authserver.exceptions;

public class EmailAlreadyExistsException extends ResourceAlreadyExistsException {
    public EmailAlreadyExistsException(String email) { super("El correo '" + email + "' ya está registrado."); }
}