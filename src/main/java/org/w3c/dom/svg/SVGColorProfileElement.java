
package org.w3c.dom.svg;

import org.w3c.dom5.DOMException;

public interface SVGColorProfileElement extends 
               SVGElement,
               SVGURIReference,
               SVGRenderingIntent {
  public String      getLocal( );
  public void           setLocal( String local )
                       throws DOMException;
  public String      getName( );
  public void           setName( String name )
                       throws DOMException;
  public short getRenderingIntent( );
  public void           setRenderingIntent( short renderingIntent )
                       throws DOMException;
}
