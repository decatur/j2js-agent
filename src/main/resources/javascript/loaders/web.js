/**
 * JavaScript Core Runtime Library.
 * Copyright (c) 2005-2007 Wolfgang Kuehn
 */

j2js.dictionary = {};
j2js.throttleInMillis = 10;

j2js.progress = function(currentlyLoaded) {
    var outerDiv = document.getElementById("ProgressBar");
    if (outerDiv == null) {
        outerDiv = document.createElement("div");
        outerDiv.id = "ProgressBar";
        outerDiv.style.position = "absolute";
        outerDiv.style.top = document.body.offsetHeight/3 + "px";
        outerDiv.style.left = document.body.offsetWidth/3 + "px";
        outerDiv.style.padding = "20px";
        outerDiv.style.backgroundColor = "red";
        outerDiv.style.fontWeight = "bold";
        document.body.appendChild(outerDiv);
        // Note: Animated gifs while rendering engine is busy is only supported
        // by opera, so don't use it!
    } else {
        outerDiv.removeChild(outerDiv.firstChild);
    }
    
    var perCentLoaded = Math.round(currentlyLoaded*100/j2js.assemblySize);
    
    var message;
    if (perCentLoaded == 100) {
        message = "Initializing ...";
    } else {
        message = "Loaded " + perCentLoaded + "%";
    }
    
    outerDiv.appendChild(document.createTextNode(message));
    window.status = message;
}

j2js.closeProgressBar = function() {
    var outerDiv = document.getElementById("ProgressBar");
    outerDiv.parentNode.removeChild(outerDiv);
    window.status = "Loading complete!";
}

j2js.scriptIndex = 1;
j2js.loadScript = function(currentlyLoaded) {
    j2js.progress(currentlyLoaded);
    
    window.setTimeout(function() { 
            j2js.loadScriptBySource(j2js.assemblyPath + (j2js.scriptIndex++) + ".js");
        }, j2js.throttleInMillis);
};

j2js.getQueryParameter = function(parameterName) {
	var query = window.location.search;
	var parameters = query.substring(1).split("&");
	
	for ( var index in parameters ) {
		var keyValue = parameters[index].split("=");
		if ( keyValue.length == 2 && keyValue[0] == parameterName ) {
			return keyValue[1];
		}
	}
	return null;
}

j2js.loadScriptBySource = function(src) {
	var script = document.createElement("script");
	script.type = "text/javascript";
	script.defer = true;
	var head = document.getElementsByTagName("head")[0];
	head.appendChild(script);
	script.src = src;
};


window.onload = function() {
	var devmodeHost = j2js.getQueryParameter("devmodeHost");
	if ( devmodeHost != null ) {
		j2js.loadScriptBySource(devmodeHost);
	} else {
		j2js.assemblyPath = document.getElementById("j2js-loader").src.replace(/\d+\.js/,"");
		j2js.loadScript(0);
	}
};