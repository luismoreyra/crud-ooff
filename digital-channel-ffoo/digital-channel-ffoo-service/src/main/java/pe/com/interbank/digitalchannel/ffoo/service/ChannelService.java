package pe.com.interbank.digitalchannel.ffoo.service;

import pe.com.interbank.digitalchannel.ffoo.model.Channel;
import pe.com.interbank.digitalchannel.ffoo.service.exception.ServiceException;

/**
 * Created by Robert Espinoza on 30/11/2016.
 */
public interface ChannelService {

    Channel findById(Integer id) throws ServiceException;

    void authenticateChannel(Integer channelId, String authUser, String authPwd) throws ServiceException;
}
