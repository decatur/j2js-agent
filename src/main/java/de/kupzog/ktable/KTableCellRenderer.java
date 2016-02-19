/*
 * Copyright (C) 2004 by Friederich Kupzog Elektronik & Software
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 
 Author: Friederich Kupzog  
 fkmk@kupzog.de
 www.kupzog.de/fkmk
 */
package de.kupzog.ktable;

import org.w3c.dom5.html.HTMLTableCellElement;

import de.kupzog.ktable.renderers.DefaultCellRenderer;

/**
 * @author Friederich Kupzog
 */
public interface KTableCellRenderer {

    public static KTableCellRenderer defaultRenderer = new DefaultCellRenderer();

    /**
     * This method is called from KTable to draw a table cell. <p>
     * Note that there are several helper methods that can do specified things
     * for you.
     * 
     * @param col The column
     * @param row The row
     * @param content The content of the cell (as given by the table model)
     * @param header True if the cell is an unscrollable header cell (not an unscrollable body cell!)
     * the case when fixed row and column elements should be highlighted because a cell in that
     * row and column has focus. 
     * @param model The KTableModel that holds the data for the cell. Note that this is only included
     * into the parameter list to allow more flexible cell renderers. Models might provide additional
     * information that can be requested when rendering.
     */
    public void drawCell(HTMLTableCellElement td, int col, int row, Object content, 
            boolean header, KTableModel model);

}