package pe.com.interbank.digitalchannel.ffoo.model;

import java.util.Date;

/**
 * Created by Robert Espinoza on 03/07/2017.
 */
public class ChannelParam {

    private Integer channelId;
    private String user;
    private Date transactionDate;

    public Integer getChannelId() {
        return channelId;
    }

    public void setChannelId(Integer channelId) {
        this.channelId = channelId;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public Date getTransactionDate() {
        return transactionDate;
    }

    public void setTransactionDate(Date transactionDate) {
        this.transactionDate = transactionDate;
    }
}
