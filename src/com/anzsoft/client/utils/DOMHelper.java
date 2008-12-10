package com.anzsoft.client.utils;

import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.Node;

public class DOMHelper 
{
	static public Node textTag(final Document doc,final String name,final String content)
	{
		Node tag = doc.createElement(name);
		tag.setNodeValue(content);
		return tag;
	}
}
