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

package com.anzsoft.client.XMPP.mandioca;

public class XmppContactStatus 
{
	public enum Type{Offline,Invisible,DND,XA,Away,Online,FFC}
	
	private String v_show,v_status;
	private boolean v_isAvaiable; 
	private int v_priority;
	
	public XmppContactStatus(final String show,final String status,int priority,boolean avaiable)
	{
		this.v_priority = priority;
		this.v_show = show;
		this.v_status = status;
		this.v_isAvaiable = avaiable;
	};
	
	public XmppContactStatus()
	{
		this.v_show = "";
		this.v_status = "";
		this.v_priority = 0;
		this.v_isAvaiable = false;
	}
	
	public String show()
	{
		return this.v_show;
	}
	
	public String status()
	{
		return this.v_status;
	}
	
	public int priority()
	{
		return this.v_priority;
	}
	
	public boolean isAvaiable()
	{
		return this.v_isAvaiable;
	}
	
	public Type type()
	{
		Type ret = Type.Online;
		if(!isAvaiable())
			ret = Type.Offline;
		else
		{
			String s = show();
			if(s.equals("away"))
				ret = Type.Away;
			else if(s.equals("xa"))
				ret = Type.XA;
			else if(s.equals("dnd"))
				ret = Type.DND;
			else if(s.equals("chat"))
				ret = Type.FFC;
		}
		return ret;
	}
}
