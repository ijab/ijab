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

import com.anzsoft.client.XMPP.XmppFactory;
import com.anzsoft.client.XMPP.XmppUserSettings;
import com.anzsoft.client.XMPP.log.LoggerOuput;
import com.google.gwt.core.client.JavaScriptException;
import com.google.gwt.core.client.JavaScriptObject;

class JsJacHttpBindingConnection extends JsJacConnection {
    public JsJacHttpBindingConnection(final XmppFactory factory, final String httpBase, final int pollInterval, LoggerOuput logger) throws JavaScriptException{
	super(factory, create(httpBase, pollInterval, logger));
    }
    
    private static native JavaScriptObject create(String httpBase, int pollInterval, LoggerOuput logger) throws JavaScriptException /*-{
	var oArgs = new Object();
	oArgs.httpbase = httpBase;
	oArgs.timerval = pollInterval;
	oArgs.oDbg = new Object();
	oArgs.oDbg.log = function (text) {
		logger.@com.anzsoft.client.XMPP.log.LoggerOuput::log(Ljava/lang/String;)(text);
	};
	var con = new $wnd.JSJaCHttpBindingConnection(oArgs);
	return con;
    }-*/;

    public native void connect(XmppUserSettings user) throws JavaScriptException /*-{
	var args = new Object();
	args.domain = user.@com.anzsoft.client.XMPP.XmppUserSettings::domain;
	args.username = user.@com.anzsoft.client.XMPP.XmppUserSettings::userName;
	args.pass = user.@com.anzsoft.client.XMPP.XmppUserSettings::password;
	args.resource = user.@com.anzsoft.client.XMPP.XmppUserSettings::resource;
	args.register = user.@com.anzsoft.client.XMPP.XmppUserSettings::shouldRegister;
	//args.authtype = 'nonsasl';
	args.authtype = user.@com.anzsoft.client.XMPP.XmppUserSettings::auth;
	args.host = user.@com.anzsoft.client.XMPP.XmppUserSettings::host;
	args.port = user.@com.anzsoft.client.XMPP.XmppUserSettings::port;
	var delegate = this.@com.anzsoft.client.XMPP.impl.JsJacConnection::connection;
	delegate.connect(args);
    }-*/;

    public native void disconnect() throws JavaScriptException /*-{
	var delegate = this.@com.anzsoft.client.XMPP.impl.JsJacConnection::connection;
	delegate.disconnect();
    }-*/;

    // TODO:
    public native void inherit() /*-{
    }-*/;

    public native boolean isPolling() /*-{
	var delegate = this.@com.anzsoft.client.XMPP.impl.JsJacConnection::connection;
	return delegate.isPolling();
    }-*/;

    public native void setPollInterval(int pollInterval) /*-{
	var delegate = this.@com.anzsoft.client.XMPP.impl.JsJacConnection::connection;
	delegate.setPollInterval(pollInterval);
    }-*/;

}
