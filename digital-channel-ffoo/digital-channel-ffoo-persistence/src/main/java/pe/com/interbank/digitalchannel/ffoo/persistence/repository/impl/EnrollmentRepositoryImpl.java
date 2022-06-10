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
import pe.com.interbank.digitalchannel.ffoo.model.Enrollment;
import pe.com.interbank.digitalchannel.ffoo.persistence.exception.RepositoryException;
import pe.com.interbank.digitalchannel.ffoo.persistence.repository.AbstractRepository;
import pe.com.interbank.digitalchannel.ffoo.persistence.repository.EnrollmentRepository;
import pe.com.interbank.digitalchannel.ffoo.persistence.util.DBConstant;

import javax.annotation.PostConstruct;
import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import static pe.com.interbank.digitalchannel.ffoo.persistence.util.PersistenceError.PERSISTENCE_ERROR;

/**
 * Created by Robert Espinoza on 05/07/2017.
 */
@Repository
public class EnrollmentRepositoryImpl extends AbstractRepository implements EnrollmentRepository {

    private static final Logger logger = LoggerFactory.getLogger(EnrollmentRepositoryImpl.class);

    private static final String SP_FIND_ENROLLMENT_BY_CLIENT_CODE = "findEnrollmentByClientCode";
    private static final String SP_INSERT_ENROLLMENT = "insertEnrollment";
    private static final String SP_UPDATE_ENROLLMENT = "updateEnrollment";

    private static final String CLIENT_CODE_PARAM = "p_client_code";
    private static final String OPTIONAL_KEY_PARAM = "p_optional_key";
    private static final String OPTIONAL_KEY_USED_PARAM = "p_optional_key_used";
    private static final String OPTIONAL_KEY_AD_COUNT_PARAM = "p_optional_key_ad_count";
    private static final String LAST_OPTIONAL_KEY_MOD_PARAM = "p_last_optional_key_mod";

    @Value("${ffoo.application.dataSource.digitalChannel.schema}")
    private String schema;

    private SimpleJdbcCall findEnrollmentByClientCodeSPCall;
    private SimpleJdbcCall insertEnrollmentSPCall;
    private SimpleJdbcCall updateEnrollmentSPCall;

    private void initFindEnrollmentByClientCodeSPCall() {
        findEnrollmentByClientCodeSPCall = new SimpleJdbcCall(jdbcTemplate).withProcedureName(SP_FIND_ENROLLMENT_BY_CLIENT_CODE);
        findEnrollmentByClientCodeSPCall.withSchemaName(schema);
        findEnrollmentByClientCodeSPCall.withCatalogName(DBConstant.PKG_FREQUENT_OPERATION);
        findEnrollmentByClientCodeSPCall.declareParameters(new SqlParameter(CLIENT_CODE_PARAM, OracleTypes.VARCHAR));
        findEnrollmentByClientCodeSPCall.declareParameters(new SqlOutParameter(RESULT_SET, OracleTypes.CURSOR, new EnrollmentRowMapper()));
        findEnrollmentByClientCodeSPCall.withoutProcedureColumnMetaDataAccess();
    }

    private void initInsertEnrollmentSPCall() {
        insertEnrollmentSPCall = new SimpleJdbcCall(jdbcTemplate).withProcedureName(SP_INSERT_ENROLLMENT);
        insertEnrollmentSPCall.withSchemaName(schema);
        insertEnrollmentSPCall.withCatalogName(DBConstant.PKG_FREQUENT_OPERATION);
        insertEnrollmentSPCall.declareParameters(new SqlParameter(ID_PARAM, OracleTypes.NUMBER));
        insertEnrollmentSPCall.declareParameters(new SqlParameter(CLIENT_CODE_PARAM, OracleTypes.VARCHAR));
        insertEnrollmentSPCall.declareParameters(new SqlParameter(OPTIONAL_KEY_PARAM, OracleTypes.VARCHAR));
        insertEnrollmentSPCall.declareParameters(new SqlParameter(OPTIONAL_KEY_USED_PARAM, OracleTypes.VARCHAR));
        insertEnrollmentSPCall.declareParameters(new SqlParameter(OPTIONAL_KEY_AD_COUNT_PARAM, OracleTypes.NUMBER));
        insertEnrollmentSPCall.declareParameters(new SqlParameter(LAST_OPTIONAL_KEY_MOD_PARAM, OracleTypes.TIMESTAMP));
        insertEnrollmentSPCall.declareParameters(new SqlOutParameter(RESULT_OUTPUT, OracleTypes.NUMBER));
    }

    private void initUpdateEnrollmentSPCall() {
        updateEnrollmentSPCall = new SimpleJdbcCall(jdbcTemplate).withProcedureName(SP_UPDATE_ENROLLMENT);
        updateEnrollmentSPCall.withSchemaName(schema);
        updateEnrollmentSPCall.withCatalogName(DBConstant.PKG_FREQUENT_OPERATION);
        updateEnrollmentSPCall.declareParameters(new SqlParameter(ID_PARAM, OracleTypes.NUMBER));
        updateEnrollmentSPCall.declareParameters(new SqlParameter(OPTIONAL_KEY_PARAM, OracleTypes.VARCHAR));
        updateEnrollmentSPCall.declareParameters(new SqlParameter(OPTIONAL_KEY_USED_PARAM, OracleTypes.VARCHAR));
        updateEnrollmentSPCall.declareParameters(new SqlParameter(OPTIONAL_KEY_AD_COUNT_PARAM, OracleTypes.NUMBER));
        updateEnrollmentSPCall.declareParameters(new SqlParameter(LAST_OPTIONAL_KEY_MOD_PARAM, OracleTypes.TIMESTAMP));
    }

    @PostConstruct
    public void setUp() {
        initFindEnrollmentByClientCodeSPCall();
        initInsertEnrollmentSPCall();
        initUpdateEnrollmentSPCall();
    }

    @Override
    public Enrollment findByClientCode(String clientCode) throws RepositoryException {
        try {
            MapSqlParameterSource params = new MapSqlParameterSource();
            params.addValue(CLIENT_CODE_PARAM, clientCode);

            Map<String, Object> response = findEnrollmentByClientCodeSPCall.execute(params);

            List<Enrollment> enrollments = (List<Enrollment>) response.get(RESULT_SET);

            if (enrollments.isEmpty()) {
                return null;
            } else {
                return enrollments.get(0);
            }
        } catch (DataAccessException e) {
            logger.error(PERSISTENCE_ERROR.getMessage(), e);
            throw new RepositoryException(PERSISTENCE_ERROR.getCode(), e.getMessage(), e);
        }
    }

    @Override
    public void create(Enrollment enrollment) throws RepositoryException {
        try {
            MapSqlParameterSource params = new MapSqlParameterSource();
            params.addValue(CLIENT_CODE_PARAM, enrollment.getClientCode());
            params.addValue(OPTIONAL_KEY_PARAM, enrollment.getOptionalKey() != null && enrollment.getOptionalKey() ? ACTIVE : INACTIVE);
            params.addValue(OPTIONAL_KEY_USED_PARAM, enrollment.getOptionalKeyUsed() != null && enrollment.getOptionalKeyUsed() ? ACTIVE : INACTIVE);
            params.addValue(OPTIONAL_KEY_AD_COUNT_PARAM, enrollment.getOptionalKeyAdCount());
            params.addValue(LAST_OPTIONAL_KEY_MOD_PARAM, enrollment.getLastOptionalKeyMod());

            Integer id = insertEnrollmentSPCall.executeObject(BigDecimal.class, params).intValue();
            enrollment.setId(id);
        } catch (DataAccessException e) {
            logger.error(PERSISTENCE_ERROR.getMessage(), e);
            throw new RepositoryException(PERSISTENCE_ERROR.getCode(), e.getMessage(), e);
        }
    }

    @Override
    public void update(Enrollment enrollment) throws RepositoryException {
        try {
            MapSqlParameterSource params = new MapSqlParameterSource();
            params.addValue(ID_PARAM, enrollment.getId());
            params.addValue(OPTIONAL_KEY_PARAM, enrollment.getOptionalKey() != null && enrollment.getOptionalKey() ? ACTIVE : INACTIVE);
            params.addValue(OPTIONAL_KEY_USED_PARAM, enrollment.getOptionalKeyUsed() != null && enrollment.getOptionalKeyUsed() ? ACTIVE : INACTIVE);
            params.addValue(OPTIONAL_KEY_AD_COUNT_PARAM, enrollment.getOptionalKeyAdCount());
            params.addValue(LAST_OPTIONAL_KEY_MOD_PARAM, enrollment.getLastOptionalKeyMod());

            updateEnrollmentSPCall.execute(params);
        } catch (DataAccessException e) {
            logger.error(PERSISTENCE_ERROR.getMessage(), e);
            throw new RepositoryException(PERSISTENCE_ERROR.getCode(), e.getMessage(), e);
        }
    }

    private class EnrollmentRowMapper implements RowMapper<Enrollment> {

        @Override
        public Enrollment mapRow(ResultSet resultSet, int i) throws SQLException {
            Enrollment enrollment = new Enrollment();

            enrollment.setId(resultSet.getInt(DBConstant.ENROLLMENT_ID));
            enrollment.setClientCode(resultSet.getString(DBConstant.ENROLLMENT_CLIENT_CODE));
            enrollment.setOptionalKey(ACTIVE.equals(resultSet.getString(DBConstant.ENROLLMENT_OPTIONAL_KEY)));
            enrollment.setOptionalKeyUsed(ACTIVE.equals(resultSet.getString(DBConstant.ENROLLMENT_OPTIONAL_KEY_USED)));
            enrollment.setOptionalKeyAdCount(resultSet.getInt(DBConstant.ENROLLMENT_OPTIONAL_KEY_AD_COUNT));
            enrollment.setLastOptionalKeyMod(resultSet.getTimestamp(DBConstant.ENROLLMENT_LAST_OPTIONAL_KEY_MOD));

            return enrollment;
        }
    }
}
