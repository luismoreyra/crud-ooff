package pe.com.interbank.digitalchannel.ffoo.persistence.repository;

import pe.com.interbank.digitalchannel.ffoo.model.ChannelParam;
import pe.com.interbank.digitalchannel.ffoo.model.FrequentOperation;
import pe.com.interbank.digitalchannel.ffoo.model.FrequentOperationFilter;
import pe.com.interbank.digitalchannel.ffoo.model.FrequentOperationHist;
import pe.com.interbank.digitalchannel.ffoo.model.OptionalKeyParam;
import pe.com.interbank.digitalchannel.ffoo.model.PageableList;
import pe.com.interbank.digitalchannel.ffoo.model.Pagination;
import pe.com.interbank.digitalchannel.ffoo.persistence.exception.RepositoryException;

/**
 * Created by Robert Espinoza on 29/11/2016.
 */
public interface FrequentOperationRepository {

    PageableList<FrequentOperation> findByFilter(FrequentOperationFilter filter,
                                                 Pagination pagination) throws RepositoryException;

    PageableList<FrequentOperationHist> findFrequentOperationHist(Integer frequentOperationId, String clientCode, Pagination pagination)
            throws RepositoryException;

    void delete(Integer id) throws RepositoryException;

    void save(FrequentOperation frequentOperation, Integer channelId, String channelUser) throws RepositoryException;

    boolean existByChannel(Integer id, String clientCode, Integer channelId) throws RepositoryException;

    FrequentOperation findById(Integer id) throws RepositoryException;

    void setOptionalKeyByClientCode(OptionalKeyParam optionalKeyParam, ChannelParam channelParam) throws RepositoryException;

    PageableList<FrequentOperation> findByFilterSpecialOperation(FrequentOperationFilter filter,
            Pagination pagination) throws RepositoryException;

    void saveSpecialOperation(FrequentOperation frequentOperation, Integer channelId, String user) throws RepositoryException;
}
