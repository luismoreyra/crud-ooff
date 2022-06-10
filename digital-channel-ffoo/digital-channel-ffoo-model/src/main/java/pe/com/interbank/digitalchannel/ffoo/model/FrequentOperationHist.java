package pe.com.interbank.digitalchannel.ffoo.model;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by Robert Espinoza on 29/11/2016.
 */
public class FrequentOperationHist implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer id;
    private Integer frequentOperationId;
    private Integer operationId;
    private String alias;
    private String clientCode;
    private boolean requiredKey;
    private String fromAccount;
    private String toIdentifier;
    private String sourceBank;
    private String sourceProduct;
    private String sourceSubProduct;
    private String sourceCurrency;
    private double amount;
    private String transactionCurrency;
    private String targetCurrency;
    private String targetBank;
    private String targetProduct;
    private String targetSubProduct;
    private String categoryCode;
    private String companyCode;
    private String serviceCode;
    private String digitalConstancy;
    private Date executionDate;
    private Integer executionChannel;
    private String frequentOperationGroupId;
    private boolean operationNotExecuted;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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

    public boolean isRequiredKey() {
        return requiredKey;
    }

    public void setRequiredKey(boolean requiredKey) {
        this.requiredKey = requiredKey;
    }

    public String getFromAccount() {
        return fromAccount;
    }

    public void setFromAccount(String fromAccount) {
        this.fromAccount = fromAccount;
    }

    public String getToIdentifier() {
        return toIdentifier;
    }

    public void setToIdentifier(String toIdentifier) {
        this.toIdentifier = toIdentifier;
    }

    public String getSourceBank() {
        return sourceBank;
    }

    public void setSourceBank(String sourceBank) {
        this.sourceBank = sourceBank;
    }

    public String getSourceProduct() {
        return sourceProduct;
    }

    public void setSourceProduct(String sourceProduct) {
        this.sourceProduct = sourceProduct;
    }

    public String getSourceSubProduct() {
        return sourceSubProduct;
    }

    public void setSourceSubProduct(String sourceSubProduct) {
        this.sourceSubProduct = sourceSubProduct;
    }

    public String getSourceCurrency() {
        return sourceCurrency;
    }

    public void setSourceCurrency(String sourceCurrency) {
        this.sourceCurrency = sourceCurrency;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getTransactionCurrency() {
        return transactionCurrency;
    }

    public void setTransactionCurrency(String transactionCurrency) {
        this.transactionCurrency = transactionCurrency;
    }

    public String getTargetCurrency() {
        return targetCurrency;
    }

    public void setTargetCurrency(String targetCurrency) {
        this.targetCurrency = targetCurrency;
    }

    public String getTargetBank() {
        return targetBank;
    }

    public void setTargetBank(String targetBank) {
        this.targetBank = targetBank;
    }

    public String getTargetProduct() {
        return targetProduct;
    }

    public void setTargetProduct(String targetProduct) {
        this.targetProduct = targetProduct;
    }

    public String getTargetSubProduct() {
        return targetSubProduct;
    }

    public void setTargetSubProduct(String targetSubProduct) {
        this.targetSubProduct = targetSubProduct;
    }

    public String getCategoryCode() {
        return categoryCode;
    }

    public void setCategoryCode(String categoryCode) {
        this.categoryCode = categoryCode;
    }

    public String getCompanyCode() {
        return companyCode;
    }

    public void setCompanyCode(String companyCode) {
        this.companyCode = companyCode;
    }

    public String getServiceCode() {
        return serviceCode;
    }

    public void setServiceCode(String serviceCode) {
        this.serviceCode = serviceCode;
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

    public String getFrequentOperationGroupId() {
        return frequentOperationGroupId;
    }

    public void setFrequentOperationGroupId(String frequentOperationGroupId) {
        this.frequentOperationGroupId = frequentOperationGroupId;
    }

    public boolean isOperationNotExecuted() {
        return operationNotExecuted;
    }

    public void setOperationNotExecuted(boolean operationNotExecuted) {
        this.operationNotExecuted = operationNotExecuted;
    }
}
