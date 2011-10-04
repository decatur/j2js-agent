
package org.w3c.dom.svg;

import org.w3c.dom5.DOMException;

public interface SVGPathSegLinetoHorizontalAbs extends 
               SVGPathSeg {
  public float   getX( );
  public void      setX( float x )
                       throws DOMException;
}
