
package org.w3c.dom.svg;

import org.w3c.dom5.DOMException;
public interface SVGICCColor {
  public String      getColorProfile( );
  public void      setColorProfile( String colorProfile )
                       throws DOMException;
  public SVGNumberList getColors( );
}
