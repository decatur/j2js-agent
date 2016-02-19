/*
 * Copyright (c) 2005 Wolfgang Kuehn
 */

package com.j2js.prodmode.net;

import j2js.Global;
import j2js.net.HttpRequest;
import j2js.net.ReadyStateChangeListener;

import org.w3c.dom5.Document;

import javascript.ScriptHelper;

/**
 * The XMLHttpRequest is a wrapper for the native XMLHttpRequest object of most modern web user agents.
 * <br />
 * This class does not completely adhere to {@link http://www.w3c.org/TR/XMLHttpRequest} 
 */
public class XMLHttpRequest implements HttpRequest {
    
    private Object nativeRequest = null;
    
    private static XMLHttpRequest singleton;
    
    public static XMLHttpRequest getSingleton() {
        if (singleton == null) {
            try {
                singleton = new XMLHttpRequest();
            } catch (Exception e) {}
        }
        return singleton;
    }
    
    private Object createActiveXObject(String name) {
        ScriptHelper.put("name", name);
        return ScriptHelper.eval("new ActiveXObject(name)");
    }
    
    private Object createNativeXMLHttpRequest() {
        try {
            return createActiveXObject("Msxml2.XMLHTTP");
        } catch (Throwable e) {  
        }
        
        try {
            return createActiveXObject("Microsoft.XMLHTTP");
        } catch (Throwable e) {
        }

        boolean b = ScriptHelper.evalBoolean("typeof XMLHttpRequest!='undefined'");
        if (b) {
            try {
                return ScriptHelper.eval("new XMLHttpRequest()");
            } catch (Throwable e) {
            }
        }
        
        if (ScriptHelper.eval("window.createRequest") != null) {
            try {
                return ScriptHelper.eval("window.createRequest()");
            } catch (Throwable e) {
            }
        }
        
        return null;
    }
    
    /**
     * Creates a new instance.
     */
    public XMLHttpRequest() {
        nativeRequest = createNativeXMLHttpRequest();
        if (nativeRequest == null) {
            throw new RuntimeException("Could not instantiate XMLHttpRequest");
        }
    }

    /**
     * @see HttpRequest#setReadyStateChangeListener(ReadyStateChangeListener)
     */
    public void setReadyStateChangeListener(ReadyStateChangeListener listener) {
        if (listener == null) {
            ScriptHelper.eval("this.nativeRequest.onreadystatechange = null");
            return;
        }
        
        ScriptHelper.put("listener", listener);
        ScriptHelper.eval("var me = this; this.nativeRequest.onreadystatechange = function() { j2js.invoke(listener, 'handleEvent(j2js.net.HttpRequest)void', [me]); }"); 
    }
    
    /**
     * @see HttpRequest#open(String, String, boolean)
     */
    public void open(String method, String uri, boolean isAsync) {
        open(method, uri, isAsync, null, null);
    }
    
    /**
     * @see HttpRequest#open(String, String, boolean, String, String)
     */
    public void open(String method, String uri, boolean isAsync, String user, String password) {
        ScriptHelper.put("method", method);
        ScriptHelper.put("uri", uri);
        ScriptHelper.put("isAsync", isAsync);
        ScriptHelper.put("user", user);
        ScriptHelper.put("password", password);
        ScriptHelper.eval("this.nativeRequest.open(method, uri, isAsync, user, password)");
    }
    
    /**
     * @see HttpRequest#send(String)
     */
    public void send(String data) {
        // Note: Some browsers will not even submit request if data is null!
        data = (data==null?"":data);
        
        ScriptHelper.put("data", data);
        ScriptHelper.eval("this.nativeRequest.send(data)");
    }
    
    /**
     * @see HttpRequest#getReadyState()
     */
    public int getReadyState() {
        return ScriptHelper.evalInt("this.nativeRequest.readyState");
    }
    
    /**
     * @see HttpRequest#getStatus()
     */
    public int getStatus() {
        // TODO: Map status 12029 (WinSock: cannot connect) to 504 (HTTP: Gateway Timeout)
        return ScriptHelper.evalInt("this.nativeRequest.status");
    }
    
    /**
     * Returns the response body.
     */
    public String getResponseText() {
        return (String) ScriptHelper.eval("this.nativeRequest.responseText");
    }
    
    /**
     * Returns the response body as an XML document. If the user agent does not recognize the body
     * as XML, then null will be returned. Note that for some user agents, the server must identify
     * the body as of mime-type "xml/text". For such a user agent, it is therefore not possible to
     * load XML documents from the local file system.
     */
    public Document getResponseXML() {
        return (Document) ScriptHelper.eval("this.nativeRequest.responseXML");
    }
    
    /**
     * @see HttpRequest#getAllResponseHeaders()
     */
    public String getAllResponseHeaders() {
        return (String) ScriptHelper.eval("this.nativeRequest.getAllResponseHeaders()");
    }
    
    /**
     * @see HttpRequest#getResponseHeader(String)
     */
    public String getResponseHeader(String name) {
        ScriptHelper.put("name", name);
        return (String) ScriptHelper.eval("this.nativeRequest.getResponseHeader(name)");
    }

    /**
     * @see HttpRequest#getResponseObject()
     */
    public Object getResponseObject() {
        return Global.JSON.parse(getResponseText().trim());
    }
}
