/*
    iJab , The Ajax web jabber client
    Copyright (c) 2006-2008 by AnzSoft
   
    Author:Fanglin Zhong <zhongfanglin@anzsoft.com>

    Started at 2008-08-20, Beijing of China

    iJab    (c) 2006-2008 by the ijab developers  

    *************************************************************************
    *                                                                       *
    * This program is free software; you can redistribute it and/or modify  *
    * it under the terms of the GNU General Public License as published by  *
    * the Free Software Foundation; either version 2 of the License, or     *
    * (at your option) any later version.                                   *
    *                                                                       *
    *************************************************************************
*/

package com.anzsoft.client.utils;

import java.util.ArrayList;

import com.google.gwt.xml.client.Element;
import com.google.gwt.xml.client.NodeList;

public class XMLHelper 
{
	public static Element queryTag(final Element e)
	{
		if(e == null)
			return null;
		NodeList list = e.getElementsByTagName("query");
		if(list.getLength()>0)
			return (Element) list.item(0);
		else
			return null;
	}
	
	public static String queryNS(final Element e)
	{
		Element queryElement = queryTag(e);
		if(queryElement != null)
			return queryElement.getAttribute("xmlns");
		else
			return "";
	}
	
	public static ArrayList<String> getSubTagsConents(final Element e,String tagName)
	{
		ArrayList<String> groups = new ArrayList<String>();
		NodeList list = e.getElementsByTagName(tagName);
		for(int index = 0;index<list.getLength();index++)
		{
			Element item = (Element) list.item(index);
			groups.add(item.getFirstChild().getNodeValue());
		}
		return groups;
	}
}
