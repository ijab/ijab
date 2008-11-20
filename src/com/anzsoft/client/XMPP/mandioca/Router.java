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

import com.anzsoft.client.XMPP.XmppListener;
import com.anzsoft.client.XMPP.XmppPacket;

abstract class Router {
    protected final ArrayList listeners;
    protected final ArrayList filters;

    public Router() {
	this.listeners = new ArrayList();
	this.filters = new ArrayList();
    }

    protected void addListener(final XmppListener listener, final XmppPacketFilter filter) {
	listeners.add(listener);
	filters.add(filter);
    }

    protected XmppPacketFilter getFilter(final int index) {
	return (XmppPacketFilter) filters.get(index);
    }

    private XmppListener getListener(final int index) {
	return (XmppListener) listeners.get(index);
    }


    protected void filterIncoming(final XmppPacket message, final XmppPacketHandler handler) {
	int total = listeners.size();
	for (int index = 0; index < total ; index++) {
	    if (getFilter(index).filterIncoming(message)) {
		handler.handle(message, getListener(index));
	    }
	}
    }

    protected void filterOutcoming(final XmppPacket message, final XmppPacketHandler handler) {
	int total = listeners.size();
	for (int index = 0; index < total ; index++) {
	    if (getFilter(index).filterOutcoming(message)) {
		handler.handle(message, getListener(index));
	    }
	}
    }

}
