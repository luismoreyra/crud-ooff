package pe.com.interbank.digitalchannel.ffoo.web.bean;

/**
 * Created by Robert Espinoza on 09/12/2016.
 */
public class HeaderRq {

    private Integer channelId;
    private String authUser;
    private String authPwd;

    public HeaderRq(Integer channelId, String authUser, String authPwd) {
        this.channelId = channelId;
        this.authUser = authUser;
        this.authPwd = authPwd;
    }

    public Integer getChannelId() {
        return channelId;
    }

    public void setChannelId(Integer channelId) {
        this.channelId = channelId;
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
}
