package pe.com.interbank.digitalchannel.ffoo.web.util;

import pe.com.interbank.digitalchannel.ffoo.core.util.AppError;

/**
 * Created by Robert Espinoza on 09/12/2016.
 */
public enum WebError implements AppError {

    INVALID_REQUEST("CP124", "Invalid parameter in request service");

    private String code;
    private String message;

    WebError(String code, String message) {
        this.code = code;
        this.message = message;
    }

    @Override
    public String getCode() {
        return code;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
