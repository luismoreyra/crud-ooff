package pe.com.interbank.digitalchannel.ffoo.model;

import java.io.Serializable;

/**
 * Created by Robert Espinoza on 29/11/2016.
 */
public class Channel implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer id;
    private String description;
    private String alias;
    private String authUser;
    private String authPwd;
    private boolean active;
    private boolean optionalKeyBlocked;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public String getAuthUser() {
        return authUser;
    }

    public void setAuthUser(String authUser) {
        this.authUser = authUser;
    }

    public String getAuthPwd() {
        return authPwd;
    }

    public void setAuthPwd(String authPwd) {
        this.authPwd = authPwd;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public boolean isOptionalKeyBlocked() {
        return optionalKeyBlocked;
    }

    public void setOptionalKeyBlocked(boolean optionalKeyBlocked) {
        this.optionalKeyBlocked = optionalKeyBlocked;
    }
}
