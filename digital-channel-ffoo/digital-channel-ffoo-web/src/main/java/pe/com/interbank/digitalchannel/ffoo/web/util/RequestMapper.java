package pe.com.interbank.digitalchannel.ffoo.web.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import pe.com.interbank.digitalchannel.ffoo.model.FrequentOperation;
import pe.com.interbank.digitalchannel.ffoo.model.FrequentOperationDetail;
import pe.com.interbank.digitalchannel.ffoo.model.Pagination;
import pe.com.interbank.digitalchannel.ffoo.web.bean.PaginationRq;
import pe.com.interbank.digitalchannel.ffoo.web.bean.SaveFrequentOperationDetailRq;
import pe.com.interbank.digitalchannel.ffoo.web.bean.SaveFrequentOperationRq;
import pe.com.interbank.digitalchannel.ffoo.web.controller.FrequentOperationController;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Robert Espinoza on 02/01/2017.
 */
@Component
public class RequestMapper {
	private static final Logger logger = LoggerFactory.getLogger(FrequentOperationController.class);
	 
    public FrequentOperation mapToFrequentOperation(final SaveFrequentOperationRq request) {
    	
    	boolean specialOp = request.getSpecialOperation() != null ? request.getSpecialOperation() : false; 
    	FrequentOperation frequentOperation = new FrequentOperation();
        List<FrequentOperationDetail> details = new ArrayList<>();
        FrequentOperationDetail detail;
        
        frequentOperation.setSpecialOperation(specialOp);
        
        frequentOperation.setId(request.getId());
        frequentOperation.setOperationId(request.getOperationId());
        frequentOperation.setAlias(request.getAlias());
        frequentOperation.setClientCode(request.getClientCode());
        frequentOperation.setFrequency(request.getFrequency());
        frequentOperation.setOptionalKey(request.isOptionalKey());
        frequentOperation.setDigitalConstancy(request.getDigitalConstancy());
        frequentOperation.setOperationNotExecuted(request.isOperationNotExecuted());

        for (SaveFrequentOperationDetailRq detailRq : request.getDetails()) {
            detail = new FrequentOperationDetail();
            detail.setFrequentOperationId(request.getId());
            detail.setFromAccount(detailRq.getFromAccount());
            detail.setToIdentifier(detailRq.getToIdentifier());
            detail.setSourceBank(detailRq.getSourceBank());
            detail.setSourceProduct(detailRq.getSourceProduct());
            detail.setSourceSubProduct(detailRq.getSourceSubProduct());
            detail.setSourceCurrency(detailRq.getSourceCurrency());
            detail.setAmount(detailRq.getAmount());
            detail.setTransactionCurrency(detailRq.getTransactionCurrency());
            detail.setTargetCurrency(detailRq.getTargetCurrency());
            detail.setTargetBank(detailRq.getTargetBank());
            detail.setTargetProduct(detailRq.getTargetProduct());
            detail.setTargetSubProduct(detailRq.getTargetSubProduct());
            detail.setCategoryCode(detailRq.getCategoryCode());
            detail.setCompanyCode(detailRq.getCompanyCode());
            detail.setServiceCode(detailRq.getServiceCode());

            details.add(detail);
        }

        frequentOperation.setDetails(details);

        return frequentOperation;
    }

    public Pagination mapToPagination(final PaginationRq request) {
        Pagination pagination = new Pagination();

        pagination.setSize(request.getSize());
        pagination.setPage(request.getPage());

        return pagination;
    }

}
