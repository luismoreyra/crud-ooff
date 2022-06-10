package pe.com.interbank.digitalchannel.ffoo.service;

import org.hamcrest.core.IsNull;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import pe.com.interbank.digitalchannel.ffoo.model.Channel;
import pe.com.interbank.digitalchannel.ffoo.persistence.exception.RepositoryException;
import pe.com.interbank.digitalchannel.ffoo.persistence.repository.ChannelRepository;
import pe.com.interbank.digitalchannel.ffoo.service.exception.ServiceException;
import pe.com.interbank.digitalchannel.ffoo.service.impl.ChannelServiceImpl;

import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

/**
 * Created by Robert Espinoza on 19/12/2016.
 */
@RunWith(MockitoJUnitRunner.class)
public class ChannelServiceTest {

    @Mock
    private ChannelRepository channelRepository;

    @InjectMocks
    private ChannelServiceImpl channelService = new ChannelServiceImpl();

    @Test
    public void testProcessFindById() throws ServiceException {
        Integer idToBeSearched = 1;
        Channel channelFound = createDummyChannel(idToBeSearched);

        try {
            when(channelRepository.findById(idToBeSearched)).thenReturn(channelFound);
        } catch (RepositoryException e) {
            throw new AssertionError(e);
        }

        Channel channel = channelService.findById(idToBeSearched);

        assertThat(channel, IsNull.notNullValue());
    }

    @Test(expected = ServiceException.class)
    public void testProcessFindByIdWhenTransactionIsNotCompletedThenThrowException() throws ServiceException {
        Integer idToBeSearched = 1;

        try {
            doThrow(RepositoryException.class).when(channelRepository).findById(idToBeSearched);
        } catch (RepositoryException e) {
            throw new AssertionError(e);
        }

        channelService.findById(idToBeSearched);
    }

    @Test(expected = ServiceException.class)
    public void testAuthenticateChannelWhenChannelIsNotFoundThenThrowException() throws ServiceException {
        Integer channelId = 1;
        String authUser = "admin";
        String authPwd = "12345";

        try {
            when(channelRepository.findById(channelId)).thenReturn(null);
        } catch (RepositoryException e) {
            throw new AssertionError(e);
        }

        channelService.authenticateChannel(channelId, authUser, authPwd);
    }

    @Test(expected = ServiceException.class)
    public void testAuthenticateChannelWhenPasswordIsWrongThenThrowException() throws ServiceException {
        Integer channelId = 1;
        String authUser = "admin";
        String wrongAuthPwd = "123456";
        Channel channelFound = createDummyChannel(channelId);

        try {
            when(channelRepository.findById(channelId)).thenReturn(channelFound);
        } catch (RepositoryException e) {
            throw new AssertionError(e);
        }

        channelService.authenticateChannel(channelId, authUser, wrongAuthPwd);
    }

    @Test(expected = ServiceException.class)
    public void testAuthenticateChannelWhenChannelIsUnavailableThenThrowException() throws ServiceException {
        Integer channelId = 1;
        String authUser = "admin";
        String authPwd = "12345";
        Channel channelFound = createDummyChannel(channelId);
        channelFound.setActive(false);

        try {
            when(channelRepository.findById(channelId)).thenReturn(channelFound);
        } catch (RepositoryException e) {
            throw new AssertionError(e);
        }

        channelService.authenticateChannel(channelId, authUser, authPwd);
    }

    private Channel createDummyChannel(Integer id) {
        Channel channel = new Channel();
        channel.setId(id);
        channel.setAlias("Mobile");
        channel.setDescription("Mobile channel");
        channel.setAuthUser("admin");
        channel.setAuthPwd("12345");
        channel.setActive(true);

        return channel;
    }

}
