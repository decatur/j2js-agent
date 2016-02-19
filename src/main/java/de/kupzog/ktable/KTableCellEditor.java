/*
 * Copyright (C) 2004 by Friederich Kupzog Elektronik & Software
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html

Authors: 
Friederich Kupzog,  fkmk@kupzog.de, www.kupzog.de/fkmk
Lorenz Maierhofer, lorenz.maierhofer@logicmindguide.com

*/
package de.kupzog.ktable;

import org.eclipse.swt.graphics.Rectangle;


public abstract class KTableCellEditor {

    /**
     * Activates the editor at the given position.
     * @param row
     * @param col
     * @param rect
     */
    public void open(KTable table, int col, int row, Rectangle rect) {
        
    }
    
    /**
     * Deactivates the editor.
     * @param save
     * If true, the content is saved to the underlying table.
     */
    public void close(boolean save) {
        
    }
    
    /**
     * Allows that external classes can set the content of 
     * the underlying 
     * @param content The new content to set.
     */
    public abstract void setContent(Object content);

}
