package com.j2js.prodmode.client;

import j2js.Global;
import j2js.client.TimerListener;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.w3c.dom5.views.Window;

import com.j2js.prodmode.net.XMLHttpRequest;
import com.j2js.prodmode.net.JSON.JSONImpl;
import com.j2js.prodmode.test.TimeoutManagerImpl;

public class UATestRunner {
    
    public static Throwable e = null;
    
    public static void main(String[] args) {
        Global.init();

        final JSONImpl json = new JSONImpl();
        
        Map<String, Object> requestObj = new HashMap<String, Object>();
        requestObj.put( "message", "Fetching test methods..." );
        
        XMLHttpRequest request = new XMLHttpRequest();
        request.open( "POST", "/j2js/test/result", false );
        request.send( Global.JSON.stringify(requestObj) );
        
        final Map<String, Object> responseObj = (Map<String, Object>) json.parse(request.getResponseText());
        System.out.println(responseObj);
        
        String className = (String) responseObj.get("className");
        String testMethodName = (String) responseObj.get("testMethodName");
        
        final Object testObject;
        try {
            testObject = Class.forName(className).newInstance();
        } catch (InstantiationException e) {
            throw new RuntimeException(e);
        } catch ( IllegalAccessException e ) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        
        final Map<String, Object> result = new HashMap<String, Object>();
        
        
        try {
            for (String methodName : (List<String>) responseObj.get("befores")) {
                invoke(testObject, methodName);
            }
            
            invoke(testObject, testMethodName);
            
            result.put("message",  "OK");
        } catch (Throwable e1) {
            e = e1;
        }
        
        
        TimerListener continuation = new TimerListener() {
            
            public void handleEvent(Window window) {
                for (String methodName : (List<String>) responseObj.get("afters")) {
                    invoke(testObject, methodName);
                }
                
                if ( e != null ) {
                    e.printStackTrace();
                    result.put("exceptionClassName", e.getClass().getName());
                    result.put("message", e.getMessage());
                }
                
                System.out.println(result);
                
                XMLHttpRequest closingRequest = new XMLHttpRequest();
                closingRequest.open("POST", "/j2js/test/result", false);
                closingRequest.send(Global.JSON.stringify(result));
                
                final Map<String, Object> finalResponse = (Map<String, Object>) json.parse(closingRequest.getResponseText());
                System.out.println(finalResponse);
                
                if ( ((Boolean) finalResponse.get("closeUA")) ) {
                    Global.window.close();
                }
            }
        };
        
        if ( e != null ) {
            continuation.handleEvent(null);
        } else {
            ((TimeoutManagerImpl) Global.timeoutManager).checkFailAfters(continuation);
        }
        
    }
    
    static void invoke(Object testObject, String methodName) {
        Method[] methods = testObject.getClass().getDeclaredMethods();
        Method method = null;
        
        for (int i=0; i<methods.length; i++) {
            if (methods[i].getName().equals(methodName)) {
                method = methods[i];
                break;
            }
        }
        
        String methodSignature = testObject.getClass().getName() + "." + methodName + "()";
        
        if ( method == null ) {
            throw new RuntimeException( "No such method: " + methodSignature);
        }
        
        System.out.println("Invoking " + methodSignature);
        method.invoke(testObject, (Object[]) null);
    }
}
