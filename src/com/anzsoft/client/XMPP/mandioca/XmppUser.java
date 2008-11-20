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

import java.util.Iterator;

import com.anzsoft.client.XMPP.HandlerCollection;
import com.anzsoft.client.XMPP.PresenceShow;
import com.anzsoft.client.XMPP.XmppMessage;
import com.anzsoft.client.XMPP.XmppPresence;

public class XmppUser 
{
	public interface XmppUserListener
	{
		public void onPresenceChanged(XmppPresence presence);
	}
    private final XmppSession session;
    private final String userID;
    private final XmppRoster roster;
    private XmppPresence presence;
    private final HandlerCollection /* <RosterEventHandler> */userListeners = new HandlerCollection();

    public XmppUser(final XmppSession session, final String userID) 
    {
		this.session = session;
		this.userID = userID;
		roster = new XmppRoster(session);
    }

    public void sendPresence(final PresenceShow presenceShow, final String message) 
    {
		presence = session.getFactory().createPresence();
		presence.setFrom(userID);
		presence.setPriority(5);
		presence.setShow(presenceShow);
		presence.setStatus(message);
		session.send(presence);
		fireOnPresenceChanged();
    }

    public void sendMessage(final String body, final String destination)
    {
		XmppMessage message = session.getFactory().createMessage();
		message.setFrom(session.getUser().getID());
		message.setTo(userID);
		message.setBody(body);
		message.setType(XmppMessage.TYPE_CHAT);
		session.send(message);
    }


    public String getID() 
    {
        return userID;
    }

    public String toString() 
    {
    	return userID;
    }

    public XmppRoster getRoster() 
    {
    	return roster;
    }
    
    public void addUserListener(XmppUserListener listener)
    {
    	userListeners.add(listener);
    }
    
    public void removeUserListener(XmppUserListener listener)
    {
    	userListeners.remove(listener);
    }
    
    public void fireOnPresenceChanged()
    {
    	Iterator iter = userListeners.iterator();
		  while (iter.hasNext())
		  {
			  XmppUserListener listener = (XmppUserListener) iter.next();
			  listener.onPresenceChanged(presence);
		  }
    }
   

}
