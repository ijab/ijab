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

public class XmppUserSettings {
    public static final AuthType SASL = new AuthType("sasl");
    public static final AuthType NON_SASL = new AuthType("nonsasl");

    public static class AuthType 
    {
		private final String constant;
	
		public static AuthType fromString(final String type)
		{
			if(!type.equalsIgnoreCase("sasl")&&!type.equalsIgnoreCase("nosasl"))
				return new AuthType("sasl");
			return new AuthType(type);
		}
		private AuthType(final String constant) 
		{
		    this.constant = constant;
		}
		public String toString() 
		{
		    return constant;
		}
    }

    public String domain;
    public String userName;
    public String password;
    public String resource;
    public String host;
    public int port;
    private AuthType authType;
    public boolean shouldRegister;
    String auth;
    public boolean isSecure;

    public XmppUserSettings(final String host,final int port,final String domain, final String userName, final String password, final String resource, final AuthType authType, final boolean shouldRegister) {
	this.domain = domain;
	this.userName = userName;
	this.password = password;
	this.resource = resource;
	this.shouldRegister = shouldRegister;
	this.host = host;
	this.port = port;
	this.isSecure = false;
	setAuthType(authType);
    }

    public XmppUserSettings(final String host,final int port,final String domain, final String userName, final String password, final AuthType authType) {
	this(host,port,domain, userName, password, "iJab", authType, false);
    }

    public String getID() {
	return XmppID.render(userName, domain, resource);
    }

    public String toString() {
	return getID();
    }

    public AuthType getAuthType() {
        return authType;
    }

    public void setAuthType(final AuthType authType) {
        this.authType = authType;
        this.auth = authType.toString();
    }
}
