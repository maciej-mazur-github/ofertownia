package pl.ofertownia.security.user.constraint;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MinOneLowerCaseCharValidator implements ConstraintValidator<MinOneLowerCaseChar, String> {

    @Override
    public boolean isValid(String password, ConstraintValidatorContext constraintValidatorContext) {
        if (password == null || "".equals(password)) {
            return false;
        }
        Pattern pattern = Pattern.compile(".*[a-z].*");
        Matcher matcher =  pattern.matcher(password);
        return matcher.find();
    }
}
