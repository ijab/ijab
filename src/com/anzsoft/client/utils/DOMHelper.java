package com.anzsoft.client.utils;

import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.Node;


public class DOMHelper 
{
	static public Element textTag(final XmlDocument doc,final String name,final String content)
	{
		Element group = doc.createElement(name);
		Node textNode = doc.createTextNode(content);
		group.appendChild(textNode);
		return group;
	}
	
	static public Element emptyTag(final XmlDocument doc,final String name)
	{
		Element tag = doc.createElement("name");
		return tag;
	}
}
