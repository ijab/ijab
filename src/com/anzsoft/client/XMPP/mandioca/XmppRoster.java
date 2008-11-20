/*
    iJab , The Ajax web jabber client
    Copyright (c) 2006-2008 by AnzSoft
   
    Author:Fanglin Zhong <zhongfanglin@anzsoft.com>

    Started at 2008-08-20, Beijing of China

    iJab    (c) 2006-2008 by the ijab developers
    
    Some code copied form gwtjsjac

    *************************************************************************
    *                                                                       *
    * This program is free software; you can redistribute it and/or modify  *
    * it under the terms of the GNU General Public License as published by  *
    * the Free Software Foundation; either version 2 of the License, or     *
    * (at your option) any later version.                                   *
    *                                                                       *
    *************************************************************************
*/

package com.anzsoft.client.XMPP.mandioca;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import com.anzsoft.client.XMPP.Debugger;
import com.anzsoft.client.XMPP.HandlerCollection;
import com.anzsoft.client.XMPP.PresenceShow;
import com.anzsoft.client.XMPP.XmppInfoQueryListener;
import com.anzsoft.client.XMPP.XmppPacket;
import com.anzsoft.client.XMPP.XmppQuery;
import com.anzsoft.client.utils.XMLHelper;
import com.google.gwt.xml.client.Document;
import com.google.gwt.xml.client.NodeList;
import com.google.gwt.xml.client.XMLParser;
import com.google.gwt.xml.client.Element;


public class XmppRoster 
{
	private final XmppInfoQueryListener rosterListener;
    private final XmppSession session;
    public Map<String,XmppContact> contacts = new HashMap<String, XmppContact>();
    private final HandlerCollection /* <RosterEventHandler> */rosterListeners = new HandlerCollection();
    public XmppRoster(final XmppSession session) 
    {
    	this.session = session;
    	rosterListener = new XmppInfoQueryListener(){
			public void onInfoQueryReceived(XmppPacket packet) 
			{
				if(!packet.getID().equalsIgnoreCase("roster_1")||!packet.getType().equalsIgnoreCase("result"))
				{
					return;
				}
				parseRoster(packet.toXML());
				session.getUser().sendPresence(PresenceShow.emptyShow(), "http://www.anzsoft.com");
			}
			public void onInfoQuerySent(XmppPacket packet) {
			}
    		
    	};
    	this.session.addInfoQueryListener(rosterListener);
    }

    public void sendQuery() 
    {
		Debugger.log("SENDING ROSTER QUERY!!");
		XmppQuery query = session.getFactory().createQuery();
		query.setType(XmppQuery.TYPE_GET);
		query.setID("roster_1");
		query.createQueryNode("jabber:iq:roster");
		session.send(query);
    }
    
    private void parseRoster(String xml)
    {
    	contacts.clear();
    	Document doc = XMLParser.parse(xml);
    	Element query = XMLHelper.queryTag(doc.getDocumentElement());
    	if(query !=null && query.getAttribute("xmlns").equals("jabber:iq:roster"))
    	{
    		NodeList itemList = query.getElementsByTagName("item");
    		for(int index = 0;index<itemList.getLength();index++)
    		{
    			Element item = (Element)itemList.item(index);
    			XmppContact contact = XmppContact.fromXml(item);
    			contacts.put(contact.getJID().toString(), contact);
    		}
    	}
    	if(!contacts.isEmpty())
    		fireOnRoster();
    }
    
    public void addRosterListener(XmppRosterListener listener)
    {
    	rosterListeners.add(listener);
    }
    
    public void removeRosterListener(XmppRosterListener listener) 
    {
    	rosterListeners.remove(listener);
    }
    
    private void fireOnRoster()
    {
    	Iterator iter = rosterListeners.iterator();
		  while (iter.hasNext())
		  {
			  XmppRosterListener listener = (XmppRosterListener) iter.next();
			  listener.onRoster(contacts);
		  }	
    }

}
