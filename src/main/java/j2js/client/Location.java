/*
 * Copyright (c) 2005 j2js.com,
 *
 * All Rights Reserved. This work is distributed under the j2js Software License [1]
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE.
 *
 * [1] http://www.j2js.com/license.txt
 */

package j2js.client;

/**
 * The Location object represents the current location of a {@link org.w3c.dom5.views.Window window}.
 * <p>
 * This version adheres to the <b>"The XMLHttpRequest Object" W3C Working Draft 07 April 2006</b> 
 * </p>
 * 
 * @author j2js.com
 */
public interface Location {
    
    /**
     * Returns the absolute URI reference.
     */
    public String getHref();
    /**
     * The location's window will navigate to the newly set value.
     */
    public void setHref(String url);
    
    /**
     * If the absolute URI reference for the current location has a fragment identifier, then the string
     * concatenation of the hash mark (#) and the fragment identifier is returned. Otherwise, an empty string
     * is returned.
     */
    public String getHash();
    
    /**
     * Returns the network host of the current URI. If the port attribute is not null, then the concatenation
     * of the hostname, a colon (:) and the port is returned.
     */
    public String getHost();
    
    /**
     * Returns the name or IP address of the network location without any port number.
     */
    public String getHostname();
    
    /**
     * Returns the path component of the current URI which consists of everything after the host and port up
     * to and excluding the first question mark (?) or hash mark (#).
     */
    public String getPathname();
    
    /**
     * Returns the port number of the network location.
     */
    public String getPort();
    
    /**
     * Returns the scheme of the URI including the trailing colon (:).
     */
    public String getProtocol();
    
    /**
     * Returns the query portion of a URI.
     * It consists of everything after the pathname up to and excluding the first hash mark (#).
     */
    public String getSearch();

    /**
     * Replaces the current history entry with the url specified.
     */       
    public void replace(String url);

    /**
     * Forces the user agent to reload the resource identified by this location.
     */
    public void reload();

    /**
     * Returns a string representation of this object.
     */
    public String toString();

}
