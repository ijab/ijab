package com.anzsoft.client.utils;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.Node;

public class XmlDocument extends JavaScriptObject
{
	protected XmlDocument()
	{
	}
	
	public final native Element documentElement()
	/*-{
		return this.documentElement;
	}-*/;
	
	public final native Element firstChild()
	/*-{
		return this.firstChild;
	 }-*/;
	
	public final native Element createElement(final String nodeName)
	/*-{
	 	return this.createElement(nodeName);
	 }-*/;
	
	public final native Element createElementNS(final String xmlns,final String elName)
	/*-{
	 	var element;
  		try {
    		element = this.createElementNS(xmlns,elName);
  		} catch (e) {
    	// fallback
    		element = this.createElement(elName);
  		}
  		if (element && element.getAttribute('xmlns') != xmlns) // fix opera 8.5x
    		element.setAttribute('xmlns',xmlns);
	 	return element;
	 }-*/;
	
	public final native Node createTextNode(final String content)
	/*-{
		return this.createTextNode(content);
	 }-*/;
	
	public final native void loadXML(final String xml)
	/*-{
	 	this.loadXML(xml);
	 }-*/;
	
	public static native XmlDocument create(final String name,final String ns)
	/*-{
	 	return $wnd.XmlDocument.create(name,ns);
	 }-*/;
}
