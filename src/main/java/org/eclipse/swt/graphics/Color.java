package org.eclipse.swt.graphics;


/**
 * Instances of this class manage the operating system resources that implement SWT's RGB color model.
 */
public class Color {

    private String rgb;
    
    /**
     * Constructs a new instance of this class given a device and the desired red, green and blue values
     * expressed as ints in the range 0 to 255 (where 0 is black and 255 is full brightness).
     */
    public Color(Device device, int red, int green, int blue) {
        int i = 1<<8 | red;
        i = i<<8 | green;
        i = i<<8 | blue;
        rgb = Integer.toHexString(i).substring(1);
    }
    
    public String toString() {
        return "#" + rgb;
    }
   
}
