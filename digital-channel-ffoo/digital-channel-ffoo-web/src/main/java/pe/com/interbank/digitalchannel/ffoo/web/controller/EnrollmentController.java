package pe.com.interbank.digitalchannel.ffoo.web.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import pe.com.interbank.digitalchannel.ffoo.core.exception.AppException;
import pe.com.interbank.digitalchannel.ffoo.model.ChannelParam;
import pe.com.interbank.digitalchannel.ffoo.model.Enrollment;
import pe.com.interbank.digitalchannel.ffoo.model.OptionalKeyParam;
import pe.com.interbank.digitalchannel.ffoo.service.EnrollmentService;
import pe.com.interbank.digitalchannel.ffoo.web.bean.FindByClientCodeRq;
import pe.com.interbank.digitalchannel.ffoo.web.bean.HeaderRq;
import pe.com.interbank.digitalchannel.ffoo.web.bean.ServiceRs;
import pe.com.interbank.digitalchannel.ffoo.web.bean.SetAdCountRq;
import pe.com.interbank.digitalchannel.ffoo.web.bean.SetOptionalKeyRq;
import pe.com.interbank.digitalchannel.ffoo.web.exception.InvalidRequestException;
import pe.com.interbank.digitalchannel.ffoo.web.util.HeaderUtil;

import javax.validation.Valid;

import static pe.com.interbank.digitalchannel.ffoo.web.util.URIConstant.ENROLLMENT_RESOURCE_URI;
import static pe.com.interbank.digitalchannel.ffoo.web.util.URIConstant.FIND_BY_ID;
import static pe.com.interbank.digitalchannel.ffoo.web.util.URIConstant.SET_AD_COUNT_URI;
import static pe.com.interbank.digitalchannel.ffoo.web.util.URIConstant.SET_OPTIONAL_KEY_URI;
import static pe.com.interbank.digitalchannel.ffoo.web.util.WebError.INVALID_REQUEST;

/**
 * Created by Robert Espinoza on 04/07/2017.
 */
@RestController
@RequestMapping(value = ENROLLMENT_RESOURCE_URI, consumes = "application/json", produces = "application/json")
public class EnrollmentController {

    private static final Logger logger = LoggerFactory.getLogger(EnrollmentController.class);

    @Autowired
    private EnrollmentService enrollmentService;

    @RequestMapping(value = FIND_BY_ID, method = RequestMethod.POST)
    public ServiceRs<Enrollment> findByClientCode(@RequestHeader HttpHeaders headers,
                                                  @RequestBody @Valid FindByClientCodeRq request,
                                                  BindingResult bindingResult) throws AppException {
        if (bindingResult.hasErrors()) {
            logger.error(INVALID_REQUEST.getMessage());
            throw new InvalidRequestException(INVALID_REQUEST, bindingResult.getFieldErrors());
        }

        HeaderRq headerRq = HeaderUtil.getHttpHeader(headers);

        Enrollment enrollment = enrollmentService.findByClientCode(request.getClientCode(), headerRq.getChannelId());

        ServiceRs<Enrollment> response = new ServiceRs<>();
        response.setContent(enrollment);

        return response;
    }

    @RequestMapping(value = SET_OPTIONAL_KEY_URI, method = RequestMethod.POST)
    public ServiceRs setOptionalKey(@RequestHeader HttpHeaders headers,
                                    @RequestBody @Valid SetOptionalKeyRq request,
                                    BindingResult bindingResult) throws AppException {
        if (bindingResult.hasErrors()) {
            logger.error(INVALID_REQUEST.getMessage());
            throw new InvalidRequestException(INVALID_REQUEST, bindingResult.getFieldErrors());
        }

        HeaderRq headerRq = HeaderUtil.getHttpHeader(headers);

        OptionalKeyParam optionalKeyParam = new OptionalKeyParam();
        optionalKeyParam.setClientCode(request.getClientCode());
        optionalKeyParam.setOptionalKey(request.getOptionalKey());

        ChannelParam channelParam = new ChannelParam();
        channelParam.setChannelId(headerRq.getChannelId());
        channelParam.setUser(headerRq.getAuthUser());

        enrollmentService.setOptionalKey(optionalKeyParam, channelParam);

        return new ServiceRs();
    }

    @RequestMapping(value = SET_AD_COUNT_URI, method = RequestMethod.POST)
    public ServiceRs setAdCount(@RequestHeader HttpHeaders headers,
                                @RequestBody @Valid SetAdCountRq request,
                                BindingResult bindingResult) throws AppException {
        if (bindingResult.hasErrors()) {
            logger.error(INVALID_REQUEST.getMessage());
            throw new InvalidRequestException(INVALID_REQUEST, bindingResult.getFieldErrors());
        }

        HeaderRq headerRq = HeaderUtil.getHttpHeader(headers);

        enrollmentService.setAdCount(request.getClientCode(), request.getAdCount(), headerRq.getChannelId());

        return new ServiceRs();
    }

}
