package pe.com.interbank.digitalchannel.ffoo.web.bean;

/**
 * Created by Robert Espinoza on 27/12/2016.
 */
public class FindFrequentOperationHistRq extends PaginationRq {

    private Integer frequentOperationId;
    private String clientCode;

    public Integer getFrequentOperationId() {
        return frequentOperationId;
    }

    public void setFrequentOperationId(Integer frequentOperationId) {
        this.frequentOperationId = frequentOperationId;
    }

	public String getClientCode() {
		return clientCode;
	}

	public void setClientCode(String clientCode) {
		this.clientCode = clientCode;
	}
}
