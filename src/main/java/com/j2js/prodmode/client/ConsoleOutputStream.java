/*
 * Copyright (c) 2005 Wolfgang Kuehn
 */

package com.j2js.prodmode.client;

import java.io.OutputStream;

import javascript.ScriptHelper;

/**
 * This stream write to the system specific console.
 */
public final class ConsoleOutputStream extends OutputStream {
    
    /**
     * Writes the specified byte to this output stream.
     */
    public void write(int b) {
        write(new byte[]{(byte) b});
    }

    /** 
     * Writes b.length bytes from the specified byte array to this output stream.
     */
    public void write(byte[] bytes) {
        write(new String(bytes));
    }
    
    public void write(String s) {
        ScriptHelper.put("s", s);
        ScriptHelper.eval("j2js.print(s)");
    }
}
