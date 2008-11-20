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

package com.anzsoft.client.XMPP.log;

import com.anzsoft.client.XMPP.XmppInfoQueryListener;
import com.anzsoft.client.XMPP.XmppPacket;

public class LoggerInfoQueryListener implements XmppInfoQueryListener{
    private final LoggerOuput output;

    public LoggerInfoQueryListener(final LoggerOuput logger) {
	this.output = logger;
    }

    public void onInfoQueryReceived(XmppPacket packet) {
	output.log("INFO QUERY RECEIVED");
	XmppLogHelper.writePacket(packet, output);
    }

    public void onInfoQuerySent(XmppPacket packet) {
	output.log("INFO QUERY SENT");
	XmppLogHelper.writePacket(packet, output);
    }

}
