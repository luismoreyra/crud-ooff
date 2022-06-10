package pe.com.interbank.digitalchannel.ffoo.model;

import java.io.Serializable;

/**
 * Created by Robert Espinoza on 02/12/2016.
 */
public class FrequentOperationFilter implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer channelId;
    private String clientCode;
    private Integer cardTypeId;
    private boolean  specialOperation;

    
    public boolean isSpecialOperation() {
		return specialOperation;
	}

	public void setSpecialOperation(boolean specialOperation) {
		this.specialOperation = specialOperation;
	}

	public Integer getChannelId() {
        return channelId;
    }

    public void setChannelId(Integer channelId) {
        this.channelId = channelId;
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
