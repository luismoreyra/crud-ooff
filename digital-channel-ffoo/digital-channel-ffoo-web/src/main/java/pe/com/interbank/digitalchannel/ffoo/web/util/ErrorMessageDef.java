package pe.com.interbank.digitalchannel.ffoo.web.util;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * Created by Robert Espinoza on 02/11/2016.
 */
@Component
@ConfigurationProperties(prefix = "error", locations = "${ffoo.application.errorDef.config}")
public class ErrorMessageDef {

    private Map<String, String> message;

    public Map<String, String> getMessage() {
        return message;
    }

    public void setMessage(Map<String, String> message) {
        this.message = message;
    }
}
