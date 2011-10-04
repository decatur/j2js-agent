
package org.w3c.dom.svg;

import org.w3c.dom5.Document;
import org.w3c.dom5.events.DocumentEvent;

public interface SVGDocument extends 
               Document,
               DocumentEvent {
  public String    getTitle( );
  public String     getReferrer( );
  public String      getDomain( );
  public String      getURL( );
  public SVGSVGElement getRootElement( );
}
