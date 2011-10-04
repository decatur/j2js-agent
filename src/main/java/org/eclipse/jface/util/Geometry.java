/*
 * Copyright (c) 2006 j2js.com,
 *
 * All Rights Reserved. This work is distributed under the j2js Software License [1]
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE.
 *
 * [1] http://www.j2js.com/license.txt
 */

package org.eclipse.jface.util;

import j2js.client.util.ExtGeometry;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;

/**
 * Contains static methods for performing simple geometric operations on the SWT geometry classes.
 * 
 * @author j2js.com
 */
public final class Geometry {
    
    /**
     * Adds two points as 2d vectors.
     */
    public static Point add(Point point1, Point point2) {
        return new Point(point1.x + point2.x, point1.y + point2.y);
    }
    
    /**
     * Returns the point in the center of the given rectangle.
     */
    public static Point centerPoint(Rectangle rect) {
        return new Point(rect.x + rect.width/2, rect.y + rect.height/2);
    }
    
    /**
     * Returns a copy of the given point.
     */
    public static Point copy(Point p) {
        if (p == null) return null;
        return new Point(p.x, p.y);
    }
    
    /**
     * Returns a copy of the given rectangle.
     */
    public static Rectangle copy(Rectangle r) {
        return new Rectangle(r.x, r.y, r.width, r.height);
    }
    
    /**
     * Returns a new rectangle with the given position and dimensions, expressed as points.
     */
    public static Rectangle createRectangle(Point position, Point size) {
        return new Rectangle(position.x, position.y, size.x, size.y);
    }

    /**
     * Returns the square of the distance between two points.
     */
    public static int distanceSquared(Point p1, Point p2) {
        int dx = p1.x - p2.x;
        int dy = p1.y - p2.y;
        return dx*dx + dy*dy;
    }
    
    /**
     * Divides both coordinates of the given point by the given scalar.
     */
    public static Point divide(Point toDivide, int scalar) {
        return new Point(toDivide.x / scalar, toDivide.y / scalar);
    }
    
    /**
     * Swaps the X and Y coordinates of the given point.
     */
    public static void flipXY(Point toFlip) {
        int x = toFlip.x;
        toFlip.x = toFlip.y;
        toFlip.y = x;
    }
    
    /**
     * Swaps the X and Y coordinates of the given rectangle, along with the height and width. 
     */
    public static void flipXY(Rectangle toFlip) {
        int x = toFlip.x;
        int width = toFlip.width;
        toFlip.x = toFlip.y;
        toFlip.y = x;
        toFlip.width = toFlip.height;
        toFlip.height = width;
    }

    /**
     * Returns the x,y position of the given rectangle.
     */
    public static Point getLocation(Rectangle rectangle) {
        return new Point(rectangle.x, rectangle.y);
    }
    
    /**
     * Returns the size of the rectangle, as a Point.
     */
    public static Point getSize(Rectangle rectangle) {
        return new Point(rectangle.width, rectangle.height);
    }
    
    
    /**
     * Returns the magnitude of the given 2d vector (represented as a Point).
     */
    public static double magnitude(Point p) {
        return Math.sqrt(p.x*p.x+p.y*p.y);
    }

    /**
     * Returns a new point whose coordinates are the maximum of the coordinates of the given points.
     */
    public static Point max(Point p1, Point p2) {
        return new Point(Math.max(p1.x, p2.x), Math.max(p1.y, p2.y));
    }

    /**
     * Returns a new point whose coordinates are the minimum of the coordinates of the given points.
     */
    public static Point min(Point p1, Point p2) {
        return new Point(Math.min(p1.x, p2.x), Math.min(p1.y, p2.y));
    }
    
    /**
     * Moves the given rectangle by the given delta.
     */
    public static void moveRectangle(Rectangle rect, Point delta) {
        rect.x += delta.x;
        rect.y += delta.y;
    }
    
    /**
     * Normalizes the given rectangle. That is, any rectangle with negative width or height becomes a rectangle 
     * with positive width or height that extends to the upper-left of the original rectangle.
     */
    public static void normalize(Rectangle rect) {
        if (rect.width < 0) {
            rect.width = -rect.width;
            rect.x -= rect.width;
        }
        if (rect.height < 0) {
            rect.height = -rect.height;
            rect.y -= rect.height;
        }
    }

    

    /**
     * Performs vector subtraction on two points.
     */
    public static Point subtract(Point point1, Point point2) {
        return new Point(point1.x - point2.x, point1.y - point2.y);
    }

    /**
     * Sets the size of the given rectangle to the given size.
     */
    public static void setSize(Rectangle rectangle, Point newSize) {
        rectangle.width = newSize.x;
        rectangle.height = newSize.y;
    }
    
    /**
     * Returns the edge of the given rectangle which is closest to the given point.
     */
    public static int getClosestSide(Rectangle boundary, Point toTest) {
        Point c = ExtGeometry.getClosestEdgePoint(boundary, toTest);
        
        if (c.y == boundary.y) return SWT.TOP;
        if (c.x == boundary.x+boundary.width) return SWT.RIGHT;
        if (c.y == boundary.y+boundary.height) return SWT.BOTTOM;
        return SWT.LEFT;
    }
    
    /**
     * Returns the dot product of the given vectors (expressed as Points)
     */
    public static int dotProduct(Point p1, Point p2) {
        return p1.x*p2.x + p1.y*p2.y;
    }
    
    /**
     * Moves each edge of the given rectangle outward by the given amount.
     * Negative values cause the rectangle to contract.
     * Does not allow the rectangle's width or height to be reduced below zero.
     */ 
    public static void expand(Rectangle rect,
            int left,
            int right,
            int top,
            int bottom) {
        rect.x -= left;
        rect.y -= top;
        rect.width = Math.max(0, rect.width+left+right);
        rect.height = Math.max(0, rect.height+top+bottom);
    }

    /**
     * Returns the distance of the given point from a particular side of the given rectangle.
     * Returns negative values for points outside the rectangle. 
     */
    public static int getDistanceFromEdge(Rectangle rectangle, Point testPoint, int edgeOfInterest) {
        Point c = ExtGeometry.getClosestEdgePoint(rectangle, testPoint, edgeOfInterest);
        int distance = (int) Math.sqrt(Geometry.distanceSquared(c, testPoint));
        
        if (!rectangle.contains(testPoint)) {
            distance = -distance;
        }
        
        return distance;
    }
    
    public static Point getDirectionVector(int distance, int direction) {
        Point directionVector = new Point(0, 0);
        if (direction == SWT.TOP) {
            directionVector.y = -direction;
        } else if (direction == SWT.BOTTOM) {
            directionVector.y = direction;
        } else if (direction == SWT.LEFT) {
            directionVector.x = -direction;
        } else { // side == SWT.RIGHT)
            directionVector.x = direction;
        }
        return directionVector;
    }
    
    /**
     * Sets result equal to toCopy.
     */
    public static void set(Rectangle result, Rectangle toCopy) {
        result.x = toCopy.x;
        result.y = toCopy.y;
        result.width = toCopy.width;
        result.height = toCopy.height;
    }

}
