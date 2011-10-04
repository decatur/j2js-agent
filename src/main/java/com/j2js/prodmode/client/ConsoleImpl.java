package com.j2js.prodmode.client;

import j2js.client.Console;
import javascript.ScriptHelper;

public class ConsoleImpl implements Console {

    public void clear() {
        ScriptHelper.eval("j2js.console_clear()");
    }

    public void write(String message) {
        ScriptHelper.put("message", message);
        ScriptHelper.eval("j2js.console_write(message)");
    }

}
