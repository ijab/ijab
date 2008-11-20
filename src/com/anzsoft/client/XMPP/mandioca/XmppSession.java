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

import com.anzsoft.client.XMPP.XmppConnection;
import com.anzsoft.client.XMPP.XmppConnectionListener;
import com.anzsoft.client.XMPP.XmppEventAdapter;
import com.anzsoft.client.XMPP.XmppFactory;
import com.anzsoft.client.XMPP.XmppInfoQueryListener;
import com.anzsoft.client.XMPP.XmppMessageListener;
import com.anzsoft.client.XMPP.XmppPacket;
import com.anzsoft.client.XMPP.XmppPacketListener;
import com.anzsoft.client.XMPP.XmppPresenceListener;
import com.anzsoft.client.XMPP.XmppUserSettings;
import com.anzsoft.client.XMPP.mandioca.rooms.XmppRoom;

public class XmppSession {
	private static final int INITIAL_RECONNECT_INTERVAL = 1000;
	private final XmppConnection connection;
	private final MessageRouter messageRouter;
	private final PresenceRouter presenceRouter;
	private final ArrayList activeChats;
	private XmppUser user;
	private int reconnectInterval;
	private final boolean reconnectOnFail;

	public XmppSession(final XmppConnection connection, final boolean reconnectOnFail) {
		this.reconnectOnFail = reconnectOnFail;
		this.reconnectInterval = INITIAL_RECONNECT_INTERVAL;
		this.connection = connection;
		this.activeChats = new ArrayList();
		this.messageRouter = new MessageRouter();
		connection.addMessageListener(messageRouter);
		this.presenceRouter = new PresenceRouter();
		connection.addPreseceListener(presenceRouter);
	}

	public void login(final XmppUserSettings userSettings) {
		connection.connect(userSettings);
		this.user = new XmppUser(this, userSettings.getID());
	}

	public void addEventListener(final XmppEventAdapter listener) {
		connection.addEventListener(listener);
	}

	public XmppUser getUser() {
		return user;
	}

	public XmppChat openChat(final String name, final String host) {
		XmppChat chat = new XmppChat(this, name, host);
		activeChats.add(chat);
		return chat;
	}

	public XmppRoom joinRoom(final String host, final String roomName, final String nick) {
		XmppRoom room = new XmppRoom(this, roomName, host, nick);
		activeChats.add(room);
		room.join(user);
		return room;
	}

	public void logout() {
		XmppChat chat;
		int total = activeChats.size();
		for (int index = 0; index < total; index++) {
			chat = (XmppChat) activeChats.get(index);
			chat.logout();
		}
		user = null;
		connection.disconnect();
	}

	public void send(final XmppPacket packet) {
		connection.send(packet);
	}

	public void addMessageListener(final XmppMessageListener listener, final XmppPacketFilter filter) {
		messageRouter.add(listener, filter);
	}

	public void addPresenceListener(final XmppPresenceListener listener, final XmppPacketFilter filter) {
		presenceRouter.add(listener, filter);
	}

	public XmppFactory getFactory() {
		return connection.getFactory();
	}

	public void send(final XmppPacket packet, final XmppPacketListener listener) {
		connection.send(packet, listener);
	}

	public void addConnectionListener(XmppConnectionListener xmppConnectionListener) {
		connection.addConnectionListener(xmppConnectionListener);
	}
	
	public void addInfoQueryListener(XmppInfoQueryListener listener)
	{
		connection.addInfoQueryListener(listener);
	}
}
