/*
 * Copyright (c) 2007 j2js.com,
 *
 * All Rights Reserved. This work is distributed under the j2js Software License [1]
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE.
 *
 * [1] http://www.j2js.com/license.txt
 */

package j2js.net;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.eclipse.swt.graphics.Point;

public class Marshaller extends HashMap<String, Object> {
    
    public static String extractPropertyName(int prefixLength, String token) {
        return token.substring(prefixLength, prefixLength+1).toLowerCase() + token.substring(prefixLength+1, token.length());
    }
    
    private static List toList(Point p) {
        List<Object> list = new ArrayList<Object>();
        list.add(p.x);
        list.add(p.y);
        return list;
    }
    
    public static Point fromList(List list) {
        Point p = new Point(0, 0);
        p.x = ((Number) list.get(0)).intValue();
        p.y = ((Number) list.get(1)).intValue();
        return p;
    }
    
    public Object put(String key, Object value) {
        if (value != null) {
            super.put(key, value);
        }
        return value;
    }
    
    public Object put(String key, Point value) {
        if (value != null) {
            super.put(key, toList(value));
        }
        return value;
    }
    
}
