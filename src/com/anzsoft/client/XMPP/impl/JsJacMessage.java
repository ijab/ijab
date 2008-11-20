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

import com.anzsoft.client.XMPP.XmppMessage;
import com.google.gwt.core.client.JavaScriptObject;

class JsJacMessage extends JsJacPacket implements XmppMessage {

    public JsJacMessage() {
	this(create());
    }

    private native static JavaScriptObject create() /*-{
    	return new $wnd.JSJaCMessage();
    }-*/;

    JsJacMessage(final JavaScriptObject jso) {
	super(jso);
    }

    public native String getBody() /*-{
	return this.@com.anzsoft.client.XMPP.impl.JsJacPacket::delegate.getBody();
    }-*/;

    public native void setBody(String body) /*-{
	this.@com.anzsoft.client.XMPP.impl.JsJacPacket::delegate.setBody(body);
    }-*/;

    public native String getBodyAsHTML() /*-{
	return this.@com.anzsoft.client.XMPP.impl.JsJacPacket::delegate.getBody().htmlEnc();
    }-*/;


    public native String getSubject() /*-{
	return this.@com.anzsoft.client.XMPP.impl.JsJacPacket::delegate.getSubject();
    }-*/;

    public native String getThread() /*-{
	return this.@com.anzsoft.client.XMPP.impl.JsJacPacket::delegate.getThread();
    }-*/;


    public static JsJacMessage newMessage(final JavaScriptObject jso) {
	return new JsJacMessage(jso);
    }

}
