
package org.w3c.dom.svg;

import org.w3c.dom5.DOMException;
public interface SVGAnimatedEnumeration {
  public short getBaseVal( );
  public void           setBaseVal( short baseVal )
                       throws DOMException;
  public short getAnimVal( );
}
