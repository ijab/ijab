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

package com.anzsoft.client.XMPP.impl;

import com.google.gwt.core.client.JavaScriptObject;

class JsJacRoomPresence extends JsJacPresence {

    public JsJacRoomPresence() {
	this(create());
    }

    JsJacRoomPresence(final JavaScriptObject delegate) {
	super(delegate);
    }

    static native JavaScriptObject create() /*-{
        var presence = new $wnd.JSJaCPresence();
        var x = presence.getDoc().createElement('x');
        x.setAttribute('xmlns','http://jabber.org/protocol/muc');
        presence.getNode().appendChild(x);
        return presence;
    }-*/;

//  if (typeof(pass) != 'undefined' && pass != '')
//    x.appendChild(aPresence.getDoc().createElement('password')).appendChild(aPresence.getDoc().createTextNode(pass));

}
