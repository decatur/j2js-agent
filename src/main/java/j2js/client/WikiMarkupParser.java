package j2js.client;

import j2js.Global;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.w3c.dom5.Element;
import org.w3c.dom5.css.CSSStyleDeclaration;
import org.w3c.dom5.html.HTMLAnchorElement;
import org.w3c.dom5.html.HTMLDocument;
import org.w3c.dom5.html.HTMLElement;
import org.w3c.dom5.html.HTMLImageElement;
import org.w3c.dom5.html.HTMLPreElement;
import org.w3c.dom5.html.HTMLTableElement;

/**
 * A simple parser for a Wiki-style text markup. The supported markup is a subset of
 * the markup used in Confluence by {@link <a href="http://www.atlassian.com">atlassian</a>}.
 * See {@link <a href="../../../../powerdash/wikiformat.html">wikiformat</a>} for examples.
 * 
 * @author j2js.com
 */
public class WikiMarkupParser {
    
    class LineIterator {
        private String[] lines;
        private int index;
        
        LineIterator(String text) {
            // Strip carriage return (at least IE6 uses those as newline in textarea elements). 
            lines = text.replaceAll("\r", "").split("\n");
            index = -1;
        }
        
        public String next() {
            if (index+1 < lines.length) {
                return lines[++index];
            }
            return null;
        }
        
        public String previous() {
            if (index-1 >= 0) {
                return lines[--index];
            }
            return null;
        }
    }
    
    private Map<String, String> tagsBySeparator;
    private Map<String, String> propertiesByKey;
    
    /*
     * Pattern matching the supported text formating markup '_' and '*', the
     * linking markup '!' and '[', and the table cell markup '|'.
     */
    private Pattern pattern = Pattern.compile("[\\[_*!|]");
    
    private HTMLDocument document = null;
    private Element root;
    
    public WikiMarkupParser() {
        tagsBySeparator = new HashMap<String, String>();
        tagsBySeparator.put("[", "a");
        tagsBySeparator.put("*", "b");
        tagsBySeparator.put("_", "i");
        tagsBySeparator.put("!", "img");
        
        propertiesByKey = new HashMap<String, String>();
    }
    
    public void addProperty(String key, String value) {
        propertiesByKey.put(key, value);
    }
    
    /**
     * Converts the specified Wiki markup to an HTML fraqment and appends it to the
     * specified container.
     * 
     * @param markup the Wiki markup
     */
    synchronized public void parse(HTMLElement container, String markup) {
        if (markup == null) return;
        
        document = (HTMLDocument) container.getOwnerDocument();
        root = container;
        Element currentElement = container;
     
        LineIterator iterator = new LineIterator(markup);
        boolean newLinePending = false;
        
        String line;
        
        while ((line=iterator.next()) != null) {
            
            if (line.length() == 0) {
                currentElement = document.createElement("p");
                root.appendChild(currentElement);
                newLinePending = false;
                continue;
            } else if (newLinePending) {
                currentElement.appendChild(document.createElement("br"));
            }
            
            newLinePending = false;
            
            int level;
            for (level=0; level<line.length(); level++) {
                char c = line.charAt(level);
                if (c != '*' && c != '#') break;
            }
            
            if (level > 0) {
                // We are processing a list item.
                
                if (currentElement.getTagName().equalsIgnoreCase("li")) {
                    currentElement = (Element) currentElement.getParentNode();
                } else if (currentElement.getTagName().equalsIgnoreCase("p")) {
                    currentElement = root;
                }
                
                int currentLevel = getNestingLevel(currentElement);
                if (currentLevel < level) {    
                    Element ul = document.createElement(line.charAt(level-1)=='*'?"ul":"ol");
                    currentElement.appendChild(ul);
                    currentElement = ul;
                } else if (level < currentLevel) {
                    for (int i = level; i<currentLevel; i++) {
                        currentElement = (Element) currentElement.getParentNode();
                    }
                }
                line = line.substring(level);
                Element li = document.createElement("li");
                currentElement.appendChild(li);
                currentElement = li;
                parseText(line, currentElement, false);
            } else if (line.matches("^h\\d\\..*")) {
                Element h = document.createElement("h" + line.charAt(1));
                parseText(line.substring(3) + " ", h, false);
                currentElement = root;
                currentElement.appendChild(h);
            } else if (line.matches("^\\{\\s*\\w+\\s*\\}")) {
                Pattern pattern = Pattern.compile("^\\{\\s*(\\w+)\\s*\\}");
                Matcher matcher = pattern.matcher(line);
                matcher.find();
                String macroName = matcher.group(1);
                
                StringBuffer sb = new StringBuffer();
                do {
                    line = iterator.next();

                    if (line == null || line.matches("^\\{\\s*" + macroName + "\\s*\\}")) {
                        break;
                    }
                    sb.append(line);
                    sb.append('\n');
                } while (true);
                processMacro(currentElement, macroName, sb.toString());
            } else {
                line = line.trim();
                if (line.startsWith("|")) {
                    HTMLTableElement table = (HTMLTableElement) document.createElement("table");
                    table.setBorder("1");
                    table.setWidth("100%");
                    currentElement = root;
                    currentElement.appendChild(table);
                    Element tBody = document.createElement("tbody");
                    table.appendChild(tBody);
                    while (line.startsWith("|")) {
                        parseTableRow(tBody, line);
                        line = iterator.next();
                        if (line == null) {
                            break;
                        }
                        line = line.trim();
                    }
                } else {
                    parseText(line, currentElement, false);
                    newLinePending = true;
                }
            }
        }
    }
    
    void processMacro(Element currentElement, String macroName, String text) {
        if (macroName.equals("noformat")) {
            HTMLPreElement pre = (HTMLPreElement) document.createElement("pre");
            Global.HTMLUtils.appendText(pre, text);
            currentElement = root;
            currentElement.appendChild(pre);
        } else if (macroName.equals("html")) {
            HTMLElement div = (HTMLElement) document.createElement("div");
            currentElement.appendChild(div);
            div.setInnerHTML( text );
        } else {
            processMacro(currentElement, "noformat", "Undefined macro " + macroName);
        }
    }
    
    int getNestingLevel(Element e) {
        int i=0;
        while (e != null && (e.getTagName().equalsIgnoreCase("ul") || e.getTagName().equalsIgnoreCase("ol"))) {
            i++;
            e = (Element) e.getParentNode();
        }
        return i;
    }
    
    String unescape(String s) {
        return s.replaceAll("\\\\([\\[_*!|])", "$1");
    }
    
    void parseTableRow(Element table, String line) {
        Element tr = document.createElement("tr");
        table.appendChild(tr);
        int index = 0;
        
        while (index < line.length()) {
            Element td;
            index++;
            if (index < line.length() && line.charAt(index) == '|') {
                td = document.createElement("th");
                index++;
            } else {
                td = document.createElement("td");
            }
            
            if (index >= line.length()) break;
            
            line = line.substring(index);
            tr.appendChild(td);
            
            index = parseText(line, td, true);
        }
    }
    
    int parseText(String line, Element e, boolean tableCellDelimiterIsStopCharacter) {
        Matcher matcher = pattern.matcher(line);
        
        /*
         * 0      i       jk         l         
         * sdfghf*dfsfsdgh_hdfsdfsdfsd_gdfgdfg
         */

        
        int endIndex = line.length();
        
        // The index from where to look for the next match of \[_*!|
        int i = 0;
        
        // The index from where no text has yet been appended. 
        int index = 0;
        
        while (true) {
            if (!matcher.find(i)) {
                break;
            }
            String sep = matcher.group();
            int j = matcher.start();
            i = matcher.end();
            
            if (j>0 && line.charAt(j-1)=='\\') {
                continue;
            }
            
            if (sep.equals("|")) {
                if (tableCellDelimiterIsStopCharacter) {
                    endIndex = j;
                    break;
                }
                continue;
            }
            
            String closingSep = sep;
            if (sep.equals("[")) closingSep = "]";
            int l = line.indexOf(closingSep, i);
            if (l== -1 || line.charAt(l-1)=='\\') {
                continue;
            }
            
            int k = i;
            i = l + closingSep.length();
            
            Element el;
            if (j > index) {
                e.appendChild(document.createTextNode(unescape(line.substring(index, j))));
            }
            el = document.createElement(tagsBySeparator.get(sep));
            if (sep.equals("[")) {
                populateLink((HTMLAnchorElement) el, line.substring(k, l));
            } else if (sep.equals("!")) {
                populateImage((HTMLImageElement) el, line.substring(k, l));
            } else {
                el.appendChild(document.createTextNode(unescape(line.substring(k, l))));
            }
            e.appendChild(el);
            index = i;
        }
        
        e.appendChild(document.createTextNode(unescape(line.substring(index, endIndex))));
        return endIndex;
    }
    
    /**
     * Returns a copy of the specified text with leading and trailing whitespace, and with the
     * specified leading and trailing delimiter omitted.
     */
    private String trim(String text, char leadingDelimiter, char trailingDelimiter) {
        text = text.trim();
        int beginIndex = 0;
        int endIndex = text.length();
        
        if (text.charAt(beginIndex) == leadingDelimiter) {
            beginIndex = 1;
        }
        
        if (text.charAt(endIndex-1) == trailingDelimiter) {
            endIndex--;
        }
        
        return text.substring(beginIndex, endIndex).trim();
    }
    
    /**
     * Pattern of the form [display|url].
     * Nested groups:                                  01     12   3  32 0 */
    public final Pattern linkPattern = Pattern.compile("([^|]*)(\\|(.*))?");
    
    /**
     * Populates the anchor element with href and optional display text, 
     * both parsed from wiki markup matching {@link WikiMarkupParser#linkPattern}.
     */
    public void populateLink(HTMLAnchorElement a, String markup) {
        markup = trim(markup, '[', ']');
        
        Matcher matcher = linkPattern.matcher(markup);
        if (!matcher.find()) return;
        
        String display = matcher.group(1);
        String href = matcher.group(3);
        
        if (display == null) {
            display = "";
        } else {
            display = display.trim();
        }
        
        if (href == null) {
            href = display;
        } else {
            href = href.trim();
        }
        
        a.setHref(href);
        a.appendChild(document.createTextNode(display));
        a.setTarget("_blank");
    }
    
    /**
     * Parses a comma-separated list of key/value pairs. Key and optional value are separated
     * by a = character.
    */
    HashMap<String, String> parseParametersByKey(String markup) {
        HashMap<String, String> parametersByKey = new HashMap<String, String>();
        String[] parameters = markup.split(",");
        for (String parameter : parameters) {
            String[] keyValue = parameter.split("=");
            String value = null;
            if (keyValue.length == 2) {
                value = keyValue[1].trim().toLowerCase();
            }
            parametersByKey.put(keyValue[0].trim().toLowerCase(), value);
        }
        return parametersByKey;
    }
    
    /**
     * Parses an image URI of the form <code>user^imgname</code>, <code>relative url</code> or
     * <code>absolute url</code>.
     */
    public String parseImageURI(String markup, Map<String, String> parametersByKey) {
        String[] parts = markup.split("\\^");
        if (parts.length == 1) {
            // Relative or absolute URL.
            return parts[0];
        }
        
        String url = propertiesByKey.get("serverURI");
        url += "?path=" + parts[1] + "&user=" + parts[0];

        String thumbnail = parametersByKey.get("thumbnail");
        
        if (parametersByKey.containsKey("thumbnail") && (thumbnail == null || thumbnail.equals("true"))) {
            url += "&type=icon";
        } /*else if ("false".equals(propertiesByKey.get("userAgentSupportsPNG"))) {
            url += "&type=alt";
        }*/
        
        return url;
    }
    
    private Map<String, String> getImageParameters(final String markup) {
        String[] parts = trim(markup, '!', '!').split("\\|");
        HashMap<String, String> parametersByKey;
        // Process parameters.
        if (parts.length == 2) {
            parametersByKey = parseParametersByKey(parts[1]);
        } else {
            parametersByKey = new HashMap<String, String>();
        }
        
        parametersByKey.put("uri", parts[0]);
        
        return parametersByKey;
    }
    
    /**
     * Populates the image element with src and optional attributes.
     * The markup must follow the pattern !uri! or !uri|parameters!.
     */
    public void populateImage(HTMLImageElement img, final String markup) {
        Map<String, String> parametersByKey = getImageParameters(markup);
        
        String url = parseImageURI(parametersByKey.get("uri"), parametersByKey);
        img.setSrc(url);
        
        CSSStyleDeclaration style = img.getStyle();
        
        if (parametersByKey.containsKey("width")) {
            style.setWidth(parametersByKey.get("width") + "px");
        }
        
        if (parametersByKey.containsKey("height")) {
            style.setHeight(parametersByKey.get("height") + "px");
        }
        
        if (parametersByKey.containsKey("alt")) {
            img.setAlt(parametersByKey.get("alt"));
        } else {
            img.setAlt(markup);
        }
    }
}
