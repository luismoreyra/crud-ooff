package pe.com.interbank.digitalchannel.ffoo.persistence.repository;

import pe.com.interbank.digitalchannel.ffoo.model.Channel;
import pe.com.interbank.digitalchannel.ffoo.persistence.exception.RepositoryException;

/**
 * Created by Robert Espinoza on 29/11/2016.
 */
public interface ChannelRepository {

    Channel findById(Integer id) throws RepositoryException;

}
