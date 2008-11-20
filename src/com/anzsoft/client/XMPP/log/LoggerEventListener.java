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

import com.anzsoft.client.XMPP.XmppError;
import com.anzsoft.client.XMPP.XmppEventListener;

public class LoggerEventListener implements XmppEventListener {
    private final LoggerOuput output;

    public LoggerEventListener(final LoggerOuput output) {
	this.output = output;
    }

    public void onConnect() {
	output.log("CONNECTED EVENT!");
    }

    public void onDisconnect() {
	output.log("DISCONNECTED!");

    }

    public void onError(final XmppError error) {
	output.log("!!! ERROR EVENT : " + error.toString() + "'");
    }

    public void onResume() {
	output.log("RESUME!");
    }

    public void onStatusChanged(final String status) {
	output.log("STATUS CHANGED! : '" + status + "'");
    }

}
