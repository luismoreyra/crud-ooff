package pe.com.interbank.digitalchannel.ffoo.persistence.util;

import pe.com.interbank.digitalchannel.ffoo.core.util.AppError;

/**
 * Created by Robert Espinoza on 09/12/2016.
 */
public enum PersistenceError implements AppError {

    PERSISTENCE_ERROR("BD099", "Error in persistence layer");

    private String code;
    private String message;

    PersistenceError(String code, String message) {
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
