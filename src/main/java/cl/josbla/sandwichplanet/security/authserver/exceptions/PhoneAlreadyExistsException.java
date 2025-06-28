package cl.josbla.sandwichplanet.security.authserver.exceptions;

public class PhoneAlreadyExistsException extends ResourceAlreadyExistsException {
    public PhoneAlreadyExistsException(String phone) { super("El teléfono '" + phone + "' ya está registrado."); }
}
