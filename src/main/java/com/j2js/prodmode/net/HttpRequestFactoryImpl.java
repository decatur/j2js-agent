/*
 * Copyright (c) 2007 Wolfgang Kuehn
 */

package com.j2js.prodmode.net;

import j2js.net.AbstractHttpRequestFactory;
import j2js.net.FormHttpRequest;
import j2js.net.HttpRequest;

/**
 * Factory for HTTP requests. 
 */
public final class HttpRequestFactoryImpl extends AbstractHttpRequestFactory {
    
    private static HttpRequest request;
    
    /**
     * This method makes a best effort to return an instance of the 'best'
     * HttpRequest implementation. An open instance is returned,
     * as if applying the {@link HttpRequest#open(String, String, boolean, String, String)}
     * method, where the first argument (the request method) is choosen appropriately
     * for the implementation, and the <code>isAsync</code> argument is set to <code>true</true>.
     */
    public HttpRequest getSingleton(String uri, String user, String password) {
        // TODO: Why return a new request?
        request = null;
        if (request == null || !request.getClass().getName().endsWith(TYPE)) {
            if (TYPE.equals("XMLHttpRequest")) {
                request = XMLHttpRequest.getSingleton();
            } else if (TYPE.equals("FormHttpRequest")) {
                request = FormHttpRequest.getSingleton();
            } else {
                assert TYPE.equals("ScriptHttpRequest");
                request = ScriptHttpRequest.getSingleton();
            }
            
            if (request == null) request = XMLHttpRequest.getSingleton();
            if (request == null) request = FormHttpRequest.getSingleton();
            if (request == null) request = ScriptHttpRequest.getSingleton();
            if (request == null) throw new RuntimeException("Cannot create an HttpRequest");
        }
        
        String method;
        if (request.getClass().getName().endsWith("ScriptHttpRequest")) {
            method = "GET";
        } else {
            method = "POST";
        }
        
        request.open(method, uri, true);
        
        return request;
    
    }

}
