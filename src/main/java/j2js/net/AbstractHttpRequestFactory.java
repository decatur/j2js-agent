/*
 * Copyright (c) 2007 j2js.com,
 *
 * All Rights Reserved. This work is distributed under the j2js Software License [1]
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE.
 *
 * [1] http://www.j2js.com/license.txt
 */

package j2js.net;

/**
 * Factory for HTTP requests. 
 * 
 * @author j2js.com
 */
public abstract class AbstractHttpRequestFactory {

    public static String TYPE = "XMLHttpRequest";
    public static int TIMEOUT_MILLIS = 4000;
    
    /**
     * This method makes a best effort to return an instance of the 'best'
     * HttpRequest implementation. An open instance is returned,
     * as if applying the {@link HttpRequest#open(String, String, boolean, String, String)}
     * method, where the first argument (the request method) is choosen appropriately
     * for the implementation, and the <code>isAsync</code> argument is set to <code>true</true>.
     */
    public abstract HttpRequest getSingleton(String uri, String user, String password);

}
