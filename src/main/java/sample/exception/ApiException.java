package sample.exception;

import javax.ws.rs.core.Response.Status;

/**
 * exception for print common error message format in case of json api
 * 
 * @author Chulhui Park <chulhui72@gmail.com>
 */
public class ApiException extends Exception{
    private static final long serialVersionUID = 5630404450796208539L;

    private final Status status;
    
    private final int code;

    /**
     * Create ApiException with specific message and exception
     * status : Status.INTERNAL_SERVER_ERROR
     * code : -1 (default value)
     * 
     * @param message exception message
     * @param e exception
     */
    public ApiException(String message, Exception e) {
        super(message, e);
        code = -1;
        status = Status.INTERNAL_SERVER_ERROR;
    }
    
    /**
     * Create ApiException with specific code and exception
     * status : Status.INTERNAL_SERVER_ERROR
     * 
     * @param code exception code
     * @param message exception message
     */
    public ApiException(int code, String message) {
        super(message);
        this.code = code; 
        status = Status.INTERNAL_SERVER_ERROR;
    }
    
    /**
     * Create ApiException with specific statuc, code and exception
     * 
     * @param status {@link javax.ws.rs.core.Response.Status}
     * @param code exception code
     * @param message exception message
     */
    public ApiException(Status status, int code, String message) {
        super(message);
        this.status = status;
        this.code = code;
    }

    public int getCode() {
        return code;
    }
    
    public Status getStatus() {
        return status;
    }
}
