package pe.com.interbank.digitalchannel.ffoo.web.validator;

import org.apache.commons.lang.StringUtils;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * Created by Robert Espinoza on 02/01/2017.
 */
public class NotEmptyValidator implements ConstraintValidator<NotEmpty, String> {


    @Override
    public void initialize(NotEmpty notEmpty) {

    }

    @Override
    public boolean isValid(String s, ConstraintValidatorContext constraintValidatorContext) {
        if (s == null) {
            return true;
        } else {
            return !StringUtils.isBlank(s);
        }
    }
}
