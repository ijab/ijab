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
import com.anzsoft.client.XMPP.XmppPacket;
import com.anzsoft.client.XMPP.XmppPresence;
import com.anzsoft.client.XMPP.XmppPresenceListener;

class PresenceRouter extends Router implements XmppPresenceListener {
    public void onPresenceReceived(final XmppPresence presence) {
	filterIncoming(presence, new XmppPacketHandler () {
	    public void handle(final XmppPacket message, final XmppListener listener) {
		((XmppPresenceListener) listener).onPresenceReceived((XmppPresence) message);
	    }
	});
    }

    public void onPresenceSent(final XmppPresence presence) {
	filterOutcoming(presence, new XmppPacketHandler () {
	    public void handle(final XmppPacket message, final XmppListener listener) {
		((XmppPresenceListener) listener).onPresenceSent((XmppPresence) message);
	    }
	});
    }

    public void add(final XmppPresenceListener listener, final XmppPacketFilter filter) {
	addListener(listener, filter);
    }

}
