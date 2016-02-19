package j2js.net;


/**
 * Listener which can be registered with an <code>XMLHttpRequest</code> Object to be invoked
 * when readyState changes value.
 */
public interface ReadyStateChangeListener {
    
    /**
     * The listener may be invoked multiple times when readyState is 3.
     * 
     * @param request The <code>XMLHttpRequest</code> from which originates the event
     */
    public void handleEvent(HttpRequest request);
}
