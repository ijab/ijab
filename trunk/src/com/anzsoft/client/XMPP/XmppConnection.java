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

public interface XmppConnection {

    void addEventListener(XmppEventListener eventListener);

    void addInfoQueryListener(XmppInfoQueryListener infoQueryListener);

    void addMessageListener(XmppMessageListener messageListener);

    void addPacketListener(XmppPacketListener packetListener);

    void addPreseceListener(XmppPresenceListener presenceListener);

    void connect(XmppUserSettings user);

    void disconnect();

    XmppFactory getFactory();

    void resume();

    void send(XmppPacket packet);

    void send(XmppPacket packet, XmppPacketListener listener);

	void addConnectionListener(XmppConnectionListener xmppConnectionListener);
	
	boolean isConnected();
}
