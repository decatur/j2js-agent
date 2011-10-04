/*
 * Copyright (c) 2010 j2js.com,
 *
 * All Rights Reserved. This work is distributed under the j2js Software License [1]
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE.
 *
 * [1] http://www.j2js.com/license.txt
 */

package j2js;

/**
 * @author j2js.com
 */
public interface Context {
    
    void init();
    
    //public String getAssemblyVersion();
    
    void close();
    
    //public void close(long millis);

}
