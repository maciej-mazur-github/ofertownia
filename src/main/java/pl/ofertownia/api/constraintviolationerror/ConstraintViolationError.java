package pl.ofertownia.api.constraintviolationerror;

public class ConstraintViolationError {
    private final String field;
    private final String message;

    public ConstraintViolationError(String field, String message) {
        this.field = field;
        this.message = message;
    }

    public String getField() {
        return field;
    }

    public String getMessage() {
        return message;
    }
}