package org.eclipse.jface.resource;

import java.net.URL;

import org.eclipse.swt.graphics.Device;
import org.eclipse.swt.graphics.Image;
import org.w3c.dom5.html.HTMLImageElement;

/**
 * An image descriptor is an object that knows how to create an SWT image.
 * 
 * @author j2js.com
 */
public class ImageDescriptor {

    private URL url;
    
    /**
     * Creates and returns a new image descriptor from a URL.
     */
    public static ImageDescriptor createFromURL(URL url) {
        ImageDescriptor desc = new ImageDescriptor();
        desc.url = url;
        return desc;
    }
    
    /**
     * Creates and returns a new SWT image for this image descriptor.
     */
    public Image createImage(Device device) {
        Image imgage = new Image();
        imgage.imageElement = (HTMLImageElement) device.document.createElement("IMG");
        imgage.imageElement.setSrc(url.toString());
        return imgage;
    }
    
}
