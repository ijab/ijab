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

package com.anzsoft.client.XMPP;

import com.anzsoft.client.XMPP.log.LoggerEventListener;
import com.anzsoft.client.XMPP.log.LoggerInfoQueryListener;
import com.anzsoft.client.XMPP.log.LoggerMessageListener;
import com.anzsoft.client.XMPP.log.LoggerOuput;
import com.anzsoft.client.XMPP.log.LoggerPacketListener;
import com.anzsoft.client.XMPP.log.LoggerPresenceListener;
import com.google.gwt.core.client.GWT;

public class Debugger  {
    public static LoggerOuput logger;

    // default logger
    static {
	logger = new LoggerOuput () {
	    public void log(final String text) {
		GWT.log(text, null);
	    }
	};
    }

    public static void initLogger(final LoggerOuput logger) {
	Debugger.logger = logger;
    }

    public static void log(final String text) {
	logger.log(text);
    }

    public static void debug(final XmppConnection connection, final LoggerOuput logger) {
	connection.addPacketListener(new LoggerPacketListener(logger));
	connection.addEventListener(new LoggerEventListener(logger));
	connection.addMessageListener(new LoggerMessageListener(logger));
	connection.addInfoQueryListener(new LoggerInfoQueryListener(logger));
	connection.addPreseceListener(new LoggerPresenceListener(logger));
    }

}
