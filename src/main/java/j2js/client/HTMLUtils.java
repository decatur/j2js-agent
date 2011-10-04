/*
 * Copyright (c) 2010 j2js.com,
 *
 * All Rights Reserved. This work is distributed under the j2js Software License [1]
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE.
 *
 * [1] http://www.j2js.com/license.txt
 */
package j2js.client;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import j2js.Global;

import org.eclipse.swt.graphics.Point;
import org.w3c.dom.svg.GetSVGDocument;
import org.w3c.dom5.Element;
import org.w3c.dom5.Node;
import org.w3c.dom5.NodeList;
import org.w3c.dom5.css.CSSStyleDeclaration;
import org.w3c.dom5.events.Event;
import org.w3c.dom5.events.MouseEvent;
import org.w3c.dom5.html.HTMLDocument;
import org.w3c.dom5.html.HTMLElement;
import org.w3c.dom5.html.HTMLPreElement;

/**
 * Utility class for accessing and manipulating the DOM.
 * 
 * @author j2js
 */
public abstract class HTMLUtils {
    
    /**
     * Sets a list of property values. Usage:
     * <pre>
     * HTMLUtils.setStyles(element, new String[] {"fontSize", "xx-large", "wordWrap", "break-word"});
     * </pre>
     */
    public void setStyles(HTMLElement element, String[] keyValuePairs) {
        CSSStyleDeclaration style = element.getStyle();
        for (int i=0; i+1<keyValuePairs.length; i+=2) {
            //System.out.println(keyValuePairs[i] + ": " + keyValuePairs[i+1]);
            style.setProperty(keyValuePairs[i], keyValuePairs[i+1], null);
        }
    }
    
    /**
     * Replaces the first childnode of the specified element by the given text.
     */
    public void setText(HTMLDocument document, String documentId, String text) {
        HTMLElement element = (HTMLElement) document.getElementById(documentId);
        setText(element, text);
    }
    
    /**
     * Replaces the first childnode of the specified element by the given text.
     */
    public void setText(HTMLElement element, String text) {
        Node child = element.getFirstChild();
        if (child != null) element.removeChild(child);
        appendText(element, text);
    }
    
    /**
     * Appends text to an element.
     */
    public void appendText(HTMLElement element, String text) {
        element.appendChild(element.getOwnerDocument().createTextNode(text));
    }
    
    /**
     * Appends text to a pre element. Gives special treatment to newline characters.
     */
    public abstract void appendText(HTMLPreElement pre, String text);
    
    public void setBorderColor(CSSStyleDeclaration style, String borderColor) {
        try {
            style.setBorderColor(borderColor);
        } catch (Exception e) {
            Logger.global.log(Level.WARNING, "Invalid color " + borderColor);
        }
    }
    
    public void setBackgroundColor(CSSStyleDeclaration style, String backgroundColor) {
        try {
            style.setBackgroundColor(backgroundColor==null?"transparent":backgroundColor);
        } catch (Exception e) {
            Logger.global.log(Level.WARNING, "Invalid color " + backgroundColor);
        }
    }
    
    /**
     * Retrieves the HTML content of an element as code.
     * 
     * @param target the HTML element
     * @return the HTML code
     */
    public abstract String getInnerHTML(HTMLElement target);

    public abstract boolean userAgentSupportsPNG();
    
    public abstract boolean hasExpandingBoxBug();
    
    public abstract boolean isWebClient();
    
    public abstract boolean isMSExplorer();
    
    public abstract boolean isNetscape();
    
    public abstract boolean isOpera();
    
    public abstract Event mostRecentEvent();
    
    public abstract String toString(Object obj);
    
    public void setDisplay(String elementId, String displayType) {
        ((HTMLElement) Global.document.getElementById(elementId)).getStyle().setDisplay(displayType);
    }
    
    public void setDisplay(HTMLElement element, String displayType) {
        element.getStyle().setDisplay(displayType);
    }
    
    public abstract void insertCSS(HTMLDocument document, String css);
    
    public Point getCursorPosition(MouseEvent event) {
        Point pos = new Point(event.getClientX(), event.getClientY());
        return pos;
    }
    
    public double getNumericValue(String s) {
        return Double.parseDouble(s.replaceAll("px", ""));
    }
    
    public Point getLocation(CSSStyleDeclaration style) {
        return new Point(
                (int) getNumericValue(style.getLeft()), 
                (int) getNumericValue(style.getTop()));
    }
    
    public Point getLocation(HTMLElement element) {
        return getLocation(element.getStyle());
    }
    
    public Point getDimension(CSSStyleDeclaration style) {
        return new Point(
                (int) getNumericValue(style.getWidth()), 
                (int) getNumericValue(style.getHeight()));
    }
    
    public abstract int getOffsetWidth(HTMLElement element);
    
    public abstract int getOffsetHeight(HTMLElement element);
    
    public void setLocation(CSSStyleDeclaration style, Point position) {
        style.setLeft(position.x + "px");
        style.setTop(position.y + "px");
    }
    
    public void setDimension(CSSStyleDeclaration style, Point position) {
        style.setWidth(position.x + "px");
        style.setHeight(position.y + "px");
    }
    
    public void removeChildren(Element element) {
        do {
            Node node = element.getFirstChild();
            if (node == null) return;
            element.removeChild(node);
        } while (true);
    }
    
    private HTMLElement findNextElement(Node node) {
        while (node != null && node.getNodeType()!= Node.ELEMENT_NODE) {
            node = node.getNextSibling();
        }
        return (HTMLElement) node;
    }
    
    public HTMLElement getNextElementSibling(Node node) {
        return findNextElement(node.getNextSibling());
    }
    
    public HTMLElement getFirstChildElement(Element element) {
        return findNextElement(element.getFirstChild());
    }
    
    public List<HTMLElement> getElementsByClassName(HTMLElement parent, String className) {
        NodeList descendants = parent.getElementsByTagName("*");
        List<HTMLElement> result = new ArrayList<HTMLElement>();
        
        for (int i=0; i<descendants.getLength(); i++) {
            HTMLElement descendant = (HTMLElement) descendants.item(i);
            if (descendant.getClassName().equals(className)) {
                result.add(descendant);
            }
        }
        
        return result;
    }
    
    public abstract void setDragging(boolean flag);
    
    public abstract void getDragging(boolean flag);
    
    public abstract void setReturnValue(Event evt, String value);
    
    public abstract void setModalNode(HTMLElement node);
    
    public abstract boolean isHTMLElement(Object obj);

}
