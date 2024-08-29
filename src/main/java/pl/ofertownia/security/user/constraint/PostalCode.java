package pl.ofertownia.security.user.constraint;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Documented
@Constraint(validatedBy = PostalCodeValidator.class)
@Target({FIELD})
@Retention(RUNTIME)
public @interface PostalCode {
    String message() default "\"${validatedValue}\" nie posiada prawidłowego formatu kodu pocztowego";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
