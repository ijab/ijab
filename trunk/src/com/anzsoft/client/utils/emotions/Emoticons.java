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

package com.anzsoft.client.utils.emotions;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.AbstractImagePrototype;
import com.google.gwt.user.client.ui.ImageBundle;

public interface Emoticons extends ImageBundle {

    public static class App {
	private static Emoticons ourInstance = null;

	public static synchronized Emoticons getInstance() {
	    if (ourInstance == null) {
		ourInstance = (Emoticons) GWT.create(Emoticons.class);
	    }
	    return ourInstance;
	}
    }

    @Resource("alien.png")
    AbstractImagePrototype alien();

    @Resource("andy.png")
    AbstractImagePrototype andy();

    @Resource("angel.png")
    AbstractImagePrototype angel();

    @Resource("angry.png")
    AbstractImagePrototype angry();

    @Resource("bandit.png")
    AbstractImagePrototype bandit();

    @Resource("blushing.png")
    AbstractImagePrototype blushing();

    @Resource("bullet_black.png")
    AbstractImagePrototype bulletBlack();

    @Resource("bullet_star.png")
    AbstractImagePrototype bulletStar();

    @Resource("cool.png")
    AbstractImagePrototype cool();

    @Resource("crying.png")
    AbstractImagePrototype crying();

    @Resource("devil.png")
    AbstractImagePrototype devil();

    @Resource("grin.png")
    AbstractImagePrototype grin();

    @Resource("happy.png")
    AbstractImagePrototype happy();

    @Resource("heart.png")
    AbstractImagePrototype heart();

    @Resource("joyful.png")
    AbstractImagePrototype joyful();

    @Resource("kissing.png")
    AbstractImagePrototype kissing();

    @Resource("lol.png")
    AbstractImagePrototype lol();

    @Resource("love.png")
    AbstractImagePrototype love();

    @Resource("ninja.png")
    AbstractImagePrototype ninja();

    @Resource("pinched.png")
    AbstractImagePrototype pinched();

    @Resource("policeman.png")
    AbstractImagePrototype policeman();

    @Resource("pouty.png")
    AbstractImagePrototype pouty();

    @Resource("sad.png")
    AbstractImagePrototype sad();

    @Resource("sick.png")
    AbstractImagePrototype sick();

    @Resource("sideways.png")
    AbstractImagePrototype sideways();

    @Resource("sleeping.png")
    AbstractImagePrototype sleeping();

    @Resource("smile.png")
    AbstractImagePrototype smile();

    @Resource("surprised.png")
    AbstractImagePrototype surprised();

    @Resource("tongue.png")
    AbstractImagePrototype tongue();

    @Resource("uncertain.png")
    AbstractImagePrototype uncertain();

    @Resource("unsure.png")
    AbstractImagePrototype unsure();

    @Resource("w00t.png")
    AbstractImagePrototype w00t();

    @Resource("whistling.png")
    AbstractImagePrototype whistling();

    @Resource("wink.png")
    AbstractImagePrototype wink();

    @Resource("wizard.png")
    AbstractImagePrototype wizard();

    @Resource("wondering.png")
    AbstractImagePrototype wondering();
}
