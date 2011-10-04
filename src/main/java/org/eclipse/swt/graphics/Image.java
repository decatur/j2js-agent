package org.eclipse.swt.graphics;

import org.w3c.dom5.html.HTMLImageElement;

/**
 * Instances of this class are graphics which have been prepared for display on a specific device.
 * 
 * @author j2js.com
 */
public class Image {
    /**
     * <b>IMPORTANT</b>: This member variable is not part of the SWT public API.
     */
     public HTMLImageElement imageElement;
     
     /**
      * Returns the bounds of the receiver. The rectangle will always have
      * x and y values of 0.
      */
     public Rectangle getBounds() {
         return new Rectangle(0, 0, imageElement.getWidth(), imageElement.getHeight());
     }
}

     