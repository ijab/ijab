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

import com.anzsoft.client.XMPP.XmppError;
import com.google.gwt.core.client.JavaScriptObject;

class JsJacError implements XmppError{
    final JavaScriptObject delegate;

    JsJacError(final JavaScriptObject delegate) {
	this.delegate = delegate;
    }

    public static JsJacError newError(final JavaScriptObject delegate) {
	return new JsJacError(delegate);
    }

//    return this.@com.anzsoft.client.XMPP.impl.JsJacError::delegate.getXml();
//    return (new $wnd.XMLSerializer()).serializeToString(node);


    public native String toString() /*-{
    	if ($wnd.XMLSerializer) {
        var node = this.@com.anzsoft.client.XMPP.impl.JsJacError::delegate;
	var serializer = new $wnd.XMLSerializer(typeof(node));
        return serializer.serializeToString(node);
        } else {
          return "use firefox to see the error!!!";
        }
    }-*/;
}
