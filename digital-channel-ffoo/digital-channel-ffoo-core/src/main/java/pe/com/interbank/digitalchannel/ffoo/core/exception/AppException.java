package pe.com.interbank.digitalchannel.ffoo.core.exception;

import pe.com.interbank.digitalchannel.ffoo.core.util.AppError;

/**
 * Created by Robert Espinoza on 12/12/2016.
 */
public class AppException extends Exception {

    private String errorCode;

    public AppException() {
    }

    public AppException(Throwable cause) {
        super(cause);
    }

    public AppException(String errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
    }

    public AppException(String errorCode, String message, Throwable cause) {
        super(message, cause);
        this.errorCode = errorCode;
    }

    public AppException(AppError errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode.getCode();
    }

    public AppException(AppError errorCode, Throwable cause) {
        super(errorCode.getMessage(), cause);
        this.errorCode = errorCode.getCode();
    }

    public String getErrorCode() {
        return errorCode;
    }
}
