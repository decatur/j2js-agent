/*
 * Copyright (c) 2007 Wolfgang Kuehn
 */

package j2js.net;

import j2js.Global;

import org.w3c.dom5.css.CSSStyleDeclaration;
import org.w3c.dom5.html.HTMLCollection;
import org.w3c.dom5.html.HTMLDocument;
import org.w3c.dom5.html.HTMLElement;
import org.w3c.dom5.html.HTMLFormElement;
import org.w3c.dom5.html.HTMLIFrameElement;
import org.w3c.dom5.html.HTMLInputElement;

/**
 * The FormHttpRequest sends HTTP POST requests by submitting a client side form.
 * 
 * The query part of the <code>uri</code> submitted to the server will always contain the keys
 * <dl>
 *     <dt>requestId</dt>
 *     <dd>The <code>requestId</code> will identify each response with its associated request once the
 * response is loaded by the client.
 *     </dd>
 *     <dt>mimeType</dt>
 *     <dd>The <code>mimeType</code> is always set to <code>text/html</code> so that the server
 *     knows that this is a ScriptHttpRequest.
 *     </dd>
 * <dl>
 * The request will have the parameters
 * <dl>
 *     <dt>data</dt>
 *     <dd>The JSON payload for this request.
 *     </dd>
 * <dl> 
 * </p><p>
 * On request, the server must respond with
 * <pre>
 *     &lt;html>&lt;head>&lt;script>
 *     j2js.onScriptLoad(requestId, responseJSON);
 *     &lt;/script>&lt;/head>&lt;body>&lt;/body>&lt;/html>
 * </pre>
 * where <code>requestId</code> is the request identifier and
 * <code>responseJSON</code> is a legal <code>JavaScript Literal Object (JSON)</code>.
 * </p>
 */
public class FormHttpRequest extends HtmlHttpRequest {

    private static String MULTIPART = "multipart/form-data";
    private static String URLENCODED = "application/x-www-form-urlencoded";
    
    private String encoding;
    private HTMLFormElement form;
    
    private static FormHttpRequest singleton;
    
    public static FormHttpRequest getSingleton() {
        if (singleton == null) {
            try {
                singleton = new FormHttpRequest();
            } catch (Exception e) {}
        }
        return singleton;
    }
    
    private FormHttpRequest() {
        super();
        HTMLDocument document = Global.document;
        HTMLElement div = (HTMLElement) document.createElement("div");
        document.getBody().appendChild(div);
        String s = "<form action='about:blank' method='post' target='REMOTE_AGENT_FRAME' enctype='text/html' acceptCharset='ISO-8859-1'><input type='hidden' name='data'/></form>";
        div.setInnerHTML(s);
        HTMLCollection forms = Global.document.getForms();
        form = (HTMLFormElement) forms.item(forms.getLength()-1);
        
        encoding = URLENCODED;
        init();
    }
    
    /**
     * <b>Important</b>: Currently, there can be only one FormHttpRequest instance.
     * Obtain this instance through {@link HttpRequestFactory#getSingleton(String, String, String)}.
     */
    public FormHttpRequest(HTMLFormElement theForm) {
        super();
        
        if ( theForm == null ) {
            throw new IllegalArgumentException("HTMLFormElement must be supplied");
        }
        
        form = theForm;
        // TODO: Insert input data element here.
        encoding = MULTIPART;
        
        init();
    }
    
    private void init() {
        HTMLDocument document = Global.document;
        
        HTMLIFrameElement frame;
        try {
            // MS-IE workaround. May otherwise not honor the frame name.
            frame = (HTMLIFrameElement) document.createElement("<IFRAME name='REMOTE_AGENT_FRAME'></IFRAME>");
        } catch (Exception e) {
            frame = (HTMLIFrameElement) document.createElement("IFRAME");
            frame.setName("REMOTE_AGENT_FRAME");
        }
        CSSStyleDeclaration style = frame.getStyle();
        style.setPosition("absolute");
        // Best not to use display:none. Frame may be ignored!
        style.setVisibility("hidden");
        
        // Note: Source may or may not exist. If a 404 response is fastes, then choose the latter.
        frame.setSrc("postBlank.html");
        document.getBody().appendChild(frame);
    }

    /**
     * Same as {@link XMLHttpRequest#open(String, String, boolean, String, String)}, with the following
     * differences: 
     * <ol>
     * <li>Same-origin security restrictions are not enforced;</li>
     * <li>Only the POST method is allowed;</li>
     * <li>Only asynchronous requests are allowed.</li>
     * </ol>
     */
    public void open(String method, String uri, boolean isAsync, String user, String password) {
        if (!"post".equalsIgnoreCase(method)) throw new UnsupportedOperationException("Method not supported: " + method);
        super.open(method, uri, isAsync, user, password);
        uriBuffer.append("&mimeType=text/html");
    }

    public void send(String data) {
        prepareSend();
        
        form.setEnctype(encoding);
        form.setAction(uriBuffer.toString());
        HTMLInputElement input = (HTMLInputElement) form.getFirstChild();
        input.setValue(data);
        form.submit();
    }

    protected void handleEvent(int status, Object jsonObject) {
        super.handleEvent(status, jsonObject);
        form.setAction("postBlank.html");
        form.submit();
    } 

}
