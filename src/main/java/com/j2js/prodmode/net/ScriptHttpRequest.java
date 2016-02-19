/*
 * Copyright (c) 2006 Wolfgang Kuehn
 */

package com.j2js.prodmode.net;

import j2js.net.HtmlHttpRequest;

import org.w3c.dom5.Element;
import org.w3c.dom5.html.HTMLScriptElement;

/**
 * The ScriptHttpRequest sends HTTP GET requests through client side script loading.
 * <p>
 * The query part of the <code>uri</code> submitted to the server will always contain the keys
 * <dl>
 *     <dt>requestId</dt>
 *     <dd>The <code>requestId</code> will identify each response with its associated request once the
 * response is loaded by the client.
 *     </dd>
 *     <dt>mimeType</dt>
 *     <dd>The <code>mimeType</code> is always set to <code>text/javascript</code> so that the server
 *     knows that this is a ScriptHttpRequest.
 *     </dd>
 *     <dt>data</dt>
 *     <dd>The JSON payload for this request.
 *     </dd>
 * <dl>
 * </p>
 * <p>
 * The <code>requestId</code> key will identify each response with its associated request once the
 * response is loaded by the client.
 * </p><p>
 * On request, the server must respond with the line
 * <pre>
 *     j2js.onScriptLoad(requestId, responseJSON);
 * </pre>
 * where <code>requestId</code> is the request identifier and
 * <code>responseJSON</code> is a legal <code>JavaScript Literal Object (JSON)</code>.
 * </p>
 *
 */
public class ScriptHttpRequest extends HtmlHttpRequest {
    
    private HTMLScriptElement script;
    
    private static ScriptHttpRequest singleton;
    
    public static ScriptHttpRequest getSingleton() {
        if (singleton == null) {
            try {
                singleton = new ScriptHttpRequest();
            } catch(Exception e) {}
        }
        return singleton;
    }
    
    /**
     * Creates a new instance.
     */
    private ScriptHttpRequest() {
        super();
    }
    
    protected void handleEvent(int status, Object jsonObject) {
        super.handleEvent(status, jsonObject);
        // Remove the script note. See the comment on new nodes in the send() method.
        script.getParentNode().removeChild(script);
        script = null;
    }
    
    /**
     * Same as {@link XMLHttpRequest#open(String, String, boolean, String, String)}, with the following
     * differences: 
     * <ol>
     * <li>Same-origin security restrictions are not enforced;</li>
     * <li>Only the GET method is allowed;</li>
     * <li>Only asynchronous requests are allowed.</li>
     * </ol>
     */
    public void open(String method, String uri, boolean isAsync, String user, String password) {
        if (script != null) {
            throw new RuntimeException("Illegal state in " + this.getClass() + ": Response is pending");
        }
        if (!"get".equalsIgnoreCase(method)) {
            throw new UnsupportedOperationException("Method not supported: " + method);
        }
        super.open(method, uri, isAsync, user, password);
        uriBuffer.append("&mimeType=text/javascript");
    }
    
    /**
     * @param data the data send to the server as part of the URL query 
     */
    public void send(String data) {
        uriBuffer.append("&data=");
        uriBuffer.append(data==null?"":data);

        prepareSend();
        
        // Opera and Firefox can use an instance of HTMLScriptElement only once.
        // We therefore have to instanciate a new one for each request.
        script = (HTMLScriptElement) j2js.Global.document.createElement("script");
        script.setType("text/javascript");
        this.script.setDefer(true);
        
        Element head = (Element) j2js.Global.document.getElementsByTagName("head").item(0);
        head.appendChild(this.script);
        
        // Note: Firefox will send the request immediately after setting the source.
        script.setSrc(uriBuffer.toString());
    } 

}
