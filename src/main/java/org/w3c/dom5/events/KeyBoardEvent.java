/*
 * Copyright (c) 2006 j2js.com,
 *
 * All Rights Reserved. This work is distributed under the j2js Software License [1]
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE.
 *
 * [1] http://www.j2js.com/license.txt
 */

package org.w3c.dom5.events;

import org.w3c.dom5.events.UIEvent;

/**
 * This is a subset of the Java binding for the DOM Level 3 Events Specification.
 * 
 * @author j2js.com
 */
public interface KeyBoardEvent extends UIEvent {
    
    /**
     * Returns the code of the key.
     */
    public String getKeyIdentifier();
}
