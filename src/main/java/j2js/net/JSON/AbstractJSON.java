/*
 * Copyright (c) 2007 Wolfgang Kuehn
 */

package j2js.net.JSON;

import java.util.List;
import java.util.Map;
import java.util.Set;

public abstract class AbstractJSON {
    
    public abstract Object parse(String s);
    
    public abstract Object nativeToJava(Object obj);
    
    public boolean prettyPrint = false;
    
    private boolean newLine;
    private int level = 0;
    private StringBuilder sb;
    
    public String stringify(Object object) {
        newLine = true;
        level = 0;
        sb = new StringBuilder();
        serializeInternal(object);
        return sb.toString();
    }
    
    private String escape(String s) {
        // TODO: Review quoting of backslash!
        s = s.replaceAll("\\r", "");
        s = s.replaceAll("\\\\", "\\\\");
        //s = s.replaceAll("\\n", "\\\\n");
        s = s.replaceAll("\\\"", "\\\\\"");
        return s;
    }
    
    private void newLine() {
        if (prettyPrint) sb.append('\n');
        newLine = true;
    }
    
    private void indent() {
        if (prettyPrint) sb.append('\n');
        newLine = true;
        level++;
    }
    
    private void unindent() {
        if (prettyPrint) sb.append('\n');
        newLine = true;
        level--;
    }
    
    void append(String s) {
        if (newLine) {
            if (prettyPrint) {
                for (int i=0; i<level; i++) {
                    sb.append(' ');
                }
            }
            newLine = false;
        }
        sb.append(s);
        
    }
    
    /**
     * @param object
     * @param sb
     */
    void serializeInternal(Object object) {
        
        if (object instanceof List) {
            List<Object> list = (List<Object>) object;
            append("[");
            indent();
            for (int i=0; i<list.size(); i++) {
                if (i>0) {
                    append(",");
                    newLine();
                }
                serializeInternal(list.get(i));
            }
            unindent();
            append("]");
        } else if (object instanceof Map) {
            Map map = (Map) object;
            append("{");
            indent();
            int i = 0;
            Set<String> keys = map.keySet();
            for (String key : keys) {
                if (i>0) {
                    append(",");
                    newLine();
                }
                append("\"");
                append(key);
                append("\":");
                serializeInternal(map.get(key));
                i++;
            }
            unindent();
            append("}");
        } else if (object instanceof String) {
            append("\"");
            append(escape(object.toString()));
            append("\"");
        } else if (object instanceof Number) {
            append(object.toString());
        } else if (object instanceof Boolean) {
            append(object.toString());
        } else if (object == null) {
            append("null");
        } else {
            throw new RuntimeException("Cannot serialize " + object);
        }
    }
}
