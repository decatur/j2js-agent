/*
 * Copyright (c) 2006 Wolfgang Kuehn
 */

package org.w3c.dom5.events;

import org.w3c.dom5.events.UIEvent;

/**
 * This is a subset of the Java binding for the DOM Level 3 Events Specification.
 */
public interface KeyBoardEvent extends UIEvent {
    
    /**
     * Returns the code of the key.
     */
    public String getKeyIdentifier();
}
