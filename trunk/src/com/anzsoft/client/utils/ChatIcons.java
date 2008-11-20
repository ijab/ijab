/*
 *
 * ((e)) emite: A pure gwt (Google Web Toolkit) xmpp (jabber) library
 *
 * (c) 2008 The emite development team (see CREDITS for details)
 * This file is part of emite.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 */
package com.anzsoft.client.utils;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.AbstractImagePrototype;
import com.google.gwt.user.client.ui.ImageBundle;

public interface ChatIcons extends ImageBundle {

    public static class App {
	private static ChatIcons ourInstance = null;

	public static synchronized ChatIcons getInstance() {
	    if (ourInstance == null) {
		ourInstance = (ChatIcons) GWT.create(ChatIcons.class);
	    }
	    return ourInstance;
	}
    }

    @Resource("add.png")
    AbstractImagePrototype add();

    @Resource("away.png")
    AbstractImagePrototype away();

    @Resource("busy.png")
    AbstractImagePrototype busy();

    @Resource("cancel.png")
    AbstractImagePrototype cancel();

    @Resource("chat.png")
    AbstractImagePrototype chat();

    @Resource("chat-new-message-small.png")
    AbstractImagePrototype chatNewMessageSmall();

    @Resource("chat-small.png")
    AbstractImagePrototype chatSmall();

    @Resource("del.png")
    AbstractImagePrototype del();

    @Resource("group-chat.png")
    AbstractImagePrototype groupChat();

    @Resource("info.png")
    AbstractImagePrototype info();

    @Resource("info-lamp.png")
    AbstractImagePrototype infoLamp();

    @Resource("invisible.png")
    AbstractImagePrototype invisible();

    @Resource("message.png")
    AbstractImagePrototype message();

    @Resource("new-chat.png")
    AbstractImagePrototype newChat();

    @Resource("new-email.png")
    AbstractImagePrototype newEmail();

    @Resource("new-message.png")
    AbstractImagePrototype newMessage();

    @Resource("not-authorized.png")
    AbstractImagePrototype notAuthorized();

    @Resource("offline.png")
    AbstractImagePrototype offline();

    @Resource("online.png")
    AbstractImagePrototype online();

    @Resource("question.png")
    AbstractImagePrototype question();

    @Resource("room-new-message-small.png")
    AbstractImagePrototype roomNewMessageSmall();

    @Resource("room-small.png")
    AbstractImagePrototype roomSmall();

    @Resource("unavailable.png")
    AbstractImagePrototype unavailable();

    @Resource("user_add.png")
    AbstractImagePrototype userAdd();

    @Resource("xa.png")
    AbstractImagePrototype xa();
    
    @Resource("icon-search.gif")
    AbstractImagePrototype iconSearch();
    
    @Resource("icon-close.gif")
    AbstractImagePrototype iconClose();
}
