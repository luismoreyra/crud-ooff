package pe.com.interbank.digitalchannel.ffoo.web.bean;

import pe.com.interbank.digitalchannel.ffoo.web.util.ValidationConstant;
import pe.com.interbank.digitalchannel.ffoo.web.validator.NotEmpty;

import javax.validation.constraints.NotNull;

/**
 * Created by Robert Espinoza on 30/06/2017.
 */
public class SetOptionalKeyRq {

    @NotEmpty(message = ValidationConstant.INVALID_PARAMETERS)
    private String clientCode;
    @NotNull(message = ValidationConstant.INVALID_PARAMETERS)
    private Boolean optionalKey;

    public String getClientCode() {
        return clientCode;
    }

    public void setClientCode(String clientCode) {
        this.clientCode = clientCode;
    }

    public Boolean getOptionalKey() {
        return optionalKey;
    }

    public void setOptionalKey(Boolean optionalKey) {
        this.optionalKey = optionalKey;
    }

}
