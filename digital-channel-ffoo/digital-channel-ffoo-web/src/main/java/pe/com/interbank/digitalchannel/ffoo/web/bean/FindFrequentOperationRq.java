package pe.com.interbank.digitalchannel.ffoo.web.bean;

import javax.validation.constraints.NotNull;

/**
 * Created by Robert Espinoza on 01/12/2016.
 */
public class FindFrequentOperationRq extends PaginationRq {

    @NotNull
    private String clientCode;
    @NotNull
    private Integer cardTypeId;
    
    private Boolean  specialOperation;
    
    
    public Boolean getSpecialOperation() {
		return specialOperation;
	}

	public void setSpecialOperation(Boolean specialOperation) {
		this.specialOperation = specialOperation;
	}

	public String getClientCode() {
        return clientCode;
    }

    public void setClientCode(String clientCode) {
        this.clientCode = clientCode;
    }

    public Integer getCardTypeId() {
        return cardTypeId;
    }

    public void setCardTypeId(Integer cardTypeId) {
        this.cardTypeId = cardTypeId;
    }
}
