package pl.ofertownia.security.user.constraint;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MinOneSpecialCharValidator implements ConstraintValidator<MinOneSpecialChar, String> {

    @Override
    public boolean isValid(String password, ConstraintValidatorContext constraintValidatorContext) {
        if (password == null || "".equals(password)) {
            return false;
        }
        Pattern pattern = Pattern.compile(".*[~!@#$%^&*()_+={\\[}\\]|\"'<,>.?/:-].*");
        Matcher matcher =  pattern.matcher(password);
        return matcher.find();
    }
}
