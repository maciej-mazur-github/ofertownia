package pl.ofertownia.security.user.constraint;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Documented
@Constraint(validatedBy = MinOneSpecialCharValidator.class)
@Target({ FIELD })
@Retention(RUNTIME)
public @interface MinOneSpecialChar {
    String message() default "Hasło musi zawierać minimum 1 znak specjalny";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
