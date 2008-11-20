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

package com.anzsoft.client.XMPP.mandioca.rooms;

import com.anzsoft.client.XMPP.XmppID;
import com.anzsoft.client.XMPP.XmppMessage;
import com.anzsoft.client.XMPP.XmppPresence;
import com.anzsoft.client.XMPP.XmppPresenceListener;
import com.anzsoft.client.XMPP.mandioca.XmppChat;
import com.anzsoft.client.XMPP.mandioca.XmppIdPacketListener;
import com.anzsoft.client.XMPP.mandioca.XmppSession;
import com.anzsoft.client.XMPP.mandioca.XmppUser;

public class XmppRoom extends XmppChat {
    private final String roomId;
    private String userId;
    private final String sessionRoomId;
    private final RoomPresenceListenerCollection presenceListeners;

    public XmppRoom(final XmppSession session, final String roomName, final String host, final String nick) {
	this(session, XmppID.render(roomName, host, nick), XmppID.render(roomName, host, null));
    }

    XmppRoom(final XmppSession session, final String userInRoomId, final String roomId) {
	super(session, userInRoomId, new XmppIdPacketListener(roomId));
	this.roomId = roomId;
	this.sessionRoomId = userInRoomId;
	this.presenceListeners = new RoomPresenceListenerCollection();

	addPresenceListener(new XmppPresenceListener() {
	    public void onPresenceReceived(final XmppPresence presence) {
		String alias = presence.getFromID().getResource();
		String type = presence.getType();
		if (type != null && type.equals(XmppPresence.TYPE_UNAVAILABLE)) {
		    presenceListeners.fireUserLeaves(alias);
		} else {
		    presenceListeners.fireUserEntered(alias, presence.getStatus());
		}
	    }

	    public void onPresenceSent(final XmppPresence presence) {
	    }
	});
    }


    public boolean isRoom() {
	return true;
    }

    private XmppPresence createRoomPresence() {
	XmppPresence presence = session.getFactory().createRoomPresence();
	presence.setFrom(userId);
	presence.setTo(sessionRoomId);
	return presence;
    }

    /**
     * @param user
     * @see http://www.xmpp.org/extensions/xep-0045.html#enter
     */
    public void join(final XmppUser user) {
	userId = user.getID();
	XmppPresence presence = createRoomPresence();
	session.send(presence);
    }


    /**
     *
     * @see http://www.xmpp.org/extensions/xep-0045.html#exit
     */
    public void logout() {
	XmppPresence presence = createRoomPresence();
	presence.setType(XmppPresence.TYPE_UNAVAILABLE);
	session.send(presence);
    }

    public void addRoomPresenceListener(final RoomPresenceListener listener) {
	presenceListeners.add(listener);
    }

    protected XmppMessage createMessage(final String body) {
	XmppMessage message = session.getFactory().createMessage();
	message.setFrom(session.getUser().getID());
	message.setTo(roomId);
	message.setBody(body);
	message.setType(XmppMessage.TYPE_MULTI);
	return message;
    }


}
