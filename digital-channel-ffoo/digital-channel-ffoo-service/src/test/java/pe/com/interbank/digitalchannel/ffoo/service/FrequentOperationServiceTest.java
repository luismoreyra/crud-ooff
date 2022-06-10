package pe.com.interbank.digitalchannel.ffoo.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.expression.spel.CodeFlow.ClinitAdder;

import pe.com.interbank.digitalchannel.ffoo.model.FrequentOperation;
import pe.com.interbank.digitalchannel.ffoo.model.FrequentOperationFilter;
import pe.com.interbank.digitalchannel.ffoo.model.PageableList;
import pe.com.interbank.digitalchannel.ffoo.model.Pagination;
import pe.com.interbank.digitalchannel.ffoo.persistence.exception.RepositoryException;
import pe.com.interbank.digitalchannel.ffoo.persistence.repository.FrequentOperationRepository;
import pe.com.interbank.digitalchannel.ffoo.service.exception.ServiceException;
import pe.com.interbank.digitalchannel.ffoo.service.impl.FrequentOperationServiceImpl;

import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

/**
 * Created by Robert Espinoza on 19/12/2016.
 */
@RunWith(MockitoJUnitRunner.class)
public class FrequentOperationServiceTest {

    @Mock
    private FrequentOperationRepository frequentOperationRepository;

    @InjectMocks
    private FrequentOperationServiceImpl frequentOperationService = new FrequentOperationServiceImpl();

    @Test
    public void testFindByFilter() throws ServiceException {
        FrequentOperationFilter filter = createDummyFindFilter();
        Pagination pagination = createDummyFindPagination();

        try {
            when(frequentOperationRepository.findByFilterSpecialOperation(filter, pagination)).thenReturn(new PageableList<FrequentOperation>());
        } catch (RepositoryException e) {
            throw new AssertionError(e);
        }

        PageableList<FrequentOperation> pageableList = frequentOperationService.findByFilter(filter, pagination);

        assertThat(pageableList, notNullValue());
    }

    @Test(expected = ServiceException.class)
    public void testFindByFilterWhenThereIsAnErrorInRepositoryThenThrowException() throws ServiceException {
        FrequentOperationFilter filter = new FrequentOperationFilter();
        Pagination pagination = new Pagination();

        try {
            doThrow(RepositoryException.class).when(frequentOperationRepository).findByFilterSpecialOperation(filter, pagination);
        } catch (RepositoryException e) {
            throw new AssertionError(e);
        }

        frequentOperationService.findByFilter(filter, pagination);
    }

    @Test
    public void testDelete() throws ServiceException {
        Integer frequentOperationId = 1;
        Integer channelId = 1;
        String clientCode = "0000000000";

        try {
            when(frequentOperationRepository.existByChannel(frequentOperationId, clientCode, channelId)).thenReturn(true);
            doNothing().when(frequentOperationRepository).delete(frequentOperationId);
        } catch (RepositoryException e) {
            throw new AssertionError(e);
        }

        frequentOperationService.delete(frequentOperationId, clientCode, channelId);
    }

    @Test(expected = ServiceException.class)
    public void testSendToDeleteWhenThereIsAnErrorInRepositoryThenThrowException() throws ServiceException {
        Integer frequentOperationId = 1;
        Integer channelId = 1;
        String clientCode = "0000000000";

        try {
            doThrow(RepositoryException.class).when(frequentOperationRepository).delete(frequentOperationId);
        } catch (RepositoryException e) {
            throw new AssertionError(e);
        }

        frequentOperationService.delete(frequentOperationId, clientCode, channelId);
    }

    private FrequentOperationFilter createDummyFindFilter() {
        FrequentOperationFilter filter = new FrequentOperationFilter();
        filter.setChannelId(1);
        filter.setClientCode("0060004372");
        filter.setCardTypeId(1);

        return filter;
    }

    private Pagination createDummyFindPagination() {
        Pagination pagination = new Pagination();
        pagination.setPage(1);
        pagination.setSize(10);

        return pagination;
    }

}
