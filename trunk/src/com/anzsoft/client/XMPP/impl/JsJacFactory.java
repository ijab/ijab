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

package com.anzsoft.client.XMPP.impl;

import com.anzsoft.client.XMPP.XmppConnection;
import com.anzsoft.client.XMPP.XmppFactory;
import com.anzsoft.client.XMPP.XmppMessage;
import com.anzsoft.client.XMPP.XmppPresence;
import com.anzsoft.client.XMPP.XmppQuery;
import com.anzsoft.client.XMPP.log.LogHelper;
import com.anzsoft.client.XMPP.log.LoggerOuput;

public class JsJacFactory implements XmppFactory {

	private static JsJacFactory instance;

	JsJacFactory() {
	}

	public static XmppFactory getInstance() {
		if (instance == null) {
			instance = new JsJacFactory();
		}
		return instance;
	}

	public XmppConnection createBindingConnection(final String httpBase, final int pollInterval, LoggerOuput logger) {
		LogHelper.log("Creating binding connection to # with #msecs.", httpBase, LogHelper.p(pollInterval), logger);
		return new JsJacHttpBindingConnection(this, httpBase, pollInterval, logger);
	}

	public XmppMessage createMessage() {
		return new JsJacMessage();
	}

	public XmppPresence createRoomPresence() {
		return new JsJacRoomPresence();
	}

	public XmppPresence createPresence() {
		return new JsJacPresence();
	}

	public XmppQuery createQuery() {
		return new JsJacQuery();
	}

}
