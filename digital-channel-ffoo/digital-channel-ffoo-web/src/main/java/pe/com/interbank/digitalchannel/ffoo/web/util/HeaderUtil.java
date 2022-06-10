package pe.com.interbank.digitalchannel.ffoo.web.util;

import org.springframework.http.HttpHeaders;
import pe.com.interbank.digitalchannel.ffoo.web.bean.HeaderRq;

import java.util.List;

/**
 * Created by Robert Espinoza on 09/12/2016.
 */
public class HeaderUtil {

    public static final String CHANNEL_ID_HEADER_PARAM = "channelId";
    public static final String CHANNEL_USER_HEADER_PARAM = "authUser";
    public static final String CHANNEL_PWD_HEADER_PARAM = "authPwd";

    public static HeaderRq getHttpHeader(HttpHeaders headers) {
        HeaderRq headerBean = null;
        if (headers != null) {
            headerBean = new HeaderRq(Integer.valueOf(getValue(headers, CHANNEL_ID_HEADER_PARAM)),
                    getValue(headers, CHANNEL_USER_HEADER_PARAM),
                    getValue(headers, CHANNEL_PWD_HEADER_PARAM));
        }
        return headerBean;
    }

    private static String getValue(HttpHeaders headers, String property) {
        List<String> values = headers.get(property);
        if (values != null) {
            return values.get(0);
        }
        return null;
    }

}
