package org.eclipse.swt;

/**
 * This class provides access to a small number of SWT system-wide methods,
 * and in addition defines the public constants provided by SWT.
 */
public class SWT {

    /**
     * Style constant for arrow button behavior (value is 1<<2).
     */
    public static final int ARROW = 1<<2;

    /**
     * The MessageBox style constant for a CANCEL button;
     * valid combinations are OK|CANCEL, YES|NO|CANCEL, RETRY|CANCEL (value is 1<<8).
     */
    public static final int CANCEL = 1<<8;
    
    /**
     * Style constant for check box behavior (value is 1<<5).
     */
    public static final int CHECK = 1<<5;
    
    /**
     * Style constant for drop down menu/list behavior (value is 1<<2).
     */
    public static final int DROP_DOWN = 1<<2;

    /**
     * SWT error constant indicating that a null argument was passed in (value is 4).
     */
    public static final int ERROR_NULL_ARGUMENT = 4;
    
    /**
     * SWT error constant indicating that the matrix is not invertible (value is 10).
     */
    public static int  ERROR_CANNOT_INVERT_MATRIX = 10;
    
    
    /**
     * Style constant for flat appearance (value is 1<<23).
     */
    public static final int FLAT = 1<<23;
    
    /**
     * Style constant for horizontal alignment or orientation behavior (value is 1<<8).
     */
    public static final int HORIZONTAL = 1<<8;
    
    /**
     * A constant known to be zero (0), used in operations which take bit flags to indicate that "no bits are set".
     */
    public static final int NONE = 0;
    
    /**
     * The MessageBox style constant for an OK button; valid combinations are OK, OK|CANCEL (value is 1<<5).
     */
    public static final int OK = 1<<5;
    
    /**
     * Style constant for pop up menu behavior (value is 1<<3).
     */
    public static final int POP_UP = 1<<3;

    /**
     * Style constant for push button behavior (value is 1<<3).
     */
    public static final int PUSH = 1<<3;
    
    /**
     * Style constant for radio button behavior (value is 1<<4).
     */
    public static final int RADIO = 1<<4;    
    
    /**
     * Style constant for line separator behavior (value is 1<<1).
     */
    public static final int SEPARATOR = 2;
    
    /**
     * Style constant for toggle button behavior (value is 1<<1).
     */
    public static int TOGGLE = 2;
    
    /**
     * Style constant for vertical alignment or orientation behavior (value is 1<<9).
     */
    public static final int VERTICAL = 1<<9;
    
    /**
     * Style constant for align left behavior (value is 1<<14). 
     */
    public static final int LEFT = 1<<14;
    
    /**
     * Style constant for align right behavior (value is 1<<17).
     */
    public static final int RIGHT = 1<<17;
    
    /**
     * Style constant for align top behavior (value is 1<<7).
     */
    public static int TOP = 1<<7;
    
    /**
     * Style constant for align bottom behavior (value is 1<<10).
     */
    public static final int BOTTOM = 1<<10;
    
    /**
     * Style constant for full row selection behavior.
     */
    public static int FULL_SELECTION = 1<<16;
    
    
    /**
     * Throws an appropriate exception based on the passed in error code.
     *
     * @param code the SWT error code
     */
    public static void error (int code) {
        if (code == SWT.ERROR_NULL_ARGUMENT) {
            throw new IllegalArgumentException("Argument cannot be null");
        } else {
            throw new SWTException(code, "");
        }
    }

}
