package pe.com.interbank.digitalchannel.ffoo.service.exception;

import pe.com.interbank.digitalchannel.ffoo.core.exception.AppException;
import pe.com.interbank.digitalchannel.ffoo.service.util.ServiceError;

/**
 * Created by Robert Espinoza on 02/12/2016.
 */
public class ServiceException extends AppException {

    public ServiceException() {
    }

    public ServiceException(Throwable cause) {
        super(cause);
    }

    public ServiceException(String errorCode, String message) {
        super(errorCode, message);
    }

    public ServiceException(String errorCode, String message, Throwable cause) {
        super(errorCode, message);
    }

    public ServiceException(ServiceError errorCode) {
        super(errorCode);
    }

    public ServiceException(ServiceError errorCode, Throwable cause) {
        super(errorCode, cause);
    }
}
