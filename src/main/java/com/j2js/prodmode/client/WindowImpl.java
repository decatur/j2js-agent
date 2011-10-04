/*
 * Copyright (c) 2005 j2js.com,
 *
 * All Rights Reserved. This work is distributed under the j2js Software License [1]
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE.
 *
 * [1] http://www.j2js.com/license.txt
 */

package com.j2js.prodmode.client;

import j2js.client.History;
import j2js.client.Location;
import j2js.client.TimerListener;
import javascript.ScriptHelper;

import org.w3c.dom5.events.Event;
import org.w3c.dom5.events.EventException;
import org.w3c.dom5.events.EventListener;
import org.w3c.dom5.events.EventTarget;
import org.w3c.dom5.html.HTMLCollection;
import org.w3c.dom5.html.HTMLDocument;
import org.w3c.dom5.views.Window;

/**
 * @see org.w3c.dom5.views.Window
 * 
 * @author j2js.com
 */
public class WindowImpl implements Window {

    public static Window self = new WindowImpl(null, ScriptHelper.eval("self"));
    
    private Object nativeWindow;
    private Location location;
    private WindowImpl parent;
    
    private WindowImpl(WindowImpl parent, Object theNativeWindow) {
        nativeWindow = theNativeWindow;
        this.parent = parent;
    }
    
    public void addEventListener(String type, EventListener listener, boolean useCapture) {
        ((EventTarget) nativeWindow).addEventListener(type, listener, useCapture);        
    }

    public boolean dispatchEvent(Event evt) throws EventException {
        throw new UnsupportedOperationException();
    }

    public void removeEventListener(String type, EventListener listener, boolean useCapture) {
        throw new UnsupportedOperationException();
    }
    
    // Begin alert
    /**
     * A modal alert dialog is presented to the user.
     */
    public void alert(String message) {
        ScriptHelper.put("message", message);
        ScriptHelper.eval("this.nativeWindow.alert(message)");
    }
    // End alert
    
    public void close() {
        ScriptHelper.eval("this.nativeWindow.close()");
    }

    public boolean confirm(String message) {
        ScriptHelper.put("message", message);
        return ScriptHelper.evalBoolean("this.nativeWindow.confirm(message)");
    }
    
    public void focus() {
        ScriptHelper.eval("this.nativeWindow.focus()");
    }

    public HTMLDocument getDocument() {
        return (HTMLDocument) ScriptHelper.eval("this.nativeWindow.document");
    }
    
    public HTMLCollection getFrames() {
        throw new UnsupportedOperationException();
    }
    
    public History getHistory() {
        throw new UnsupportedOperationException();
    }
    
    public Location getLocation() {
        if (location == null) {
            location = new LocationImpl(this);
        }
        return location;
    }
    
    public String getName() {
        return (String) ScriptHelper.eval("this.nativeWindow.name");
    }
 
    public Window getParent() {
        return parent;
    }
    
    public Window getSelf() {
        return this;
    }
    
    public Window getTop() {
        Window window = this;
        while (true) {
            Window currentParent = window.getParent();
            if (currentParent == null) {
                return window;
            }
            window = currentParent;
        }
    }
    
    public Window getWindow() {
        return this;
    }
    
    public Window open(String url, String name, String features) {
        // Internet Explorer will not handle non-word characters in title.
        if (!name.matches("\\w+")) {
            throw new RuntimeException("Window title must consist of non-word characters: " + name);
        }
        ScriptHelper.put("url", url);
        ScriptHelper.put("name", name);
        ScriptHelper.put("features", features);
        Object w = ScriptHelper.eval("this.nativeWindow.open(url, name, features)");
        if (w == null) {
            // Opening of window was blocked by the browser.
            return null;
        }
        return new WindowImpl(this, w);
    }
    
    public String prompt(String message, String defaultResponse) {
        ScriptHelper.put("message", message);
        ScriptHelper.put("defaultResponse", defaultResponse);
        return (String) ScriptHelper.eval("this.nativeWindow.prompt(message, defaultResponse)");
    }

    public long setInterval(TimerListener listener, long delayInMillis) {
        ScriptHelper.put("listener", listener);
        ScriptHelper.put("delayInMillis", delayInMillis);
        return ScriptHelper.evalLong("j2js.createTimerDelegate(this, listener, delayInMillis, 'Interval')");
    }

    public void clearInterval(long timerId) {
        ScriptHelper.put("timerId", timerId);
        ScriptHelper.eval("window.clearInterval(timerId)");
    }
    
    public long setTimeout(TimerListener listener, long delayInMillis) {
        ScriptHelper.put("listener", listener);
        ScriptHelper.put("delayInMillis", delayInMillis);
        return ScriptHelper.evalLong("j2js.createTimerDelegate(this, listener, delayInMillis, 'Timeout')");
    }

    public void clearTimeout(long timerId) {
        ScriptHelper.put("timerId", timerId);
        ScriptHelper.eval("window.clearTimeout(timerId)");
    }

    @Override
    public void setUserData(String key, String value) {
        throw new UnsupportedOperationException();
        //
    }

    @Override
    public String getUserData(String key) {
        throw new UnsupportedOperationException();
        //return null;
    }    

}
