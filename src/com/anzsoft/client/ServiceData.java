/*
    iJab , The Ajax web jabber client
    Copyright (c) 2006-2008 by AnzSoft
   
    Author:Fanglin Zhong <zhongfanglin@anzsoft.com>

    Started at 2008-08-20, Beijing of China

    iJab    (c) 2006-2008 by the ijab developers  

    *************************************************************************
    *                                                                       *
    * This program is free software; you can redistribute it and/or modify  *
    * it under the terms of the GNU General Public License as published by  *
    * the Free Software Foundation; either version 2 of the License, or     *
    * (at your option) any later version.                                   *
    *                                                                       *
    *************************************************************************
*/


package com.anzsoft.client;

import com.google.gwt.core.client.JavaScriptObject;

public class ServiceData<E extends JavaScriptObject> extends JavaScriptObject
{
	static class Service extends JavaScriptObject
	{
		protected Service()
		{
			
		}
		
		public final native String getName()
		/*-{
			return this.name;
		}-*/;
		
		public final native String getJid()
		/*-{
			return this.jid;
		}-*/;
	}
	
	protected ServiceData()
	{
		
	}
	
	public final native int length() /*-{ return this.length; }-*/;
	public final native E get(int i) /*-{ return this[i];     }-*/;
}
