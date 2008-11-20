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

public class XmppStatus {
    public static final XmppStatus INITIALIZING = new XmppStatus("initializing", false);
    public static final XmppStatus CONNECTING = new XmppStatus("connecting", false);
    public static final XmppStatus RESUMING = new XmppStatus("resuming", false);
    public static final XmppStatus PROCESSING = new XmppStatus("processing", false);
    public static final XmppStatus SUSPENDING = new XmppStatus("suspending", false);
    public static final XmppStatus ABORTED = new XmppStatus("aborted", false);
    public static final XmppStatus DISCONNECTING = new XmppStatus("disconnecting", false);
    public static final XmppStatus ERROR_REQUEST = new XmppStatus("onerror_fallback", true);
    public static final XmppStatus ERROR_PROTOCOL = new XmppStatus("protoerror_fallback", true);
    public static final XmppStatus ERROR_SERVER = new XmppStatus("internal_server_error", true);
    public static final XmppStatus ERROR_UNKNOWN = new XmppStatus("unknown", true);
    private static final HashMap constants = new HashMap();

    static {
	constants.put(INITIALIZING.getID(), INITIALIZING);
	constants.put(CONNECTING.getID(), CONNECTING);
	constants.put(RESUMING.getID(), RESUMING);
	constants.put(PROCESSING.getID(), PROCESSING);
	constants.put(SUSPENDING.getID(), SUSPENDING);
	constants.put(ABORTED.getID(), ABORTED);
	constants.put(DISCONNECTING.getID(), DISCONNECTING);
	constants.put(ERROR_REQUEST.getID(), ERROR_REQUEST);
	constants.put(ERROR_PROTOCOL.getID(), ERROR_PROTOCOL);
	constants.put(ERROR_SERVER.getID(), ERROR_SERVER);
    }

    private final boolean isError;
    private final String id;

    private XmppStatus(final String id, final boolean isError) {
	this.id = id;
	this.isError = isError;
    }

    public static XmppStatus getStatus(final String statusId) {
	XmppStatus status = (XmppStatus) constants.get(statusId);
	if (status == null)
	    status = ERROR_UNKNOWN;
	return status;
    }

    public String getID() {
	return id;
    }

    public String toString() {
	return id;
    }

    public boolean isError() {
        return isError;
    }

}
