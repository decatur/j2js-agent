/*
 * Copyright (c) 2007 Wolfgang Kuehn
 */

package j2js.net;

/**
 * Factory for HTTP requests. 
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
