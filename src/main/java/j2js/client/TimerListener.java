/*
 * Copyright (c) 2005 Wolfgang Kuehn
 */

package j2js.client;

import org.w3c.dom5.views.Window;

/**
 * Object implementing this interface may be registered with a {@link org.w3c.dom5.views.Window Window}
 * as timeout listeners.
 */
public interface TimerListener {
    public void handleEvent(Window window);
}
