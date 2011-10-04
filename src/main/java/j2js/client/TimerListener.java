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

import org.w3c.dom5.views.Window;

/**
 * Object implementing this interface may be registered with a {@link org.w3c.dom5.views.Window Window}
 * as timeout listeners.
 * 
 * @author j2js.com
 */
public interface TimerListener {
    public void handleEvent(Window window);
}
