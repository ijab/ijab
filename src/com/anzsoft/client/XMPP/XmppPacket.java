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

import com.anzsoft.client.utils.XmlDocument;
import com.google.gwt.dom.client.Node;

public interface XmppPacket {

	Node getNode();
	
	XmlDocument getDoc();
	
    String getFrom();

    XmppID getFromID();

    String getID();

    String getRootType();

    String getTo();

    XmppID getToID();

    String getType();

    String getXMLLang();

    String getXMLNS();
    
    String toXML();

    void setFrom(String from);

    void setFrom(XmppID id);

    void setID(String id);

    void setTo(String stringID);

    void setTo(XmppID toId);

    void setType(String type);

}
