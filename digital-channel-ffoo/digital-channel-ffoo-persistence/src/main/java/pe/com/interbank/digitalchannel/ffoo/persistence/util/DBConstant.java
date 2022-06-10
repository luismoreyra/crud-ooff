package pe.com.interbank.digitalchannel.ffoo.persistence.util;

/**
 * Created by Robert Espinoza on 13/12/2016.
 */
public class DBConstant {

    public static final String PKG_FREQUENT_OPERATION = "PKG_FREQUENT_OPERATION";

    public static final String FREQUENT_OPERATION_COLUMN_ID = "id";
    public static final String FREQUENT_OPERATION_COLUMN_ALIAS = "alias";
    public static final String FREQUENT_OPERATION_COLUMN_CLIENT_CODE = "client_code";
    public static final String FREQUENT_OPERATION_COLUMN_FREQUENCY = "frequency";
    public static final String FREQUENT_OPERATION_COLUMN_OPERATION_ID = "operation_id";
    public static final String FREQUENT_OPERATION_COLUMN_REQUIRED_KEY = "required_key";

    public static final String FREQUENT_OPERATION_COLUMN_MODIFICATION_DATE = "modification_date";

    public static final String FREQUENT_OPERATION_COLUMN_OPTIONAL_KEY_BLOCKED = "optional_key_blocked";


    public static final String FREQUENT_OPERATION_DETAIL_COLUMN_ID = "id";
    public static final String FREQUENT_OPERATION_DETAIL_COLUMN_FREQUENT_OPERATION_ID = "frequent_operation_id";
    public static final String FREQUENT_OPERATION_DETAIL_COLUMN_FROM_ACCOUNT = "from_account";
    public static final String FREQUENT_OPERATION_DETAIL_COLUMN_TO_IDENTIFIER = "to_identifier";
    public static final String FREQUENT_OPERATION_DETAIL_COLUMN_SOURCE_BANK = "source_bank";
    public static final String FREQUENT_OPERATION_DETAIL_COLUMN_SOURCE_CURRENCY = "source_currency";
    public static final String FREQUENT_OPERATION_DETAIL_COLUMN_SOURCE_PRODUCT = "source_product";
    public static final String FREQUENT_OPERATION_DETAIL_COLUMN_SOURCE_SUB_PRODUCT = "source_sub_product";
    public static final String FREQUENT_OPERATION_DETAIL_COLUMN_AMOUNT = "amount";
    public static final String FREQUENT_OPERATION_DETAIL_COLUMN_TRANSACTION_CURRENCY = "transaction_currency";
    public static final String FREQUENT_OPERATION_DETAIL_COLUMN_TARGET_BANK = "target_bank";
    public static final String FREQUENT_OPERATION_DETAIL_COLUMN_TARGET_CURRENCY = "target_currency";
    public static final String FREQUENT_OPERATION_DETAIL_COLUMN_TARGET_PRODUCT = "target_product";
    public static final String FREQUENT_OPERATION_DETAIL_COLUMN_TARGET_SUB_PRODUCT = "target_sub_product";
    public static final String FREQUENT_OPERATION_DETAIL_COLUMN_CATEGORY_CODE = "category_code";
    public static final String FREQUENT_OPERATION_DETAIL_COLUMN_COMPANY_CODE = "company_code";
    public static final String FREQUENT_OPERATION_DETAIL_COLUMN_SERVICE_CODE = "service_code";

    public static final String FREQUENT_OPERATION_HIST_COLUMN_ID = "id";
    public static final String FREQUENT_OPERATION_HIST_COLUMN_FREQUENT_OPERATION_ID = "frequent_operation_id";
    public static final String FREQUENT_OPERATION_HIST_COLUMN_OPERATION_ID = "operation_id";
    public static final String FREQUENT_OPERATION_HIST_COLUMN_ALIAS = "alias";
    public static final String FREQUENT_OPERATION_HIST_COLUMN_CLIENT_CODE = "client_code";
    public static final String FREQUENT_OPERATION_HIST_COLUMN_REQUIRED_KEY = "required_key";
    public static final String FREQUENT_OPERATION_HIST_COLUMN_FROM_ACCOUNT = "from_account";
    public static final String FREQUENT_OPERATION_HIST_COLUMN_TO_IDENTIFIER = "to_identifier";
    public static final String FREQUENT_OPERATION_HIST_COLUMN_SOURCE_BANK = "source_bank";
    public static final String FREQUENT_OPERATION_HIST_COLUMN_SOURCE_PRODUCT = "source_product";
    public static final String FREQUENT_OPERATION_HIST_COLUMN_SOURCE_SUB_PRODUCT = "source_sub_product";
    public static final String FREQUENT_OPERATION_HIST_COLUMN_SOURCE_CURRENCY = "source_currency";
    public static final String FREQUENT_OPERATION_HIST_COLUMN_AMOUNT = "amount";
    public static final String FREQUENT_OPERATION_HIST_COLUMN_TRANSACTION_CURRENCY = "transaction_currency";
    public static final String FREQUENT_OPERATION_HIST_COLUMN_TARGET_CURRENCY = "target_currency";
    public static final String FREQUENT_OPERATION_HIST_COLUMN_TARGET_BANK = "target_bank";
    public static final String FREQUENT_OPERATION_HIST_COLUMN_TARGET_PRODUCT = "target_product";
    public static final String FREQUENT_OPERATION_HIST_COLUMN_TARGET_SUB_PRODUCT = "target_sub_product";
    public static final String FREQUENT_OPERATION_HIST_COLUMN_CATEGORY_CODE = "category_code";
    public static final String FREQUENT_OPERATION_HIST_COLUMN_COMPANY_CODE = "company_code";
    public static final String FREQUENT_OPERATION_HIST_COLUMN_SERVICE_CODE = "service_code";
    public static final String FREQUENT_OPERATION_HIST_COLUMN_DIGITAL_CONSTANCY = "digital_constancy";
    public static final String FREQUENT_OPERATION_HIST_COLUMN_EXECUTION_DATE = "execution_date";
    public static final String FREQUENT_OPERATION_HIST_COLUMN_EXECUTION_CHANNEL = "execution_channel";
    public static final String FREQUENT_OPERATION_HIST_COLUMN_FREQUENT_OPERATION_GROUP_ID = "frequent_operation_group_id";

    public static final String OPERATION_COLUMN_OPERATION_CODE = "operation_code";
    public static final String OPERATION_COLUMN_SPECIAL_OPERATION = "special_operation";
    public static final String FREQUENT_OPERATION_TIME_FORMAT = "dd-MM-yyyy HH:mm:ss";

    public static final String DIGITAL_CHANNEL_ID = "id";
    public static final String DIGITAL_CHANNEL_ALIAS = "alias";
    public static final String DIGITAL_CHANNEL_DESCRIPTION = "description";
    public static final String DIGITAL_CHANNEL_AUTH_USER = "auth_user";
    public static final String DIGITAL_CHANNEL_AUTH_PWD = "auth_pwd";
    public static final String DIGITAL_CHANNEL_FLAG_ACTIVE = "flag_active";
    public static final String DIGITAL_CHANNEL_OPTIONAL_KEY_BLOCKED = "optional_key_blocked";

    public static final String ENROLLMENT_ID = "id";
    public static final String ENROLLMENT_CLIENT_CODE = "client_code";
    public static final String ENROLLMENT_OPTIONAL_KEY = "optional_key";
    public static final String ENROLLMENT_OPTIONAL_KEY_USED = "optional_key_used";
    public static final String ENROLLMENT_OPTIONAL_KEY_AD_COUNT = "optional_key_ad_count";
    public static final String ENROLLMENT_LAST_OPTIONAL_KEY_MOD = "last_optional_key_mod";

}
