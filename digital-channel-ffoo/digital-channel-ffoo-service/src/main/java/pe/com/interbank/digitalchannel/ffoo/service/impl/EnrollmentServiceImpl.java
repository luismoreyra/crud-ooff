package pe.com.interbank.digitalchannel.ffoo.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pe.com.interbank.digitalchannel.ffoo.model.Channel;
import pe.com.interbank.digitalchannel.ffoo.model.ChannelParam;
import pe.com.interbank.digitalchannel.ffoo.model.Enrollment;
import pe.com.interbank.digitalchannel.ffoo.model.OptionalKeyParam;
import pe.com.interbank.digitalchannel.ffoo.persistence.exception.RepositoryException;
import pe.com.interbank.digitalchannel.ffoo.persistence.repository.EnrollmentRepository;
import pe.com.interbank.digitalchannel.ffoo.service.ChannelService;
import pe.com.interbank.digitalchannel.ffoo.service.EnrollmentService;
import pe.com.interbank.digitalchannel.ffoo.service.FrequentOperationService;
import pe.com.interbank.digitalchannel.ffoo.service.exception.ServiceException;

import java.util.Date;

import static pe.com.interbank.digitalchannel.ffoo.service.util.ServiceError.BLOCKED_OPTIONAL_KEY;
import static pe.com.interbank.digitalchannel.ffoo.service.util.ServiceError.UNHANDLED_ERROR;

/**
 * Created by Robert Espinoza on 04/07/2017.
 */
@Service
public class EnrollmentServiceImpl implements EnrollmentService {

    private final Logger logger = LoggerFactory.getLogger(EnrollmentServiceImpl.class);

    @Autowired
    private EnrollmentRepository enrollmentRepository;

    @Autowired
    private FrequentOperationService frequentOperationService;

    @Autowired
    private ChannelService channelService;

    @Transactional(rollbackFor = ServiceException.class)
    @Override
    public void setOptionalKey(OptionalKeyParam optionalKeyParam, ChannelParam channelParam) throws ServiceException {
        try {
            validateBlockedChannel(channelParam.getChannelId());

            Enrollment enrollment = findByClientCode(optionalKeyParam.getClientCode());

            if (enrollment != null) {
                if (!enrollment.getOptionalKey().equals(optionalKeyParam.getOptionalKey())) {
                    if (optionalKeyParam.getOptionalKey()) {
                        enrollment.setOptionalKeyUsed(true);
                    }

                    enrollment.setLastOptionalKeyMod(new Date());
                    enrollment.setOptionalKey(optionalKeyParam.getOptionalKey());

                    channelParam.setTransactionDate(enrollment.getLastOptionalKeyMod());

                    enrollmentRepository.update(enrollment);
                    frequentOperationService.setOptionalKeyByClientCode(optionalKeyParam, channelParam);
                }
            } else {
                Enrollment enrollmentToBeCreated = new Enrollment();
                enrollmentToBeCreated.setClientCode(optionalKeyParam.getClientCode());
                enrollmentToBeCreated.setOptionalKey(optionalKeyParam.getOptionalKey());
                enrollmentToBeCreated.setOptionalKeyUsed(optionalKeyParam.getOptionalKey());
                enrollmentToBeCreated.setOptionalKeyAdCount(0);
                enrollmentToBeCreated.setLastOptionalKeyMod(new Date());

                channelParam.setTransactionDate(enrollmentToBeCreated.getLastOptionalKeyMod());

                enrollmentRepository.create(enrollmentToBeCreated);
                frequentOperationService.setOptionalKeyByClientCode(optionalKeyParam, channelParam);
            }
        } catch (RepositoryException e) {
            logger.error(e.getMessage(), e);
            throw new ServiceException(e.getErrorCode(), e.getMessage(), e);
        }
    }

    @Override
    public void setAdCount(String clientCode, Integer adCount, Integer channelId) throws ServiceException {
        try {
            validateBlockedChannel(channelId);

            Enrollment enrollment = findByClientCode(clientCode);

            if (enrollment != null) {
                enrollment.setOptionalKeyAdCount(adCount);

                enrollmentRepository.update(enrollment);
            } else {
                Enrollment enrollmentToBeCreated = new Enrollment();
                enrollmentToBeCreated.setClientCode(clientCode);
                enrollmentToBeCreated.setOptionalKey(false);
                enrollmentToBeCreated.setOptionalKeyUsed(false);
                enrollmentToBeCreated.setOptionalKeyAdCount(adCount);

                enrollmentRepository.create(enrollmentToBeCreated);
            }
        } catch (RepositoryException e) {
            logger.error(e.getMessage(), e);
            throw new ServiceException(e.getErrorCode(), e.getMessage(), e);
        }
    }

    @Override
    public Enrollment findByClientCode(String clientCode) throws ServiceException {
        try {
            return enrollmentRepository.findByClientCode(clientCode);
        } catch (RepositoryException e) {
            logger.error(e.getMessage(), e);
            throw new ServiceException(e.getErrorCode(), e.getMessage(), e);
        }
    }

    @Override
    public Enrollment findByClientCode(String clientCode, Integer channelId) throws ServiceException {
        Enrollment enrollment = findByClientCode(clientCode);
        Channel channel = channelService.findById(channelId);

        if (enrollment != null) {
            enrollment.setOptionalKeyBlocked(channel.isOptionalKeyBlocked());
        } else {
            if (channel.isOptionalKeyBlocked()) {
                throw new ServiceException(BLOCKED_OPTIONAL_KEY);
            }
        }

        return enrollment;
    }

    private void validateBlockedChannel(Integer channelId) throws ServiceException {
        Channel channel = channelService.findById(channelId);

        if (channel != null) {
            if (channel.isOptionalKeyBlocked()) {
                throw new ServiceException(BLOCKED_OPTIONAL_KEY);
            }
        } else {
            throw new ServiceException(UNHANDLED_ERROR.getCode(), "Channel info not found");
        }
    }
}
