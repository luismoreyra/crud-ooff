package pe.com.interbank.digitalchannel.ffoo.web.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.FieldError;
import pe.com.interbank.digitalchannel.ffoo.core.exception.AppException;
import pe.com.interbank.digitalchannel.ffoo.web.bean.ErrorInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Robert Espinoza on 02/11/2016.
 */
@Component
public class ErrorMessageBuilder {

    @Autowired
    private ErrorMessageDef errorMessageDef;

    public String buildMessageByCode(final String code) {
        String message = errorMessageDef.getMessage().get(code);

        if (message == null) {
            message = "Description not found for this error code";
        }

        return message;
    }

    public String buildMessageByException(final AppException exception) {
        String message = errorMessageDef.getMessage().get(exception.getErrorCode());

        if (message == null) {
            message = exception.getMessage();
        }

        return message;
    }

    public List<ErrorInfo> buildErrorContent(final List<FieldError> fieldErrors) {
        List<ErrorInfo> errorInfoList = new ArrayList<>();
        ErrorInfo errorInfo;

        for (FieldError fieldError : fieldErrors) {
            errorInfo = new ErrorInfo();
            errorInfo.setCode(fieldError.getDefaultMessage());
            errorInfo.setMessage(buildMessageByCode(fieldError.getDefaultMessage()));

            errorInfoList.add(errorInfo);
        }

        return errorInfoList;
    }

}
