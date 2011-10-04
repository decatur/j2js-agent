/*
 * Copyright (c) 2006 j2js.com,
 *
 * All Rights Reserved. This work is distributed under the j2js Software License [1]
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE.
 *
 * [1] http://www.j2js.com/license.txt
 */

package org.eclipse.swt.graphics;

import org.eclipse.swt.SWTException;
import org.eclipse.swt.SWT;

/**
 * Instances of this class represent transformation matrices for points 
 * expressed as (x, y) pairs of floating point numbers.
 * 
 * T*r = M*r + d
 * T^-1*r = M^-1*(r-d) = M^-1*r - M^-1*d
 */
public class Transform {
    
    private float m11;
    private float m12;
    private float m21;
    private float m22;
    private float dx;
    private float dy;

    /**
     * Constructs a new identity Transform.
     */
    public Transform(Device device) {
        this.m11 = 1;
        this.m12 = 0;
        this.m21 = 0;
        this.m22 = 1;
        this.dx = 0;
        this.dy = 0;
    }

    /**
     * Constructs a new Transform given all of the elements that represent the matrix that describes the transformation.
     */
    public Transform(Device device,
            float m11,
            float m12,
            float m21,
            float m22,
            float dx,
            float dy) {
        this.m11 = m11;
        this.m12 = m12;
        this.m21 = m21;
        this.m22 = m22;
        this.dx = dx;
        this.dy = dy;
    }
    
    /**
     * Fills the parameter with the values of the transformation matrix that the receiver represents,
     * in the order {m11, m12, m21, m22, dx, dy}.
     */
    public void getElements(float[] elements) {
        elements[0] = m11;
        elements[1] = m12;
        elements[2] = m21;
        elements[3] = m22;
        elements[4] = dx;
        elements[5] = dy;
    }
    
    /**
     * Modifies the receiver so that it represents a transformation that is equivalent to its previous
     * transformation scaled by (scaleX, scaleY). 
     */
    public void scale(float scaleX, float scaleY) {
        m11 = scaleX*m11;
        m12 = scaleX*m12;
        m21 = scaleY*m21;
        m22 = scaleY*m22;
    }
    
    /**
     * Modifies the receiver such that the matrix it represents becomes the result of
     * multiplying the matrix it previously represented by the argument.
     */
    public void multiply(Transform matrix) {
        // this.M -> matrix.M*this.M
        float t11 = matrix.m11*m11 + matrix.m12*m21;
        float t12 = matrix.m11*m12 + matrix.m12*m22;
        float t21 = matrix.m21*m11 + matrix.m22*m21;
        float t22 = matrix.m21*m12 + matrix.m22*m22;
        
        m11 = t11;
        m12 = t12;
        m21 = t21;
        m22 = t22;
        
        // this.d -> matrix.d + matrix.M*this.d        
        float[] d = new float[]{dx, dy};
        matrix.transform(d);
        dx = d[0];
        dy = d[1];
    }

    
    /**
     * Given an array containing points described by alternating x and y values,
     * modify that array such that each point has been replaced with the result of
     * applying the transformation represented by the receiver to that point.
     */
    public void transform(float[] pointArray) {
        for (int i=0; i<pointArray.length; i+=2) {
            float x = pointArray[i];
            float y = pointArray[i+1];
            pointArray[i]   = m11*x + m12*y + dx;
            pointArray[i+1] = m21*x + m22*y + dy;
        }
    }
    
    /**
     * Modifies the receiver such that the matrix it represents becomes the mathematical
     * inverse of the matrix it previously represented. 
     */
    public void invert() {
        float det = m11*m22 - m12*m21;
        if (det == 0) {
            throw new SWTException(SWT.ERROR_CANNOT_INVERT_MATRIX);
        }
        m11 = m22 / det;
        m12 = -m12 / det;
        m21 = -m21 / det;
        m22 = m11 / det;
        
        float[] d = new float[]{dx, dy};
        dx = 0;
        dy = 0;
        transform(d);
        dx = d[0];
        dy = d[1];
    }

     
    public String toString() {
        StringBuilder sb = new StringBuilder("Transform[");
        float[] elements = new float[6];
        getElements(elements);
        sb.append(elements.toString());
        sb.append("]");
        return sb.toString();
    }
    
}
