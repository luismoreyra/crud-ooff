package pe.com.interbank.digitalchannel.ffoo.persistence.exception;

import pe.com.interbank.digitalchannel.ffoo.core.exception.AppException;
import pe.com.interbank.digitalchannel.ffoo.persistence.util.PersistenceError;

/**
 * Created by Robert Espinoza on 29/11/2016.
 */
public class RepositoryException extends AppException {

    public RepositoryException() {
    }

    public RepositoryException(String errorCode, String message) {
        super(errorCode, message);
    }

    public RepositoryException(String errorCode, String message, Throwable cause) {
        super(errorCode, message, cause);
    }

    public RepositoryException(PersistenceError errorCode) {
        super(errorCode);
    }

    public RepositoryException(PersistenceError errorCode, Throwable cause) {
        super(errorCode, cause);
    }
}
