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

package com.anzsoft.client.XMPP.mandioca.rooms;

import java.util.ArrayList;
import java.util.Iterator;


class RoomPresenceListenerCollection extends ArrayList 
{
	public void fireUserLeaves(final String alias) 
	{
		for(Iterator it = iterator(); it.hasNext(); ) 
		{
			((RoomPresenceListener) it.next()).onUserLeft(alias);
		}
	}

	public void fireUserEntered(final String alias, MUCItem item) 
	{
		for(Iterator it = iterator(); it.hasNext(); ) 
		{
			((RoomPresenceListener) it.next()).onUserEntered(alias, item);
		}
	}
}
