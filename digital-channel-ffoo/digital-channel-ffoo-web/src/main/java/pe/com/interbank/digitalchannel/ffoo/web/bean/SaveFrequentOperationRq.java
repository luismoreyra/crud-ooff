package pe.com.interbank.digitalchannel.ffoo.web.bean;

import pe.com.interbank.digitalchannel.ffoo.web.util.ValidationConstant;
import pe.com.interbank.digitalchannel.ffoo.web.validator.NotEmpty;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

/**
 * Created by Robert Espinoza on 02/01/2017.
 */
public class SaveFrequentOperationRq {
	
	private Boolean specialOperation;
	private Integer id;
    @NotNull(message = ValidationConstant.REQUIRED_OPERATION_CODE)
    private Integer operationId;
    @NotNull(message = ValidationConstant.REQUIRED_ALIAS_CODE)
    @NotEmpty(message = ValidationConstant.REQUIRED_ALIAS_CODE)
    @Size(max = 40, message = ValidationConstant.MAX_SIZE_ALIAS_CODE)
    //@Pattern(regexp = "^[\\p{Alnum}]*$", message = ValidationConstant.ALPHANUMERIC_ALIAS_CODE)
    private String alias;
    @NotNull(message = ValidationConstant.REQUIRED_CLIENT_CODE)
    @NotEmpty(message = ValidationConstant.REQUIRED_CLIENT_CODE)
    private String clientCode;
    @Min(value = 1, message = ValidationConstant.INVALID_FREQUENCY_CODE)
    private Integer frequency;
    private boolean requiredKey;
    private boolean optionalKey;
    private boolean updateOptionalKey;
    private String digitalConstancy;
    private boolean operationNotExecuted;
    @NotNull(message = ValidationConstant.NO_FREQUENT_OPERATION_DETAIL_CODE)
    @Size(min = 1, message = ValidationConstant.NO_FREQUENT_OPERATION_DETAIL_CODE)
    @Valid
    private List<SaveFrequentOperationDetailRq> details;
    
    
    public Boolean getSpecialOperation() {
		return specialOperation;
	}

	public void setSpecialOperation(Boolean specialOperation) {
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

    public boolean isRequiredKey() {
        return requiredKey;
    }

    public void setRequiredKey(boolean requiredKey) {
        this.requiredKey = requiredKey;
    }

    public boolean isOptionalKey() {
        return optionalKey;
    }

    public void setOptionalKey(boolean optionalKey) {
        this.optionalKey = optionalKey;
    }

    public boolean isUpdateOptionalKey() {
        return updateOptionalKey;
    }

    public void setUpdateOptionalKey(boolean updateOptionalKey) {
        this.updateOptionalKey = updateOptionalKey;
    }

    public String getDigitalConstancy() {
        return digitalConstancy;
    }

    public void setDigitalConstancy(String digitalConstancy) {
        this.digitalConstancy = digitalConstancy;
    }

    public boolean isOperationNotExecuted() {
        return operationNotExecuted;
    }

    public void setOperationNotExecuted(boolean operationNotExecuted) {
        this.operationNotExecuted = operationNotExecuted;
    }

    public List<SaveFrequentOperationDetailRq> getDetails() {
        return details;
    }

    public void setDetails(List<SaveFrequentOperationDetailRq> details) {
        this.details = details;
    }
}
