
package org.w3c.dom.svg;

import org.w3c.dom5.DOMException;

public interface SVGAltGlyphElement extends 
               SVGTextPositioningElement,
               SVGURIReference {
  public String getGlyphRef( );
  public void      setGlyphRef( String glyphRef )
                       throws DOMException;
  public String getFormat( );
  public void      setFormat( String format )
                       throws DOMException;
}
