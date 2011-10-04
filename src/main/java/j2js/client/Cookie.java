/*
 * Copyright (c) 2007 j2js.com,
 *
 * All Rights Reserved. This work is distributed under the j2js Software License [1]
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE.
 *
 * [1] http://www.j2js.com/license.txt
 */

package j2js.client;

import java.util.Date;

import org.w3c.dom5.html.HTMLDocument;

/**
 * Instances of this class represent cookies managed by web browsers.
 * <br/>
 * A cookie is uniquely specified by its name. This class does not support
 * discriminating cookies by path or sub-domain. If cookies are send from the
 * server to the browser via http, it does only support those cookies send
 * with a path of '/'.
 *  
 * @author j2js.com
 */
public class Cookie {
    
    private String name;
    private String value;
    //private String subDomain;
    private String path = "/";
    private Integer days;
    private boolean isSecure = false;
    private HTMLDocument document;
    
    /**
     * Instantiates a new cookie with the specified name and value. The key components <tt>path</tt> and
     * <tt>sub-domain</tt> are set to default values, which are the path and the sub-domain of the current
     * document.  
     */
    public Cookie(HTMLDocument document, String theName, String theValue) {
        this.document = document;
        name = theName;
        value = theValue;
    }
    
    private StringBuilder keyComponents(Integer days) {
        StringBuilder sb = new StringBuilder();
        appendKeyValue(sb, name, value);
        appendKeyValue(sb, "path", path);
        //appendKeyValue(sb, "domain", subDomain);
        if (days != null) {
            Date d = new Date();
            d.setTime(d.getTime() + days*24*60*60*1000);
            //appendKeyValue("expires", String.format("%1$ta, %1$td %1$tb %1$tY %1$tT %1$tz", d));
            appendKeyValue(sb, "expires", d.toString());
        }
        return sb;
    }
    
    private void appendKeyValue(StringBuilder sb, String key, String v) {
        if (v == null) return;
        sb.append(key);
        sb.append('=');
        sb.append(v);
        sb.append(';');
    }
    
    /**
     * Retrieves the value of the cookie.
     */
    public String getValue() {
        return value;
    }
    
    /**
     * Sets the expiration date of the cookie.
     */
    public void setExpirationInDays(Integer theDays) {
        days = theDays;
    }
    
    /**
     * Sets the access path of the cookie, see {@link Cookie#loadCookies(String)}.
     */
//    public void setPath(String thePath) {
//        path = thePath;
//    }
    
    /**
     * Sets the sub-domain of the cookie. This is part of the key.
     */
//    public void setSubDomain(String theSubDomain) {
//        subDomain = theSubDomain;
//    }
    
    /**
     * Sets whether the cookie shall only be transmitted over a secure HTTPS connection.
     */
    public void setSecure(boolean isSecure) {
        this.isSecure = isSecure;
    }
    
    /**
     * Saves the cookie to the browser specific cookie store.
     */
    public void save() {
        StringBuilder sb = keyComponents(days);
        if (isSecure) sb.append("secure");
        
        document.setCookie( sb.toString() );
    }
    
    /**
     * Removes the cookie from the browser specific cookie store.
     */
    public void delete() {
        StringBuilder sb = keyComponents(-1);
        document.setCookie( sb.toString() );
    }
    
    /**
     * Loads the specified cookie from the cookie store.
     */
    public static Cookie loadCookie(HTMLDocument document, String name) {
        Cookie[] cookies = loadCookies(document, name);
        if (cookies == null) return null;
        // Note that the first cookie is the one with the most specific path, and
        // the last cookie the one stored under the root '/'.
        return cookies[cookies.length-1];
    }
    
    /**
     * Loads all cookies by name from the cookie store associated with the current document.
     * Returns the cookie set for the current documents path and for all parent paths.
     */
    private static Cookie[] loadCookies(HTMLDocument document, String name) {
        // Need to prepend ';' in case first cookie has specified name.
        String allCookies = ";" + document.getCookie();
        /*
         * Example: allCookies = ";a=a1a1;y=yy;a=a2a2;z=zz".
         * If we split at 'a=', we get the parts [";", "a1a1;y=yy;", "a2a2;z=zz"].
         * Therefore, all parts but the first will start with the desired cookie values.
         */
        
        //System.out.println(allCookies);
        String[] parts = allCookies.split("\\s*" + name + "=");
        
        if (parts.length < 2) return null;
        
        Cookie[] cookies = new Cookie[parts.length-1];
        for (int i=0; i<cookies.length; i++) {
            cookies[i] = new Cookie(document, name, parts[i+1].split(";")[0]);
        }
        return cookies;
    }

}
