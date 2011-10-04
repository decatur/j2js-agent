package j2js.client;

import j2js.Global;

import org.w3c.dom5.Document;
import org.w3c.dom5.events.EventListener;
import org.w3c.dom5.events.EventTarget;
import org.w3c.dom5.html.HTMLButtonElement;
import org.w3c.dom5.html.HTMLElement;

/**
 * Utility class to bind a event listener to a DOM element.
 * <br/>Usage:
 * <pre>
        new AbstractEventListener(document, "ok") {
            public void handleEvent(Event evt) {
                // do something.
            }
        };
 * </pre>
 * 
 * @author j2js.com
 */
public abstract class AbstractEventListener implements EventListener {     
    
    public Document document;
    
    public AbstractEventListener(Document theDocument) {
        document = theDocument;
    }
    
    public AbstractEventListener(Document theDocument, String actionId) {
        document = theDocument;
        bindTo(actionId);
    }
    
    public AbstractEventListener(HTMLButtonElement button) {
        document = button.getOwnerDocument();
        ((EventTarget) button).addEventListener("click", this, false);
    }
    
    public AbstractEventListener(HTMLElement element, String eventType) {
        document = element.getOwnerDocument();
        ((EventTarget) element).addEventListener(eventType, this, false);
    }
    
    public void bindTo(String actionId) {
        EventTarget target = (EventTarget) document.getElementById(actionId);
        if (target == null) throw new IllegalArgumentException("No element with id " + actionId);
        bindTo(target);
    }
    
    /**
     * Binds this listener to the <i>click</i> event.
     * Anchors are not allowed as targets, because the implicit handling of
     * click events by anchor elements may interfere with the desired handling
     * mechanism (for example beforeunload on MS-IExplorer).
     * 
     * @param target the event target.
     */
    public void bindTo(EventTarget target) {
        target.addEventListener("click", this, false);
        if (((HTMLElement) target).getTagName().equals("A")) {
            // Anchors do not behave uniform across browsers with respect
            // to the click event handler and the default event.
            throw new IllegalArgumentException("Cannot bind to an anchor");
        }
    }
    
    public void bindTo(EventTarget target, String action) {
        target.addEventListener(action, this, false);
    }
    
    public HTMLButtonElement bindTo(HTMLElement buttonBar, String buttonName, String buttonLabel, String buttonToolTip) {
        HTMLButtonElement button = (HTMLButtonElement) document.createElement("BUTTON");
        button.setClassName("shellButton");
        button.setName(buttonName);
        Global.HTMLUtils.setText(button, buttonLabel);
        if (buttonToolTip != null) {
            button.setTitle(buttonToolTip);
        }
        buttonBar.appendChild(button);
        this.bindTo((EventTarget) button);
        return button;
    }
    
}