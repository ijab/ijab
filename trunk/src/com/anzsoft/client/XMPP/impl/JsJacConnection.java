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

import com.anzsoft.client.XMPP.Debugger;
import com.anzsoft.client.XMPP.XmppConnection;
import com.anzsoft.client.XMPP.XmppConnectionListener;
import com.anzsoft.client.XMPP.XmppEventAdapter;
import com.anzsoft.client.XMPP.XmppEventListener;
import com.anzsoft.client.XMPP.XmppFactory;
import com.anzsoft.client.XMPP.XmppInfoQueryListener;
import com.anzsoft.client.XMPP.XmppMessageListener;
import com.anzsoft.client.XMPP.XmppPacket;
import com.anzsoft.client.XMPP.XmppPacketListener;
import com.anzsoft.client.XMPP.XmppPresenceListener;
import com.anzsoft.client.XMPP.XmppStatus;
import com.anzsoft.client.XMPP.XmppUserSettings;
import com.google.gwt.core.client.JavaScriptException;
import com.google.gwt.core.client.JavaScriptObject;

abstract class JsJacConnection implements XmppConnection {
	protected final JavaScriptObject connection;
	private final XmppFactory factory;

	public JsJacConnection(final XmppFactory factory, final JavaScriptObject connection) {
		this.factory = factory;
		this.connection = connection;
	}
	
    public void addConnectionListener(final XmppConnectionListener listener) {
		this.addEventListener(new XmppEventAdapter() {
			public void onConnect() {
				listener.onConnect();
			}

			public void onDisconnect() {
				listener.onDisconnect();
			}
		});
    }



	public void connect(final XmppUserSettings user, int delayMillis) {
		if (delayMillis <= 0)
			delayMillis = 1;
		Debugger.log("Trying to connect in " + delayMillis + " miliseconds.");
		connect(user);
	}

	public native void addEventListener(XmppEventListener listener) /*-{
	       var delegate = this.@com.anzsoft.client.XMPP.impl.JsJacConnection::connection;
	   	delegate.registerHandler('onConnect', function() {
	   		listener.@com.anzsoft.client.XMPP.XmppEventListener::onConnect()();
	   	});
	   	delegate.registerHandler('onDisconnect', function() {
	   		listener.@com.anzsoft.client.XMPP.XmppEventListener::onDisconnect()();
	   	});
	   	delegate.registerHandler('onResume', function() {
	   		listener.@com.anzsoft.client.XMPP.XmppEventListener::onResume()();
	   	});
	   	delegate.registerHandler('onStatusChanged', function(statusID) {
	   		listener.@com.anzsoft.client.XMPP.XmppEventListener::onStatusChanged(Ljava/lang/String;)(statusID);
	   	});
	   	delegate.registerHandler('onError', function(error) {
		var e = @com.anzsoft.client.XMPP.impl.JsJacError::newError(Lcom/google/gwt/core/client/JavaScriptObject;)(error);
	   		listener.@com.anzsoft.client.XMPP.XmppEventListener::onError(Lcom/anzsoft/client/XMPP/XmppError;)(e);
	   	});
	   }-*/;

	public native void addInfoQueryListener(XmppInfoQueryListener listener) /*-{
	   	var delegate = this.@com.anzsoft.client.XMPP.impl.JsJacConnection::connection;
	       delegate.registerHandler('iq_in', function(packet) {
	   		var p = @com.anzsoft.client.XMPP.impl.JsJacPacket::newPacket(Lcom/google/gwt/core/client/JavaScriptObject;)(packet);
	   		listener.@com.anzsoft.client.XMPP.XmppInfoQueryListener::onInfoQueryReceived(Lcom/anzsoft/client/XMPP/XmppPacket;)(p);
	       });
	       delegate.registerHandler('iq_out', function(packet) {
	   		var p = @com.anzsoft.client.XMPP.impl.JsJacPacket::newPacket(Lcom/google/gwt/core/client/JavaScriptObject;)(packet);
	   		listener.@com.anzsoft.client.XMPP.XmppInfoQueryListener::onInfoQuerySent(Lcom/anzsoft/client/XMPP/XmppPacket;)(p);
	       });
	   }-*/;

	public native void addMessageListener(XmppMessageListener listener) /*-{
	   	var delegate = this.@com.anzsoft.client.XMPP.impl.JsJacConnection::connection;
	   	delegate.registerHandler('message_in', function(packet) {
	   		var m = @com.anzsoft.client.XMPP.impl.JsJacMessage::newMessage(Lcom/google/gwt/core/client/JavaScriptObject;)(packet);
	   		listener.@com.anzsoft.client.XMPP.XmppMessageListener::onMessageReceived(Lcom/anzsoft/client/XMPP/XmppMessage;)(m);
	   	});
	   	delegate.registerHandler('message_out', function(packet) {
	   		var m = @com.anzsoft.client.XMPP.impl.JsJacMessage::newMessage(Lcom/google/gwt/core/client/JavaScriptObject;)(packet);
	   		listener.@com.anzsoft.client.XMPP.XmppMessageListener::onMessageSent(Lcom/anzsoft/client/XMPP/XmppMessage;)(m);
	   	});
	   }-*/;

	public native void addPacketListener(XmppPacketListener listener) /*-{
	   	var delegate = this.@com.anzsoft.client.XMPP.impl.JsJacConnection::connection;
	   	delegate.registerHandler('packet_in', function(packet) {
	   		var p = @com.anzsoft.client.XMPP.impl.JsJacPacket::newPacket(Lcom/google/gwt/core/client/JavaScriptObject;)(packet);
	   		listener.@com.anzsoft.client.XMPP.XmppPacketListener::onPacketReceived(Lcom/anzsoft/client/XMPP/XmppPacket;)(p);
	   	});
	   	delegate.registerHandler('packet_out', function(packet) {
	   		var p = @com.anzsoft.client.XMPP.impl.JsJacPacket::newPacket(Lcom/google/gwt/core/client/JavaScriptObject;)(packet);
	   		listener.@com.anzsoft.client.XMPP.XmppPacketListener::onPacketSent(Lcom/anzsoft/client/XMPP/XmppPacket;)(p);
	   	});
	   }-*/;

	public native void addPreseceListener(XmppPresenceListener listener) /*-{
	   	var delegate = this.@com.anzsoft.client.XMPP.impl.JsJacConnection::connection;
	   	delegate.registerHandler('presence_in', function(packet) 
	   	{
		   		var p = @com.anzsoft.client.XMPP.impl.JsJacPresence::newPresence(Lcom/google/gwt/core/client/JavaScriptObject;)(packet);
		   		listener.@com.anzsoft.client.XMPP.XmppPresenceListener::onPresenceReceived(Lcom/anzsoft/client/XMPP/XmppPresence;)(p);
	   	});
	   	delegate.registerHandler('presence_out', function(packet) {
	   		var p = @com.anzsoft.client.XMPP.impl.JsJacPresence::newPresence(Lcom/google/gwt/core/client/JavaScriptObject;)(packet);
	   		listener.@com.anzsoft.client.XMPP.XmppPresenceListener::onPresenceSent(Lcom/anzsoft/client/XMPP/XmppPresence;)(p);
	   	});
	   }-*/;

	public abstract void connect(XmppUserSettings user) throws JavaScriptException;

	public abstract void disconnect() throws JavaScriptException;

	public JavaScriptObject getDelegate() {
		return connection;
	}

	public XmppFactory getFactory() {
		return factory;
	}

	public native int getPollInterval() /*-{
	   	var delegate = this.@com.anzsoft.client.XMPP.impl.JsJacConnection::connection;
	return delegate.getPollInterval();
	   }-*/;

	public XmppStatus getStatus() {
		return XmppStatus.getStatus(getStatusID());
	}

	public native String getStatusID() /*-{
	   	var delegate = this.@com.anzsoft.client.XMPP.impl.JsJacConnection::connection;
	   	return delegate.status();
	   }-*/;

	public native boolean isConnected() /*-{
	   	var delegate = this.@com.anzsoft.client.XMPP.impl.JsJacConnection::connection;
	return delegate.connected();
	   }-*/;

	/**
	 * Resumes this connection from saved state (cookie)
	 */
	public native void resume() /*-{
	   	var delegate = this.@com.anzsoft.client.XMPP.impl.JsJacConnection::connection;
	   	delegate.resume();
	   }-*/;

	// FIXME: only one method for both sends!!
	public native void send(XmppPacket packet) throws JavaScriptException /*-{
	   	var delegate = this.@com.anzsoft.client.XMPP.impl.JsJacConnection::connection;
	   	var jsPacket = packet.@com.anzsoft.client.XMPP.impl.JsJacPacket::delegate;
		delegate.send(jsPacket);
	   }-*/;

	public native void send(final XmppPacket packet, final XmppPacketListener listener) /*-{
	   	var delegate = this.@com.anzsoft.client.XMPP.impl.JsJacConnection::connection;
	   	var jsPacket = packet.@com.anzsoft.client.XMPP.impl.JsJacPacket::delegate;
		delegate.send(jsPacket, function(packet) {
		var p = @com.anzsoft.client.XMPP.impl.JsJacPacket::newPacket(Lcom/google/gwt/core/client/JavaScriptObject;)(packet);
	   		listener.@com.anzsoft.client.XMPP.XmppPacketListener::onPacketReceived(Lcom/anzsoft/client/XMPP/XmppPacket;)(p);
	});
	   }-*/;

	/**
	 * Suspsends this connection (saving state for later resume)
	 * 
	 * @throws JavaScriptException
	 */
	public native void suspend() throws JavaScriptException /*-{
	   	var delegate = this.@com.anzsoft.client.XMPP.impl.JsJacConnection::connection;
	   	delegate.suspend();
	   }-*/;
}
