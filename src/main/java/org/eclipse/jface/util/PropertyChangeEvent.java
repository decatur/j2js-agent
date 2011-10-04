package org.eclipse.jface.util;

public class PropertyChangeEvent {
    
    private Object newValue;
    private Object oldValue;
    
    /**
     * Creates a new property change event.
    */
    public PropertyChangeEvent(Object source, String property, Object theOldValue, Object theNewValue) {
        newValue = theNewValue;
        oldValue = theOldValue;
    }

    /**
     * Returns the new value of the property.
     */
    public Object getNewValue() {
        return newValue;
    }

        
    /**
     * Returns the old value of the property.
     */
    public Object getOldValue() {
        return oldValue;
    }
}
