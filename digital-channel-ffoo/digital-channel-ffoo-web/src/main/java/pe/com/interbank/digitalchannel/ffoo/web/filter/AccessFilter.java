package pe.com.interbank.digitalchannel.ffoo.web.filter;


import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;
import pe.com.interbank.digitalchannel.ffoo.core.exception.AppException;
import pe.com.interbank.digitalchannel.ffoo.service.ChannelService;
import pe.com.interbank.digitalchannel.ffoo.web.bean.ServiceRs;
import pe.com.interbank.digitalchannel.ffoo.web.util.ErrorMessageBuilder;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.StringWriter;

/**
 * Servlet Filter implementation class SecurityFilter
 */
@WebFilter(urlPatterns = {"/rest/*"}, description = "Channel Authentication Filter")
public class AccessFilter implements Filter {
    private static final Logger logger = LoggerFactory.getLogger(AccessFilter.class);
    private static final String CHANNEL_USER_PARAM = "authUser";
    private static final String CHANNEL_PWD_PARAM = "authPwd";
    private static final String CHANNEL_ID_PARAM = "channelId";

    @Autowired
    private ErrorMessageBuilder errorMessageBuilder;

    @Autowired
    private ChannelService channelService;

    public AccessFilter() {
    }

    public void destroy() {
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        SpringBeanAutowiringSupport.processInjectionBasedOnServletContext(this,
                filterConfig.getServletContext());
    }

    public void doFilter(ServletRequest request, ServletResponse response,
                         FilterChain chain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;

        req.setCharacterEncoding("UTF-8");
        res.setCharacterEncoding("UTF-8");

        String authUser = (StringUtils.isEmpty(req.getHeader(CHANNEL_USER_PARAM))) ? null : req.getHeader(CHANNEL_USER_PARAM);
        String authPwd = (StringUtils.isEmpty(req.getHeader(CHANNEL_PWD_PARAM))) ? null : req.getHeader(CHANNEL_PWD_PARAM);
        Integer channelId = (!StringUtils.isNumeric(req.getHeader(CHANNEL_ID_PARAM))) ? null : Integer.valueOf(req.getHeader(CHANNEL_ID_PARAM));

        logger.info("Accessing to the URL " + req.getRequestURL());
        logger.info("Header:");
        logger.info("channelId: " + channelId);
        logger.info("authUser: " + authUser);
        logger.info("authPwd: " + authPwd);

        try {

            channelService.authenticateChannel(channelId, authUser, authPwd);

            chain.doFilter(request, response);

        } catch (AppException e) {
            ServiceRs responseDetail = new ServiceRs();
            responseDetail.setStatusCode(e.getErrorCode());
            responseDetail.setStatusMessage(errorMessageBuilder.buildMessageByException(e));

            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.configure(SerializationFeature.INDENT_OUTPUT, true);
            objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
            StringWriter stringEmp = new StringWriter();
            objectMapper.writeValue(stringEmp, responseDetail);
            res.setStatus(200);
            res.setContentType("application/json");
            res.setCharacterEncoding("UTF-8");
            res.getWriter().write(stringEmp.toString());
        }

    }

}
