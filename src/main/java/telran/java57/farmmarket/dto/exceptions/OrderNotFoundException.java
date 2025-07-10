package telran.java57.farmmarket.dto.exceptions;

public class OrderNotFoundException extends RuntimeException {
    public OrderNotFoundException(String id) {
        super("order with id " + id + "not found");
    }
}
