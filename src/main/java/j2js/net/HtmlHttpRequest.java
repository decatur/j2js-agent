/*
 * Copyright (c) 2005 j2js.com,
 *
 * All Rights Reserved. This work is distributed under the j2js Software License [1]
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE.
 *
 * [1] http://www.j2js.com/license.txt
 */

package j2js.net;

import j2js.Global;
import j2js.client.TimerListener;

import java.util.Date;
import java.util.HashMap;

import org.w3c.dom5.views.Window;

/**
 * Subclasses of {@link HtmlHttpRequest} are independed of the avaliablility of the {@link j2js.net.XMLHttpRequest}
 * object, and will also work in environments with strict security policies.
 * The awkward same-origin security restriction is not enforced.
 * 
 * @author j2js.com
 */
public abstract class HtmlHttpRequest implements HttpRequest {
    
    static HashMap<String, HtmlHttpRequest> cache = new HashMap<String, HtmlHttpRequest>();
    private static int HTTP_OK = 200;
    
    static int idSequence = 0;
    
    String requestId;
    public int readyState = STATE_UNINITIALIZED;
    int status;
    public Object jsonObject;
    protected StringBuffer uriBuffer = new StringBuffer();
    ReadyStateChangeListener listener;
    private long timeOutHandler;
    
    /**
     * This method is also called from <code>j2js.onScriptLoad</code> (see runtime.js) whenever a ScriptHttpRequest or
     * script or FormHttpRequest returns.
     * 
     * @param requestId
     * @param theJsonObjec
     */
    public static void handleEvent(String requestId, final Object theJsonObjec) {
        final HtmlHttpRequest request = cache.remove(requestId);

        // This prevents a response from activating code after a time out.
        if (request.readyState != HttpRequest.STATE_SENT) {
            return;
        }
        Global.window.clearTimeout(request.timeOutHandler);
        
        /* There is a bug/feature in Firefox 1.5 preventing an iframe to issue an XMLHttpRequest.
         * The workaround is to let the global window execute all further code (which may contain
         * such requests).
        */
        TimerListener timerListener = new TimerListener() {
            public void handleEvent(Window window) {
                request.handleEvent( HTTP_OK, Global.JSON.nativeToJava(theJsonObjec) );
            };
        };
        
        Global.window.setTimeout(timerListener, 1);
    }
    
    protected HtmlHttpRequest() {
        
    }
    
    protected void handleEvent(int theStatus, Object theJsonObject) {
        jsonObject = theJsonObject;
        readyState = STATE_LOADED;
        status = theStatus;
        if ( listener != null ) {
            listener.handleEvent(this);
        }
    }
    
    /**
     * @see XMLHttpRequest#setReadyStateChangeListener(ReadyStateChangeListener)
     */
    public void setReadyStateChangeListener(ReadyStateChangeListener listener) {
        this.listener = listener;
    }
    
    public ReadyStateChangeListener getReadyStateChangeListener() {
        return listener;
    }
    
    public void open(String method, String uri, boolean isAsync) {
        open(method, uri, isAsync, null, null);
    }
    
    /**
     * This method must be called by any overwriting method.
     */
    public void open(String method, String uri, boolean isAsync, String user, String password) {
        if (!isAsync) throw new UnsupportedOperationException("Sync not supported");
        
        readyState = STATE_OPEN;
        status = 0;
        uriBuffer.setLength(0);
        uriBuffer.append(uri);
        if (uri.toString().indexOf("?") == -1) {
            uriBuffer.append('?'); 
        } else {
            uriBuffer.append('&'); 
        }
        
        
        if (user != null && password != null) {
            uriBuffer.append("&user=" + user + "&password=" + password);
        }
    }
    
    protected void prepareSend() {
        readyState = HttpRequest.STATE_SENT;
        requestId = String.valueOf(new Date().getTime());
        uriBuffer.append("&requestId=" + requestId);
        cache.put(requestId, this);
        
        TimerListener timerListener = new TimerListener() {
            public void handleEvent(Window window) {
                // TODO: Is 504 (Gateway Timeout) the best choice? 
                HtmlHttpRequest.this.handleEvent(504, "Timeout");
            };
        };
        
        timeOutHandler = Global.window.setTimeout(timerListener, AbstractHttpRequestFactory.TIMEOUT_MILLIS);
    }
    
    /**
     * @see XMLHttpRequest#getReadyState()
     */
    public int getReadyState() {
        return readyState;
    }
    
    /**
     * @see XMLHttpRequest#getStatus()
     */
    public int getStatus() {
        return status;
    }
    
    /**
     * @see HttpRequest#getResponseObject()
     */
    public Object getResponseObject() {
        return jsonObject;
    }
    
    /**
     * Always returns the empty string.
     */
    public String getAllResponseHeaders() {
        return "";
    }
    
    /**
     * Always returns the empty string.
     */
    public String getResponseHeader(String name) {
        return "";
    }
}
