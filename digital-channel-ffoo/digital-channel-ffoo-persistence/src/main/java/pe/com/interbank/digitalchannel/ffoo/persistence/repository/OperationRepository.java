package pe.com.interbank.digitalchannel.ffoo.persistence.repository;

import pe.com.interbank.digitalchannel.ffoo.persistence.exception.RepositoryException;

/**
 * Created by Robert Espinoza on 26/12/2016.
 */
public interface OperationRepository {

    boolean existByChannel(Integer operationId, Integer channelId) throws RepositoryException;

}