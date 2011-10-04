package org.eclipse.swt.graphics;

/**
 * Instances of this class represent rectangular areas in an (x, y) coordinate system.
 * 
 * @author j2js.com
 */
public class Rectangle {

    /**
     * The height of the rectangle.
     */
    public int height;
    
    /**
     * The width of the rectangle.
     */
    public int width;
    
    /**
     * The x coordinate of the rectangle.
     */
    public int x;
    
    /*
     * The y coordinate of the rectangle
     */
    public int y;
    
    /**
     * Construct a new instance of this class given the x, y, width and height values.
     */
    public Rectangle(int theX, int theY, int theWidth, int theHeight) {
        x = theX; y = theY; width = theWidth; height = theHeight;
    }
    
    /**
     * Returns true if the given point is inside the area specified by the receiver,
     * and false otherwise.
     */
    public boolean contains(Point pt) {
        return pt.x>=x && pt.x<=x+width && pt.y>=y && pt.y<=y+height;
    }
    
    /**
     * Compares this object to the specified object.
     */
    public boolean equals(Object obj) {
        if (obj == null || !(obj instanceof Rectangle)) return false;
        Rectangle other = (Rectangle) obj;
        return x == other.x && y == other.y && width == other.width && height == other.height;
    }
    
    /**
     *     Returns a string containing a concise, human-readable description of the receiver.
     */
    public String toString() {
        return "(" + x + ", " + y  + ", " + width + ", " + height + ")";
    }
    
    /**
     * Returns a new rectangle which represents the union of the receiver and the given rectangle. 
     */
    public Rectangle union(Rectangle rect) {
        Rectangle r = new Rectangle(0, 0, 0, 0);
        r.x = Math.min(x, rect.x);
        r.y = Math.min(y, rect.y);
        r.width = Math.max(x + width, rect.x + rect.width) - r.x;
        r.height = Math.max(y + height, rect.y + rect.height) - r.y;
        return r;
    }
}
