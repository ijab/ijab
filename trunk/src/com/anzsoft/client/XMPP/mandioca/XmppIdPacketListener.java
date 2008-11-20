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
import com.anzsoft.client.XMPP.Debugger;
import com.anzsoft.client.XMPP.XmppPacket;


public class XmppIdPacketListener implements XmppPacketFilter{
    private final String selfID;

    public XmppIdPacketListener(final String id) {
	this.selfID = id;
    }

    public boolean filterIncoming(final XmppPacket packet) {
	String packetFrom = packet.getFromID().toStringNoResource();
	boolean shouldFilter = packetFrom.equals(selfID);
	Debugger.log("ChatPacket FILTER IN(" + selfID + "): " + packetFrom + "=> " + shouldFilter);
	return shouldFilter;
    }

    public boolean filterOutcoming(final XmppPacket packet) {
	String packetTo = packet.getTo();
	boolean shouldFilter = packetTo.equals(selfID);
	Debugger.log("ChatPacket FILTER OUT(" + selfID + "): " + packetTo + "=> " + shouldFilter);
	return shouldFilter;
    }
}
