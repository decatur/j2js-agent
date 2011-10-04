/*
 * Copyright (c) 2007 j2js.com,
 *
 * All Rights Reserved. This work is distributed under the j2js Software License [1]
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE.
 *
 * [1] http://www.j2js.com/license.txt
 */

package com.j2js.prodmode.net.JSON;

import j2js.net.JSON.AbstractJSON;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javascript.ScriptHelper;

public final class JSONImpl extends AbstractJSON {

    private String typeOf(Object obj) {
        ScriptHelper.put("obj", obj);
        String type = (String) ScriptHelper.eval("typeof(obj)");
        if (type.equals("string") || type.equals("number")) return type;
        
        if (!type.equals("object")) return type;
        
        ScriptHelper.put("obj", obj);
        String t = (String) ScriptHelper.eval("typeof(obj.join)");
        if (t.equals("function")) return "array";
        return type; // That is "object".
    }
    
    public Object parse(String s) {
        // Note that typeof(eval("<?php ?>")) == "xml" on some browsers.
        ScriptHelper.eval("var object;");
        ScriptHelper.put("s", s);
        Object obj = null;
        try {
             obj = ScriptHelper.eval("eval('object = ' + s)");
        } catch (Exception e) {
            throw new RuntimeException("Could not deserialize: " + s);
        }
        return nativeToJava(obj);
    }
    
    public Object nativeToJava(Object obj) {
        if (obj == null) return null;
        
        Object out;
        String type = typeOf(obj);
        
        if (type.equals("array")) {
            List<Object> list = Arrays.asList((Object[]) obj);
            for (int i=0; i<list.size(); i++) {
                list.set(i, nativeToJava(list.get(i)));
            }
            out = list;
        } else if (type.equals("object")) {
            Map map = new HashMap(obj);
            Set<String> keys = map.keySet();
            for (String key : keys) {
                map.put(key, nativeToJava(map.get(key)));
            }
            out = new HashMap(obj);
        } else if (type.equals("string")) {
            out = (String) obj;
        } else if (type.equals("number")) {
            ScriptHelper.put("obj", obj);
            out = ScriptHelper.evalDouble("obj");
        } else if (type.equals("boolean")) {
            ScriptHelper.put("obj", obj);
            out = ScriptHelper.evalBoolean("obj");
        } else {
            throw new RuntimeException("Unsupported type: " + type);
        }
        return out;
    }
}
