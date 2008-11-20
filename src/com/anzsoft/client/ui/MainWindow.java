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

import com.anzsoft.client.JabberApp;
import com.anzsoft.client.XMPP.XmppID;
import com.anzsoft.client.XMPP.mandioca.XmppSession;
import com.extjs.gxt.ui.client.Style.HorizontalAlignment;
import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.widget.Window;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.button.StatusButtonBar;
import com.extjs.gxt.ui.client.widget.layout.RowData;
import com.extjs.gxt.ui.client.widget.layout.RowLayout;


public class MainWindow extends Window 
{
	private RosterPanel rosterPanel;
	private UserIndicator indicator;
	protected StatusButtonBar buttonBar;
	
	private Button logoutButton;
	public MainWindow(XmppSession session)
	{
		setLayout(new RowLayout());
		setWidth(280);
		setHeight(600);
		setHeading("iJab");
		//setIconCls("MainWindow-Icon");
		setClosable(false);
		setCollapsible(true);
		setAnimCollapse(true);
		//setCls("MainWindow");
		this.setMinimizable(false);
		this.setMaximizable(true);
		
		rosterPanel = new RosterPanel(JabberApp.getConstants().buddies());
		indicator = new UserIndicator(XmppID.parseId(session.getUser().toString()).getNode());
		add(indicator,new RowData(1,-1));
		add(rosterPanel,new RowData(1,1));
		
		buttonBar = new StatusButtonBar();
		buttonBar.setButtonAlign(HorizontalAlignment.LEFT);
		setButtonBar(buttonBar);
		
		logoutButton = new Button(JabberApp.getConstants().logout());
		logoutButton.addSelectionListener(new SelectionListener<ButtonEvent>()
		{
			public void componentSelected(ButtonEvent ce)
			{
				JabberApp.instance().logout();
			}
		});
		
		buttonBar.add(logoutButton);
	}
	
	public RosterPanel getRosterPanel()
	{
		return this.rosterPanel;
	}
	
	public UserIndicator getIndicator()
	{
		return this.indicator;
	}
}
