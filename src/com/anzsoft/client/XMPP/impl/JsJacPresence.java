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

import com.anzsoft.client.XMPP.PresenceShow;
import com.anzsoft.client.XMPP.XmppPresence;
import com.anzsoft.client.utils.XMLHelper;
import com.google.gwt.core.client.JavaScriptException;
import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.xml.client.Document;
import com.google.gwt.xml.client.Element;
import com.google.gwt.xml.client.XMLParser;

class JsJacPresence extends JsJacPacket implements XmppPresence{

    public JsJacPresence() {
	this(create());
    }


    private static native JavaScriptObject create() throws JavaScriptException /*-{
    	return new $wnd.JSJaCPresence();
    }-*/;

    JsJacPresence(final JavaScriptObject jso) {
	super(jso);
    }

    static JsJacPresence newPresence(final JavaScriptObject jso) {
	return new JsJacPresence(jso);
    }

    public int getPriority() {
	try {
	    return new Integer(getNativePriority()).intValue();
	} catch (NumberFormatException e) {
	    return 0;
	}
    }

    public native String getNativePriority() /*-{
	return this.@com.anzsoft.client.XMPP.impl.JsJacPacket::delegate.getPriority();
    }-*/;

    public native void setPriority(int priority)  /*-{
	this.@com.anzsoft.client.XMPP.impl.JsJacPacket::delegate.setPriority(priority);
    }-*/;

    public native String getStatus() /*-{
	return this.@com.anzsoft.client.XMPP.impl.JsJacPacket::delegate.getStatus();
    }-*/;

    public native void setStatus(String message)  /*-{
	this.@com.anzsoft.client.XMPP.impl.JsJacPacket::delegate.setStatus(message);
    }-*/;

    public void setShow(final PresenceShow presenceShow) {
	setShowID(presenceShow.toString());
    }

    public PresenceShow getShow() {
	return PresenceShow.get(getShowID());
    }

    private native String getShowID() /*-{
	return this.@com.anzsoft.client.XMPP.impl.JsJacPacket::delegate.getShow();
    }-*/;


    private native void setShowID(String showID)  /*-{
	this.@com.anzsoft.client.XMPP.impl.JsJacPacket::delegate.setShow(showID);
    }-*/;


	public String getNick() 
	{
		String xml = toXML();
		Document doc = XMLParser.parse(xml);
		Element rootEl = doc.getDocumentElement();
		if(!rootEl.getTagName().equals("presence"))
			return "";
		Element nickEl = XMLHelper.findSubTag(rootEl, "nick");
		if(nickEl != null && nickEl.getAttribute("xmlns").equals("http://jabber.org/protocol/nick"))
			return nickEl.getNodeValue();
		return "";
	}


}
