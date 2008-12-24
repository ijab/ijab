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
package com.anzsoft.client.XMPP.log;

import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.button.ButtonBar;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.google.gwt.user.client.ui.TextArea;


public class DebugPanel extends LayoutContainer implements LoggerOuput {
    private final TextArea area;
    private ButtonBar buttonBar;

    public DebugPanel()
    {
    	setLayout(new FitLayout());
    	area = new TextArea();
    	//area.setWidth("500");
    	//area.setHeight("500");
    	add(area);
    	buttonBar = new ButtonBar();
    	Button clearButton = new Button("Clear");
    	clearButton.addSelectionListener(new SelectionListener<ButtonEvent>()
		{
    		public void componentSelected(ButtonEvent ce) 
    		{
    			area.setText("");
    		}

		});
    	buttonBar.add(clearButton);
    	//add(buttonBar);
    }
    
    public ButtonBar getBar()
    {
    	return buttonBar;
    }

    public void log(final String message) {
	area.setText(area.getText() + message + "\n");
    }

}
