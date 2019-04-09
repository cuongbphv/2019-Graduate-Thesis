package com.graduate.thesis.backend.validator;

import com.graduate.thesis.backend.entity.UserAccount;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

/**
 * @author Huy Pham
 */

@Component("beforeLoginValidator")
public class UserAccountValidator implements Validator {

    @Override
    public boolean supports(Class<?> aClass) {
        return UserAccount.class.equals(aClass);
    }

    @Override
    public void validate(Object o, Errors errors) {
        UserAccount user = (UserAccount) o;
        if (checkInputString(user.getEmail())) {
            errors.rejectValue("email", "email.empty");
        }

        if (checkInputString(user.getPassword())) {
            errors.rejectValue("password", "password.empty");
        }
    }

    private boolean checkInputString(String input) {
        return (input == null || input.trim().length() == 0);
    }
}
