package pe.com.interbank.digitalchannel.ffoo.service;

import pe.com.interbank.digitalchannel.ffoo.model.ChannelParam;
import pe.com.interbank.digitalchannel.ffoo.model.Enrollment;
import pe.com.interbank.digitalchannel.ffoo.model.OptionalKeyParam;
import pe.com.interbank.digitalchannel.ffoo.service.exception.ServiceException;

/**
 * Created by Robert Espinoza on 04/07/2017.
 */
public interface EnrollmentService {

    void setOptionalKey(OptionalKeyParam optionalKeyParam, ChannelParam channelParam) throws ServiceException;

    void setAdCount(String clientCode, Integer adCount, Integer channelId) throws ServiceException;

    Enrollment findByClientCode(String clientCode) throws ServiceException;

    Enrollment findByClientCode(String clientCode, Integer channelId) throws ServiceException;
}
