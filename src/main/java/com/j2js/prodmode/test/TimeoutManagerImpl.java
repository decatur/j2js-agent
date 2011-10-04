package com.j2js.prodmode.test;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeoutException;

import org.w3c.dom5.views.Window;

import com.j2js.prodmode.client.UATestRunner;
import com.j2js.test.Timeout;
import com.j2js.test.TimeoutManager;

import j2js.Global;
import j2js.client.TimerListener;

public class TimeoutManagerImpl implements TimeoutManager {
        
    private static Map<String, Timeout> failAfters = new HashMap<String, Timeout>();
    
    public Timeout failAfter(String description, long millis) {
        System.out.println("failAfter: " + description + " " + millis);
        Timeout to = new Timeout(description, millis);
        failAfters.put(to.id, new Timeout(description, millis));
        return to;
    }
    
    public void defuse(Timeout timeout) {
        System.out.println("defuse: " + timeout.description);
        Timeout to = failAfters.get(timeout.id);
        if ( to != null && System.currentTimeMillis() - to.initTime < to.failAfter ) {
            failAfters.remove(timeout.id);
        }
    }

    public void clearFailAfters() {
        failAfters.clear();
    }
    
    public void checkFailAfters(final TimerListener continuation) {
        long maxExpirationTime = 0;
        for ( String id : failAfters.keySet() ) {
            Timeout to = failAfters.get(id);
            maxExpirationTime = Math.max(maxExpirationTime, to.initTime + to.failAfter);
        }

        long remainingMillis = Math.max(0, maxExpirationTime - System.currentTimeMillis());
        
        Global.window.setTimeout(new TimerListener() {
            public void handleEvent(Window window) {
                
                for ( String id : failAfters.keySet() ) {
                    UATestRunner.e = new TimeoutException("Timed out: " + failAfters.get(id).description );
                    break;
                }
                
                continuation.handleEvent(window);
            }
        }, remainingMillis);
        
    }
}
