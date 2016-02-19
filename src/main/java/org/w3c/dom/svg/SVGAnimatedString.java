
package org.w3c.dom.svg;

import org.w3c.dom5.DOMException;
public interface SVGAnimatedString {
  public String getBaseVal( );
  public void           setBaseVal( String baseVal )
                       throws DOMException;
  public String getAnimVal( );
}
