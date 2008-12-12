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

import com.anzsoft.client.XMPP.XmppQuery;
import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.dom.client.Element;

public class JsJacQuery extends JsJacPacket implements XmppQuery {
    public JsJacQuery() {
	super(create());
    }

    private static native JavaScriptObject create()/*-{
    	return new $wnd.JSJaCIQ();
    }-*/;

    public native String getQueryXMLNS() /*-{
    	return this.@com.anzsoft.client.XMPP.impl.JsJacPacket::delegate.getQueryXMLNS();
    }-*/;

    public native Element setQuery(String xmlns) /*-{
    	return this.@com.anzsoft.client.XMPP.impl.JsJacPacket::delegate.setQuery(xmlns);
    }-*/;

	public native void setIQ(String to, String type, String id) 
	/*-{
		this.@com.anzsoft.client.XMPP.impl.JsJacPacket::delegate.setIQ(to,type,id);
	}-*/;
}
