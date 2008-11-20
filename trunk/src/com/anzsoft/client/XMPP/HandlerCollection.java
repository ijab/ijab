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


import java.util.ArrayList;
import java.util.Iterator;

public class HandlerCollection {
  private static final ArrayList EMPTY_LIST = new ArrayList();
  
  private ArrayList handlers;

  public void add(Object handler) {
    handlers = (handlers == null) ? new ArrayList() : new ArrayList(handlers);
    handlers.add(handler);
  }

  public void remove(Object handler) {
    if (handlers == null || handler == null) {
      return;
    }

    Iterator it = handlers.iterator();
    handlers = new ArrayList();
    while (it.hasNext()) {
      Object obj = it.next();
      if (obj != handler) {
        handlers.add(obj);
      }
    }
  }

  public Iterator iterator() {
    return (handlers == null) ? EMPTY_LIST.iterator()
        : handlers.iterator();
  }

  public int size() {
    return (handlers == null) ? 0 : handlers.size();
  }
  
  public void clear() {
    handlers = null;
  }
}