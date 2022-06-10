package pe.com.interbank.digitalchannel.ffoo.web.validator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.ANNOTATION_TYPE;
import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Created by Robert Espinoza on 02/01/2017.
 */
@Target({METHOD, FIELD, ANNOTATION_TYPE})
@Retention(RUNTIME)
@Constraint(validatedBy = MinAmountValidator.class)
@Documented
public @interface MinAmount {

    String message() default "Amount should be more than zero";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

}
