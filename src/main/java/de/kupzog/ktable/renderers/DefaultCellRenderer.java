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
package de.kupzog.ktable.renderers;

import j2js.Global;

import org.w3c.dom5.Text;
import org.w3c.dom5.html.HTMLDocument;
import org.w3c.dom5.html.HTMLElement;
import org.w3c.dom5.html.HTMLTableCellElement;

import de.kupzog.ktable.KTableCellRenderer;
import de.kupzog.ktable.KTableModel;


/**
 * Class that provides additional facilities commonly used when writing custom renderers.
 * 
 * @author j2js
 */
public class DefaultCellRenderer implements KTableCellRenderer {

    public void drawCell(HTMLTableCellElement td, int col, int row, Object content, boolean header, KTableModel model) {
        if (content == null) content = "null";
        
        // Cannot use <code>content instanceof HTMLElement</code>.
        if (Global.HTMLUtils.isHTMLElement(content)) {
            td.appendChild((HTMLElement) content);
        } else {
            HTMLDocument doc = (HTMLDocument) td.getOwnerDocument();
            Text text = doc.createTextNode(content.toString());
            td.appendChild(text);
        }
    }
    
}
