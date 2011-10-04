j2js.tracing = false;
j2js.nativeStackTrace = false;

var stackTrace = new StackTrace();

function StackTrace() {
    this.entries = new Array();
    this.currentMethod;
}

StackTrace.prototype.fillInCurrentLineNumber = function() {
    if (this.entries.length > 0) {
        this.peek().lineNumber = j2js.ln;
    }
}

StackTrace.prototype.push = function(signature) {
    this.fillInCurrentLineNumber();
    this.entries.push({"signature":signature, "lineNumber":null});
}

StackTrace.prototype.peek = function() {
    return this.entries[this.entries.length-1];
}

StackTrace.prototype.pop = function() {
    var poped = this.entries.pop();
    if (this.entries.length > 0) {
        j2js.ln = this.peek().lineNumber;
    }
    return poped;
}

StackTrace.prototype.toString = function() {
    var s = "";
    var offset = 1;
    for (var i=this.entries.length-1-offset; i>=0; i--) {
        var entry = this.entries[i];
        s += "\n\tat " + entry.signature+ ":" + entry.lineNumber;
    }
    return s;
}

function Clazz(name) {
    this.name = name;
    this.constr = null;
    this.superClass = null;
    this.cp = null;
    this.clinit = false;
    this.isThrowable = false;
}

j2js.classesByName = new Array();

/** This array contains, by index, all
 *    (a) methods signatures as strings
 *    (b) classes as Clazz instance.
 */
j2js.c = new Array();

j2js.defineClass = function(clazz, constructor, superClass) {
    if (clazz == null) throw "Class not declared";
    clazz.superClass = superClass;
    
    clazz.constr = constructor;
    if (clazz.superClass == null) {
        clazz.constr.prototype = new Object();
    } else {
        //j2js.println(clazz.name + " " + clazz.superClass);
        clazz.constr.prototype = new clazz.superClass.constr();
    }
    
    return clazz;
}

j2js.gc = function(classSignatureIndex) {
    var clazz = j2js.c[classSignatureIndex];
	// TODO: Make the compiler find this!
    if (clazz == null) throw "Could not resolve class with index " + classSignatureIndex;
    clazz.init();
    return clazz;
}

/**
 * Returns the named native class.
 */
j2js.forName = function(name) {
    var clazz = j2js.classesByName[name];

    if (clazz == null) {
        throw new Error("Class not found: " + name);
    }
    
    clazz.init();
    
    return clazz;
}

Clazz.prototype.init = function() {
    if (!this.clinit) {
        this.clinit = true;
        // Note: this.constr is null for interfaces. Interfaces do not have
        // constructors, und must not have an initializer.
        if (this.constr != null && this.constr.prototype["<clinit>()void"] != null) {
            this.invokeStatic("<clinit>()void");
            this.constr.prototype["<clinit>()void"] = null;
        }
    }
}

j2js.getContentDocument = function(svgObject) {
    // TODO: Move this to Java
    var doc = svgObject.contentDocument;
    if (doc != null) return doc;
    doc = svgObject.getSVGDocument();
    if (doc != null) return doc;
    var message =
        "Currently SVG support requires either \n" +
        "Firefox 1.5 or higher with native SVG support or\n" +
        "Internet Explorer 6.0 or higher with Adobe SVG Viewer 3.0 or higher or" +
        "Opera 9.0 or higher with native SVG support";
    
    throw j2js.createException("java.lang.RuntimeException", message);
}

j2js.signalState = 0;

j2js.createException = function(className, message) {
    if (j2js.signalState == 1) throw "Recursive exception creation";
    j2js.signalState = 1;

    var exception;
    try {
        exception = j2js.forName(className).newInstance();
        j2js.invoke(exception, "<init>(java.lang.String)void", [message]);
    } catch(e) {
        throw "Could not create exception for " + message;
    } finally {
        j2js.signalState = 0;
    }
    return exception;
}

/**
 * Returns the specified exception. According VM Spec's athrow instruction,
 * if objectref is null, a NullPointerException is returned instead of objectref.
 */
j2js.nullSaveException = function(objectref) {
    if (objectref == null) {
    	objectref = j2js.createException("java.lang.NullPointerException", null);
    }
    
    return objectref;
}

j2js.invoke = function(obj, signature, parameters) {
    if (obj == null) {
        throw j2js.createException("java.lang.NullPointerException", "Cannot invoke " + signature + " on null");
    }

    if (obj.clazz == null) {
        obj = String(obj);
        //if (signature == "toString()") {
        //    return String(obj);
        //}
        //throw j2js.createException("java.lang.RuntimeException", "Cannot invoke method '" + signature + "' on host object:\n" + inspect(obj));
    }
    var clazz = obj.clazz;
    return clazz.invoke(obj, signature, parameters);
}

j2js.invokeStatic = function(classSignature, methodSignature, parameters) {
    var clazz = j2js.forName(classSignature);
    return clazz.invokeStatic(methodSignature, parameters);
}

// Check Null reference.
j2js.cn = function(obj) {
    if (obj == null) {
        throw j2js.createException("java.lang.NullPointerException", "Cannot access field on null");
    }
    return obj;
}

// Delegates to j2js.invoke.
j2js.i = function(obj, classSignatureIndex, methodSignatureIndex, parameters) {
    // TODO: We don't need classSignatureIndex here.
    return j2js.invoke(obj, j2js.c[methodSignatureIndex], parameters);
}

// Invokes special method.
j2js.iSp = function(obj, classSignatureIndex, methodSignatureIndex, parameters) {
    return j2js.gc(classSignatureIndex).invoke(obj, j2js.c[methodSignatureIndex], parameters);
}

// Invokes super method
j2js.iSu = function(obj, classSignatureIndex, methodSignatureIndex, parameters) {
    // TODO: This is the same as "invoke special". Correct?
    return j2js.gc(classSignatureIndex).invoke(obj, j2js.c[methodSignatureIndex], parameters);
}

// Delegates to class.invokeStatic
j2js.iSt = function(classSignatureIndex, methodSignatureIndex, parameters) {
    var clazz = j2js.gc(classSignatureIndex);
    return clazz.invokeStatic(j2js.c[methodSignatureIndex], parameters);
}

// Declare class and cache it by signature and index.
j2js.dcC = function(signature, index) {
    var clazz = new Clazz(signature);
    j2js.classesByName[signature] = clazz;
    j2js.c[index] = clazz;
    return clazz;
}

// Delegates to j2js.defineClass
j2js.dfC = function(signatureIndex, constructor, superSignatureIndex) {
	var superClass;
	if (superSignatureIndex == null) {
		superClass = null;
	} else {
		superClass = j2js.c[superSignatureIndex];
	}
	
    return j2js.defineClass(j2js.c[signatureIndex], constructor, superClass);
}

// Corresponds to j2js.newInstance
j2js.nI = function(classSignatureIndex) {
    return j2js.gc(classSignatureIndex).newInstance();
}

// Corresponds to j2js.staticFieldRef
j2js.sFR = function(classSignatureIndex) {
    return j2js.gc(classSignatureIndex).constr.prototype;
}


// Corresponds to Clazz.prototype.defineMethod
Clazz.prototype.dM = function(signature, signatureIndex, method) {
    j2js.c[signatureIndex] = signature;
    this.constr.prototype[signature] = method;
}

// Corresponds to Clazz.prototype.defineStaticMethod
Clazz.prototype.dSM = Clazz.prototype.dM;

Clazz.prototype.invoke = function(obj, methodSignature, parameters) {
    var qName = this.name + "#" + methodSignature;
    if (j2js.tracing && qName.substring(0,5) != "java.") {
        //j2js.tracing = false;
        var s = "";
        for (var i=0; i<stackTrace.entries.length; i++) s += "  ";
        j2js.println(s + qName);
        //j2js.tracing = true;
    }
    
    if (obj != this && this.name == "java.lang.reflect.Proxy" && methodSignature.substring(0, 1)!="<") {
        var classSignature = "java.lang.reflect.Proxy";
        var ms = "invoke(java.lang.reflect.Proxy,java.lang.String,java.lang.Object[]java.lang.Object)";
        return j2js.invokeStatic(classSignature, ms, [obj, methodSignature, arguments]);
    }
    
    var method = this.constr.prototype[methodSignature];
    if (method == null) {
        var msg = "No such method: " + qName;
        // If this is a top level invokation, then at least log here.
        // TODO: Clean this up.
        if (stackTrace.entries.length == 0) j2js.println(msg);
        throw j2js.createException("java.lang.RuntimeException", msg);
    }
    if (parameters == null) parameters = [];

    stackTrace.push(qName);

    var result;
    try {
        result = method.apply(obj, parameters);
    } catch (exception) {
        
        if (exception.clazz == null || !exception.clazz.isThrowable) {
            // Handle non-Java exception. This should not happen.
            exception = j2js.createException("java.lang.RuntimeException", j2js.exceptionToString(exception));
        }
        
            
        if (stackTrace.entries.length > 1) {
            // This is not a top-level invocation.
            throw exception;
        }

        // This is a top-level invocation.
        try {
               try {
                   var engine = j2js.invokeStatic("j2js.client.Engine", "getEngine()j2js.client.Engine", null);
                   j2js.invoke(engine, "handleEvent(java.lang.Throwable)void", [exception]);
               } catch(e) {
                   // Engine class not loaded.
               }

        } catch(e) {
            j2js.println("Could not write exception because of: " + e);
            j2js.println("\nOriginal exception: " + j2js.exceptionToString(exception));
            throw exception;
        }
    } finally {
        stackTrace.pop();
    }
    
    if (result != null && result.clazz != null && result.clazz.name == "java.lang.String") {
        result = String(result);
    }
    
    return result;
}

Clazz.prototype.invokeStatic = function(methodSignature, parameters) {
    return this.invoke(this, methodSignature, parameters);
}

j2js.exceptionToString = function(exception) {
    var message = j2js.inspect(exception);//String(exception);
    if (exception.stack != null && j2js.nativeStackTrace) { 
        var lines = exception.stack.split("\n");
        for (var i=0; i<lines.length; i++) {
            message += "\n" + lines[i];
        }
    }
    return message;
}

j2js.handleNewLine = function(s) {
    if (navigator.appVersion.indexOf("MSIE") != -1) {
        return s.replace(/\n/g, "\n\r");
    }
    return s;
}

j2js.console_element = null;

j2js.console_init = function() {
    var id = "java.lang.System.out";
    var consoleContainer = document.getElementById(id);
    if (consoleContainer == null) {
        consoleContainer = document.createElement("div");
        document.body.appendChild(consoleContainer);
    }

    j2js.console_element = document.createElement("pre");
    consoleContainer.appendChild(j2js.console_element);
}

j2js.console_write = function(message) {
    message = String(message);
    if ( message!=null ) {
        // TODO: On what browsers do we have to issue a carriage return?
        message = j2js.handleNewLine(message);
    } else {
        message = "null";
    }
    try {
        if ( j2js.console_element == null ) {
            j2js.console_init();
        }
        j2js.console_element.appendChild(document.createTextNode(message));
    } catch (e) {
        alert("Could not print string:\n\t" + message + "\nfor reason:\n\t" + j2js.inspect(e)); 
    }
}

j2js.console_clear = function() {
    if ( j2js.console_element != null ) {
        var newElement = document.createElement("pre");
        consoleContainer.replaceChild(newElement, j2js.console_element);
        j2js.console_element = newElement;
    }
}

j2js.println = function(message) {
    j2js.print(message + "\n");
}

j2js.print = function(message) {
    if (typeof(window) == "undefined") print(message);
    else j2js.console_write(message);
}

Clazz.prototype.getName = function() {
    return this.name;
}

/**
 * Returns a non-initialized new instance.
 */
Clazz.prototype.newInstance = function() {
    if (this.name == "java.lang.String") {
        return "";
    }
    
    var obj = new this.constr();
    obj.clazz = this;
    
    if (this.isThrowable) {
        stackTrace.fillInCurrentLineNumber();
        j2js.invoke(obj, "fillInStackTrace()java.lang.Throwable");
    }
    
    return obj;
}

/**
 * Checks if the specified object can be cast to the specified class.
 */
j2js.checkCast = function(obj, className) {
    if (obj == null) return null;
    if (obj.clazz == null) return obj;
    var clazz = j2js.forName(className);
    if (!clazz.isAssignableFrom(obj.clazz)) {
        throw j2js.createException("java.lang.RuntimeException", "Cannot cast " + obj.clazz.name + " to " + className);
    }
    
    return obj;
}

j2js.isInstanceof = function(obj, className) {
    if (obj == null) return false;

    if (j2js.classesByName[className] == null) {
        // The class was not assembled, so it was never reverenced by the deployed code.
        // Therefore the object cannot have this type.
        return false;
    }
    var clazz = j2js.forName(className);
    return clazz.isAssignableFrom(obj.clazz);
}

/**
 * Determines if the class or interface represented by this Class object is either the same as,
 * or is a superclass or superinterface of, the class or interface represented by the specified
 * Class parameter. 
 */
Clazz.prototype.isAssignableFrom = function(otherClass) {

    // Look at itself.
    if (this == otherClass) return true;
    
    // Look at its superclass (recursively).
    if (otherClass.superClass != null && this.isAssignableFrom(otherClass.superClass)) return true;
    
    // Look at all superinterfaces (recursively).
    for (var i in otherClass.interfaces) {
        // TODO: Assemble interfaces also for this to work!
        if (this.isAssignableFrom(j2js.gc(otherClass.interfaces[i]))) return true;
    }
    
    // Found none.
    return false;
}

j2js.modalNode = null;
j2js.isDragging = false;
j2js.createDelegate = function(elem, type, listener, useCapture) {
    if (elem == null) {
        throw j2js.createException("java.lang.NullPointerException", "Cannot invoke addEventListener on null"); 
    }
    
    // TODO: Should we use addEventListener() or attachEvent() if these exist?
    elem["on" + type] = function(evt) { 
        var isIE = (evt == null);
        if (isIE) {
            evt = window.event;
            evt.target = evt.srcElement;
        }
        
        j2js.mostRecentEvent = evt;

        if (isIE) {
            evt.getButton = function() {
                if (this.button == 1) return 0;
                if (this.button == 4) return 1;
                return this.button;
            }
            
            evt.currentTarget = elem;
            evt.stopPropagation = function() {j2js.mostRecentEvent.cancelBubble = true;};
            evt.preventDefault = function() {j2js.mostRecentEvent.returnValue = false;};
        } else {
            evt.getButton = function() {
                return this.button;
            }
        }
        
        // Nihilate event if target is not a descendent of the modal node.
        if (j2js.modalNode != null && !j2js.isDragging) {
            var node = evt.target;
            do {
                if (node == j2js.modalNode) break;
                if (node == null) {
                    evt.stopPropagation();
                    evt.preventDefault();
                    return;
                }
                node = node.parentNode;
            } while (true);
        }
        
        if ((evt.type == "keydown" || evt.type == "keyup") && evt.keyIdentifier == null){
        	// TODO: Extend the key identifier set according w3c.
        	// TODO: What if evt.keyIdentifier is set, but has non-conforming value, for example 'A' instead of 'U+000041'?
        	// See also: JavaScript Madness: Keyboard Events.
        	if (evt.which == 13 || evt.keyCode == 13) {
            	evt.keyIdentifier = "Enter";
            }
        }
        
        try {
            j2js.invoke(listener, "handleEvent(org.w3c.dom5.events.Event)void", [evt]);
        } catch(e) {
            j2js.println(j2js.invoke(e, "toString()java.lang.String", []));
        }
        //if (isIE) return j2js.mostRecentEvent.returnValue;
    };
}

j2js.removeDelegate = function(elem, type, listener, useCapture) {
    if (elem == null) {
        throw j2js.createException("java.lang.NullPointerException", "Cannot invoke addEventListener on null"); 
    }
    
    // TODO: Should we use removeEventListener() or attachEvent() if these exist?
    elem["on" + type] = null;
}

/**
 * Type must be "Interval" or "Timeout".
 */
j2js.createTimerDelegate = function(windowImpl, listener, delayInMillis, type) {
    var f = function() { 
        try {
            j2js.invoke(listener, "handleEvent(org.w3c.dom5.views.Window)void", [windowImpl]);
        } catch(e) {
            j2js.println(j2js.invoke(e, "toString()java.lang.String", []));
        }
    };
    return windowImpl.nativeWindow["set" + type](f, delayInMillis);
}

j2js.cmp = function(value1, value2) {
    if (value1 > value2) return 1;
    if (value1 < value2) return -1;
    return 0;
}

// Truncate a number. Needed for integral types in casting and division.
j2js.trunc = function(f) {
    if (f < 0) return Math.ceil(f);
    return Math.floor(f);
}

/**  Narrows the number n to the specified type. The type
 *  must be 0xff (byte) or 0xffff (short).
 *  See 5.1.3 "Narrowing Primitive Conversions" of the Java Language Specification.
 */
j2js.narrow = function(n, bits) {
   n = j2js.trunc(n);
   n = n & bits;
   if (n > (bits>>>1)) n -= (bits+1);
   return n;
}

/**
 * Returns a new multidimensional array of the specified array type [...[T and the desired dimensions.
 * For example, if there are three dimensions, then the returned array is
 *     new [[[T[dim[0]][dim[1]][dim[2]]
 * If T is the boolean type, then the elements are initialized to false.
 * Otherwise, if T is not a class, then the elements are initialize to numeric 0.
 * 
 * If index > 0, then the first index dimensions are ignored. For example, index = 1 returns
 *     new [[T[dim[1]][dim[2]]
 * This way, we can employ the method recursively.
 */
j2js.newArray = function(classSignature, dim, index) {
	if (index == null) index = 0;
	var subSignature = classSignature.substr(index);
	var dimensionAtIndex = dim[index];
	
    var array = new Array(dimensionAtIndex);
    array.clazz = j2js.forName(subSignature);
    
    if (subSignature == "Z") {
        for (var i=0; i<dimensionAtIndex; i++) {
            array[i] = false;
        }
    } else if (subSignature.charAt(1) != "[" && subSignature.charAt(1) != "L") {
        for (var i=0; i<dimensionAtIndex; i++) {
            array[i] = 0;
        }
    } else {
        // Component type is a reference (array or object type).
 		if (index+1 < dim.length) {
 			for (var i=0; i<dimensionAtIndex; i++) {
            	array[i] = j2js.newArray(classSignature, dim, index+1);
        	}
 		}
 	}
    return array;
}

/**
 * Returns a shallow clone of the specified array.
 * This method is used in java.lang.Object#clone()java.lang.Object
 */
j2js.cloneArray = function(other) {
    var dim = other.length;
    var array = new Array(dim);
    array.clazz = other.clazz;
    for (var i=0; i<dim; i++) {
        array[i] = other[i];
    }
    return array;
}

// Returns all attributes of the specified object as a string.
j2js.inspect = function(object) {
    var s = "Value " + String(object);
      
    if (object == null || object == undefined) return "null";
      
    if (typeof(object)=="string") return object;

    var attributes = new Array();
    for (var e in object) {
        attributes[attributes.length] = e;
    }

    if (attributes.length > 0) {
          attributes.sort();
          s += "\n\tAttributes:\n"; 
          for (var e in attributes) {
              var attribute = attributes[e];
              var value = "";
              try {
                  value = object[attribute];
              } catch (e) {
                  value += "While fetching attribute: " + e.message;
              }
              var type = typeof(value);
              if (type == "function") {
                  s += "\t" + attribute + "[" + type + "]\n";
              } else {
                  s += "\t" + attribute + "[" + type + "]: " + value + "\n";
              }
          }
    }
    
    return s;
}

j2js.onLoad = function(signature) {
    var throwableClass = j2js.forName("java.lang.Throwable");

    for (var name in j2js.classesByName) {
        var clazz = j2js.classesByName[name];
        
        if (throwableClass.isAssignableFrom(clazz)) {
            clazz.isThrowable = true;
        }
    }

    try {
        j2js.invokeStatic('com.j2js.prodmode.ContextImpl', 'premain(java.lang.String,java.lang.String)void', signature.split('#'));
    } finally {
        j2js.closeProgressBar();
    }
};

j2js.unquote = function (s) {
    if ( s == null || s.length < 2 ) {
        return s;
    }
    
    var r = "";
    for (var i=0; i<s.length; i++) {
        var c = s.charAt(i);
        if ( c == '\\' && i < s.length-1 ) {
            c = s.charAt(++i);
        }
        r += c;
    }
    return r;
};

j2js.onScriptLoad = function(id, text) {
    j2js.invokeStatic(
        "j2js.net.HtmlHttpRequest",
        "handleEvent(java.lang.String,java.lang.Object)void",
        [id, text]);
}


