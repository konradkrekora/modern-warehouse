package pl.trinity.warehouse.user_service.exception;

public class UserAlreadyExistsException extends RuntimeException {

    public UserAlreadyExistsException() {
        super("Użytkownik o podanej nazwie już istnieje!");
    }

    public UserAlreadyExistsException(String message) {
        super(message);
    }
}