package j2js.net;

import org.w3c.dom5.events.Event;

import j2js.net.HttpRequest;

public interface NetworkEvent extends Event {
    
    public HttpRequest getHttpRequest();


}
