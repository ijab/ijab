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

import com.anzsoft.client.XMPP.XmppMessage;
import com.anzsoft.client.XMPP.XmppMessageListener;

public class LoggerMessageListener implements XmppMessageListener {
    private final LoggerOuput output;

    public LoggerMessageListener(final LoggerOuput output) {
	this.output = output;
    }

    public void onMessageReceived(final XmppMessage message) {
	output.log("MESSAGE RECEIVED");
	XmppLogHelper.writeMessage(message, output);
    }

    public void onMessageSent(final XmppMessage message) {
	output.log("MESSAGE SENT");
	XmppLogHelper.writeMessage(message, output);
    }

}
