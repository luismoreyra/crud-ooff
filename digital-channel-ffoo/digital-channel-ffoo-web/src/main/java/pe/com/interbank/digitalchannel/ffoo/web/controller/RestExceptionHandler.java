package pe.com.interbank.digitalchannel.ffoo.web.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import pe.com.interbank.digitalchannel.ffoo.core.exception.AppException;
import pe.com.interbank.digitalchannel.ffoo.service.util.ServiceError;
import pe.com.interbank.digitalchannel.ffoo.web.bean.ErrorInfo;
import pe.com.interbank.digitalchannel.ffoo.web.bean.ServiceRs;
import pe.com.interbank.digitalchannel.ffoo.web.exception.InvalidRequestException;
import pe.com.interbank.digitalchannel.ffoo.web.util.ErrorMessageBuilder;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * Created by Robert Espinoza on 09/12/2016.
 */
@ControllerAdvice
public class RestExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(RestExceptionHandler.class);

    @Autowired
    private ErrorMessageBuilder errorMessageBuilder;

    @ExceptionHandler(InvalidRequestException.class)
    @ResponseStatus(value = HttpStatus.OK)
    @ResponseBody
    public ServiceRs handleInvalidRequestException(HttpServletRequest req, InvalidRequestException ex) {

        ServiceRs<List<ErrorInfo>> response = new ServiceRs();
        String msg = errorMessageBuilder.buildMessageByException(ex);

        response.setStatusCode(ex.getErrorCode());
        response.setStatusMessage(msg);
        response.setContent(errorMessageBuilder.buildErrorContent(ex.getFieldErrors()));

        return response;
    }

    @ExceptionHandler(AppException.class)
    @ResponseStatus(value = HttpStatus.OK)
    @ResponseBody
    public ServiceRs handleAppException(HttpServletRequest req, AppException ex) {

        ServiceRs response = new ServiceRs();
        String msg = errorMessageBuilder.buildMessageByException(ex);
        response.setStatusCode(ex.getErrorCode());
        response.setStatusMessage(msg);

        return response;
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(value = HttpStatus.OK)
    @ResponseBody
    public ServiceRs handleGeneralException(HttpServletRequest req, Exception ex) {

        logger.error(ex.getMessage(), ex);

        ServiceRs response = new ServiceRs();
        String code = ServiceError.UNHANDLED_ERROR.getCode();
        String msg = errorMessageBuilder.buildMessageByCode(code) + " - " + ex.getMessage();
        response.setStatusCode(code);
        response.setStatusMessage(msg);
        return response;
    }

}
