package pl.ofertownia.api.constraintviolationerror;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ConstraintViolationError {
    private final String field;
    private final String message;
}