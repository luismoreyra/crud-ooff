package pe.com.interbank.digitalchannel.ffoo.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Robert Espinoza on 29/11/2016.
 */
public class FrequentOperation implements Serializable {

	private static final long serialVersionUID = 1L;
    
	private String modificationDate;
	private boolean specialOperation;
	private Integer id;
	private Integer operationId;
	private String operationCode;
	private String alias;
	private String clientCode;
	private Integer frequency;
	private boolean requiredKey;
	private boolean optionalKey;
	@JsonIgnore
	private String digitalConstancy;
	@JsonIgnore
	private boolean operationNotExecuted;
	private List<FrequentOperationDetail> details;
	private Boolean optionalKeyBlocked;

	
	public String getModificationDate() {
		return modificationDate;
	}

	
	public void setModificationDate(String modificationDate) {
		this.modificationDate = modificationDate;
	}

	public boolean isSpecialOperation() {
		return specialOperation;
	}

	public void setSpecialOperation(boolean specialOperation) {
		this.specialOperation = specialOperation;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getOperationId() {
		return operationId;
	}

	public void setOperationId(Integer operationId) {
		this.operationId = operationId;
	}

	public String getOperationCode() {
		return operationCode;
	}

	public void setOperationCode(String operationCode) {
		this.operationCode = operationCode;
	}

	public String getAlias() {
		return alias;
	}

	public void setAlias(String alias) {
		this.alias = alias;
	}

	public String getClientCode() {
		return clientCode;
	}

	public void setClientCode(String clientCode) {
		this.clientCode = clientCode;
	}

	public Integer getFrequency() {
		return frequency;
	}


	public void setFrequency(Integer frequency) {
		this.frequency = frequency;
	}

    public boolean isOptionalKey() {
        return optionalKey;
    }

    public void setOptionalKey(boolean optionalKey) {
        this.optionalKey = optionalKey;
    }

    public String getDigitalConstancy() {
        return digitalConstancy;
    }


	public boolean isRequiredKey() {
		return requiredKey;
	}

	public void setRequiredKey(boolean requiredKey) {
		this.requiredKey = requiredKey;
	}


	public void setDigitalConstancy(String digitalConstancy) {
		this.digitalConstancy = digitalConstancy;
	}

    public Boolean getOptionalKeyBlocked() {
        return optionalKeyBlocked;
    }

    public void setOptionalKeyBlocked(Boolean optionalKeyBlocked) {
        this.optionalKeyBlocked = optionalKeyBlocked;
    }

    public List<FrequentOperationDetail> getDetails() {
        return details;
    }


	public boolean isOperationNotExecuted() {
		return operationNotExecuted;
	}

	public void setOperationNotExecuted(boolean operationNotExecuted) {
		this.operationNotExecuted = operationNotExecuted;
	}


	public void setDetails(List<FrequentOperationDetail> details) {
		this.details = details;
	}

}
