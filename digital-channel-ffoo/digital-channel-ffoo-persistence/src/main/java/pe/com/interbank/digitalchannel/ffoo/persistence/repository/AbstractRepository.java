package pe.com.interbank.digitalchannel.ffoo.persistence.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import pe.com.interbank.digitalchannel.ffoo.model.Pagination;

/**
 * Created by Robert Espinoza on 09/12/2016.
 */
public abstract class AbstractRepository {

	protected static final String SPECIAL_OPERATION = "p_special_operation";
    protected static final String ID_PARAM = "p_id";

    protected static final String SIZE_PARAM = "p_size";
    protected static final String PAGE_PARAM = "p_page";

    protected static final String TOTAL_ELEMENTS_OUTPUT = "p_total_elements";
    protected static final String TOTAL_PAGES_OUTPUT = "p_total_pages";

    protected static final String TRANSACTION_DATE_PARAM = "p_transaction_date";

    protected static final String RESULT_SET = "p_list";
    protected static final String RESULT_OUTPUT = "p_result";
    protected static final String ACTIVE = "1";
    protected static final String INACTIVE = "0";

    @Autowired
    protected JdbcTemplate jdbcTemplate;

    protected void formatPagination(Pagination pagination) {
        if (pagination.getSize() == 0) {
            pagination.setPage(1);
        }
    }

}
