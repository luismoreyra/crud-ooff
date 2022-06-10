package pe.com.interbank.digitalchannel.ffoo.web.bean;

import pe.com.interbank.digitalchannel.ffoo.web.util.ValidationConstant;
import pe.com.interbank.digitalchannel.ffoo.web.validator.NotEmpty;

import javax.validation.constraints.NotNull;

/**
 * Created by Robert Espinoza on 06/07/2017.
 */
public class SetAdCountRq {

    @NotEmpty(message = ValidationConstant.INVALID_PARAMETERS)
    private String clientCode;
    @NotNull(message = ValidationConstant.INVALID_PARAMETERS)
    private Integer adCount;

    public String getClientCode() {
        return clientCode;
    }

    public void setClientCode(String clientCode) {
        this.clientCode = clientCode;
    }

    public Integer getAdCount() {
        return adCount;
    }

    public void setAdCount(Integer adCount) {
        this.adCount = adCount;
    }
}
