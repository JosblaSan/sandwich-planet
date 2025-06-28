package cl.josbla.sandwichplanet.security.authserver.exceptions;

public class RutAlreadyExistsException extends ResourceAlreadyExistsException {
    public RutAlreadyExistsException(String rut) { super("El RUT '" + rut + "' ya está registrado."); }
}