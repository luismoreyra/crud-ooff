package pe.com.interbank.digitalchannel.ffoo.model;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * Created by Robert Espinoza on 09/01/2017.
 */
public class FrequentOperationHistGroup implements Serializable {

    private static final long serialVersionUID = 1L;

    private String frequentOperationGroupId;
    private Integer frequentOperationId;
    private Integer operationId;
    private String alias;
    private String clientCode;
    private String digitalConstancy;
    private Date executionDate;
    private Integer executionChannel;
    private boolean requiredKey;
    private List<FrequentOperationHistDetail> details;

    public String getFrequentOperationGroupId() {
        return frequentOperationGroupId;
    }

    public void setFrequentOperationGroupId(String frequentOperationGroupId) {
        this.frequentOperationGroupId = frequentOperationGroupId;
    }

    public Integer getFrequentOperationId() {
        return frequentOperationId;
    }

    public void setFrequentOperationId(Integer frequentOperationId) {
        this.frequentOperationId = frequentOperationId;
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

    public String getDigitalConstancy() {
        return digitalConstancy;
    }

    public void setDigitalConstancy(String digitalConstancy) {
        this.digitalConstancy = digitalConstancy;
    }

    public Date getExecutionDate() {
        return executionDate;
    }

    public void setExecutionDate(Date executionDate) {
        this.executionDate = executionDate;
    }

    public Integer getExecutionChannel() {
        return executionChannel;
    }

    public void setExecutionChannel(Integer executionChannel) {
        this.executionChannel = executionChannel;
    }

    public boolean isRequiredKey() {
        return requiredKey;
    }

    public void setRequiredKey(boolean requiredKey) {
        this.requiredKey = requiredKey;
    }

    public List<FrequentOperationHistDetail> getDetails() {
        return details;
    }

    public void setDetails(List<FrequentOperationHistDetail> details) {
        this.details = details;
    }
}
