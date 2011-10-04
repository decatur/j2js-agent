/*
 * Copyright (c) 2005 j2js.com,
 *
 * All Rights Reserved. This work is distributed under the j2js Software License [1]
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE.
 *
 * [1] http://www.j2js.com/license.txt
 */

package j2js.net;

import j2js.Global;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.w3c.dom5.events.Event;
import org.w3c.dom5.html.HTMLFormElement;

/**
 * A {@link RemoteProcedureCall} is able to invoke a remote procedure asynchronically.
 *  
 * @author j2js.com
 */
public abstract class RemoteProcedureCall implements CallbackHandler, ReadyStateChangeListener {

    private String url;
    private String version;
    private String ticket;
    private HTMLFormElement attachment;
    
    /**
     * Constructs a new {@link RemoteProcedureCall}. If the url contains a
     * '*' character, then this character is replaced by the operation name.
     * <br/>Example: The call
     * <pre>
     *     new RemoteProcedureCall("http://foo.com/requests/*.php", "checkout")
     * </pre>
     * will have the communication endpoint "http://foo.com/requests/checkout.php".
     * 
     * <p>
     * If the url does not contain a '*' character, then the operation name is passed in the
     * payload of the remote procedure call under the key <code>operationName</code>.
     * </p>
     * 
     * @param theUrl the remote server URL
     * @param theOperationName the remote operation name
     * @param theVersion the version of the client.
     * May be <code>null</code> if the version is already encoded in the
     * operation name or the server URL.
     */
    public RemoteProcedureCall(String theUrl, String theVersion) {
        url = theUrl;
        version = theVersion;
    }
    
    public RemoteProcedureCall(String theUrl, String theVersion, HTMLFormElement theAttachment) {
        this(theUrl, theVersion);
        attachment = theAttachment;
    }
    
    private void reportFailure(String message) {
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("message", "Request to " + url + " failed: " + message);
        onFailure(map);
    }
    
    /**
     * This method is not part of the public interface!
     */
    public void handleEvent(HttpRequest request) {
       
        if (request.getReadyState() != HttpRequest.STATE_LOADED) {
            return;
        }
        
        if (request.getStatus() == 404) {
            reportFailure("Service is not available");
            return;
        }
        
        if (request.getStatus() == 504) {
            reportFailure("because service is not available or responds with illegal protocol");
            return;
        }
        
        if (request.getStatus() != 200 && request.getStatus() != 0) {
            reportFailure("with status " + request.getStatus());
            return;
        }
        
        Object object = null;
        try {
            object = request.getResponseObject();
        } catch (Exception e) {
            reportFailure(": " +  e.getMessage());
            return;
        }

        if (!(object instanceof Map)) {
            //System.out.println(object.toString());
            reportFailure("to return a JSON map");
            return;
        }
        
        Logger.global.log(Level.FINER, object.toString());
        Map<String, Object> map = (Map<String, Object>) object;
        
        if (map.containsKey("error")) {
            onFailure(map);
        } else {
            onSuccess(map);
        }
    }
    
    public void execute(Object object) {
        execute(Global.JSON.stringify(object));
    }
    
    private void appendAttribute(StringBuffer sb, String key, String value) {
        if (value == null) return;
        sb.append('"');
        sb.append(key);
        sb.append("\":\"");
        sb.append(value);
        sb.append("\", "); 
    }
    
    public void execute(String s) {
        StringBuffer sb = new StringBuffer();
        sb.append("{");
        appendAttribute(sb, "ticket", ticket);
        appendAttribute(sb, "version", version);

        sb.append("\"payLoad\":" + s + "}");

        HttpRequest request;
        
        if (attachment == null) {
            request = Global.httpRequestFactory.getSingleton(url, null, null);
        } else {
            request = new FormHttpRequest(attachment);
            request.open("post", url, true);
        }
        
        Logger.global.info("Using request " + request.getClass());
        
        if (request.getReadyState() == HttpRequest.STATE_SENT || request.getReadyState() == HttpRequest.STATE_RECEIVING)
            throw new RuntimeException("Waiting for remote call to finish");

        request.setReadyStateChangeListener(this);
        
        Logger.global.finer("Posting to " + url + ": " + sb.toString());
        request.send(sb.toString());
    }

    public String getTicket() {
        return ticket;
    }

    public void setTicket(String theTicket) {
        ticket = theTicket;
    }
}
