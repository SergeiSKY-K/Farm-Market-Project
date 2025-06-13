package telran.java57.farmmarket.dto.exceptions;

public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException(String id) {
        super("user with id " + id + "not found");
    }
}
