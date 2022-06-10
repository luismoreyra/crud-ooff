package pe.com.interbank.digitalchannel.ffoo.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.Date;

/**
 * Created by Robert Espinoza on 04/07/2017.
 */
public class Enrollment {

    private Integer id;
    private String clientCode;
    private Boolean optionalKey;
    private Boolean optionalKeyUsed;
    private Integer optionalKeyAdCount;
    private Boolean optionalKeyBlocked;
    @JsonIgnore
    private Date lastOptionalKeyMod;

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

    public Boolean getOptionalKey() {
        return optionalKey;
    }

    public void setOptionalKey(Boolean optionalKey) {
        this.optionalKey = optionalKey;
    }

    public Boolean getOptionalKeyUsed() {
        return optionalKeyUsed;
    }

    public void setOptionalKeyUsed(Boolean optionalKeyUsed) {
        this.optionalKeyUsed = optionalKeyUsed;
    }

    public Integer getOptionalKeyAdCount() {
        return optionalKeyAdCount;
    }

    public void setOptionalKeyAdCount(Integer optionalKeyAdCount) {
        this.optionalKeyAdCount = optionalKeyAdCount;
    }

    public Boolean getOptionalKeyBlocked() {
        return optionalKeyBlocked;
    }

    public void setOptionalKeyBlocked(Boolean optionalKeyBlocked) {
        this.optionalKeyBlocked = optionalKeyBlocked;
    }

    public Date getLastOptionalKeyMod() {
        return lastOptionalKeyMod;
    }

    public void setLastOptionalKeyMod(Date lastOptionalKeyMod) {
        this.lastOptionalKeyMod = lastOptionalKeyMod;
    }

}
