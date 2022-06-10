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

import pe.com.interbank.digitalchannel.ffoo.model.ChannelParam;
import pe.com.interbank.digitalchannel.ffoo.model.FrequentOperation;
import pe.com.interbank.digitalchannel.ffoo.model.FrequentOperationDetail;
import pe.com.interbank.digitalchannel.ffoo.model.FrequentOperationFilter;
import pe.com.interbank.digitalchannel.ffoo.model.FrequentOperationHist;
import pe.com.interbank.digitalchannel.ffoo.model.OptionalKeyParam;
import pe.com.interbank.digitalchannel.ffoo.model.PageableList;
import pe.com.interbank.digitalchannel.ffoo.model.Pagination;
import pe.com.interbank.digitalchannel.ffoo.persistence.exception.RepositoryException;
import pe.com.interbank.digitalchannel.ffoo.persistence.repository.AbstractRepository;
import pe.com.interbank.digitalchannel.ffoo.persistence.repository.FrequentOperationRepository;
import pe.com.interbank.digitalchannel.ffoo.persistence.util.DBConstant;

import javax.annotation.PostConstruct;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import static pe.com.interbank.digitalchannel.ffoo.persistence.util.PersistenceError.PERSISTENCE_ERROR;

/**
 * Created by Robert Espinoza on 05/12/2016.
 */
@Repository
public class FrequentOperationRepositoryImpl extends AbstractRepository implements FrequentOperationRepository {

    private static final Logger logger = LoggerFactory.getLogger(FrequentOperationRepositoryImpl.class);

    private static final String SP_FIND_FREQ_OPERATIONS = "findFreqOperations";
    private static final String SP_FIND_FREQ_OPERATIONS_SPECIAL = "findFreqOperationsV2";
    private static final String SP_FIND_FREQ_OPERATION_DETAIL = "findFreqOperationDetails";
    private static final String SP_FIND_FREQ_OPERATION_HIST_V2 = "findFreqOperationHistV2";
    private static final String SP_DELETE_FREQ_OPERATION = "deleteFreqOperationById";
    private static final String SP_SAVE_FREQ_OPERATION = "saveFreqOperation";
    private static final String SP_SAVE_FREQ_OPERATION_SPECIAL = "saveFreqOperationV2";
    private static final String SP_SAVE_FREQ_OPERATION_DETAIL = "saveFreqOperationDetail";
    private static final String SP_SAVE_FREQ_OPERATION_HIST = "saveFreqOperationHist";
    private static final String SP_DELETE_FREQ_OPERATION_DETAIL = "deleteFreqOperationDetails";
    private static final String SP_FIND_FREQ_OPERATION_BY_ID = "findFreqOperationById";
    private static final String SP_EXIST_FREQ_OPERATION_BY_CHANNEL_V2 = "existFreqOperationByChannelV2";
    private static final String SP_SET_OPTIONAL_KEY_BY_CLIENT_CODE = "setOptionalKeyByClientCode";

    private static final String FREQUENT_OPERATION_ID_PARAM = "p_frequent_operation_id";
    private static final String OPERATION_ID_PARAM = "p_operation_id";
    private static final String ALIAS_PARAM = "p_alias";
    private static final String CLIENT_CODE_PARAM = "p_client_code";
    private static final String FREQUENCY_PARAM = "p_frequency";
    private static final String REQUIRED_KEY_PARAM = "p_required_key";
    private static final String FROM_ACCOUNT_PARAM = "p_from_account";
    private static final String TO_IDENTIFIER_PARAM = "p_to_identifier";
    private static final String SOURCE_BANK_PARAM = "p_source_bank";
    private static final String SOURCE_PRODUCT_PARAM = "p_source_product";
    private static final String SOURCE_SUB_PRODUCT_PARAM = "p_source_sub_product";
    private static final String SOURCE_CURRENCY_PARAM = "p_source_currency";
    private static final String AMOUNT_PARAM = "p_amount";
    private static final String TRANSACTION_CURRENCY_PARAM = "p_transaction_currency";
    private static final String TARGET_CURRENCY_PARAM = "p_target_currency";
    private static final String TARGET_BANK_PARAM = "p_target_bank";
    private static final String TARGET_PRODUCT_PARAM = "p_target_product";
    private static final String TARGET_SUB_PRODUCT_PARAM = "p_target_sub_product";
    private static final String CATEGORY_CODE_PARAM = "p_category_code";
    private static final String COMPANY_CODE_PARAM = "p_company_code";
    private static final String SERVICE_CODE_PARAM = "p_service_code";
    private static final String DIGITAL_CONSTANCY_PARAM = "p_digital_constancy";
    private static final String OPERATION_NOT_EXECUTED_PARAM = "p_operation_not_executed";
    private static final String CHANNEL_ID_PARAM = "p_digital_channel_id";
    private static final String CHANNEL_USER_PARAM = "p_digital_channel_user";
    private static final String CARD_TYPE_ID_PARAM = "p_card_type_id";
    private static final String SPECIAL_OPERATION = "p_special_operation"; 

    @Value("${ffoo.application.dataSource.digitalChannel.schema}")
    private String schema;

    private SimpleJdbcCall findFreqOperationsSPCall;
    private SimpleJdbcCall findFreqOperationDetailSPCall;
    private SimpleJdbcCall findFreqOperationHistSPCallV2;
    private SimpleJdbcCall deleteFreqOperationSPCall;
    private SimpleJdbcCall deleteFreqOperationDetailSPCall;
    private SimpleJdbcCall saveFreqOperationSPCall;
    private SimpleJdbcCall saveFreqOperationDetailSPCall;
    private SimpleJdbcCall saveFreqOperationHistSPCall;
    private SimpleJdbcCall existFreqOperationByChannelSPCallV2;
    private SimpleJdbcCall findFreqOperationByIdSPCall;
    private SimpleJdbcCall setOptionalKeyByClientCodeSPCall;
    private SimpleJdbcCall findFreqOperationsSpecialSPCall;
    private SimpleJdbcCall saveFreqOperationSpecialSPCall;
    
    private void initFindFreqOperationsSPCall() {
        findFreqOperationsSPCall = new SimpleJdbcCall(jdbcTemplate).withProcedureName(SP_FIND_FREQ_OPERATIONS);
        findFreqOperationsSPCall.withSchemaName(schema);
        findFreqOperationsSPCall.withCatalogName(DBConstant.PKG_FREQUENT_OPERATION);
        findFreqOperationsSPCall.declareParameters(new SqlParameter(CLIENT_CODE_PARAM, OracleTypes.VARCHAR));
        findFreqOperationsSPCall.declareParameters(new SqlParameter(CHANNEL_ID_PARAM, OracleTypes.NUMBER));
        findFreqOperationsSPCall.declareParameters(new SqlParameter(CARD_TYPE_ID_PARAM, OracleTypes.NUMBER));
        findFreqOperationsSPCall.declareParameters(new SqlParameter(SIZE_PARAM, OracleTypes.NUMBER));
        findFreqOperationsSPCall.declareParameters(new SqlParameter(PAGE_PARAM, OracleTypes.NUMBER));
        findFreqOperationsSPCall.declareParameters(new SqlOutParameter(TOTAL_ELEMENTS_OUTPUT, OracleTypes.NUMBER));
        findFreqOperationsSPCall.declareParameters(new SqlOutParameter(TOTAL_PAGES_OUTPUT, OracleTypes.NUMBER));
        findFreqOperationsSPCall.declareParameters(new SqlOutParameter(RESULT_SET, OracleTypes.CURSOR, new FrequentOperationRowMapper()));
        findFreqOperationsSPCall.withoutProcedureColumnMetaDataAccess();
    }

    private void initFindFreqOperationsSpecialSPCall() {
    	findFreqOperationsSpecialSPCall = new SimpleJdbcCall(jdbcTemplate).withProcedureName(SP_FIND_FREQ_OPERATIONS_SPECIAL);
    	findFreqOperationsSpecialSPCall.withSchemaName(schema);
    	findFreqOperationsSpecialSPCall.withCatalogName(DBConstant.PKG_FREQUENT_OPERATION);
    	findFreqOperationsSpecialSPCall.declareParameters(new SqlParameter(CLIENT_CODE_PARAM, OracleTypes.VARCHAR));
    	findFreqOperationsSpecialSPCall.declareParameters(new SqlParameter(CHANNEL_ID_PARAM, OracleTypes.NUMBER));
    	findFreqOperationsSpecialSPCall.declareParameters(new SqlParameter(CARD_TYPE_ID_PARAM, OracleTypes.NUMBER));
        findFreqOperationsSpecialSPCall.declareParameters(new SqlParameter(SPECIAL_OPERATION, OracleTypes.VARCHAR));
        findFreqOperationsSpecialSPCall.declareParameters(new SqlParameter(SIZE_PARAM, OracleTypes.NUMBER));
        findFreqOperationsSpecialSPCall.declareParameters(new SqlParameter(PAGE_PARAM, OracleTypes.NUMBER));
        findFreqOperationsSpecialSPCall.declareParameters(new SqlOutParameter(TOTAL_ELEMENTS_OUTPUT, OracleTypes.NUMBER));
        findFreqOperationsSpecialSPCall.declareParameters(new SqlOutParameter(TOTAL_PAGES_OUTPUT, OracleTypes.NUMBER));
        findFreqOperationsSpecialSPCall.declareParameters(new SqlOutParameter(RESULT_SET, OracleTypes.CURSOR, new FrequentOperationRowMapper()));
        findFreqOperationsSpecialSPCall.withoutProcedureColumnMetaDataAccess();
    }
    
    private void initFindFreqOperationDetailSPCall() {
        findFreqOperationDetailSPCall = new SimpleJdbcCall(jdbcTemplate).withProcedureName(SP_FIND_FREQ_OPERATION_DETAIL);
        findFreqOperationDetailSPCall.withSchemaName(schema);
        findFreqOperationDetailSPCall.withCatalogName(DBConstant.PKG_FREQUENT_OPERATION);
        findFreqOperationDetailSPCall.declareParameters(new SqlOutParameter(RESULT_SET, OracleTypes.CURSOR, new FrequentOperationDetailRowMapper()));
        findFreqOperationDetailSPCall.declareParameters(new SqlParameter(FREQUENT_OPERATION_ID_PARAM, OracleTypes.NUMBER));
        findFreqOperationDetailSPCall.withoutProcedureColumnMetaDataAccess();
    }
    
    private void initFindFreqOperationHistSPCallV2() {
        findFreqOperationHistSPCallV2 = new SimpleJdbcCall(jdbcTemplate).withProcedureName(SP_FIND_FREQ_OPERATION_HIST_V2);
        findFreqOperationHistSPCallV2.withSchemaName(schema);
        findFreqOperationHistSPCallV2.withCatalogName(DBConstant.PKG_FREQUENT_OPERATION);
        findFreqOperationHistSPCallV2.declareParameters(new SqlParameter(FREQUENT_OPERATION_ID_PARAM, OracleTypes.NUMBER));
        findFreqOperationHistSPCallV2.declareParameters(new SqlParameter(CLIENT_CODE_PARAM, OracleTypes.VARCHAR));
        findFreqOperationHistSPCallV2.declareParameters(new SqlParameter(SIZE_PARAM, OracleTypes.NUMBER));
        findFreqOperationHistSPCallV2.declareParameters(new SqlParameter(PAGE_PARAM, OracleTypes.NUMBER));
        findFreqOperationHistSPCallV2.declareParameters(new SqlOutParameter(TOTAL_ELEMENTS_OUTPUT, OracleTypes.NUMBER));
        findFreqOperationHistSPCallV2.declareParameters(new SqlOutParameter(TOTAL_PAGES_OUTPUT, OracleTypes.NUMBER));
        findFreqOperationHistSPCallV2.declareParameters(new SqlOutParameter(RESULT_SET, OracleTypes.CURSOR, new FrequentOperationDetailHistRowMapper()));
        findFreqOperationHistSPCallV2.withoutProcedureColumnMetaDataAccess();
    }

    private void initDeleteFreqOperationSPCall() {
        deleteFreqOperationSPCall = new SimpleJdbcCall(jdbcTemplate).withProcedureName(SP_DELETE_FREQ_OPERATION);
        deleteFreqOperationSPCall.withSchemaName(schema);
        deleteFreqOperationSPCall.withCatalogName(DBConstant.PKG_FREQUENT_OPERATION);
        deleteFreqOperationSPCall.declareParameters(new SqlParameter(ID_PARAM, OracleTypes.NUMBER));
    }

    private void initDeleteFreqOperationDetailSPCall() {
        deleteFreqOperationDetailSPCall = new SimpleJdbcCall(jdbcTemplate).withProcedureName(SP_DELETE_FREQ_OPERATION_DETAIL);
        deleteFreqOperationDetailSPCall.withSchemaName(schema);
        deleteFreqOperationDetailSPCall.withCatalogName(DBConstant.PKG_FREQUENT_OPERATION);
        deleteFreqOperationDetailSPCall.declareParameters(new SqlParameter(FREQUENT_OPERATION_ID_PARAM, OracleTypes.NUMBER));
    }

    private void initSaveFreqOperationSPCall() {
        saveFreqOperationSPCall = new SimpleJdbcCall(jdbcTemplate).withProcedureName(SP_SAVE_FREQ_OPERATION);
        saveFreqOperationSPCall.withSchemaName(schema);
        saveFreqOperationSPCall.withCatalogName(DBConstant.PKG_FREQUENT_OPERATION);
        saveFreqOperationSPCall.declareParameters(new SqlParameter(ID_PARAM, OracleTypes.NUMBER));
        saveFreqOperationSPCall.declareParameters(new SqlParameter(OPERATION_ID_PARAM, OracleTypes.NUMBER));
        saveFreqOperationSPCall.declareParameters(new SqlParameter(ALIAS_PARAM, OracleTypes.VARCHAR));
        saveFreqOperationSPCall.declareParameters(new SqlParameter(CLIENT_CODE_PARAM, OracleTypes.VARCHAR));
        saveFreqOperationSPCall.declareParameters(new SqlParameter(FREQUENCY_PARAM, OracleTypes.NUMBER));
        saveFreqOperationSPCall.declareParameters(new SqlParameter(REQUIRED_KEY_PARAM, OracleTypes.VARCHAR));
        saveFreqOperationSPCall.declareParameters(new SqlParameter(CHANNEL_ID_PARAM, OracleTypes.NUMBER));
        saveFreqOperationSPCall.declareParameters(new SqlParameter(CHANNEL_USER_PARAM, OracleTypes.VARCHAR));
        saveFreqOperationSPCall.declareParameters(new SqlParameter(TRANSACTION_DATE_PARAM, OracleTypes.TIMESTAMP));
        saveFreqOperationSPCall.declareParameters(new SqlOutParameter(RESULT_OUTPUT, OracleTypes.NUMBER));
    }

    private void initSaveFreqOperationSpecialSPCall() {
        saveFreqOperationSpecialSPCall = new SimpleJdbcCall(jdbcTemplate).withProcedureName(SP_SAVE_FREQ_OPERATION_SPECIAL);
        saveFreqOperationSpecialSPCall.withSchemaName(schema);
        saveFreqOperationSpecialSPCall.withCatalogName(DBConstant.PKG_FREQUENT_OPERATION);
        saveFreqOperationSpecialSPCall.declareParameters(new SqlParameter(SPECIAL_OPERATION, OracleTypes.VARCHAR));
        saveFreqOperationSpecialSPCall.declareParameters(new SqlParameter(ID_PARAM, OracleTypes.NUMBER));
        saveFreqOperationSpecialSPCall.declareParameters(new SqlParameter(OPERATION_ID_PARAM, OracleTypes.NUMBER));
        saveFreqOperationSpecialSPCall.declareParameters(new SqlParameter(ALIAS_PARAM, OracleTypes.VARCHAR));
        saveFreqOperationSpecialSPCall.declareParameters(new SqlParameter(CLIENT_CODE_PARAM, OracleTypes.VARCHAR));
        saveFreqOperationSpecialSPCall.declareParameters(new SqlParameter(FREQUENCY_PARAM, OracleTypes.NUMBER));
        saveFreqOperationSpecialSPCall.declareParameters(new SqlParameter(REQUIRED_KEY_PARAM, OracleTypes.VARCHAR));
        saveFreqOperationSpecialSPCall.declareParameters(new SqlParameter(CHANNEL_ID_PARAM, OracleTypes.NUMBER));
        saveFreqOperationSpecialSPCall.declareParameters(new SqlParameter(CHANNEL_USER_PARAM, OracleTypes.VARCHAR));
        saveFreqOperationSpecialSPCall.declareParameters(new SqlParameter(TRANSACTION_DATE_PARAM, OracleTypes.TIMESTAMP));
        saveFreqOperationSpecialSPCall.declareParameters(new SqlOutParameter(RESULT_OUTPUT, OracleTypes.NUMBER));
    }

    private void initSaveFreqOperationDetailSPCall() {
        saveFreqOperationDetailSPCall = new SimpleJdbcCall(jdbcTemplate).withProcedureName(SP_SAVE_FREQ_OPERATION_DETAIL);
        saveFreqOperationDetailSPCall.withSchemaName(schema);
        saveFreqOperationDetailSPCall.withCatalogName(DBConstant.PKG_FREQUENT_OPERATION);
        saveFreqOperationDetailSPCall.declareParameters(new SqlParameter(FREQUENT_OPERATION_ID_PARAM, OracleTypes.NUMBER));
        saveFreqOperationDetailSPCall.declareParameters(new SqlParameter(FROM_ACCOUNT_PARAM, OracleTypes.VARCHAR));
        saveFreqOperationDetailSPCall.declareParameters(new SqlParameter(TO_IDENTIFIER_PARAM, OracleTypes.VARCHAR));
        saveFreqOperationDetailSPCall.declareParameters(new SqlParameter(SOURCE_BANK_PARAM, OracleTypes.VARCHAR));
        saveFreqOperationDetailSPCall.declareParameters(new SqlParameter(SOURCE_PRODUCT_PARAM, OracleTypes.VARCHAR));
        saveFreqOperationDetailSPCall.declareParameters(new SqlParameter(SOURCE_SUB_PRODUCT_PARAM, OracleTypes.VARCHAR));
        saveFreqOperationDetailSPCall.declareParameters(new SqlParameter(SOURCE_CURRENCY_PARAM, OracleTypes.VARCHAR));
        saveFreqOperationDetailSPCall.declareParameters(new SqlParameter(AMOUNT_PARAM, OracleTypes.NUMBER));
        saveFreqOperationDetailSPCall.declareParameters(new SqlParameter(TRANSACTION_CURRENCY_PARAM, OracleTypes.VARCHAR));
        saveFreqOperationDetailSPCall.declareParameters(new SqlParameter(TARGET_CURRENCY_PARAM, OracleTypes.VARCHAR));
        saveFreqOperationDetailSPCall.declareParameters(new SqlParameter(TARGET_BANK_PARAM, OracleTypes.VARCHAR));
        saveFreqOperationDetailSPCall.declareParameters(new SqlParameter(TARGET_PRODUCT_PARAM, OracleTypes.VARCHAR));
        saveFreqOperationDetailSPCall.declareParameters(new SqlParameter(TARGET_SUB_PRODUCT_PARAM, OracleTypes.VARCHAR));
        saveFreqOperationDetailSPCall.declareParameters(new SqlParameter(CATEGORY_CODE_PARAM, OracleTypes.VARCHAR));
        saveFreqOperationDetailSPCall.declareParameters(new SqlParameter(COMPANY_CODE_PARAM, OracleTypes.VARCHAR));
        saveFreqOperationDetailSPCall.declareParameters(new SqlParameter(SERVICE_CODE_PARAM, OracleTypes.VARCHAR));
        saveFreqOperationDetailSPCall.declareParameters(new SqlParameter(CHANNEL_USER_PARAM, OracleTypes.VARCHAR));
        saveFreqOperationDetailSPCall.declareParameters(new SqlParameter(TRANSACTION_DATE_PARAM, OracleTypes.TIMESTAMP));
    }

    private void initSaveFreqOperationHistSPCall() {
        saveFreqOperationHistSPCall = new SimpleJdbcCall(jdbcTemplate).withProcedureName(SP_SAVE_FREQ_OPERATION_HIST);
        saveFreqOperationHistSPCall.withSchemaName(schema);
        saveFreqOperationHistSPCall.withCatalogName(DBConstant.PKG_FREQUENT_OPERATION);
        saveFreqOperationHistSPCall.declareParameters(new SqlParameter(FREQUENT_OPERATION_ID_PARAM, OracleTypes.NUMBER));
        saveFreqOperationHistSPCall.declareParameters(new SqlParameter(OPERATION_ID_PARAM, OracleTypes.NUMBER));
        saveFreqOperationHistSPCall.declareParameters(new SqlParameter(ALIAS_PARAM, OracleTypes.VARCHAR));
        saveFreqOperationHistSPCall.declareParameters(new SqlParameter(CLIENT_CODE_PARAM, OracleTypes.VARCHAR));
        saveFreqOperationHistSPCall.declareParameters(new SqlParameter(REQUIRED_KEY_PARAM, OracleTypes.VARCHAR));
        saveFreqOperationHistSPCall.declareParameters(new SqlParameter(FROM_ACCOUNT_PARAM, OracleTypes.VARCHAR));
        saveFreqOperationHistSPCall.declareParameters(new SqlParameter(TO_IDENTIFIER_PARAM, OracleTypes.VARCHAR));
        saveFreqOperationHistSPCall.declareParameters(new SqlParameter(SOURCE_BANK_PARAM, OracleTypes.VARCHAR));
        saveFreqOperationHistSPCall.declareParameters(new SqlParameter(SOURCE_PRODUCT_PARAM, OracleTypes.VARCHAR));
        saveFreqOperationHistSPCall.declareParameters(new SqlParameter(SOURCE_SUB_PRODUCT_PARAM, OracleTypes.VARCHAR));
        saveFreqOperationHistSPCall.declareParameters(new SqlParameter(SOURCE_CURRENCY_PARAM, OracleTypes.VARCHAR));
        saveFreqOperationHistSPCall.declareParameters(new SqlParameter(AMOUNT_PARAM, OracleTypes.NUMBER));
        saveFreqOperationHistSPCall.declareParameters(new SqlParameter(TRANSACTION_CURRENCY_PARAM, OracleTypes.VARCHAR));
        saveFreqOperationHistSPCall.declareParameters(new SqlParameter(TARGET_CURRENCY_PARAM, OracleTypes.VARCHAR));
        saveFreqOperationHistSPCall.declareParameters(new SqlParameter(TARGET_BANK_PARAM, OracleTypes.VARCHAR));
        saveFreqOperationHistSPCall.declareParameters(new SqlParameter(TARGET_PRODUCT_PARAM, OracleTypes.VARCHAR));
        saveFreqOperationHistSPCall.declareParameters(new SqlParameter(TARGET_SUB_PRODUCT_PARAM, OracleTypes.VARCHAR));
        saveFreqOperationHistSPCall.declareParameters(new SqlParameter(CATEGORY_CODE_PARAM, OracleTypes.VARCHAR));
        saveFreqOperationHistSPCall.declareParameters(new SqlParameter(COMPANY_CODE_PARAM, OracleTypes.VARCHAR));
        saveFreqOperationHistSPCall.declareParameters(new SqlParameter(SERVICE_CODE_PARAM, OracleTypes.VARCHAR));
        saveFreqOperationHistSPCall.declareParameters(new SqlParameter(DIGITAL_CONSTANCY_PARAM, OracleTypes.VARCHAR));
        saveFreqOperationHistSPCall.declareParameters(new SqlParameter(OPERATION_NOT_EXECUTED_PARAM, OracleTypes.VARCHAR));
        saveFreqOperationHistSPCall.declareParameters(new SqlParameter(CHANNEL_ID_PARAM, OracleTypes.NUMBER));
        saveFreqOperationHistSPCall.declareParameters(new SqlParameter(CHANNEL_USER_PARAM, OracleTypes.VARCHAR));
        saveFreqOperationHistSPCall.declareParameters(new SqlParameter(TRANSACTION_DATE_PARAM, OracleTypes.TIMESTAMP));
    }
    
    private void initExistFreqOperationByChannelSPCallV2() {
        existFreqOperationByChannelSPCallV2 = new SimpleJdbcCall(jdbcTemplate).withProcedureName(SP_EXIST_FREQ_OPERATION_BY_CHANNEL_V2);
        existFreqOperationByChannelSPCallV2.withSchemaName(schema);
        existFreqOperationByChannelSPCallV2.withCatalogName(DBConstant.PKG_FREQUENT_OPERATION);
        existFreqOperationByChannelSPCallV2.declareParameters(new SqlParameter(ID_PARAM, OracleTypes.NUMBER));
        existFreqOperationByChannelSPCallV2.declareParameters(new SqlParameter(CLIENT_CODE_PARAM, OracleTypes.VARCHAR));
        existFreqOperationByChannelSPCallV2.declareParameters(new SqlParameter(CHANNEL_ID_PARAM, OracleTypes.NUMBER));
        existFreqOperationByChannelSPCallV2.declareParameters(new SqlOutParameter(RESULT_OUTPUT, OracleTypes.NUMBER));
    }

    private void initFindFreqOperationByIdSPCall() {
        findFreqOperationByIdSPCall = new SimpleJdbcCall(jdbcTemplate).withProcedureName(SP_FIND_FREQ_OPERATION_BY_ID);
        findFreqOperationByIdSPCall.withSchemaName(schema);
        findFreqOperationByIdSPCall.withCatalogName(DBConstant.PKG_FREQUENT_OPERATION);
        findFreqOperationByIdSPCall.declareParameters(new SqlParameter(ID_PARAM, OracleTypes.NUMBER));
        findFreqOperationByIdSPCall.declareParameters(new SqlOutParameter(RESULT_SET, OracleTypes.CURSOR, new FrequentOperationRowMapper()));
        findFreqOperationByIdSPCall.withoutProcedureColumnMetaDataAccess();
    }

    private void initSetOptionalKeyByClientCodeSPCall() {
        setOptionalKeyByClientCodeSPCall = new SimpleJdbcCall(jdbcTemplate).withProcedureName(SP_SET_OPTIONAL_KEY_BY_CLIENT_CODE);
        setOptionalKeyByClientCodeSPCall.withSchemaName(schema);
        setOptionalKeyByClientCodeSPCall.withCatalogName(DBConstant.PKG_FREQUENT_OPERATION);
        setOptionalKeyByClientCodeSPCall.declareParameters(new SqlParameter(CLIENT_CODE_PARAM, OracleTypes.VARCHAR));
        setOptionalKeyByClientCodeSPCall.declareParameters(new SqlParameter(REQUIRED_KEY_PARAM, OracleTypes.VARCHAR));
        setOptionalKeyByClientCodeSPCall.declareParameters(new SqlParameter(CHANNEL_ID_PARAM, OracleTypes.NUMBER));
        setOptionalKeyByClientCodeSPCall.declareParameters(new SqlParameter(CHANNEL_USER_PARAM, OracleTypes.VARCHAR));
        setOptionalKeyByClientCodeSPCall.declareParameters(new SqlParameter(TRANSACTION_DATE_PARAM, OracleTypes.TIMESTAMP));
    }

    @PostConstruct
    public void setUp() {
        initFindFreqOperationsSPCall();
        initFindFreqOperationDetailSPCall();
        initFindFreqOperationHistSPCallV2();
        initDeleteFreqOperationSPCall();
        initDeleteFreqOperationDetailSPCall();
        initSaveFreqOperationSPCall();
        initSaveFreqOperationDetailSPCall();
        initSaveFreqOperationHistSPCall();
        initExistFreqOperationByChannelSPCallV2();
        initFindFreqOperationByIdSPCall();
        initSetOptionalKeyByClientCodeSPCall();
        initFindFreqOperationsSpecialSPCall();
        initSaveFreqOperationSpecialSPCall();
    }

    @Override
    public PageableList<FrequentOperation> findByFilter(FrequentOperationFilter filter, Pagination pagination) throws RepositoryException {

        try {
            formatPagination(pagination);

            MapSqlParameterSource params = new MapSqlParameterSource();
            params.addValue(CLIENT_CODE_PARAM, filter.getClientCode());
            params.addValue(CHANNEL_ID_PARAM, filter.getChannelId());
            params.addValue(CARD_TYPE_ID_PARAM, filter.getCardTypeId());
            params.addValue(SIZE_PARAM, pagination.getSize());
            params.addValue(PAGE_PARAM, pagination.getPage());
            params.addValue(TOTAL_ELEMENTS_OUTPUT, 0);
            params.addValue(TOTAL_PAGES_OUTPUT, 0);

            Map<String, Object> response = findFreqOperationsSPCall.execute(params);

            List<FrequentOperation> frequentOperations = (List<FrequentOperation>) response.get(RESULT_SET);

            PageableList<FrequentOperation> pageableList = new PageableList<>();
            pageableList.setContent(frequentOperations);
            pageableList.setSize(pagination.getSize());
            pageableList.setPage(pagination.getPage());
            BigDecimal totalElements = (BigDecimal) response.get(TOTAL_ELEMENTS_OUTPUT);
            pageableList.setTotalElements(totalElements.intValue());
            BigDecimal totalPages = (BigDecimal) response.get(TOTAL_PAGES_OUTPUT);
            pageableList.setTotalPages(totalPages.intValue());

            return pageableList;
        } catch (DataAccessException e) {
            logger.error(PERSISTENCE_ERROR.getMessage(), e);
            throw new RepositoryException(PERSISTENCE_ERROR.getCode(), e.getMessage(), e);
        }
    }

    @Override
    public void delete(Integer id) throws RepositoryException {
        try {
            MapSqlParameterSource params = new MapSqlParameterSource();
            params.addValue(ID_PARAM, id);

            deleteFreqOperationSPCall.execute(params);
        } catch (DataAccessException e) {
            logger.error(PERSISTENCE_ERROR.getMessage(), e);
            throw new RepositoryException(PERSISTENCE_ERROR.getCode(), e.getMessage(), e);
        }
    }

    @Override
    public void save(FrequentOperation frequentOperation, Integer channelId, String user) throws RepositoryException {
        try {
        	
            Date transactionDate = new Date();

            MapSqlParameterSource params = new MapSqlParameterSource();
            params.addValue(SPECIAL_OPERATION, frequentOperation.isSpecialOperation() ? ACTIVE : INACTIVE);
            params.addValue(ID_PARAM, frequentOperation.getId());
            params.addValue(OPERATION_ID_PARAM, frequentOperation.getOperationId());
            params.addValue(ALIAS_PARAM, frequentOperation.getAlias());
            params.addValue(CLIENT_CODE_PARAM, frequentOperation.getClientCode());
            params.addValue(FREQUENCY_PARAM, frequentOperation.getFrequency());
            params.addValue(REQUIRED_KEY_PARAM, frequentOperation.isOptionalKey() ? ACTIVE : INACTIVE);
            params.addValue(CHANNEL_ID_PARAM, channelId);
            params.addValue(CHANNEL_USER_PARAM, user);
            params.addValue(TRANSACTION_DATE_PARAM, transactionDate);

            Integer frequentOperationId = saveFreqOperationSPCall.executeObject(BigDecimal.class, params).intValue();
            frequentOperation.setId(frequentOperationId);

            deleteFrequentOperationDetail(frequentOperationId);

            if (frequentOperation.getDetails() != null) {
                for (FrequentOperationDetail frequentOperationDetail : frequentOperation.getDetails()) {
                    frequentOperationDetail.setFrequentOperationId(frequentOperationId);

                    saveFrequentOperationDetail(frequentOperationDetail, transactionDate, user);
                }

                for (FrequentOperationHist frequentOperationHist : generateHistoric(frequentOperation)) {
                    saveFrequentOperationHist(frequentOperationHist, transactionDate, channelId, user);
                }
            }
        } catch (DataAccessException e) {
            logger.error(PERSISTENCE_ERROR.getMessage(), e);
            throw new RepositoryException(PERSISTENCE_ERROR.getCode(), e.getMessage(), e);
        }
    }

    @Override
    public boolean existByChannel(Integer id, String clientCode, Integer channelId) throws RepositoryException {
        try {
            MapSqlParameterSource params = new MapSqlParameterSource();
            params.addValue(ID_PARAM, id);
            params.addValue(CLIENT_CODE_PARAM, clientCode);
            params.addValue(CHANNEL_ID_PARAM, channelId);

            int result = existFreqOperationByChannelSPCallV2.executeObject(BigDecimal.class, params).intValue();

            return result == 1;
        } catch (DataAccessException e) {
            logger.error(PERSISTENCE_ERROR.getMessage(), e);
            throw new RepositoryException(PERSISTENCE_ERROR.getCode(), e.getMessage(), e);
        }
    }

    @Override
    public FrequentOperation findById(Integer id) throws RepositoryException {
        try {
            MapSqlParameterSource params = new MapSqlParameterSource();
            params.addValue(ID_PARAM, id);

            Map<String, Object> response = findFreqOperationByIdSPCall.execute(params);

            List<FrequentOperation> frequentOperations = (List<FrequentOperation>) response.get(RESULT_SET);

            if (frequentOperations.isEmpty()) {
                return null;
            } else {
                return frequentOperations.get(0);
            }
        } catch (DataAccessException e) {
            logger.error(PERSISTENCE_ERROR.getMessage(), e);
            throw new RepositoryException(PERSISTENCE_ERROR.getCode(), e.getMessage(), e);
        }
    }

    @Override
    public void setOptionalKeyByClientCode(OptionalKeyParam optionalKeyParam, ChannelParam channelParam) throws RepositoryException {
        try {
            MapSqlParameterSource params = new MapSqlParameterSource();
            params.addValue(CLIENT_CODE_PARAM, optionalKeyParam.getClientCode());
            params.addValue(REQUIRED_KEY_PARAM, optionalKeyParam.getOptionalKey() ? ACTIVE : INACTIVE);
            params.addValue(CHANNEL_ID_PARAM, channelParam.getChannelId());
            params.addValue(CHANNEL_USER_PARAM, channelParam.getUser());
            params.addValue(TRANSACTION_DATE_PARAM, channelParam.getTransactionDate());

            setOptionalKeyByClientCodeSPCall.execute(params);
        } catch (DataAccessException e) {
            logger.error(PERSISTENCE_ERROR.getMessage(), e);
            throw new RepositoryException(PERSISTENCE_ERROR.getCode(), e.getMessage(), e);
        }
    }

    public void deleteFrequentOperationDetail(Integer frequentOperationId) throws RepositoryException {
        try {
            MapSqlParameterSource params = new MapSqlParameterSource();
            params.addValue(FREQUENT_OPERATION_ID_PARAM, frequentOperationId);

            deleteFreqOperationDetailSPCall.execute(params);
        } catch (DataAccessException e) {
            logger.error(PERSISTENCE_ERROR.getMessage(), e);
            throw new RepositoryException(PERSISTENCE_ERROR.getCode(), e.getMessage(), e);
        }
    }
    
   private void saveFrequentOperationDetail(FrequentOperationDetail frequentOperationDetail, Date transactionDate, String user) throws RepositoryException {
        try {
            MapSqlParameterSource params = new MapSqlParameterSource();
            params.addValue(FREQUENT_OPERATION_ID_PARAM, frequentOperationDetail.getFrequentOperationId());
            params.addValue(FROM_ACCOUNT_PARAM, frequentOperationDetail.getFromAccount());
            params.addValue(TO_IDENTIFIER_PARAM, frequentOperationDetail.getToIdentifier());
            params.addValue(SOURCE_BANK_PARAM, frequentOperationDetail.getSourceBank());
            params.addValue(SOURCE_PRODUCT_PARAM, frequentOperationDetail.getSourceProduct());
            params.addValue(SOURCE_SUB_PRODUCT_PARAM, frequentOperationDetail.getSourceSubProduct());
            params.addValue(SOURCE_CURRENCY_PARAM, frequentOperationDetail.getSourceCurrency());
            params.addValue(AMOUNT_PARAM, frequentOperationDetail.getAmount());
            params.addValue(TRANSACTION_CURRENCY_PARAM, frequentOperationDetail.getTransactionCurrency());
            params.addValue(TARGET_CURRENCY_PARAM, frequentOperationDetail.getTargetCurrency());
            params.addValue(TARGET_BANK_PARAM, frequentOperationDetail.getTargetBank());
            params.addValue(TARGET_PRODUCT_PARAM, frequentOperationDetail.getTargetProduct());
            params.addValue(TARGET_SUB_PRODUCT_PARAM, frequentOperationDetail.getTargetSubProduct());
            params.addValue(CATEGORY_CODE_PARAM, frequentOperationDetail.getCategoryCode());
            params.addValue(COMPANY_CODE_PARAM, frequentOperationDetail.getCompanyCode());
            params.addValue(SERVICE_CODE_PARAM, frequentOperationDetail.getServiceCode());
            params.addValue(CHANNEL_USER_PARAM, user);
            params.addValue(TRANSACTION_DATE_PARAM, transactionDate);

            saveFreqOperationDetailSPCall.execute(params);
        } catch (DataAccessException e) {
            logger.error(PERSISTENCE_ERROR.getMessage(), e);
            throw new RepositoryException(PERSISTENCE_ERROR.getCode(), e.getMessage(), e);
        }
    }

    private void saveFrequentOperationHist(FrequentOperationHist frequentOperationHist, Date transactionDate, Integer channelId, String user)
            throws RepositoryException {
        try {
            MapSqlParameterSource params = new MapSqlParameterSource();
            params.addValue(FREQUENT_OPERATION_ID_PARAM, frequentOperationHist.getFrequentOperationId());
            params.addValue(OPERATION_ID_PARAM, frequentOperationHist.getOperationId());
            params.addValue(ALIAS_PARAM, frequentOperationHist.getAlias());
            params.addValue(CLIENT_CODE_PARAM, frequentOperationHist.getClientCode());
            params.addValue(REQUIRED_KEY_PARAM, frequentOperationHist.isRequiredKey() ? ACTIVE : INACTIVE);
            params.addValue(FROM_ACCOUNT_PARAM, frequentOperationHist.getFromAccount());
            params.addValue(TO_IDENTIFIER_PARAM, frequentOperationHist.getToIdentifier());
            params.addValue(SOURCE_BANK_PARAM, frequentOperationHist.getSourceBank());
            params.addValue(SOURCE_PRODUCT_PARAM, frequentOperationHist.getSourceProduct());
            params.addValue(SOURCE_SUB_PRODUCT_PARAM, frequentOperationHist.getSourceSubProduct());
            params.addValue(SOURCE_CURRENCY_PARAM, frequentOperationHist.getSourceCurrency());
            params.addValue(AMOUNT_PARAM, frequentOperationHist.getAmount());
            params.addValue(TRANSACTION_CURRENCY_PARAM, frequentOperationHist.getTransactionCurrency());
            params.addValue(TARGET_CURRENCY_PARAM, frequentOperationHist.getTargetCurrency());
            params.addValue(TARGET_BANK_PARAM, frequentOperationHist.getTargetBank());
            params.addValue(TARGET_PRODUCT_PARAM, frequentOperationHist.getTargetProduct());
            params.addValue(TARGET_SUB_PRODUCT_PARAM, frequentOperationHist.getTargetSubProduct());
            params.addValue(CATEGORY_CODE_PARAM, frequentOperationHist.getCategoryCode());
            params.addValue(COMPANY_CODE_PARAM, frequentOperationHist.getCompanyCode());
            params.addValue(SERVICE_CODE_PARAM, frequentOperationHist.getServiceCode());
            params.addValue(DIGITAL_CONSTANCY_PARAM, frequentOperationHist.getDigitalConstancy());
            params.addValue(OPERATION_NOT_EXECUTED_PARAM, frequentOperationHist.isOperationNotExecuted() ? ACTIVE : INACTIVE);
            params.addValue(CHANNEL_ID_PARAM, channelId);
            params.addValue(CHANNEL_USER_PARAM, user);
            params.addValue(TRANSACTION_DATE_PARAM, transactionDate);

            saveFreqOperationHistSPCall.execute(params);
        } catch (DataAccessException e) {
            logger.error(PERSISTENCE_ERROR.getMessage(), e);
            throw new RepositoryException(PERSISTENCE_ERROR.getCode(), e.getMessage(), e);
        }
    }

    private List<FrequentOperationHist> generateHistoric(FrequentOperation frequentOperation) {
        List<FrequentOperationHist> frequentOperationHistList = new ArrayList<>();

        FrequentOperationHist frequentOperationHist;

        for (FrequentOperationDetail frequentOperationDetail : frequentOperation.getDetails()) {
            frequentOperationHist = new FrequentOperationHist();
            frequentOperationHist.setFrequentOperationId(frequentOperation.getId());
            frequentOperationHist.setOperationId(frequentOperation.getOperationId());
            frequentOperationHist.setAlias(frequentOperation.getAlias());
            frequentOperationHist.setClientCode(frequentOperation.getClientCode());
            frequentOperationHist.setRequiredKey(frequentOperation.isOptionalKey());
            frequentOperationHist.setFromAccount(frequentOperationDetail.getFromAccount());
            frequentOperationHist.setToIdentifier(frequentOperationDetail.getToIdentifier());
            frequentOperationHist.setSourceBank(frequentOperationDetail.getSourceBank());
            frequentOperationHist.setSourceProduct(frequentOperationDetail.getSourceProduct());
            frequentOperationHist.setSourceSubProduct(frequentOperationDetail.getSourceSubProduct());
            frequentOperationHist.setSourceCurrency(frequentOperationDetail.getSourceCurrency());
            frequentOperationHist.setAmount(frequentOperationDetail.getAmount());
            frequentOperationHist.setTransactionCurrency(frequentOperationDetail.getTransactionCurrency());
            frequentOperationHist.setTargetCurrency(frequentOperationDetail.getTargetCurrency());
            frequentOperationHist.setTargetBank(frequentOperationDetail.getTargetBank());
            frequentOperationHist.setTargetProduct(frequentOperationDetail.getTargetProduct());
            frequentOperationHist.setTargetSubProduct(frequentOperationDetail.getTargetSubProduct());
            frequentOperationHist.setCategoryCode(frequentOperationDetail.getCategoryCode());
            frequentOperationHist.setCompanyCode(frequentOperationDetail.getCompanyCode());
            frequentOperationHist.setServiceCode(frequentOperationDetail.getServiceCode());
            frequentOperationHist.setDigitalConstancy(frequentOperation.getDigitalConstancy());
            frequentOperationHist.setOperationNotExecuted(frequentOperation.isOperationNotExecuted());

            frequentOperationHistList.add(frequentOperationHist);
        }

        return frequentOperationHistList;
    }

    private List<FrequentOperationDetail> findFrequentOperationDetails(Integer frequentOperationId) throws RepositoryException {
        try {
            MapSqlParameterSource params = new MapSqlParameterSource();
            params.addValue(FREQUENT_OPERATION_ID_PARAM, frequentOperationId);

            Map<String, Object> response = findFreqOperationDetailSPCall.execute(params);

            return (List<FrequentOperationDetail>) response.get(RESULT_SET);
        } catch (DataAccessException e) {
            logger.error(PERSISTENCE_ERROR.getMessage(), e);
            throw new RepositoryException(PERSISTENCE_ERROR.getCode(), e.getMessage(), e);
        }

    }

    public PageableList<FrequentOperationHist> findFrequentOperationHist(Integer frequentOperationId, String clientCode, Pagination pagination)
            throws RepositoryException {
        try {
            formatPagination(pagination);

            MapSqlParameterSource params = new MapSqlParameterSource();
            params.addValue(FREQUENT_OPERATION_ID_PARAM, frequentOperationId);
            params.addValue(CLIENT_CODE_PARAM, clientCode);
            params.addValue(SIZE_PARAM, pagination.getSize());
            params.addValue(PAGE_PARAM, pagination.getPage());
            params.addValue(TOTAL_ELEMENTS_OUTPUT, 0);
            params.addValue(TOTAL_PAGES_OUTPUT, 0);

            Map<String, Object> response = findFreqOperationHistSPCallV2.execute(params);

            List<FrequentOperationHist> frequentOperationHists = (List<FrequentOperationHist>) response.get(RESULT_SET);

            PageableList<FrequentOperationHist> pageableList = new PageableList<>();
            pageableList.setContent(frequentOperationHists);
            pageableList.setSize(pagination.getSize());
            pageableList.setPage(pagination.getPage());
            BigDecimal totalElements = (BigDecimal) response.get(TOTAL_ELEMENTS_OUTPUT);
            pageableList.setTotalElements(totalElements.intValue());
            BigDecimal totalPages = (BigDecimal) response.get(TOTAL_PAGES_OUTPUT);
            pageableList.setTotalPages(totalPages.intValue());

            return pageableList;
        } catch (DataAccessException e) {
            logger.error(PERSISTENCE_ERROR.getMessage(), e);
            throw new RepositoryException(PERSISTENCE_ERROR.getCode(), e.getMessage(), e);
        }
    }

    private class FrequentOperationRowMapper implements RowMapper<FrequentOperation> {

        @Override
        public FrequentOperation mapRow(ResultSet resultSet, int i) throws SQLException {
            FrequentOperation frequentOperation = new FrequentOperation();
            
            frequentOperation.setId(resultSet.getInt(DBConstant.FREQUENT_OPERATION_COLUMN_ID));
            frequentOperation.setAlias(resultSet.getString(DBConstant.FREQUENT_OPERATION_COLUMN_ALIAS));
            frequentOperation.setClientCode(resultSet.getString(DBConstant.FREQUENT_OPERATION_COLUMN_CLIENT_CODE));
            frequentOperation.setFrequency(resultSet.getInt(DBConstant.FREQUENT_OPERATION_COLUMN_FREQUENCY));
            frequentOperation.setOperationId(resultSet.getInt(DBConstant.FREQUENT_OPERATION_COLUMN_OPERATION_ID));
            frequentOperation.setOperationCode(resultSet.getString(DBConstant.OPERATION_COLUMN_OPERATION_CODE));
            frequentOperation.setModificationDate(formatDate(resultSet.getDate(DBConstant.FREQUENT_OPERATION_COLUMN_MODIFICATION_DATE))); //format.parse(f)); 
        	frequentOperation.setSpecialOperation(resultSet.getString(DBConstant.OPERATION_COLUMN_SPECIAL_OPERATION).equals(ACTIVE) ? true : false );
            frequentOperation.setOptionalKey(ACTIVE.equals(resultSet.getString(DBConstant.FREQUENT_OPERATION_COLUMN_REQUIRED_KEY)));

            try {
            	frequentOperation.setDetails(findFrequentOperationDetails(frequentOperation.getId()));

            } catch (RepositoryException  e) {
                throw new SQLException(e);
            }

            return frequentOperation;
        }
    }

    private class FrequentOperationDetailRowMapper implements RowMapper<FrequentOperationDetail> {

        @Override
        public FrequentOperationDetail mapRow(ResultSet resultSet, int i) throws SQLException {
            FrequentOperationDetail frequentOperationDetail = new FrequentOperationDetail();
            
            frequentOperationDetail.setId(resultSet.getInt(DBConstant.FREQUENT_OPERATION_DETAIL_COLUMN_ID));
            frequentOperationDetail.setFrequentOperationId(resultSet.getInt(DBConstant.FREQUENT_OPERATION_DETAIL_COLUMN_FREQUENT_OPERATION_ID));
            frequentOperationDetail.setFromAccount(resultSet.getString(DBConstant.FREQUENT_OPERATION_DETAIL_COLUMN_FROM_ACCOUNT));
            frequentOperationDetail.setToIdentifier(resultSet.getString(DBConstant.FREQUENT_OPERATION_DETAIL_COLUMN_TO_IDENTIFIER));
            frequentOperationDetail.setSourceBank(resultSet.getString(DBConstant.FREQUENT_OPERATION_DETAIL_COLUMN_SOURCE_BANK));
            frequentOperationDetail.setSourceCurrency(resultSet.getString(DBConstant.FREQUENT_OPERATION_DETAIL_COLUMN_SOURCE_CURRENCY));
            frequentOperationDetail.setSourceProduct(resultSet.getString(DBConstant.FREQUENT_OPERATION_DETAIL_COLUMN_SOURCE_PRODUCT));
            frequentOperationDetail.setSourceSubProduct(resultSet.getString(DBConstant.FREQUENT_OPERATION_DETAIL_COLUMN_SOURCE_SUB_PRODUCT));
            frequentOperationDetail.setAmount(resultSet.getDouble(DBConstant.FREQUENT_OPERATION_DETAIL_COLUMN_AMOUNT));
            frequentOperationDetail.setTransactionCurrency(resultSet.getString(DBConstant.FREQUENT_OPERATION_DETAIL_COLUMN_TRANSACTION_CURRENCY));
            frequentOperationDetail.setTargetBank(resultSet.getString(DBConstant.FREQUENT_OPERATION_DETAIL_COLUMN_TARGET_BANK));
            frequentOperationDetail.setTargetCurrency(resultSet.getString(DBConstant.FREQUENT_OPERATION_DETAIL_COLUMN_TARGET_CURRENCY));
            frequentOperationDetail.setTargetProduct(resultSet.getString(DBConstant.FREQUENT_OPERATION_DETAIL_COLUMN_TARGET_PRODUCT));
            frequentOperationDetail.setTargetSubProduct(resultSet.getString(DBConstant.FREQUENT_OPERATION_DETAIL_COLUMN_TARGET_SUB_PRODUCT));
            frequentOperationDetail.setCategoryCode(resultSet.getString(DBConstant.FREQUENT_OPERATION_DETAIL_COLUMN_CATEGORY_CODE));
            frequentOperationDetail.setCompanyCode(resultSet.getString(DBConstant.FREQUENT_OPERATION_DETAIL_COLUMN_COMPANY_CODE));
            frequentOperationDetail.setServiceCode(resultSet.getString(DBConstant.FREQUENT_OPERATION_DETAIL_COLUMN_SERVICE_CODE));
           
            return frequentOperationDetail;
        }
    }

    private class FrequentOperationDetailHistRowMapper implements RowMapper<FrequentOperationHist> {

        @Override
        public FrequentOperationHist mapRow(ResultSet resultSet, int i) throws SQLException {
            FrequentOperationHist frequentOperationHist = new FrequentOperationHist();

            frequentOperationHist.setId(resultSet.getInt(DBConstant.FREQUENT_OPERATION_HIST_COLUMN_ID));
            frequentOperationHist.setFrequentOperationId(resultSet.getInt(DBConstant.FREQUENT_OPERATION_HIST_COLUMN_FREQUENT_OPERATION_ID));
            frequentOperationHist.setOperationId(resultSet.getInt(DBConstant.FREQUENT_OPERATION_HIST_COLUMN_OPERATION_ID));
            frequentOperationHist.setAlias(resultSet.getString(DBConstant.FREQUENT_OPERATION_HIST_COLUMN_ALIAS));
            frequentOperationHist.setClientCode(resultSet.getString(DBConstant.FREQUENT_OPERATION_HIST_COLUMN_CLIENT_CODE));
            frequentOperationHist.setAmount(resultSet.getDouble(DBConstant.FREQUENT_OPERATION_HIST_COLUMN_AMOUNT));
            frequentOperationHist.setTargetBank(resultSet.getString(DBConstant.FREQUENT_OPERATION_HIST_COLUMN_TARGET_BANK));
            frequentOperationHist.setTargetProduct(resultSet.getString(DBConstant.FREQUENT_OPERATION_HIST_COLUMN_TARGET_PRODUCT));
            frequentOperationHist.setTargetSubProduct(resultSet.getString(DBConstant.FREQUENT_OPERATION_HIST_COLUMN_TARGET_SUB_PRODUCT));
            frequentOperationHist.setTargetCurrency(resultSet.getString(DBConstant.FREQUENT_OPERATION_HIST_COLUMN_TARGET_CURRENCY));
            frequentOperationHist.setTransactionCurrency(resultSet.getString(DBConstant.FREQUENT_OPERATION_HIST_COLUMN_TRANSACTION_CURRENCY));
            frequentOperationHist.setSourceBank(resultSet.getString(DBConstant.FREQUENT_OPERATION_HIST_COLUMN_SOURCE_BANK));
            frequentOperationHist.setSourceProduct(resultSet.getString(DBConstant.FREQUENT_OPERATION_HIST_COLUMN_SOURCE_PRODUCT));
            frequentOperationHist.setSourceSubProduct(resultSet.getString(DBConstant.FREQUENT_OPERATION_HIST_COLUMN_SOURCE_SUB_PRODUCT));
            frequentOperationHist.setSourceCurrency(resultSet.getString(DBConstant.FREQUENT_OPERATION_HIST_COLUMN_SOURCE_CURRENCY));
            frequentOperationHist.setFromAccount(resultSet.getString(DBConstant.FREQUENT_OPERATION_HIST_COLUMN_FROM_ACCOUNT));
            frequentOperationHist.setToIdentifier(resultSet.getString(DBConstant.FREQUENT_OPERATION_HIST_COLUMN_TO_IDENTIFIER));
            frequentOperationHist.setCategoryCode(resultSet.getString(DBConstant.FREQUENT_OPERATION_HIST_COLUMN_CATEGORY_CODE));
            frequentOperationHist.setCompanyCode(resultSet.getString(DBConstant.FREQUENT_OPERATION_HIST_COLUMN_COMPANY_CODE));
            frequentOperationHist.setServiceCode(resultSet.getString(DBConstant.FREQUENT_OPERATION_HIST_COLUMN_SERVICE_CODE));
            frequentOperationHist.setExecutionDate(resultSet.getTimestamp(DBConstant.FREQUENT_OPERATION_HIST_COLUMN_EXECUTION_DATE));
            frequentOperationHist.setExecutionChannel(resultSet.getInt(DBConstant.FREQUENT_OPERATION_HIST_COLUMN_EXECUTION_CHANNEL));
            frequentOperationHist.setDigitalConstancy(resultSet.getString(DBConstant.FREQUENT_OPERATION_HIST_COLUMN_DIGITAL_CONSTANCY));
            frequentOperationHist.setRequiredKey(ACTIVE.equals(resultSet.getString(DBConstant.FREQUENT_OPERATION_HIST_COLUMN_REQUIRED_KEY)));
            frequentOperationHist.setFrequentOperationGroupId(resultSet.getString(DBConstant.FREQUENT_OPERATION_HIST_COLUMN_FREQUENT_OPERATION_GROUP_ID));

            return frequentOperationHist;
        }
    }
    
    @SuppressWarnings("unused")
   	private String formatDate(java.sql.Date date){
       	String formatedDate = null;
       	DateFormat format = new SimpleDateFormat(DBConstant.FREQUENT_OPERATION_TIME_FORMAT);
           if(date != null){
           	Date fecha = new java.util.Date(date.getTime());
           	formatedDate = format.format(fecha);
           } 
       	return formatedDate;
       }

    
    @Override
    public PageableList<FrequentOperation> findByFilterSpecialOperation(FrequentOperationFilter filter, Pagination pagination) throws RepositoryException {

        try {
            formatPagination(pagination);

            MapSqlParameterSource params = new MapSqlParameterSource();
            params.addValue(CLIENT_CODE_PARAM, filter.getClientCode());
            params.addValue(CHANNEL_ID_PARAM, filter.getChannelId());
            params.addValue(CARD_TYPE_ID_PARAM, filter.getCardTypeId());
            params.addValue(SPECIAL_OPERATION, filter.isSpecialOperation() ? ACTIVE : INACTIVE);
            params.addValue(SIZE_PARAM, pagination.getSize());
            params.addValue(PAGE_PARAM, pagination.getPage());
            params.addValue(TOTAL_ELEMENTS_OUTPUT, 0);
            params.addValue(TOTAL_PAGES_OUTPUT, 0);

            Map<String, Object> response = findFreqOperationsSpecialSPCall.execute(params);

            List<FrequentOperation> frequentOperations = (List<FrequentOperation>) response.get(RESULT_SET);

            PageableList<FrequentOperation> pageableList = new PageableList<>();
            pageableList.setContent(frequentOperations);
            pageableList.setSize(pagination.getSize());
            pageableList.setPage(pagination.getPage());
            BigDecimal totalElements = (BigDecimal) response.get(TOTAL_ELEMENTS_OUTPUT);
            pageableList.setTotalElements(totalElements.intValue());
            BigDecimal totalPages = (BigDecimal) response.get(TOTAL_PAGES_OUTPUT);
            pageableList.setTotalPages(totalPages.intValue());

            return pageableList;
        } catch (DataAccessException e) {
            logger.error(PERSISTENCE_ERROR.getMessage(), e);
            throw new RepositoryException(PERSISTENCE_ERROR.getCode(), e.getMessage(), e);
        }
    }

    
    @Override
    public void saveSpecialOperation(FrequentOperation frequentOperation, Integer channelId, String user) throws RepositoryException {
        try {
        	
            Date transactionDate = new Date();

            MapSqlParameterSource params = new MapSqlParameterSource();
            params.addValue(SPECIAL_OPERATION, frequentOperation.isSpecialOperation() ? ACTIVE : INACTIVE);
            params.addValue(ID_PARAM, frequentOperation.getId());
            params.addValue(OPERATION_ID_PARAM, frequentOperation.getOperationId());
            params.addValue(ALIAS_PARAM, frequentOperation.getAlias());
            params.addValue(CLIENT_CODE_PARAM, frequentOperation.getClientCode());
            params.addValue(FREQUENCY_PARAM, frequentOperation.getFrequency());
            params.addValue(REQUIRED_KEY_PARAM, frequentOperation.isOptionalKey() ? ACTIVE : INACTIVE);
            params.addValue(CHANNEL_ID_PARAM, channelId);
            params.addValue(CHANNEL_USER_PARAM, user);
            params.addValue(TRANSACTION_DATE_PARAM, transactionDate);

            Integer frequentOperationId = saveFreqOperationSpecialSPCall.executeObject(BigDecimal.class, params).intValue();
            frequentOperation.setId(frequentOperationId);

            deleteFrequentOperationDetail(frequentOperationId);

            if (frequentOperation.getDetails() != null) {
                for (FrequentOperationDetail frequentOperationDetail : frequentOperation.getDetails()) {
                    frequentOperationDetail.setFrequentOperationId(frequentOperationId);

                    saveFrequentOperationDetail(frequentOperationDetail, transactionDate, user);
                }

                for (FrequentOperationHist frequentOperationHist : generateHistoric(frequentOperation)) {
                    saveFrequentOperationHist(frequentOperationHist, transactionDate, channelId, user);
                }
            }
        } catch (DataAccessException e) {
            logger.error(PERSISTENCE_ERROR.getMessage(), e);
            throw new RepositoryException(PERSISTENCE_ERROR.getCode(), e.getMessage(), e);
        }
    }

    
}
