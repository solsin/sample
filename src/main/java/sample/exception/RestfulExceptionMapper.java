package sample.exception;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.Response.Status.Family;
import javax.ws.rs.core.Response.StatusType;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;

import config.FilterConfig.MDCFilter;
import lombok.extern.slf4j.Slf4j;

/**
 * Apache CXF exception handler(json, jsonp, xml content-type) 
 * 
 * @author Chlhui Park <charles@plgrim.com>
 */
@Provider
@Slf4j
public class RestfulExceptionMapper implements ExceptionMapper<Exception> {
    
    private class UserDefinedStatusType implements Response.StatusType {
        private int statusCode;
        private String responsePhrase;

        public UserDefinedStatusType(int statusCode, String responsePhrase) {
            this.statusCode = statusCode;
            this.responsePhrase = responsePhrase;
        }

        /**
         * @see javax.ws.rs.core.Response.StatusType#getStatusCode()
         */
        @Override
        public int getStatusCode() {
            return this.statusCode;
        }

        /**
         * @see javax.ws.rs.core.Response.StatusType#getFamily()
         */
        @Override
        public Family getFamily() {
            return Family.SERVER_ERROR;
        }

        /**
         * @see javax.ws.rs.core.Response.StatusType#getReasonPhrase()
         */
        @Override
        public String getReasonPhrase() {
            return this.responsePhrase;
        }
    }
    
    public RestfulExceptionMapper() {
    }

    @Override
    public Response toResponse(Exception exception) {
        if (exception instanceof javax.ws.rs.WebApplicationException) {
            return creteResponseForWebAppliationException(exception);
        } else if (exception instanceof com.fasterxml.jackson.core.JsonParseException) {
            return createResponse(Status.BAD_REQUEST, ApiResponse.ERROR, "bad input");
        } else if (exception instanceof ApiException) {
            ApiException apiException = (ApiException) exception;
            return createResponse(Status.INTERNAL_SERVER_ERROR, apiException.getCode(), apiException.getMessage());
        }

        String executionId = MDC.get(MDCFilter.EXECUTION_ID);        
        Logger logger = log;
        StackTraceElement[] ste = exception.getStackTrace();
        if (ste != null && ste.length > 0) {
            logger = LoggerFactory.getLogger(ste[0].getClassName());
        }
        
        String className = exception.getClass().getName();
        StatusType status = Status.INTERNAL_SERVER_ERROR;
        if (className.startsWith("org.springframework.security")) {
            status = Status.UNAUTHORIZED;
            logger.warn("rejected by spring security: {}", exception.getMessage());
        } else {
            MDC.put("restfulError", executionId);
            logger.error("ERROR Occured during execute rest api: {}", exception.getMessage(), exception);
        }
        
        return createResponse(status, ApiResponse.ERROR, "["+executionId+"] Server Error:"+exception.getMessage());
    }

    private Response creteResponseForWebAppliationException(Exception exception) {
        javax.ws.rs.WebApplicationException e = (javax.ws.rs.WebApplicationException) exception;
        Response.StatusType statusType = new UserDefinedStatusType(e.getResponse().getStatus(),
                exception.getMessage());
        return createResponse(statusType, ApiResponse.ERROR, statusType.getReasonPhrase());
    }

    private Response createResponse(StatusType statusType, int code, String message) {
        return Response.status(statusType)
                .entity(new ApiResponse(code, message))
                .type("application/json;charset=UTF-8")
                .build();
    }
}