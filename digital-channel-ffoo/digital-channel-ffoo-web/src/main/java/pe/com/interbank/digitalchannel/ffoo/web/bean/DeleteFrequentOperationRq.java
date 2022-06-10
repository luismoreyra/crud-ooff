package pe.com.interbank.digitalchannel.ffoo.web.bean;

/**
 * Created by Robert Espinoza on 20/12/2016.
 */
public class DeleteFrequentOperationRq {

    private Integer id;
    private String clientCode;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

	public String getClientCode() {
		return clientCode;
	}

	public void setClientCode(String clientCode) {
		this.clientCode = clientCode;
	}

}
