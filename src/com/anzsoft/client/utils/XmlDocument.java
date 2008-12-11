package com.anzsoft.client.utils;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.Node;

public class XmlDocument extends JavaScriptObject
{
	protected XmlDocument()
	{
	}
	public final native Element createElement(String nodeName)
	/*-{
	 	return this.createElement(nodeName);
	 }-*/;
	
	public final native Node createTextNode(String content)
	/*-{
		return this.createTextNode(content);
	 }-*/;
}
