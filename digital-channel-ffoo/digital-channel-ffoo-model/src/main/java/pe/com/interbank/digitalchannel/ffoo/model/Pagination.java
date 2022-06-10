package pe.com.interbank.digitalchannel.ffoo.model;

import java.io.Serializable;

/**
 * Created by Robert Espinoza on 02/12/2016.
 */
public class Pagination implements Serializable {

    private static final long serialVersionUID = 1L;

    private int size;
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
