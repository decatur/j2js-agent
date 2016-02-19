/*
 * Copyright (c) 2005 Wolfgang Kuehn
 */

package j2js;

import j2js.client.Console;
import j2js.client.HTMLUtils;
import j2js.net.AbstractHttpRequestFactory;
import j2js.net.JSON.AbstractJSON;

import org.w3c.dom5.html.HTMLDocument;
import org.w3c.dom5.views.Window;

import com.j2js.test.TimeoutManager;

/**
 * TODO: Change package.
 */
public final class Global {
    
    public static Window window;
    public static HTMLDocument document;
    public static HTMLUtils HTMLUtils;
    public static AbstractJSON JSON;
    public static AbstractHttpRequestFactory httpRequestFactory;
    public static Console console;
    
    public static TimeoutManager timeoutManager;
    
    private static Context context;
    
    public static void init() {
        if ( System.getProperty( "java.vendor" ) == null ) {
            return;
        }
            
        try {
            context = (Context) Class.forName("j2js.DevModeContext").newInstance();
            context.init();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        
    }
    
    public static void close() {
        context.close();
    }
    
}
