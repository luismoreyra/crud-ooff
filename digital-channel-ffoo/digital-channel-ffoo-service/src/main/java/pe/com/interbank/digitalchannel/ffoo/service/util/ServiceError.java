package pe.com.interbank.digitalchannel.ffoo.service.util;

import pe.com.interbank.digitalchannel.ffoo.core.util.AppError;

/**
 * Created by Robert Espinoza on 09/12/2016.
 */
public enum ServiceError implements AppError {

    DUPLICATE_ALIAS("CS201", "There is an operation with the same alias"),
    INVALID_SOURCE_TARGET_ACCOUNT("CS202", "Source and target account have not to be the same"),
    INVALID_CREDENTIALS("CS203", "Invalid channel credentials"),
    NOT_AVAILABLE_CHANNEL("CS204", "Channel is not available"),
    NOT_AVAILABLE_OPERATION("CS205", "The operation is not available for the channel"),
    ENTITY_NOT_FOUND("CS206", "Entity not found"),
    BLOCKED_OPTIONAL_KEY("CS207", "Optional key is blocked in channel"),
    UNHANDLED_ERROR("CS299", "Unhandled error"),
    INVALID_DATA("7001", "Los datos ingresados son incorrectos. Por favor verifica los datos ingresados."),
    OPERATION_NOT_ALLOWED("7003", "Operación no permitida.");

    private String code;
    private String message;

    ServiceError(String code, String message) {
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
