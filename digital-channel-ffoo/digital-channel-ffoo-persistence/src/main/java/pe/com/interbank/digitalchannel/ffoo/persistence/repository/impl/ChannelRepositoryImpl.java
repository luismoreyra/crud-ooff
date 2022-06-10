package pe.com.interbank.digitalchannel.ffoo.persistence.repository.impl;

import oracle.jdbc.OracleTypes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.SqlOutParameter;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;
import org.springframework.stereotype.Repository;
import pe.com.interbank.digitalchannel.ffoo.model.Channel;
import pe.com.interbank.digitalchannel.ffoo.persistence.exception.RepositoryException;
import pe.com.interbank.digitalchannel.ffoo.persistence.repository.AbstractRepository;
import pe.com.interbank.digitalchannel.ffoo.persistence.repository.ChannelRepository;
import pe.com.interbank.digitalchannel.ffoo.persistence.util.DBConstant;

import javax.annotation.PostConstruct;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import static pe.com.interbank.digitalchannel.ffoo.persistence.util.PersistenceError.PERSISTENCE_ERROR;

/**
 * Created by Robert Espinoza on 06/12/2016.
 */
@Repository
public class ChannelRepositoryImpl extends AbstractRepository implements ChannelRepository {

    private static final Logger logger = LoggerFactory.getLogger(ChannelRepositoryImpl.class);

    private static final String SP_FIND_CHANNEL_BY_ID = "findChannelById";

    private static final String ID_PARAM = "p_id";

    @Value("${ffoo.application.dataSource.digitalChannel.schema}")
    private String schema;

    private SimpleJdbcCall findChannelByIdSPCall;

    @PostConstruct
    public void setUp() {
        findChannelByIdSPCall = new SimpleJdbcCall(jdbcTemplate).withProcedureName(SP_FIND_CHANNEL_BY_ID);
        findChannelByIdSPCall.withSchemaName(schema);
        findChannelByIdSPCall.withCatalogName(DBConstant.PKG_FREQUENT_OPERATION);
        findChannelByIdSPCall.declareParameters(new SqlOutParameter(RESULT_SET, OracleTypes.CURSOR, new ChannelRowMapper()));
        findChannelByIdSPCall.declareParameters(new SqlParameter(ID_PARAM, OracleTypes.NUMBER));
        findChannelByIdSPCall.withoutProcedureColumnMetaDataAccess();
    }

    @Override
    public Channel findById(Integer id) throws RepositoryException {
        try {
            MapSqlParameterSource params = new MapSqlParameterSource();
            params.addValue(ID_PARAM, id);

            Map<String, Object> response = findChannelByIdSPCall.execute(params);

            List<Channel> channels = (List<Channel>) response.get(RESULT_SET);

            if (channels.isEmpty()) {
                return null;
            } else {
                return channels.get(0);
            }
        } catch (DataAccessException e) {
            logger.error(PERSISTENCE_ERROR.getMessage(), e);
            throw new RepositoryException(PERSISTENCE_ERROR.getCode(), e.getMessage(), e);
        }
    }

    private class ChannelRowMapper implements RowMapper<Channel> {

        @Override
        public Channel mapRow(ResultSet resultSet, int i) throws SQLException {
            Channel channel = new Channel();

            channel.setId(resultSet.getInt(DBConstant.DIGITAL_CHANNEL_ID));
            channel.setAlias(resultSet.getString(DBConstant.DIGITAL_CHANNEL_ALIAS));
            channel.setDescription(resultSet.getString(DBConstant.DIGITAL_CHANNEL_DESCRIPTION));
            channel.setAuthUser(resultSet.getString(DBConstant.DIGITAL_CHANNEL_AUTH_USER));
            channel.setAuthPwd(resultSet.getString(DBConstant.DIGITAL_CHANNEL_AUTH_PWD));
            channel.setActive(ACTIVE.equals(resultSet.getString(DBConstant.DIGITAL_CHANNEL_FLAG_ACTIVE)));
            channel.setOptionalKeyBlocked(ACTIVE.equals(resultSet.getString(DBConstant.DIGITAL_CHANNEL_OPTIONAL_KEY_BLOCKED)));

            return channel;
        }
    }
}
