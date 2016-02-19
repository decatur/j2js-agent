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

import java.util.ArrayList;
import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.w3c.dom5.Node;
import org.w3c.dom5.events.Event;
import org.w3c.dom5.events.EventListener;
import org.w3c.dom5.events.EventTarget;
import org.w3c.dom5.html.HTMLDocument;
import org.w3c.dom5.html.HTMLElement;
import org.w3c.dom5.html.HTMLInputElement;
import org.w3c.dom5.html.HTMLTableCellElement;
import org.w3c.dom5.html.HTMLTableElement;
import org.w3c.dom5.html.HTMLTableRowElement;
import org.w3c.dom5.html.HTMLTableSectionElement;

/**
 * Custom drawn tabel widget for SWT GUIs.
 * <p>
 * The idea of KTable is to have a flexible grid of cells to display data in it.
 * The class focuses on displaying data and not on collecting the data to
 * display. The latter is done by the <code>KTableModel</code> which has to be implemented
 * for each specific case. Some default tasks are done by a base implementation
 * called <code>KTableDefaultModel</code>. Look also into <code>KTableSortedModel</code> that provides 
 * a transparent sorting of cells.<br>
 * The table asks the table model for the amount of
 * columns and rows, the sizes of columns and rows and for the content of the
 * cells which are currently drawn. Even if the table has a million rows, it
 * won�t get slower because it only requests those cells it currently draws.
 * Only a bad table model can influence the drawing speed negatively.
 * <p>
 * When drawing a cell, the table calls a <code>KTableCellRenderer</code> to do this work.
 * The table model determines which cell renderer is used for which cell. A
 * default renderer is available (<code>KTableCellRenderer.defaultRenderer</code>), but the
 * creation of self-written renderers for specific purposes is assumed. 
 * Some default renderers are available in the package <code>de.kupzog.ktable.cellrenderers.*</code>.
 * <p>
 * KTable allows to F columns and rows. Each column can have an individual
 * size while the rows are all of the same height except the first row. Multiple
 * column and row headers are possible. These "fixed" cells will not be scrolled
 * out of sight. The column and row count always starts in the upper left corner
 * with 0, independent of the number of column headers or row headers.
 * <p>
 * It is also possible to span cells over several rows and/or columns. The KTable
 * asks the model do provide this information via <code>belongsToCell(col, row)</code>.
 * This method must return the cell the given cell should be merged with.
 * <p>
 * Changing of model values is possible by implementations of <code>KTableCellEditor</code>.
 * Again the KTable asks the model to provide an implementation. Note that there
 * are multiple celleditors available in the package <code>de.kupzog.ktable.editors</code>!
 * 
 * @author Friederich Kupzog
 * @see de.kupzog.ktable.KTableModel
 * @see de.kupzog.ktable.KTableCellRenderer
 * @see de.kupzog.ktable.KTableCellEditor
 */
public class KTable implements EventListener {    
    
    private int style;
    private HTMLElement container;
    private Node refChild;
    private KTableModel model;
    private HTMLTableElement tableElement;
    private HTMLDocument doc;
    
    private List<Integer> selectedRows = new ArrayList<Integer>();

    //////////////////////////////////////////////////////////////////////////////
    // CONSTRUCTOR & DISPOSE
    //////////////////////////////////////////////////////////////////////////////

    /**
     * Creates a new KTable.
     * 
     * possible styles: 
     * <ul>
     * <li><b>SWT.V_SCROLL</b> - show vertical scrollbar and allow vertical scrolling by arrow keys</li>
     * <li><b>SWT.H_SCROLL</b> - show horizontal scrollbar and allow horizontal scrolling by arrow keys</li>
     * <li><b>SWTX.AUTO_SCROLL</b> - Dynamically shows vertical and horizontal scrollbars when they are necessary.</li>
     * <li><b>SWTX.FILL_WITH_LASTCOL</b> - Makes the table enlarge the last column to always fill all space.</li>
     * <li><b>SWTX.FILL_WITH_DUMMYCOL</b> - Makes the table fill any remaining space with dummy columns to fill all space.</li>
     * <li><b>SWT.FLAT</b> - Does not paint a dark outer border line.</li>
     * <li><b>SWT.MULTI</b> - Sets the "Multi Selection Mode".
     * In this mode, more than one cell or row can be selected.
     * The user can achieve this by shift-click and ctrl-click.
     * The selected cells/rows can be scattored ofer the complete table.
     * If you pass false, only a single cell or row can be selected.
     * This mode can be combined with the "Row Selection Mode".</li>
     * <li><b>SWT.FULL_SELECTION</b> - Sets the "Full Selection Mode".
     * In the "Full Selection Mode", the table always selects a complete row.
     * Otherwise, each individual cell can be selected.
     * This mode can be combined with the "Multi Selection Mode".</li>
     * <li><b>SWTX.EDIT_ON_KEY</b> - Activates a possibly present cell editor
     * on every keystroke. (Default: only ENTER). However, note that editors
     * can specify which events they accept.</li>
     * <li><b>SWTX.MARK_FOCUS_HEADERS</b> - Makes KTable draw left and top header cells
     * in a different style when the focused cell is in their row/column.
     * This mimics the MS Excel behavior that helps find the currently
     * selected cell(s).</li>
     * <li><b>SWT.HIDE_SELECTION</b> - Hides the selected cells when the KTable
     * looses focus.</li>
     * After creation a table model should be added using setModel().
     */
    public KTable(HTMLElement theContainer, Node theRefChild, int theStyle) {
        container = theContainer;
        refChild = theRefChild;
        style = theStyle;
        
        doc = (HTMLDocument) container.getOwnerDocument();
        tableElement = (HTMLTableElement) doc.createElement("TABLE");
        tableElement.setCellPadding("0px");
        tableElement.setCellSpacing("0px");
        tableElement.getStyle().setMargin("5pt");
        
        tableElement.setBorder("1");
        container.insertBefore(tableElement, refChild);
    }

    public void dispose() {
    }
    
    public void setModel(KTableModel theModel) {
        model = theModel;
    }
    
    public void redraw() {
        
        if (tableElement.getFirstChild() != null) {
            tableElement.removeChild(tableElement.getFirstChild());
            selectedRows.clear();
        }
        
        HTMLTableSectionElement tbody = (HTMLTableSectionElement) doc.createElement("TBODY");
        tableElement.appendChild(tbody);
        
        for (int row=0; row<model.getRowCount(); row++) {
            HTMLTableRowElement tr = (HTMLTableRowElement) doc.createElement("TR");
            tr.setAttribute("KTable.rowIndex", String.valueOf(row));
            // Eager DOM pinning!
            tbody.appendChild(tr);
            
            HTMLTableCellElement td;
            String tagName;
            
            if (row >= model.getFixedHeaderRowCount()) {
                tagName = "TD";
                if ((style & SWT.FULL_SELECTION) != 0) {
                    td = (HTMLTableCellElement) doc.createElement(tagName);
                    HTMLInputElement input = (HTMLInputElement) doc.createElement("input");
                    input.setAttribute("type", "checkbox");
                    EventTarget target = (EventTarget) input;
                    target.addEventListener("change", this, false);
                    td.appendChild(input);
                    tr.appendChild(td);
                }
            } else {
                tagName = "TH";
                if ((style & SWT.FULL_SELECTION) != 0) {
                    td = (HTMLTableCellElement) doc.createElement(tagName);
                    tr.appendChild(td);
                }
            }
            
            for (int col=0; col<model.getColumnCount(); col++) {
                Point masterCellLocation = model.belongsToCell(col, row);
                if (masterCellLocation.x != col || masterCellLocation.y != row) {
                    continue;
                }
                
                td = (HTMLTableCellElement) doc.createElement(tagName);
                // Eager DOM pinning!
                tr.appendChild(td);
                
                Point cellSpan = model.getCellSpan(col, row);
                if (cellSpan.x > 1) {
                    td.setColSpan(cellSpan.x);
                }
                if (cellSpan.x > 1) {
                    td.setRowSpan(cellSpan.y);
                }
                
                KTableCellRenderer renderer = model.getCellRenderer(col, row);
                renderer.drawCell(td, col, row, model.getContentAt(col, row), false, model);
                String toolTip = model.getTooltipAt(col, row);
                if (toolTip != null) {
                    td.setTitle(toolTip);
                }
            }
        }
    }
    
    public void handleEvent(Event evt) {
        HTMLInputElement input = (HTMLInputElement) evt.getTarget();
        HTMLTableRowElement tr = (HTMLTableRowElement) input.getParentNode().getParentNode();
        int rowIndex = Integer.parseInt(tr.getAttribute("KTable.rowIndex"));
        if (input.getChecked()) {
            selectedRows.add(rowIndex);
        } else {
            selectedRows.remove((Integer) rowIndex);
        }
    }
    
    /**
     * Returns an array of the selected row numbers.
     * Returns null if not in Row Selection Mode.
     * Returns an array with one or none element if not in Multi Selection Mode.<p>
     * NOTE: This returns the cell indices as seen by the KTable. If you use a sorting model,
     * don't forget to map the indices properly.
     */
    public int[] getRowSelection() {
        int[] rowIndices = new int[selectedRows.size()];

        int i = 0;
        for (Integer rowIndex : selectedRows) {
            rowIndices[i++] = rowIndex;
        }
        
        return rowIndices;
    }
    
    public HTMLTableElement getTableElement() {
        return tableElement;
    }

 }