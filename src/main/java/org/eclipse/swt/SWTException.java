package org.eclipse.swt;

/**
 * This runtime exception is thrown whenever a recoverable error occurs internally in SWT.
 */
public class SWTException extends RuntimeException {

    public int code;
    
    public SWTException() {
        super();
    }
    
    public SWTException(String message) {
        super(message);
    }
    
    public SWTException(Throwable cause) {
        super(cause);
    }
    
    public SWTException(int theCode) {
        super("");
        code = theCode;
    }
    
    /**
     * Constructs a new instance of this class with its stack trace, error code and message filled in.
     */
    public SWTException(int theCode, String message) {
        super(message);
        code = theCode;
    }

}
