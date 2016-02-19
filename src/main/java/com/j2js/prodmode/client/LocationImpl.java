/*
 * Copyright (c) 2011 Wolfgang Kuehn
 */

package com.j2js.prodmode.client;

import org.w3c.dom5.views.Window;

import j2js.client.Location;
import javascript.ScriptHelper;

/**
 * @see j2js.client.Location
 */
public class LocationImpl implements Location {

    private Object location;
    
    LocationImpl(Window window) {
        ScriptHelper.put("window", window);
        location = ScriptHelper.eval("window.nativeWindow.location");
    }

    /**
     * @see j2js.client.Location#getHash()
     */
    public String getHash() {
        return (String) ScriptHelper.eval("this.location.hash");
    }


    /**
     * @see j2js.client.Location#getHost()
     */
    public String getHost() {
        return (String) ScriptHelper.eval("this.location.host");
    }

    /**
     * @see j2js.client.Location#getHostname()
     */
    public String getHostname() {
        return (String) ScriptHelper.eval("this.location.hostname");
    }

    /**
     * @see j2js.client.Location#getHref()
     */
    public String getHref() {
        return (String) ScriptHelper.eval("this.location.href");
    }

    /**
     * @see j2js.client.Location#setHref(String)
     */
    public void setHref(String url) {
        ScriptHelper.put("url", url);
        ScriptHelper.eval("this.location.href = url");
    }

    /**
     * @see j2js.client.Location#getPathname()
     */
    public String getPathname() {
        return (String) ScriptHelper.eval("this.location.pathname");
    }

    /**
     * @see j2js.client.Location#getPort()
     */
    public String getPort() {
        return (String) ScriptHelper.eval("this.location.port");
    }

    /**
     * @see j2js.client.Location#getProtocol()
     */
    public String getProtocol() {
        return (String) ScriptHelper.eval("this.location.protocol");
    }

    /**
     * @see j2js.client.Location#getSearch()
     */
    public String getSearch() {
        return (String) ScriptHelper.eval("this.location.search");
    }

    /**
     * @see j2js.client.Location#reload()
     */
    public void reload() {
        ScriptHelper.eval("this.location.reload()");
    }
    
    /**
     * @see j2js.client.Location#replace(String)
     */
    public void replace(String url) {
        ScriptHelper.put("url", url);
        ScriptHelper.eval("this.location.replace(url)");
    }
    
    /**
     * @see j2js.client.Location#toString()
     */
    public String toString() {
        return getHref();
    }

}
