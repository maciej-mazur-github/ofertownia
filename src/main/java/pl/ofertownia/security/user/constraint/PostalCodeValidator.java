package pl.ofertownia.security.user.constraint;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PostalCodeValidator implements ConstraintValidator<PostalCode, String> {

    @Override
    public boolean isValid(String postalCode, ConstraintValidatorContext constraintValidatorContext) {
        if (postalCode == null || "".equals(postalCode)) {
            return false;
        }
        Pattern pattern = Pattern.compile("[0-9]{2}-[0-9]{3}");
        Matcher matcher =  pattern.matcher(postalCode);
        return matcher.find();
    }
}
