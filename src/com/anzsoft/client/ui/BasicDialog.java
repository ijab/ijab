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

package com.anzsoft.client.ui;

import com.extjs.gxt.ui.client.Style.HorizontalAlignment;
import com.extjs.gxt.ui.client.widget.Window;



public class BasicDialog extends Window {

    public BasicDialog(final String caption, final boolean modal, final boolean autoScroll) {
        setAutoWidth(true);
        // Param values
        setTitle(caption);
        setModal(modal);
        //setAutoScroll(autoScroll);
        // Def values
        setShadow(true);
        setPlain(true);
        setClosable(true);
        setCollapsible(true);
        setResizable(true);
        this.setCloseAction(CloseAction.HIDE);
        //setCloseAction(Window.HIDE);
        this.setButtonAlign(HorizontalAlignment.RIGHT);
    }

    public BasicDialog(final String caption, final boolean modal) {
        this(caption, modal, false);
    }

    public BasicDialog(final String caption, final boolean modal, final boolean autoScroll, final int width,
            final int height, final int minWidth, final int minHeight) {
        this(caption, modal, autoScroll);
        setAutoWidth(false);
        // Param values
        setWidth(width);
        setHeight(height);
        setMinWidth(minWidth);
        setMinHeight(minHeight);
    }

    public BasicDialog(final String caption, final boolean modal, final boolean autoScroll, final int width,
            final int height) {
        this(caption, modal, autoScroll, width, height, width, height);
    }

}

