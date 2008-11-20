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

import java.util.HashMap;
import java.util.Map;

public class PresenceShow {
    public static final PresenceShow CHAT = new PresenceShow("chat");
    public static final PresenceShow AWAY = new PresenceShow("away");
    public static final PresenceShow NOT_AVAILABLE= new PresenceShow("xa");
    public static final PresenceShow DO_NOT_DISTURB = new PresenceShow("dnd");
    private static final Map shows;

    static {
	shows = new HashMap();
	shows.put(CHAT.getID(), CHAT);
	shows.put(AWAY.getID(), AWAY);
	shows.put(NOT_AVAILABLE.getID(), NOT_AVAILABLE);
	shows.put(DO_NOT_DISTURB.getID(), DO_NOT_DISTURB);
    }
    private final String token;

    private PresenceShow(final String token) {
        this.token = token;
    }
    public String getID() {
        return token;
    }
    public String toString() {
        return token;
    }
    public static PresenceShow emptyShow()
    {
    	return new PresenceShow("");
    }

    public static PresenceShow get(final String showID) {
	return (PresenceShow) shows.get(showID);
    }
}