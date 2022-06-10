package pe.com.interbank.digitalchannel.ffoo.model;

import java.io.Serializable;

/**
 * Created by Robert Espinoza on 27/12/2016.
 */
public class FrequentOperationHistFilter implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer frequentOperationId;

    public Integer getFrequentOperationId() {
        return frequentOperationId;
    }

    public void setFrequentOperationId(Integer frequentOperationId) {
        this.frequentOperationId = frequentOperationId;
    }
}
