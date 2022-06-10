package pe.com.interbank.digitalchannel.ffoo.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pe.com.interbank.digitalchannel.ffoo.model.Channel;
import pe.com.interbank.digitalchannel.ffoo.model.ChannelParam;
import pe.com.interbank.digitalchannel.ffoo.model.FrequentOperation;
import pe.com.interbank.digitalchannel.ffoo.model.FrequentOperationDetail;
import pe.com.interbank.digitalchannel.ffoo.model.FrequentOperationFilter;
import pe.com.interbank.digitalchannel.ffoo.model.FrequentOperationHist;
import pe.com.interbank.digitalchannel.ffoo.model.FrequentOperationHistDetail;
import pe.com.interbank.digitalchannel.ffoo.model.FrequentOperationHistGroup;
import pe.com.interbank.digitalchannel.ffoo.model.OptionalKeyParam;
import pe.com.interbank.digitalchannel.ffoo.model.PageableList;
import pe.com.interbank.digitalchannel.ffoo.model.Pagination;
import pe.com.interbank.digitalchannel.ffoo.persistence.exception.RepositoryException;
import pe.com.interbank.digitalchannel.ffoo.persistence.repository.FrequentOperationRepository;
import pe.com.interbank.digitalchannel.ffoo.persistence.repository.OperationRepository;
import pe.com.interbank.digitalchannel.ffoo.service.ChannelService;
import pe.com.interbank.digitalchannel.ffoo.service.FrequentOperationService;
import pe.com.interbank.digitalchannel.ffoo.service.exception.ServiceException;

import java.util.ArrayList;
import java.util.List;

import static pe.com.interbank.digitalchannel.ffoo.service.util.ServiceError.ENTITY_NOT_FOUND;
import static pe.com.interbank.digitalchannel.ffoo.service.util.ServiceError.INVALID_SOURCE_TARGET_ACCOUNT;
import static pe.com.interbank.digitalchannel.ffoo.service.util.ServiceError.NOT_AVAILABLE_OPERATION;

/**
 * Created by Robert Espinoza on 30/11/2016.
 */
@Service
public class FrequentOperationServiceImpl implements FrequentOperationService {

    private final Logger logger = LoggerFactory.getLogger(FrequentOperationServiceImpl.class);

    @Autowired
    private FrequentOperationRepository frequentOperationRepository;

    @Autowired
    private OperationRepository operationRepository;

    @Autowired
    private ChannelService channelService;

    @Override
    public PageableList<FrequentOperation> findByFilter(FrequentOperationFilter filter, Pagination pagination) throws ServiceException {
        try {
        		return frequentOperationRepository.findByFilterSpecialOperation(filter, pagination);
        } catch (RepositoryException e) {
            logger.error(e.getMessage(), e);
            throw new ServiceException(e.getErrorCode(), e.getMessage(), e);
        }
    }

    @Override
    public PageableList<FrequentOperationHistGroup> findFrequentOperationHist(Integer frequentOperationId, String clientCode, Pagination pagination) throws ServiceException {
        try {
            PageableList<FrequentOperationHist> freqOperationHistPageableList =
                    frequentOperationRepository.findFrequentOperationHist(frequentOperationId, clientCode, pagination);

            List<FrequentOperationHistGroup> frequentOperationHistGroupList = convertToFrequentOperationHistGroup(
                    freqOperationHistPageableList.getContent());

            PageableList<FrequentOperationHistGroup> histGroupPageableList = new PageableList<>();
            histGroupPageableList.setSize(freqOperationHistPageableList.getSize());
            histGroupPageableList.setPage(freqOperationHistPageableList.getPage());
            histGroupPageableList.setTotalElements(freqOperationHistPageableList.getTotalElements());
            histGroupPageableList.setTotalPages(freqOperationHistPageableList.getTotalPages());
            histGroupPageableList.setContent(frequentOperationHistGroupList);

            return histGroupPageableList;
        } catch (RepositoryException e) {
            logger.error(e.getMessage(), e);
            throw new ServiceException(e.getErrorCode(), e.getMessage(), e);
        }
    }

    private List<FrequentOperationHistGroup> convertToFrequentOperationHistGroup(List<FrequentOperationHist> freqOperationHistList) {
        List<FrequentOperationHistGroup> frequentOperationHistGroupList = new ArrayList<>();
        FrequentOperationHistGroup frequentOperationHistGroup = null;
        String lastGroupId = null;

        for (FrequentOperationHist frequentOperationHist : freqOperationHistList) {

            if (!frequentOperationHist.getFrequentOperationGroupId().equals(lastGroupId)) {
                if (frequentOperationHistGroup != null) {
                    frequentOperationHistGroupList.add(frequentOperationHistGroup);
                }

                frequentOperationHistGroup = createFrequentOperationHistGroup(frequentOperationHist);
                lastGroupId = frequentOperationHist.getFrequentOperationGroupId();
            } else {
				if (frequentOperationHistGroup != null) {
					frequentOperationHistGroup.getDetails().add(createFrequentOperationHistDetail(frequentOperationHist));
				}
            }
        }

        if (frequentOperationHistGroup != null) {
            frequentOperationHistGroupList.add(frequentOperationHistGroup);
        }

        return frequentOperationHistGroupList;
    }

    private FrequentOperationHistGroup createFrequentOperationHistGroup(FrequentOperationHist frequentOperationHist) {
        FrequentOperationHistGroup frequentOperationHistGroup = new FrequentOperationHistGroup();

        frequentOperationHistGroup.setFrequentOperationId(frequentOperationHist.getFrequentOperationId());
        frequentOperationHistGroup.setOperationId(frequentOperationHist.getOperationId());
        frequentOperationHistGroup.setAlias(frequentOperationHist.getAlias());
        frequentOperationHistGroup.setClientCode(frequentOperationHist.getClientCode());
        frequentOperationHistGroup.setDigitalConstancy(frequentOperationHist.getDigitalConstancy());
        frequentOperationHistGroup.setExecutionChannel(frequentOperationHist.getExecutionChannel());
        frequentOperationHistGroup.setExecutionDate(frequentOperationHist.getExecutionDate());
        frequentOperationHistGroup.setRequiredKey(frequentOperationHist.isRequiredKey());
        frequentOperationHistGroup.setFrequentOperationGroupId(frequentOperationHist.getFrequentOperationGroupId());
        frequentOperationHistGroup.setDetails(new ArrayList<FrequentOperationHistDetail>());
        frequentOperationHistGroup.getDetails().add(createFrequentOperationHistDetail(frequentOperationHist));

        return frequentOperationHistGroup;
    }

    private FrequentOperationHistDetail createFrequentOperationHistDetail(FrequentOperationHist frequentOperationHist) {
        FrequentOperationHistDetail frequentOperationHistDetail = new FrequentOperationHistDetail();

        frequentOperationHistDetail.setFromAccount(frequentOperationHist.getFromAccount());
        frequentOperationHistDetail.setToIdentifier(frequentOperationHist.getToIdentifier());
        frequentOperationHistDetail.setAmount(frequentOperationHist.getAmount());
        frequentOperationHistDetail.setSourceBank(frequentOperationHist.getSourceBank());
        frequentOperationHistDetail.setSourceProduct(frequentOperationHist.getSourceProduct());
        frequentOperationHistDetail.setSourceSubProduct(frequentOperationHist.getSourceSubProduct());
        frequentOperationHistDetail.setSourceCurrency(frequentOperationHist.getSourceCurrency());
        frequentOperationHistDetail.setTransactionCurrency(frequentOperationHist.getTransactionCurrency());
        frequentOperationHistDetail.setTargetBank(frequentOperationHist.getTargetBank());
        frequentOperationHistDetail.setTargetProduct(frequentOperationHist.getTargetProduct());
        frequentOperationHistDetail.setTargetSubProduct(frequentOperationHist.getTargetSubProduct());
        frequentOperationHistDetail.setTargetCurrency(frequentOperationHist.getTargetCurrency());
        frequentOperationHistDetail.setCategoryCode(frequentOperationHist.getCategoryCode());
        frequentOperationHistDetail.setCompanyCode(frequentOperationHist.getCompanyCode());
        frequentOperationHistDetail.setServiceCode(frequentOperationHist.getServiceCode());

        return frequentOperationHistDetail;
    }

    @Transactional(rollbackFor = ServiceException.class)
    @Override
    public void delete(Integer id, String clientCode, Integer channelId) throws ServiceException {
        try {
            if (frequentOperationRepository.existByChannel(id, clientCode, channelId)) {
            	frequentOperationRepository.delete(id);
            } else {
                logger.error(ENTITY_NOT_FOUND.getMessage());
                throw new ServiceException(ENTITY_NOT_FOUND);
            }
        } catch (RepositoryException e) {
            logger.error(e.getMessage(), e);
            throw new ServiceException(e.getErrorCode(), e.getMessage(), e);
        }
    }

    @Transactional(rollbackFor = ServiceException.class)
    @Override
    public void save(FrequentOperation frequentOperation, Integer channelId, String user) throws ServiceException {
        try {
            if (operationRepository.existByChannel(frequentOperation.getOperationId(), channelId)) {
                validateAccounts(frequentOperation);
                frequentOperationRepository.saveSpecialOperation(frequentOperation, channelId, user);
            } else {
                logger.error(NOT_AVAILABLE_OPERATION.getMessage());
                throw new ServiceException(NOT_AVAILABLE_OPERATION);
            }
        } catch (RepositoryException e) {
            logger.error(e.getMessage(), e);
            throw new ServiceException(e.getErrorCode(), e.getMessage(), e);
        }
    }

    @Override
    public void saveKeepingOptionalKey(FrequentOperation frequentOperation, Integer channelId, String user) throws ServiceException {
        FrequentOperation existingFrequentOperation = findById(frequentOperation.getId(), channelId);

        if (existingFrequentOperation != null) {
            frequentOperation.setOptionalKey(existingFrequentOperation.isOptionalKey());
        }

        save(frequentOperation, channelId, user);
    }

    @Override
    public FrequentOperation findById(Integer id, Integer channelId) throws ServiceException {
        try {
            FrequentOperation frequentOperation = frequentOperationRepository.findById(id);

            if (frequentOperation != null) {
                Channel channel = channelService.findById(channelId);

                frequentOperation.setOptionalKeyBlocked(channel.isOptionalKeyBlocked());
            }

            return frequentOperation;
        } catch (RepositoryException e) {
            logger.error(e.getMessage(), e);
            throw new ServiceException(e.getErrorCode(), e.getMessage(), e);
        }
    }

    @Override
    public void setOptionalKeyByClientCode(OptionalKeyParam optionalKeyParam, ChannelParam channelParam) throws ServiceException {
        try {
            frequentOperationRepository.setOptionalKeyByClientCode(optionalKeyParam, channelParam);
        } catch (RepositoryException e) {
            logger.error(e.getMessage(), e);
            throw new ServiceException(e.getErrorCode(), e.getMessage(), e);
        }
    }

    private void validateAccounts(FrequentOperation frequentOperation) throws ServiceException {
        for (FrequentOperationDetail detail : frequentOperation.getDetails()) {
            if (detail.getFromAccount().equals(detail.getToIdentifier())) {
                throw new ServiceException(INVALID_SOURCE_TARGET_ACCOUNT);
            }
        }
    }

}
