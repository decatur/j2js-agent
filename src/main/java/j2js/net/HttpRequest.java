/*
 * Copyright (c) 2005 Wolfgang Kuehn
 */

package j2js.net;

/**
 * The HttpRequest object allows HTTP client functionality, such as submitting form data or 
 * loading data from a remote Web site.
 * <p>
 * The interface adheres as much as posible to the <quote>The XMLHttpRequest Object</quote> W3C Working Draft 05 April 2006.
 * http://www.w3.org/TR/XMLHttpRequest
 * </p>
 */
public interface HttpRequest {
    
    /**
     * The initial value.
     */
    public static int STATE_UNINITIALIZED = 0;

    /**
     * The open() method has been successfully called.
     */
    public static int STATE_OPEN = 1;

    /**
     * The user agent successfully completed the request, but no data has yet been received.
     */
    public static int STATE_SENT = 2;

    /**
     * Immediately before receiving the message body (if any). All HTTP headers have been received.
     */
    public static int STATE_RECEIVING = 3;

    /**
     * The data transfer has been completed.
     */
    public static int STATE_LOADED = 4;

    /**
     * Registers a listener which will invoked when readyState changes value.
     * The listener may be invoked multiple times when readyState is 3.
     */
    public abstract void setReadyStateChangeListener(
            ReadyStateChangeListener listener);

    /**
     * This is a reduced version of {@link  #open(String, String, boolean, String, String) open}.
     */
    public abstract void open(String method, String uri, boolean isAsync);

    /**
     * Initializes or resets the object.
     * This method will initialise the object by setting the readyState attribute to <code>STATE_OPEN</code>,
     * resetting the responseText, responseXML, status, and statusText attributes to their initial values,
     * and resetting the list of request headers.
     * <p>
     * Same-origin security restrictions may be enforced at this point, depending on the implementation.
     * </p>
     * <p>
     * The user and password arguments, even if null, will always take precedence over a user name and
     * password specified in the URI.
     * 
     * @param method a valid HTTP method name. The GET, POST, and HEAD values will be supported.
     * @param uri a URI, which will be resolved to an absolute URI using the window.location.href value as base.
     * @param isAsync
     * @param user user to authenticate by server
     * @param password the password of the user
     */
    public abstract void open(String method, String uri, boolean isAsync,
            String user, String password);

    /**
     * Sends the configured request to the server. One of the <code>open()</code> methods must be called
     * before this method.
     * 
     * @param data the data posted to the server
     */
    public abstract void send(String data);

    /**
     * Returns the state of the object.
     */
    public abstract int getReadyState();

    /**
     * Returns the HTTP response status available after the HTTP headers have been received.
     */
    public abstract int getStatus();

    /**
     * Returns the response object.
     */
    public abstract Object getResponseObject();

    /**
     * Returns all response headers from the most recent response. The empty
     * string will be returned if the ready state has not yet been receiving.
     */
    public abstract String getAllResponseHeaders();

    /**
     * Returns the specified response header from the most recent response. The empty
     * string will be returned if the ready state has not yet been receiving.
     */
    public abstract String getResponseHeader(String name);

}