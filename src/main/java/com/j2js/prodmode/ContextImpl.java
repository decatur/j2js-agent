/*
 * Copyright (c) 2005 j2js.com,
 *
 * All Rights Reserved. This work is distributed under the j2js Software License [1]
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE.
 *
 * [1] http://www.j2js.com/license.txt
 */

package com.j2js.prodmode;

import java.io.PrintStream;
import java.net.URLDecoder;
import java.util.ArrayList;

import javascript.ScriptHelper;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;

import com.j2js.prodmode.client.ConsoleImpl;
import com.j2js.prodmode.client.ConsoleOutputStream;
import com.j2js.prodmode.client.HTMLUtilsImpl;
import com.j2js.prodmode.client.WindowImpl;
import com.j2js.prodmode.net.HttpRequestFactoryImpl;
import com.j2js.prodmode.net.JSON.JSONImpl;
import com.j2js.prodmode.test.TimeoutManagerImpl;

import j2js.Context;
import j2js.Global;


/**
 * @author j2js.com
 */
public final class ContextImpl implements Context {

    // Need to maintain the order of properties. TODO: Implement and use LinkedHashMap instead. 
    private static ArrayList<String> propertyValues = new ArrayList<String>();
    
    private static void foo() {
        Global.window = WindowImpl.self;
        
        ScriptEngineManager manager = new ScriptEngineManager();
        System.scriptEngine = manager.getEngineByName("JavaScript");
        System.err = new PrintStream(new ConsoleOutputStream());
        System.out = new PrintStream(new ConsoleOutputStream());
        
        Global.document = Global.window.getDocument();
        //System.out.println("Setting " + Global.document);
        
        Global.JSON = new JSONImpl();
        Global.HTMLUtils = new HTMLUtilsImpl();
        Global.httpRequestFactory = new HttpRequestFactoryImpl();
        Global.console = new ConsoleImpl();
        
        Global.timeoutManager = new TimeoutManagerImpl();
        
        initializePropertiesFromQuery();
        System.properties.put("java.version", "1.5 J2JS");
    }
    
    private static void premain(String className, String methodName) {
        ContextImpl foo = new ContextImpl();
        ContextImpl.foo();
        ScriptEngine engine = System.scriptEngine;
        engine.put("className", className);
        engine.put("methodName", methodName);
        
        if (methodName.matches(".*\\(.+\\).*")) {
            // Method has parameters.
            if (!methodName.matches(".*\\(java.lang.String\\[\\]\\).*"))
                throw new RuntimeException("Method must have no argument or argument of type 'java.lang.String[]'");
            String[] argv = propertyValues.toArray();
            engine.put("argv", argv);
            engine.eval("j2js.invokeStatic(className, methodName, [argv])");
        } else {
            engine.eval("j2js.invokeStatic(className, methodName, [])");
        }
    }
    
    private static void initializePropertiesFromQuery() {
        // Initialize system properties through query part of URL.
        String query = (String) System.scriptEngine.eval("window.location.search");
        // Drop '?'.
        query = query.substring(1).trim();
        String[] keyValuePairs = query.split("&");
        for (int i=0; i<keyValuePairs.length; i++) {
            String[] keyValue = keyValuePairs[i].split("=");
            if (keyValue.length != 2) continue;
            String value = URLDecoder.decode(keyValue[1], "UTF-8");
            System.properties.put(keyValue[0], value);
            propertyValues.add(value);
        }
    }
    
    public void init() {   
    }
    
    public void close() {
    }
    
    public void close(long millis) {
    }
    
    public static String getAssemblyVersion() {
        return (String) ScriptHelper.eval("j2js.assemblyVersion");
    }
    
}
