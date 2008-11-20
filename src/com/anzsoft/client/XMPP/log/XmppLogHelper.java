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
package com.anzsoft.client.XMPP.log;

import com.anzsoft.client.XMPP.XmppID;
import com.anzsoft.client.XMPP.XmppMessage;
import com.anzsoft.client.XMPP.XmppPacket;
import com.anzsoft.client.XMPP.XmppPresence;
import com.anzsoft.client.XMPP.XmppQuery;
import com.anzsoft.client.XMPP.XmppStatus;

public class XmppLogHelper {

    public static void writeStatus(final XmppStatus xmppStatus, final LoggerOuput output) {
        output.log("[status - id: " + xmppStatus.getID() + "]");
    }

    public static void writePacket(final XmppPacket packet, final LoggerOuput output) {
    	output.log(packet.toXML());
    	/*
	output.log("[packet -  root type: " + packet.getRootType()+ "]");
        output.log("[packet -  from: " + packet.getFrom() + "]");
        output.log("[packet -  to: " + packet.getTo() + "]");
        output.log("[packet -  id: " + packet.getID() + "]");
        output.log("[packet -  type: " + packet.getType() + "]");
        output.log("[packet -  xml:lang: " + packet.getXMLLang() + "]");
        output.log("[packet -  xmlns: " + packet.getXMLNS() + "]");
        */
        // FIXME: these methods are not secure!
//        LogHelper.writeID(xmppPacket.getFromID(), output);
//        LogHelper.writeID(xmppPacket.getToID(), output);
    }

    public static void writeMessage(final XmppMessage message, final LoggerOuput output) {
        writePacket(message, output);
        /*
        output.log("[message - body: " + message.getBody() + "]");
        output.log("[message - subject: " + message.getSubject() + "]");
        output.log("[message - thread: " + message.getThread() + "]");
        */
    }

    public static void writePresence(final XmppPresence presence, final LoggerOuput output) {
        writePacket(presence, output);
        /*
        output.log("[presence - priority: " + presence.getPriority() + "]");
        output.log("[presence - status (msg): " + presence.getStatus() + "]");
        output.log("[presence - show: " + presence.getShow() + "]");
        */
    }

    // TODO: put it again when getFromJID and getToJID are more secure!
    static void writeID(final XmppID id, final LoggerOuput output) {
    	/*
        output.log("[id - domain: " + id.getDomain()+ "]");
        output.log("[id - node: " + id.getNode()+ "]");
        output.log("[id - resource: " + id.getResource() + "]");
        */

    }

    public static void writeQuery(final XmppQuery query, final LoggerOuput output) {
	writePacket(query, output);
	output.log("[query - xmlns: " + query.getQueryXMLNS()+ "]");
    }


}
