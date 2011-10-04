package com.j2js.prodmode.client;


import java.util.HashMap;
import java.util.Map;

import org.w3c.dom5.Document;
import org.w3c.dom5.events.EventListener;
import org.w3c.dom5.events.EventTarget;
import org.w3c.dom5.html.HTMLIFrameElement;
import org.w3c.dom5.views.Window;

public class IFrame {
    
    private static Map<String, IFrame> iframes = new HashMap<String, IFrame>();
    
    public static IFrame getOrCreateIFrame(Window window, String name, EventListener listener) {
        if (iframes.containsKey(name)) return iframes.get(name);
        
        ((EventTarget) window).addEventListener("iframeload", listener, false);
        
        IFrame iframe = new IFrame(window, name);
        iframes.put(name, iframe);
        return iframe;
    }
    
    private HTMLIFrameElement element;
    
    private IFrame(Window window, String name) {
        Document document = window.getDocument();
        element = (HTMLIFrameElement) document.createElement("IFRAME");
        document.appendChild(element);
    }
    
    public void setSrc(String src) {
        element.setSrc(src);
    }
    
    public HTMLIFrameElement getHTMLIFrameElement() {
        return element;
    }
}
