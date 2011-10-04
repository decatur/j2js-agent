package org.eclipse.swt.graphics;

public final class Point {
    
    /**
     * The x coordinate of the point.
     */
    public int x;
    
    /**
     * The y coordinate of the point.
     */
    public int y;

    
    /**
     * Constructs a new point with the given x and y coordinates.
     */
    public Point(int theX, int theY) {
        x = theX;
        y = theY;
    }
    
    /**
     * Compares this object to the specified object.
     */
    public boolean equals(Object obj) {
        if (obj == null || !(obj instanceof Point)) return false;
        Point p = (Point) obj;
        return x == p.x && y == p.y;
    }
    
    /**
     *     Returns a string containing a concise, human-readable description of the receiver.
     */
    public String toString() {
        return "(" + x + ", " + y + ")";
    }

    
    
}
