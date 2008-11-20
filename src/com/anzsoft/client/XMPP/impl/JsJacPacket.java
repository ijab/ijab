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

import com.anzsoft.client.XMPP.XmppID;
import com.anzsoft.client.XMPP.XmppPacket;
import com.google.gwt.core.client.JavaScriptObject;

class JsJacPacket implements XmppPacket {
    protected final JavaScriptObject delegate;

    JsJacPacket(final JavaScriptObject jso) {
	this.delegate = jso;
    }

    static JsJacPacket newPacket(final JavaScriptObject jso) {
	return new JsJacPacket(jso);
    }

    public native String getFrom() /*-{
	return this.@com.anzsoft.client.XMPP.impl.JsJacPacket::delegate.getFrom();
    }-*/;


    public XmppID getFromID() {
	return XmppID.parseId(getFrom());
    }

    public native void setFrom(String from) /*-{
	this.@com.anzsoft.client.XMPP.impl.JsJacPacket::delegate.setFrom(from);
   }-*/;

    public void setFrom(final XmppID from) {
	setFrom(from.toString());
    }


    public native String getID() /*-{
	return this.@com.anzsoft.client.XMPP.impl.JsJacPacket::delegate.getID();
    }-*/;

    public native void setID(final String id) /*-{
	this.@com.anzsoft.client.XMPP.impl.JsJacPacket::delegate.setID(id);
    }-*/;

    public native String getTo() /*-{
	return this.@com.anzsoft.client.XMPP.impl.JsJacPacket::delegate.getTo();
    }-*/;


    public XmppID getToID() {
	return XmppID.parseId(getTo());
    }

    public native void setTo(String toUserID) /*-{
	this.@com.anzsoft.client.XMPP.impl.JsJacPacket::delegate.setTo(toUserID);
    }-*/;

    public void setTo(final XmppID id) {
	setTo(id.toString());
    }


    public native String getType() /*-{
	return this.@com.anzsoft.client.XMPP.impl.JsJacPacket::delegate.getType();
     }-*/;

    public native void setType(String type) /*-{
	this.@com.anzsoft.client.XMPP.impl.JsJacPacket::delegate.setType(type);
    }-*/;


    public native String getXMLLang() /*-{
	return this.@com.anzsoft.client.XMPP.impl.JsJacPacket::delegate.getXMLLang();
     }-*/;

    public native String getXMLNS() /*-{
	return this.@com.anzsoft.client.XMPP.impl.JsJacPacket::delegate.getXMLNS();
     }-*/;

    public native String getRootType() /*-{
	return this.@com.anzsoft.client.XMPP.impl.JsJacPacket::delegate.pType();
    }-*/;

    public native String toXML() /*-{
	return this.@com.anzsoft.client.XMPP.impl.JsJacPacket::delegate.xml();
    }-*/;

}
