package pe.com.interbank.digitalchannel.ffoo.web.exception;

import org.springframework.validation.FieldError;
import pe.com.interbank.digitalchannel.ffoo.core.exception.AppException;
import pe.com.interbank.digitalchannel.ffoo.web.util.WebError;

import java.util.List;

/**
 * Created by Robert Espinoza on 09/12/2016.
 */
public class InvalidRequestException extends AppException {

    private List<FieldError> fieldErrors;

    public InvalidRequestException(String message, String errorCode, List<FieldError> fieldErrors) {
        super(errorCode, message);
        this.fieldErrors = fieldErrors;
    }

    public InvalidRequestException(WebError errorCode, List<FieldError> fieldErrors) {
        super(errorCode);
        this.fieldErrors = fieldErrors;
    }

    public List<FieldError> getFieldErrors() {
        return fieldErrors;
    }
}
