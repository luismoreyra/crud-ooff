package pe.com.interbank.digitalchannel.ffoo.persistence.repository.impl;

import oracle.jdbc.OracleTypes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.SqlOutParameter;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;
import org.springframework.stereotype.Repository;
import pe.com.interbank.digitalchannel.ffoo.persistence.exception.RepositoryException;
import pe.com.interbank.digitalchannel.ffoo.persistence.repository.AbstractRepository;
import pe.com.interbank.digitalchannel.ffoo.persistence.repository.OperationRepository;
import pe.com.interbank.digitalchannel.ffoo.persistence.util.DBConstant;

import javax.annotation.PostConstruct;
import java.math.BigDecimal;

import static pe.com.interbank.digitalchannel.ffoo.persistence.util.PersistenceError.PERSISTENCE_ERROR;

/**
 * Created by Robert Espinoza on 26/12/2016.
 */
@Repository
public class OperationRepositoryImpl extends AbstractRepository implements OperationRepository {

    private static final Logger logger = LoggerFactory.getLogger(ChannelRepositoryImpl.class);

    private static final String SP_EXIST_BY_CHANNEL = "existOperationByChannel";

    private static final String OPERATION_ID_PARAM = "p_operation_id";
    private static final String CHANNEL_ID_PARAM = "p_digital_channel_id";

    @Value("${ffoo.application.dataSource.digitalChannel.schema}")
    private String schema;

    private SimpleJdbcCall existByChannelSPCall;

    @PostConstruct
    public void setUp() {
        existByChannelSPCall = new SimpleJdbcCall(jdbcTemplate).withProcedureName(SP_EXIST_BY_CHANNEL);
        existByChannelSPCall.withSchemaName(schema);
        existByChannelSPCall.withCatalogName(DBConstant.PKG_FREQUENT_OPERATION);
        existByChannelSPCall.declareParameters(new SqlParameter(OPERATION_ID_PARAM, OracleTypes.NUMBER));
        existByChannelSPCall.declareParameters(new SqlParameter(CHANNEL_ID_PARAM, OracleTypes.NUMBER));
        existByChannelSPCall.declareParameters(new SqlOutParameter(RESULT_OUTPUT, OracleTypes.NUMBER));
        existByChannelSPCall.withoutProcedureColumnMetaDataAccess();
    }

    @Override
    public boolean existByChannel(Integer operationId, Integer channelId) throws RepositoryException {
        try {
            MapSqlParameterSource params = new MapSqlParameterSource();
            params.addValue(OPERATION_ID_PARAM, operationId);
            params.addValue(CHANNEL_ID_PARAM, channelId);

            int result = existByChannelSPCall.executeObject(BigDecimal.class, params).intValue();

            return result == 1;
        } catch (DataAccessException e) {
            logger.error(PERSISTENCE_ERROR.getMessage(), e);
            throw new RepositoryException(PERSISTENCE_ERROR.getCode(), e.getMessage(), e);
        }
    }
}
