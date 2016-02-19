package j2js.client;

import org.w3c.dom5.events.Event;
import org.w3c.dom5.events.EventListener;

public class EventExecution implements Runnable {

    public Event event;
    public EventListener listener;
    
    public EventExecution(Event event, EventListener listener) {
        this.event = event;
        this.listener = listener;
    }

    public void run() {
        listener.handleEvent(event);
    }
    
}
