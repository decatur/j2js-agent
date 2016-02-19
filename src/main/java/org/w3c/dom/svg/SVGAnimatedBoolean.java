
package org.w3c.dom.svg;

import org.w3c.dom5.DOMException;
public interface SVGAnimatedBoolean {
  public boolean getBaseVal( );
  public void           setBaseVal( boolean baseVal )
                       throws DOMException;
  public boolean getAnimVal( );
}
