/*
 * Copyright (c) 2005 j2js.com,
 *
 * All Rights Reserved. This work is distributed under the j2js Software License [1]
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE.
 *
 * [1] http://www.j2js.com/license.txt
 */

package j2js.client;

/**
 * Through this interface the user agent has restricted access to the client's
 * past navigations.
 * 
 * @author j2js
 */
public interface History {
    
    public int getLength();
    
    /**
     * Navigate to the previous location (if any).
     */
    public void back();
    
    /**
     * Navigate to the next location (if any).
     */
    public void forward();
    
    /**
     * Navigate to the specified incremental location (if any).
     * Therefore, <code>go(-1)==back()</code> and <code>go(1)==forward()</code>.
     * <p>
     * TODO: Is <code>go(0)</code> equivalent to <code>location.reload()</code>? 
     * </p> 
     */
    public void go(int delta);
}
