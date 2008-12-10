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

// var JSJACJID_FORBIDDEN = ['"',' ','&','\'','/',':','<','>','@'];

public class XmppID {
    private String domain;
    private String node;
    private String resource;

    public XmppID(final String node, final String domain, final String resource) {
	this.node = node;
	this.domain = domain;
	this.resource = resource;
    }
    
    public String getDomain() {
	return domain;
    }

    public void setDomain(final String domain) {
	this.domain = domain;
    }

    public String getNode() {
	return node;
    }

    public void setNode(final String node) {
	this.node = node;
    }

    public String getResource() {
	return resource;
    }

    public void setResource(final String resource) {
	this.resource = resource;
    }

    public String toString() {
	return render(node, domain, resource);
    }

    public static XmppID parseId(String sid) {
	String node = null;
	String domain;
	String resource = null;

	if (sid == null) {
	    return null;
	}

	int indexOfAt = sid.indexOf('@');
	if (indexOfAt > 0) {
	    node = sid.substring(0, indexOfAt);
	    sid = sid.substring(indexOfAt + 1);
	}
	int indexOfSlash = sid.indexOf('/');
	if (indexOfSlash > 0) {
	    resource = sid.substring(indexOfSlash + 1);
	    sid = sid.substring(0, indexOfSlash);
	}
	domain = sid;
	return new XmppID(node, domain, resource);
    }


    public static String render(final String node, final String domain, final String resource) {
	String nodeString = node != null ? node + "@" : "";
	String resString = resource != null ? "/" + resource : "";
	return nodeString + domain  + resString;
    }

    public String toStringNoResource() {
	return render(node, domain, null);
    }

    public int hashCode() {
	final int prime = 31;
	int result = 1;
	result = prime * result + ((domain == null) ? 0 : domain.hashCode());
	result = prime * result + ((node == null) ? 0 : node.hashCode());
	result = prime * result + ((resource == null) ? 0 : resource.hashCode());
	return result;
    }

    public boolean equals(final Object obj) {
	if (this == obj)
	    return true;
	if (obj == null)
	    return false;
	final XmppID other = (XmppID) obj;
	if (domain == null) {
	    if (other.domain != null)
		return false;
	} else if (!domain.equals(other.domain))
	    return false;
	if (node == null) {
	    if (other.node != null)
		return false;
	} else if (!node.equals(other.node))
	    return false;
	if (resource == null) {
	    if (other.resource != null)
		return false;
	} else if (!resource.equals(other.resource))
	    return false;
	return true;
    }
    
    public boolean compareNoResouce(final XmppID other)
    {
    	if (this == other)
    	    return true;
    	if (other == null)
    	    return false;
    	if (domain == null) {
    	    if (other.domain != null)
    		return false;
    	} else if (!domain.equals(other.domain))
    	    return false;
    	if (node == null) {
    	    if (other.node != null)
    		return false;
    	} else if (!node.equals(other.node))
    	    return false;
    	return true;
    }
    
    public boolean compareNoResouce(final String jid)
    {
    	final XmppID other = XmppID.parseId(jid);
    	if (domain == null) {
    	    if (other.domain != null)
    		return false;
    	} else if (!domain.equals(other.domain))
    	    return false;
    	if (node == null) {
    	    if (other.node != null)
    		return false;
    	} else if (!node.equals(other.node))
    	    return false;
    	return true;
    }



}
