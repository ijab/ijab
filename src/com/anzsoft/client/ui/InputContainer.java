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

import com.extjs.gxt.ui.client.Style.VerticalAlignment;
import com.extjs.gxt.ui.client.widget.ComponentHelper;
import com.extjs.gxt.ui.client.widget.layout.TableData;
import com.extjs.gxt.ui.client.widget.toolbar.ToolBar;
import com.extjs.gxt.ui.client.widget.toolbar.ToolItem;

public class InputContainer extends ToolBar
{
	public InputContainer()
	{
		baseStyle = "x-toolbar";
		layoutOnChange = true;
		enableLayout = true;
	}
	
	public boolean addInputItem(ToolItem item,TableData data)
	{
		int index = getItemCount();
		boolean added = super.insert(item, index);
	    if (added) 
	    {
	      data.setVerticalAlign(VerticalAlignment.MIDDLE);
	      ComponentHelper.setLayoutData(item, data);
	    }
	    return added;
	}
}
