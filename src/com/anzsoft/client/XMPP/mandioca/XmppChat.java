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

import com.anzsoft.client.XMPP.XmppMessage;
import com.anzsoft.client.XMPP.XmppMessageListener;
import com.anzsoft.client.XMPP.XmppPresenceListener;

public class XmppChat {
    protected final String sessionUserId;
    protected final XmppSession session;
    protected XmppIdPacketListener filter;

    // FIXME:
    // id = name@host
    // clientID = name@host/resource
    public static String buildId(final String name, final String host) {
	return name + "@" + host;
    }

    public XmppChat(final XmppSession session, final String name, final String host) {
	this(session, buildId(name, host), new XmppIdPacketListener(buildId(name, host)));
    }

    protected XmppChat(final XmppSession session, final String id, final XmppIdPacketListener filter) {
	this.session = session;
	this.sessionUserId = id;
	this.filter = filter;
    }

    public boolean isRoom() {
	return false;
    }

    public void sendMessage(final String body) {
	XmppMessage message = createMessage(body);
	session.send(message);
    }

    protected XmppMessage createMessage(final String body) {
	XmppMessage message = session.getFactory().createMessage();
	message.setFrom(session.getUser().getID());
	message.setTo(sessionUserId);
	message.setBody(body);
	message.setType(XmppMessage.TYPE_CHAT);
	return message;
    }

    public void addMessageListener(final XmppMessageListener messageListener) {
	session.addMessageListener(messageListener, filter);
    }

    protected void addPresenceListener(final XmppPresenceListener listener) {
	session.addPresenceListener(listener, filter);
    }

    public void logout() {
    }

}
