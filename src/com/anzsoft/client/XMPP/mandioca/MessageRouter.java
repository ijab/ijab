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

import com.anzsoft.client.XMPP.XmppListener;
import com.anzsoft.client.XMPP.XmppMessage;
import com.anzsoft.client.XMPP.XmppMessageListener;
import com.anzsoft.client.XMPP.XmppPacket;

class MessageRouter extends Router implements XmppMessageListener {

    public void onMessageReceived(final XmppMessage message) {
	filterIncoming(message, new XmppPacketHandler () {
	    public void handle(final XmppPacket message, final XmppListener listener) {
		((XmppMessageListener) listener).onMessageReceived((XmppMessage) message);
	    }
	});
    }

    public void onMessageSent(final XmppMessage message) {
	filterOutcoming(message, new XmppPacketHandler () {
	    public void handle(final XmppPacket message, final XmppListener listener) {
		((XmppMessageListener) listener).onMessageSent((XmppMessage) message);
	    }
	});
    }

    public void add(final XmppMessageListener messageListener, final XmppPacketFilter filter) {
	super.addListener(messageListener, filter);
    }
}
