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

import java.util.ArrayList;
import java.util.List;

import com.anzsoft.client.XMPP.XmppID;
import com.anzsoft.client.utils.XMLHelper;
import com.google.gwt.xml.client.Element;

public class XmppContact 
{
	public static enum Subscription {
        /**
         * "both" -- both the user and the contact have subscriptions to each
         * other's presence information
         */
        both,
        /**
         * "from" -- the contact has a subscription to the user's presence
         * information, but the user does not have a subscription to the
         * contact's presence information
         */
        from,
        /**
         * "none" -- the user does not have a subscription to the contact's
         * presence information, and the contact does not have a subscription to
         * the user's presence information
         */
        none,
        /**
         * "to" -- the user has a subscription to the contact's presence
         * information, but the contact does not have a subscription to the
         * user's presence information
         */
        to,
        /**
         * "remove" -- the use has removed from the roster
         */
        remove
    }
	
	private final ArrayList<String> groups;
	private final XmppID jid;
	private final String name;
	private XmppContactStatus status;
	private Subscription subscription;
	
	public static XmppContact fromXml(Element e)
	{
		XmppID jid = XmppID.parseId(e.getAttribute("jid"));
		String subscription = e.getAttribute("subscription");
		String name = e.getAttribute("name");
		ArrayList<String> groups = XMLHelper.getSubTagsConents(e, "group");
		return new XmppContact(jid,Subscription.valueOf(subscription),name,groups);
		
	}
	
	public XmppContact(final XmppID jid,final Subscription subscription,String name,ArrayList<String> groups)
	{
		this.jid = jid;
		this.subscription = subscription;
		this.name = name;
		this.groups = groups;
		this.status = new XmppContactStatus();
	}
	
	//get attr
	public List<String> getGroups()
	{
		return this.groups;
	}
	
	public XmppID getJID()
	{
		return this.jid;
	}
	
	public XmppContactStatus getStatus()
	{
		return this.status;
	}
	
	public Subscription getSubscription()
	{
		return this.subscription;
	}
	
	public String getName()
	{
		return this.name;
	}
	
	public String getAvatar()
	{
		return "images/default_avatar.png";
	}
	
	//get attr
	public void setStatus(final XmppContactStatus status)
	{
		this.status = status;
	}
	
	public void setSubscription(Subscription subscription)
	{
		this.subscription = subscription;
	}
	
}
