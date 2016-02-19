/*
 * Copyright (c) 2005 Wolfgang Kuehn
 */

package org.w3c.dom5.views;

import j2js.client.History;
import j2js.client.Location;
import j2js.client.TimerListener;

import org.w3c.dom5.events.EventTarget;
import org.w3c.dom5.html.HTMLCollection;
import org.w3c.dom5.html.HTMLDocument;


/**
 * The Window interface represents a browsing context, or a container, that displays a document.
 * <p>
 * This version adheres to the <quote>"The XMLHttpRequest Object" W3C Working Draft 07 April 2006</quote> 
 * </p>
 */
    
public interface Window extends EventTarget {
    
    /**
     * A modal alert dialog is presented to the user.
     */
    public void alert(String message);

    /**
     * Closes this Window object.
     */
    public void close();

    /**
     * A modal yes/no choice is presented to the user.
     */
    public boolean confirm(String message);

    /**
     * Activates this window object.
     */
    public void focus();

    /**
     * Returns the document associated with this window object.
     */
    public HTMLDocument getDocument();

    /**
     * Returns a collection of all direct child windows to this Window object.
     */
    public HTMLCollection getFrames();

    /**
     * Returns the history.
     */
    public History getHistory();

    /*
     * The cardinality of getFrames()?
     */
    // public long getLength();


    /**
     * Returns the location that represents the window's current location.
     */
    public Location getLocation();
    
    /**
     * Returns the unique name used to refer to this Window object.
     */
    public String getName();

    //public Navigator getNavigator();

    //public Window getOpener();

    /**
     * Returns the Window object that contains this object, or null if this object is the topmost Window.
     */
    public Window getParent();

    /**
     * Returns this object.
     */
    public Window getSelf();

    //public String getStatus();

    /**
     * Returns the topmost Window object in the hierarchy that contains this object.
     */
    public Window getTop();

    /**
     * Returns this object.
     */
    public Window getWindow();

    /**
     * <p>
     * Opens a new window. If a window with the specified name already exists, then
     * that window is returned.
     * </p>
     * <p>
     * Due to the widespread use of popup blockers, this method should only be used
     * in an intranet-setting where there is no blocker or the blocker is deactivated
     * for the desired sites.
     * </p>
     * 
     * @param url the URL from which to load the content into the window 
     * @param name the name or title of the window (must only consist of letters and underscore)
     * @param features a list of features (e.g. "menubar=yes,location=no")
     * @return the window
     */
    public Window open(String url, String name, String features); 

    /**
     * Prompts the user for a response string.
     * 
     * @param  message message to display to the user
     * @param  defaultResponse the default response
     * @return the user or default response
     */
    public String prompt(String message, String defaultResponse);


    //public void setDefaultStatus(String defaultStatus);

    //public void setStatus(String status);

    /**
     * This method calls the listener every time a specified number of milliseconds elapses, or until canceled by
     * a call to clearInterval. The methods returns an intervalID which may be used in a subsequent call to
     * clearInterval to cancel the interval.
     */
    public long setInterval(TimerListener listener, long delayInMillis);
  
    /**
     * Cancels a timeout that was set with the setInterval method.
     */
    public void clearInterval(long timerId);
    
    /**
     * This method calls the listener once after a specified number of milliseconds elapses, or until canceled by
     * a call to clearTimeout. The methods returns a timerID which may be used in a subsequent call to
     * clearTimeout to cancel the interval.
     */
    public long setTimeout(TimerListener listener, long delayInMillis);
    
    /**
     * Cancels a timeout that was set with the setTimeout method.
     */
    public void clearTimeout(long timerId);
    
    public void setUserData(String key, String value);
    public String getUserData(String key);
  
    //public CSSStyleDeclaration getComputedStyle(Element elt);
    //public CSSStyleDeclaration getComputedStyle(Element elt, String pseudoElt);
}
