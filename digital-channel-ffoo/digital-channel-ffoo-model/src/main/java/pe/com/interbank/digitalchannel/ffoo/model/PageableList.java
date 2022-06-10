package pe.com.interbank.digitalchannel.ffoo.model;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Robert Espinoza on 01/12/2016.
 */
public class PageableList<T> implements Serializable {

    private static final long serialVersionUID = 1L;

    private int totalElements;
    private int totalPages;
    private int size;
    private int page;
    private List<T> content;

    public int getTotalElements() {
        return totalElements;
    }

    public void setTotalElements(int totalElements) {
        this.totalElements = totalElements;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(int totalPages) {
        this.totalPages = totalPages;
    }

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

    public List<T> getContent() {
        return content;
    }

    public void setContent(List<T> content) {
        this.content = content;
    }
}
