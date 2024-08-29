package pl.ofertownia.security.user.constraint;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MinOneUpperCaseCharValidator implements ConstraintValidator<MinOneUpperCaseChar, String> {

    @Override
    public boolean isValid(String password, ConstraintValidatorContext constraintValidatorContext) {
        if (password != null) {
            Pattern pattern = Pattern.compile(".*[A-Z].*");
            Matcher matcher =  pattern.matcher(password);
            return matcher.find();
        }
        return false;
    }
}
