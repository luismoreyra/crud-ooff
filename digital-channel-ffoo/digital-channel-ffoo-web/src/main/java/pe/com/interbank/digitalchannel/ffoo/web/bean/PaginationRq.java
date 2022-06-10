package pe.com.interbank.digitalchannel.ffoo.web.bean;

import pe.com.interbank.digitalchannel.ffoo.web.util.ValidationConstant;

import javax.validation.constraints.Min;

/**
 * Created by Robert Espinoza on 01/12/2016.
 */
public class PaginationRq {

    @Min(value = 0, message = ValidationConstant.INVALID_PAGINATION_SIZE_CODE)
    private int size;
    @Min(value = 1, message = ValidationConstant.INVALID_PAGINATION_PAGE_CODE)
    private int page;

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }
}
