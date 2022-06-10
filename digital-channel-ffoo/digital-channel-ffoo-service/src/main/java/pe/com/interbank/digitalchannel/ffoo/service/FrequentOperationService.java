package pe.com.interbank.digitalchannel.ffoo.service;

import pe.com.interbank.digitalchannel.ffoo.model.ChannelParam;
import pe.com.interbank.digitalchannel.ffoo.model.FrequentOperation;
import pe.com.interbank.digitalchannel.ffoo.model.FrequentOperationFilter;
import pe.com.interbank.digitalchannel.ffoo.model.FrequentOperationHistGroup;
import pe.com.interbank.digitalchannel.ffoo.model.OptionalKeyParam;
import pe.com.interbank.digitalchannel.ffoo.model.PageableList;
import pe.com.interbank.digitalchannel.ffoo.model.Pagination;
import pe.com.interbank.digitalchannel.ffoo.service.exception.ServiceException;

/**
 * Created by Robert Espinoza on 30/11/2016.
 */
public interface FrequentOperationService {

    PageableList<FrequentOperation> findByFilter(FrequentOperationFilter filter, Pagination pagination) throws ServiceException;

    PageableList<FrequentOperationHistGroup> findFrequentOperationHist(Integer frequentOperationId, String clientCode, Pagination pagination) throws ServiceException;

    void delete(Integer id, String clientCode, Integer channelId) throws ServiceException;

    void save(FrequentOperation frequentOperation, Integer channelId, String user) throws ServiceException;

    void saveKeepingOptionalKey(FrequentOperation frequentOperation, Integer channelId, String user) throws ServiceException;

    FrequentOperation findById(Integer id, Integer channelId) throws ServiceException;

    void setOptionalKeyByClientCode(OptionalKeyParam optionalKeyParam, ChannelParam channelParam) throws ServiceException;
}
