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

    public native void createQueryNode(String xmlns) /*-{
    	this.@com.anzsoft.client.XMPP.impl.JsJacPacket::delegate.setQuery(xmlns);
    }-*/;
}
