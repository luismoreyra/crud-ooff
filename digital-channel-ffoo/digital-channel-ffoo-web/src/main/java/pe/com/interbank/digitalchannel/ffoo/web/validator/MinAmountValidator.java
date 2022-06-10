package pe.com.interbank.digitalchannel.ffoo.web.validator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * Created by Robert Espinoza on 02/01/2017.
 */
public class MinAmountValidator implements ConstraintValidator<MinAmount, Double> {
    @Override
    public void initialize(MinAmount minAmount) {

    }

    @Override
    public boolean isValid(Double v, ConstraintValidatorContext constraintValidatorContext) {
        if (v == null) {
            return true;
        } else {
            return v >= 0;
        }
    }
}
