package pe.com.interbank.digitalchannel.ffoo.web.bean;

import com.fasterxml.jackson.annotation.JsonInclude;

/**
 * Created by Robert Espinoza on 09/12/2016.
 */
public class ServiceRs<T> {

    private String statusCode = "000";
    private String statusMessage = "Success";
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private T content;

    public String getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(String statusCode) {
        this.statusCode = statusCode;
    }

    public String getStatusMessage() {
        return statusMessage;
    }

    public void setStatusMessage(String statusMessage) {
        this.statusMessage = statusMessage;
    }

    public T getContent() {
        return content;
    }

    public void setContent(T content) {
        this.content = content;
    }
}
