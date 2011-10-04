/*
 * Copyright (c) 2010 j2js.com,
 *
 * All Rights Reserved. This work is distributed under the j2js Software License [1]
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE.
 *
 * [1] http://www.j2js.com/license.txt
 */
package com.j2js.prodmode.client;

import j2js.client.HTMLUtils;
import javascript.JSObject;
import javascript.ScriptHelper;
import org.w3c.dom5.Node;
import org.w3c.dom5.events.Event;
import org.w3c.dom5.html.HTMLDocument;
import org.w3c.dom5.html.HTMLElement;
import org.w3c.dom5.html.HTMLPreElement;
import org.w3c.dom5.html.HTMLStyleElement;

/**
 * Utility class for accessing and manipulating the DOM.
 * 
 * @author j2js
 */
public class HTMLUtilsImpl extends HTMLUtils {
    
    /**
     * Appends text to a pre element. Gives special treatment to newline characters.
     */
    public void appendText(HTMLPreElement pre, String text) {
        ScriptHelper.put("text", text);
        text = (String) ScriptHelper.eval("j2js.handleNewLine(text)");
        pre.appendChild(pre.getOwnerDocument().createTextNode(text));
    }
    
    public String getInnerHTML(HTMLElement target) {
        System.scriptEngine.put("target", target);
        return (String) System.scriptEngine.eval("target.innerHTML");
    }

    public boolean userAgentSupportsPNG() {
        if (!isMSExplorer()) return true;
        int majorVersion = ScriptHelper.evalInt("navigator.userAgent.match('MSIE (\\\\d+)'), RegExp.$1");
        return majorVersion >= 7;
    }
    
    public boolean hasExpandingBoxBug() {
        if (!isMSExplorer()) return true;
        int majorVersion = ScriptHelper.evalInt("navigator.userAgent.match('MSIE (\\\\d+)'), RegExp.$1");
        return majorVersion != 0 && majorVersion < 7;
    }
    
    public boolean isWebClient() {
        return ScriptHelper.evalBoolean("typeof(window) != 'undefined'");
    }
    
    public boolean isMSExplorer() {
        return ScriptHelper.evalBoolean("navigator.appName == 'Microsoft Internet Explorer'");
    }
    
    public boolean isNetscape() {
        return ScriptHelper.evalBoolean("navigator.appName == 'Netscape'");
    }
    
    public boolean isOpera() {
        return ScriptHelper.evalBoolean("navigator.appName == 'Opera'");
    }
    
    public Event mostRecentEvent() {
        return (Event) System.scriptEngine.eval("j2js.mostRecentEvent");
    }
    
    public String toString(Object obj) {
        System.scriptEngine.put("obj", obj);
        return (String) System.scriptEngine.eval("j2js.inspect(obj)");
    }
    
    public void insertCSS(HTMLDocument document, String css) {
        HTMLStyleElement style = (HTMLStyleElement) document.createElement("style");
        style.setType("text/css");

        Node head = document.getElementsByTagName("head").item(0);
        head.appendChild(style);
        
        if (JSObject.containsKey(style, "styleSheet")) {
            System.scriptEngine.put("style", style);
            System.scriptEngine.put("css", css);
            System.scriptEngine.eval("style.styleSheet.cssText = css");
        }else{
            style.appendChild(document.createTextNode(css));
        }
    }
     
    public int getOffsetWidth(HTMLElement element) {
        ScriptHelper.put("element", element);
        return ScriptHelper.evalInt("element.offsetWidth");
    }
    
    public int getOffsetHeight(HTMLElement element) {
        ScriptHelper.put("element", element);
        return ScriptHelper.evalInt("element.offsetHeight");
    }

    public void getDragging(boolean flag) {
        throw new UnsupportedOperationException();
        //
    }

    public void setDragging(boolean flag) {
        ScriptHelper.put("j2js.isDragging", flag);
    }
    
    public void setReturnValue(Event evt, String value) {
        ScriptHelper.put("evt", evt);
        ScriptHelper.put("value", value);
        ScriptHelper.eval("evt.returnValue=value");
    }
    
    public void setModalNode(HTMLElement node) {
        System.scriptEngine.put("node", node);
        System.scriptEngine.eval("j2js.modalNode = node");;
    }
    
    public void storeCookie(String rawCookie) {
        //System.out.println("Saved cookie: " + rawCookie);
        ScriptHelper.put("rawCookie", rawCookie);
        ScriptHelper.eval("document.cookie = rawCookie");
    }
    
    public String loadCookies() {
        return (String) ScriptHelper.eval("document.cookie");
    }

    public boolean isHTMLElement(Object obj) {
        // TODO: Why not make this work: obj instanceof HTMLElement;
        return JSObject.containsKey(obj, "tagName");
    }

}
