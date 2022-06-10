package pe.com.interbank.digitalchannel.ffoo.web.filter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

public class LoggingFilter extends OncePerRequestFilter {

    protected static final Logger logger = LoggerFactory.getLogger(LoggingFilter.class);
    private static final String REQUEST_PREFIX = "Request: ";
    private static final String RESPONSE_PREFIX = "Response: ";
    private AtomicLong id = new AtomicLong(100);

    private List<String> excludedUris = new ArrayList<>();

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, final FilterChain filterChain) throws ServletException, IOException {
        long requestId = id.incrementAndGet();
        request = new RequestWrapper(requestId, request);
        response = new ResponseWrapper(requestId, response);
        try {
            filterChain.doFilter(request, response);
        } finally {
            logRequest(request);
            logResponse((ResponseWrapper) response, request.getRequestURI());

        }

    }

    public void addExcludedUri(final String uri) {
        excludedUris.add(uri);
    }

    private void logRequest(final HttpServletRequest request) {
        StringBuilder msg = new StringBuilder();
        msg.append(REQUEST_PREFIX);
        if (request instanceof RequestWrapper) {
            msg.append("request id=").append(((RequestWrapper) request).getId()).append("; ");
        }
        HttpSession session = request.getSession(false);
        if (session != null) {
            msg.append("session id=").append(session.getId()).append("; ");
        }
        if (request.getContentType() != null) {
            msg.append("content type=").append(request.getContentType()).append("; ");
        }
        msg.append("uri=").append(request.getRequestURI());
        if (request.getQueryString() != null) {
            msg.append('?').append(request.getQueryString());
        }

        if (request instanceof RequestWrapper && !isMultipart(request)) {
            RequestWrapper requestWrapper = (RequestWrapper) request;
            try {
                String charEncoding = requestWrapper.getCharacterEncoding() != null ? requestWrapper.getCharacterEncoding() :
                        "UTF-8";
                msg.append("; payload=").append(new String(requestWrapper.toByteArray(), charEncoding));
            } catch (UnsupportedEncodingException e) {
                logger.warn("Failed to parse request payload", e);
            }

        }
        logger.info(msg.toString());
    }

    private boolean isMultipart(final HttpServletRequest request) {
        return request.getContentType() != null && request.getContentType().startsWith("multipart/form-data");
    }

    private void logResponse(final ResponseWrapper response, final String uri) {
        StringBuilder msg = new StringBuilder();
        msg.append(RESPONSE_PREFIX);
        msg.append("request id=").append((response.getId())).append("; ");
        msg.append("status=").append(response.getStatus()).append("; ");
        try {
            if (!isExcludedUri(uri)) {
                msg.append(" payload=").append(new String(response.toByteArray(), response.getCharacterEncoding()));
            } else {
                msg.append(" payload=no available");
            }

        } catch (UnsupportedEncodingException e) {
            logger.warn("Failed to parse response payload", e);
        }
        logger.info(msg.toString());
    }

    private boolean isExcludedUri(String uri) {
        for (String excludedUri : excludedUris) {
            if (uri.contains(excludedUri)) {
                return true;
            }
        }
        return false;
    }

}
