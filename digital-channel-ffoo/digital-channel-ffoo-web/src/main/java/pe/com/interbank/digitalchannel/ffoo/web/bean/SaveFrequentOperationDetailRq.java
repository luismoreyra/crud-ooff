package pe.com.interbank.digitalchannel.ffoo.web.bean;

import pe.com.interbank.digitalchannel.ffoo.web.util.ValidationConstant;
import pe.com.interbank.digitalchannel.ffoo.web.validator.MinAmount;
import pe.com.interbank.digitalchannel.ffoo.web.validator.NotEmpty;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

/**
 * Created by Robert Espinoza on 02/01/2017.
 */
public class SaveFrequentOperationDetailRq {

    @NotNull(message = ValidationConstant.REQUIRED_FROM_ACCOUNT_CODE)
    @NotEmpty(message = ValidationConstant.REQUIRED_FROM_ACCOUNT_CODE)
    private String fromAccount;
    @NotNull(message = ValidationConstant.REQUIRED_TO_IDENTIFIER_CODE)
    @NotEmpty(message = ValidationConstant.REQUIRED_TO_IDENTIFIER_CODE)
    @Size(max = 25, message = ValidationConstant.MAX_SIZE_TO_IDENTIFIER_CODE)
    @Pattern(regexp = "^[\\p{Alnum}]*$", message = ValidationConstant.ALPHANUMERIC_TO_IDENTIFIER_CODE)
    private String toIdentifier;
    private String sourceBank;
    //@NotNull(message = ValidationConstant.REQUIRED_SOURCE_PRODUCT_CODE)
    //@NotEmpty(message = ValidationConstant.REQUIRED_SOURCE_PRODUCT_CODE)
    private String sourceProduct;
    //@NotNull(message = ValidationConstant.REQUIRED_SOURCE_SUB_PRODUCT_CODE)
    //@NotEmpty(message = ValidationConstant.REQUIRED_SOURCE_SUB_PRODUCT_CODE)
    private String sourceSubProduct;
    private String sourceCurrency;
    @NotNull(message = ValidationConstant.REQUIRED_AMOUNT_CODE)
    @MinAmount(message = ValidationConstant.MIN_AMOUNT_CODE)
    private Double amount;
    @NotNull(message = ValidationConstant.REQUIRED_TRANSACTION_CURRENCY_CODE)
    @NotEmpty(message = ValidationConstant.REQUIRED_TRANSACTION_CURRENCY_CODE)
    private String transactionCurrency;
    private String targetCurrency;
    private String targetBank;
    //@NotNull(message = ValidationConstant.REQUIRED_TARGET_PRODUCT_CODE)
    //@NotEmpty(message = ValidationConstant.REQUIRED_TARGET_PRODUCT_CODE)
    private String targetProduct;
    //@NotNull(message = ValidationConstant.REQUIRED_TARGET_SUB_PRODUCT_CODE)
    //@NotEmpty(message = ValidationConstant.REQUIRED_TARGET_SUB_PRODUCT_CODE)
    private String targetSubProduct;
    private String categoryCode;
    private String companyCode;
    private String serviceCode;

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

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
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
}
