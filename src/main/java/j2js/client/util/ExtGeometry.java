package j2js.client.util;


import org.eclipse.jface.util.Geometry;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;

public final class ExtGeometry {

    /**
     * Returns the closest point to x which lies in the interval [i,j].
     */
    private static int project(int x, int i, int j) {
        if (x<i) return i;
        if (x>j) return j;
        return x;
    }
    
    /**
     * Returns the closest point of the given point on any edge of the given rectangle.
     */
    public static Point getClosestEdgePoint(Rectangle r, Point p) {
        Point lr = getLowerRightVertex(r);
        Point c = new Point(project(p.x, r.x, lr.x), project(p.y, r.y, lr.y));
        
        if (r.contains(p)) {
            Point copy = Geometry.copy(c);
            // Distance to TOP
            int dist = p.y - r.y;
            c.y = r.y;
            
            // Distance to RIGHT
            int d = lr.x - p.x;
            if (d<dist) {
                dist = d;
                c.x = lr.x;
                c.y = copy.y;
            }
            
            // Distance to BOTTOM
            d = lr.y - p.y;
            if (d<dist) {
                dist = d;
                c.x = copy.x;
                c.y = lr.y;
            }
            
            // Distance to LEFT
            d = p.x - r.x;
            if (d<dist) {
                c.x = r.x;
                c.y = copy.y;
            }
        }
        
        return c;
    }
    
    /**
     * Returns the closest point of the given point on a particular side of the given rectangle.
     */
    public static Point getClosestEdgePoint(Rectangle r, Point p, int edgeOfInterest) {
        Point lr = getLowerRightVertex(r);
        Point c = new Point(project(p.x, r.x, lr.x), project(p.y, r.y, lr.y));
        
        if (edgeOfInterest == SWT.TOP) {
            c.y = r.y;
        } else if (edgeOfInterest == SWT.RIGHT) {
            c.x = lr.x;
        } else if (edgeOfInterest == SWT.BOTTOM) {
            c.y = lr.y;
        } else if (edgeOfInterest == SWT.LEFT) {
            c.x = r.x;
        } else {
            throw new RuntimeException("Invalid edge of interest: " + edgeOfInterest);
        }
        return c;
    }
    
    /*
     * Returns true iff p lies on the straight segment between p1 and p2
     * with respect to integer arithmetic.
     */
    public static boolean contains(Point p1, Point p2, Point p) {
        Point delta = Geometry.subtract(p, p1);
        Point delta2 = Geometry.subtract(p2, p1);
        
        double dot = Geometry.dotProduct(delta, delta2);
        double dot2 = Geometry.dotProduct(delta2, delta2);
        
        if (dot < 0 || dot > dot2) return false;
        
        return dot*delta2.x == dot2*delta.x && dot*delta2.y == dot2*delta.y;
    }
    
    /**
     * Returns the first intersection between the segment from start to end and
     * the specified rectangle. The segment must be strictly horizontal.
     */
    private static Point getHorizontalIntersection(Point start, Point end, Rectangle r) {
        Point lr = getLowerRightVertex(r);
        
        if (start.y < r.y || start.y > lr.y) return null;
        else if (start.x >= r.x && start.x <= lr.x
                && (start.y == r.y || start.y == lr.y)) return Geometry.copy(start);
        
        Point intersection = new Point(0, start.y);
        
        if (start.x <= r.x && end.x >= r.x) {
            intersection.x = r.x;
        } else if (start.x >= r.x && start.x <= lr.x) {
            if (end.x <= r.x) {
                intersection.x = r.x;
            } else if (end.x >= lr.x) {
                intersection.x = lr.x;
            } else return null;
        } else if (start.x >= lr.x && end.x <= lr.x) {
            intersection.x = lr.x;
        } else {
            return null;
        }
        
        return intersection;
    }
    
    /**
     * Returns the first intersection between the segment from start to end and
     * the specified rectangle. The segment must be strictly horizontal or vertical.
     */
    public static Point getIntersection(Point start, Point end, Rectangle r) {

        if (start.y == end.y) {
            return getHorizontalIntersection(start, end, r);
        } else if (start.x == end.x) {
            Point startFlipped = Geometry.copy(start);
            Geometry.flipXY(startFlipped);
            Point endFlipped = Geometry.copy(end);
            Geometry.flipXY(endFlipped);
            Rectangle rFlipped = Geometry.copy(r);
            Geometry.flipXY(rFlipped);
            Point intersection = getHorizontalIntersection(startFlipped, endFlipped, rFlipped);
            if (intersection != null) Geometry.flipXY(intersection);
            return intersection;
        }
        
        throw new RuntimeException();
    }
    
    public static Point getLowerRightVertex(Rectangle r) {
        return new Point(r.x + r.width, r.y + r.height);
    }

}
