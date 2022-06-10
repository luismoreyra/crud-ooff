package pe.com.interbank.digitalchannel.ffoo.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pe.com.interbank.digitalchannel.ffoo.model.Channel;
import pe.com.interbank.digitalchannel.ffoo.persistence.exception.RepositoryException;
import pe.com.interbank.digitalchannel.ffoo.persistence.repository.ChannelRepository;
import pe.com.interbank.digitalchannel.ffoo.service.ChannelService;
import pe.com.interbank.digitalchannel.ffoo.service.exception.ServiceException;

import static pe.com.interbank.digitalchannel.ffoo.service.util.ServiceError.INVALID_CREDENTIALS;
import static pe.com.interbank.digitalchannel.ffoo.service.util.ServiceError.NOT_AVAILABLE_CHANNEL;

/**
 * Created by Robert Espinoza on 06/12/2016.
 */
@Service
public class ChannelServiceImpl implements ChannelService {

    private static final Logger logger = LoggerFactory.getLogger(ChannelServiceImpl.class);

    @Autowired
    private ChannelRepository channelRepository;

    @Override
    public Channel findById(Integer id) throws ServiceException {
        try {
            return channelRepository.findById(id);
        } catch (RepositoryException e) {
            logger.error(e.getMessage(), e);
            throw new ServiceException(e.getErrorCode(), e.getMessage(), e);
        }
    }

    @Override
    public void authenticateChannel(Integer channelId, String authUser, String authPwd) throws ServiceException {
        Channel channel = findById(channelId);

        if (channel != null) {
            if (!channel.getAuthUser().equals(authUser) || !channel.getAuthPwd().equals(authPwd)) {
                logger.error(INVALID_CREDENTIALS.getMessage());
                throw new ServiceException(INVALID_CREDENTIALS);
            } else {
                if (!channel.isActive()) {
                    logger.error(NOT_AVAILABLE_CHANNEL.getMessage());
                    throw new ServiceException(NOT_AVAILABLE_CHANNEL);
                }
            }
        } else {
            logger.error(INVALID_CREDENTIALS.getMessage());
            throw new ServiceException(INVALID_CREDENTIALS);
        }
    }
}
