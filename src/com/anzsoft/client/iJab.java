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


import com.anzsoft.client.JabberApp.LoginListener;
import com.anzsoft.client.XMPP.XmppID;
import com.anzsoft.client.ui.ChatWindow;
import com.google.gwt.core.client.EntryPoint;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class iJab implements EntryPoint 
{
	
	/**
   * This is the entry point method.
   */	
  public void onModuleLoad()
  { 
	  defineBridgeMethod();
	  /*
	  if(!silent)
	  {
		  JabberApp.instance().run(silent);
		  onUnloadbefore(JabberApp.getConstants().leavePrompt());
	  }
	  */
  }
  
  public native static void onUnloadbefore(String msg)
  /*-{
		window.onbeforeunload = function (evt) 
		{
		  var message = msg;
		  if (typeof evt == 'undefined') {
		    evt = window.event;
		  }
		  if (evt) {
		    evt.returnValue = message;
		  }
		  return message;
		}
   }-*/;
  
  public static void talkTo(final String jid)
  {
	  if(JabberApp.instance().connected())
		  ChatWindow.openChat(XmppID.parseId(jid));
	  else
	  {
		  String randomId = createRandomUserName();
		  JabberApp.instance().run(randomId,randomId,true,new LoginListener()
		  {
			public void onLogined() 
			{
				ChatWindow.openChat(XmppID.parseId(jid));
			}
			  
		  });
	  }
  }
  
  public static void talkTo(final String userName,final String password,final String target)
  {
	  if(JabberApp.instance().connected())
		  ChatWindow.openChat(XmppID.parseId(target));
	  else
	  {
		  JabberApp.instance().run(userName,password,true,new LoginListener()
		  {
			public void onLogined() 
			{
				ChatWindow.openChat(XmppID.parseId(target));
			}
			  
		  });
	  }
  }
  
  private static String createRandomUserName()
  {
	  char[] s = new char[5];
	  char itoh[]={'0','1','2','3','4','5','6','7','8','9','A','B','C','D','E','F'};
	  for (int i = 0; i < 5; i++)
	  {
		  s[i] = (char) Math.floor(Math.random()*0x10);
	  }

	  //s[14] = 4;
	  //s[19] =(char) ((s[19] & 0x3) | 0x8);

	  for (int i = 0; i < 5; i++)
		  s[i] = itoh[s[i]];

	  //s[8] = s[13] = s[18] = s[23] = '-';

	  return (("Visitor")+ "("+new String(s)+")");
  }
  
  public static void loginUser(final String user,final String password, boolean silent)
  {
	  JabberApp.instance().run(user,password,silent,null);
  }
  
  public static void loginAnonymouse(boolean silent)
  {
	  String randomId = createRandomUserName();
	  JabberApp.instance().run(randomId,randomId,silent,null);
  }
  
  public static void login()
  {
	  JabberApp.instance().run(false);
	  onUnloadbefore(JabberApp.getConstants().leavePrompt());
  }
  
  public static void logout()
  {
	  JabberApp.instance().logout();
  }
  
  private native void defineBridgeMethod()
  /*-{
   	$wnd.talkTo = function(jid)
   	{
   		return @com.anzsoft.client.iJab::talkTo(Ljava/lang/String;)(jid);
   	}
   	
   	$wnd.loginUser = function(user,password,silent)
   	{
   		return @com.anzsoft.client.iJab::loginUser(Ljava/lang/String;Ljava/lang/String;Z)(user,password,silent);
   	}
   	
   	$wnd.loginAnonymouse = function(silent)
   	{
   		return @com.anzsoft.client.iJab::loginAnonymouse(Z)(silent);
   	}
   	
   	$wnd.logoutJabber = function()
   	{
   		return @com.anzsoft.client.iJab::logout();
   	}
   	
   	$wnd.doJabberLogin = function()
   	{
   		return @com.anzsoft.client.iJab::login()();
   	}
   	
   	$wnd.loginAndTalkTo = function(user,password,target)
   	{
   		return @com.anzsoft.client.iJab::talkTo(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)(user,password,target);
   	}
  }-*/;
}
