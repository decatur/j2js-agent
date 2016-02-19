
package org.w3c.dom.svg;

import org.w3c.dom5.DOMException;
public interface SVGAnimatedNumber {
  public float getBaseVal( );
  public void           setBaseVal( float baseVal )
                       throws DOMException;
  public float getAnimVal( );
}
