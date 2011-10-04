package j2js.client;

public final class Engine {

    private static Engine engine;
    
    private GenericEventListener listener;
    
    public static Engine getEngine() {
        if (engine == null) {
            engine = new Engine();
        }
        return engine;
    }
    
    private Engine() {}
    
    /**
     * This method is called by the Script Runtime every time an unhandled
     * exception occurs.
     */
    public void handleEvent(Throwable error) {
        error.printStackTrace();
        if (listener != null) listener.handleEvent(error);
    }
    
    public void setErrorListener(GenericEventListener theListener) {
        listener = theListener;
    }
    
}
