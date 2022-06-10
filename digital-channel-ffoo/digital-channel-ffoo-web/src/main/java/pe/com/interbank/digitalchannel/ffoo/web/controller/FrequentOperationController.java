package pe.com.interbank.digitalchannel.ffoo.web.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import pe.com.interbank.digitalchannel.ffoo.core.exception.AppException;
import pe.com.interbank.digitalchannel.ffoo.model.FrequentOperation;
import pe.com.interbank.digitalchannel.ffoo.model.FrequentOperationFilter;
import pe.com.interbank.digitalchannel.ffoo.model.FrequentOperationHistGroup;
import pe.com.interbank.digitalchannel.ffoo.model.PageableList;
import pe.com.interbank.digitalchannel.ffoo.model.Pagination;
import pe.com.interbank.digitalchannel.ffoo.service.FrequentOperationService;
import pe.com.interbank.digitalchannel.ffoo.service.exception.ServiceException;
import pe.com.interbank.digitalchannel.ffoo.web.bean.DeleteFrequentOperationRq;
import pe.com.interbank.digitalchannel.ffoo.web.bean.FindByIdRq;
import pe.com.interbank.digitalchannel.ffoo.web.bean.FindFrequentOperationHistRq;
import pe.com.interbank.digitalchannel.ffoo.web.bean.FindFrequentOperationRq;
import pe.com.interbank.digitalchannel.ffoo.web.bean.HeaderRq;
import pe.com.interbank.digitalchannel.ffoo.web.bean.SaveFrequentOperationRq;
import pe.com.interbank.digitalchannel.ffoo.web.bean.ServiceRs;
import pe.com.interbank.digitalchannel.ffoo.web.exception.InvalidRequestException;
import pe.com.interbank.digitalchannel.ffoo.web.util.HeaderUtil;
import pe.com.interbank.digitalchannel.ffoo.web.util.RequestMapper;

import javax.validation.Valid;

import static pe.com.interbank.digitalchannel.ffoo.service.util.ServiceError.INVALID_DATA;
import static pe.com.interbank.digitalchannel.ffoo.service.util.ServiceError.OPERATION_NOT_ALLOWED;
import static pe.com.interbank.digitalchannel.ffoo.web.util.URIConstant.DELETE_URI;
import static pe.com.interbank.digitalchannel.ffoo.web.util.URIConstant.FIND_BY_FILTER_URI;
import static pe.com.interbank.digitalchannel.ffoo.web.util.URIConstant.FIND_BY_ID;
import static pe.com.interbank.digitalchannel.ffoo.web.util.URIConstant.FIND_FREQUENT_OPERATION_HIST;
import static pe.com.interbank.digitalchannel.ffoo.web.util.URIConstant.FREQUENT_OPERATION_RESOURCE_URI;
import static pe.com.interbank.digitalchannel.ffoo.web.util.URIConstant.SAVE_URI;
import static pe.com.interbank.digitalchannel.ffoo.web.util.WebError.INVALID_REQUEST;

/**
 * Created by Robert Espinoza on 28/11/2016.
 */
@RestController
@RequestMapping(value = FREQUENT_OPERATION_RESOURCE_URI, consumes = "application/json", produces = "application/json")
public class FrequentOperationController {

    private static final Logger logger = LoggerFactory.getLogger(FrequentOperationController.class);

    @Autowired
    private FrequentOperationService frequentOperationService;

    @Autowired
    private RequestMapper requestMapper;

    @RequestMapping(value = FIND_BY_FILTER_URI, method = RequestMethod.POST)
    public ServiceRs<PageableList<FrequentOperation>> findByFilters(@RequestHeader HttpHeaders headers,
                                                                    @RequestBody @Valid FindFrequentOperationRq filter,
                                                                    BindingResult bindingResult) throws AppException {

        if (bindingResult.hasErrors()) {
            logger.error(INVALID_REQUEST.getMessage());
            throw new InvalidRequestException(INVALID_REQUEST, bindingResult.getFieldErrors());
        }

        HeaderRq headerRq = HeaderUtil.getHttpHeader(headers);

        FrequentOperationFilter frequentOperationFilter = new FrequentOperationFilter();
        frequentOperationFilter.setClientCode(filter.getClientCode());
        frequentOperationFilter.setCardTypeId(filter.getCardTypeId());
        frequentOperationFilter.setChannelId(headerRq.getChannelId());
        frequentOperationFilter.setSpecialOperation(filter.getSpecialOperation() != null ? filter.getSpecialOperation() : false);
        
        Pagination pagination = requestMapper.mapToPagination(filter);

        PageableList<FrequentOperation> frequentOperationPageableList = frequentOperationService.findByFilter(frequentOperationFilter, pagination);

        ServiceRs<PageableList<FrequentOperation>> response = new ServiceRs<>();
        response.setContent(frequentOperationPageableList);

        return response;
    }

    @RequestMapping(value = FIND_FREQUENT_OPERATION_HIST, method = RequestMethod.POST)
    public ServiceRs<PageableList<FrequentOperationHistGroup>> findFrequentOperationHist(@RequestHeader HttpHeaders headers,
                                                                                         @RequestBody @Valid FindFrequentOperationHistRq filter,
                                                                                         BindingResult bindingResult) throws AppException {

        if (bindingResult.hasErrors()) {
            logger.error(INVALID_REQUEST.getMessage());
            throw new InvalidRequestException(INVALID_REQUEST, bindingResult.getFieldErrors());
        }

        Pagination pagination = requestMapper.mapToPagination(filter);

        PageableList<FrequentOperationHistGroup> frequentOperationHistPageableList = frequentOperationService.findFrequentOperationHist(filter.getFrequentOperationId(), filter.getClientCode(), pagination);

        ServiceRs<PageableList<FrequentOperationHistGroup>> response = new ServiceRs<>();
        response.setContent(frequentOperationHistPageableList);

        if (frequentOperationHistPageableList.getContent().isEmpty())
        	throw new ServiceException(INVALID_DATA);

        return response;
    }

    @RequestMapping(value = DELETE_URI, method = RequestMethod.POST)
    public ServiceRs delete(@RequestHeader HttpHeaders headers,
                            @RequestBody @Valid DeleteFrequentOperationRq deleteFrequentOperationRq,
                            BindingResult bindingResult) throws AppException {
    	try {
    		if (bindingResult.hasErrors()) {
    			logger.error(INVALID_REQUEST.getMessage());
    			throw new InvalidRequestException(INVALID_REQUEST, bindingResult.getFieldErrors());
    		}

    		HeaderRq headerRq = HeaderUtil.getHttpHeader(headers);

    		frequentOperationService.delete(deleteFrequentOperationRq.getId(), deleteFrequentOperationRq.getClientCode(), headerRq.getChannelId());

    		return new ServiceRs();
    	} catch(Exception e) {
    		throw new ServiceException(OPERATION_NOT_ALLOWED);
    	}
    }

    @RequestMapping(value = SAVE_URI, method = RequestMethod.POST)
    public ServiceRs save(@RequestHeader HttpHeaders headers,
                          @RequestBody @Valid SaveFrequentOperationRq frequentOperationRq,
                          BindingResult bindingResult) throws AppException {
        if (bindingResult.hasErrors()) {
            logger.error(INVALID_REQUEST.getMessage());
            throw new InvalidRequestException(INVALID_REQUEST, bindingResult.getFieldErrors());
        }

        HeaderRq headerRq = HeaderUtil.getHttpHeader(headers);

        FrequentOperation frequentOperation = requestMapper.mapToFrequentOperation(frequentOperationRq);

        if (frequentOperationRq.isUpdateOptionalKey()) {
            frequentOperationService.save(frequentOperation, headerRq.getChannelId(), headerRq.getAuthUser());
        } else {
            frequentOperationService.saveKeepingOptionalKey(frequentOperation, headerRq.getChannelId(), headerRq.getAuthUser());
        }

        return new ServiceRs();
    }

    @RequestMapping(value = FIND_BY_ID, method = RequestMethod.POST)
    public ServiceRs<FrequentOperation> findById(@RequestHeader HttpHeaders headers,
                                                 @RequestBody @Valid FindByIdRq findByIdRq,
                                                 BindingResult bindingResult) throws AppException {

        logger.info("Get OF:" + findByIdRq.getId());

        if (bindingResult.hasErrors()) {
            logger.error(INVALID_REQUEST.getMessage());
            throw new InvalidRequestException(INVALID_REQUEST, bindingResult.getFieldErrors());
        }

        HeaderRq headerRq = HeaderUtil.getHttpHeader(headers);

        FrequentOperation frequentOperation = frequentOperationService.findById(findByIdRq.getId(), headerRq.getChannelId());

        ServiceRs<FrequentOperation> response = new ServiceRs<>();
        response.setContent(frequentOperation);

        return response;
    }

}
