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
import com.anzsoft.client.iJabConstants;
import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.DisclosurePanel;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.PasswordTextBox;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.client.ui.FlexTable.FlexCellFormatter;


public class LoginForm extends SimplePanel 
{
	public LoginForm()
	{
		add(createAdvancedForm());
		//setWidget(createAdvancedForm());
	}
	
	private Widget createAdvancedForm() 
	{
		iJabConstants constants = (iJabConstants) GWT.create(iJabConstants.class);
	    // Create a table to layout the form options
	    FlexTable layout = new FlexTable();
	    layout.setCellSpacing(6);
	    layout.setWidth("300px");
	    FlexCellFormatter cellFormatter = layout.getFlexCellFormatter();

	    // Add a title to the form
	    /*
	    layout.setHTML(0, 0,constants.iJabLogin());
	    cellFormatter.setColSpan(0, 0, 2);
	    cellFormatter.setHorizontalAlignment(0, 0,
	        HasHorizontalAlignment.ALIGN_CENTER);
	*/
	    // Add some standard form options
	    final TextBox userBox = new TextBox();
	    userBox.setText("imdev");
	    layout.setHTML(0, 0, constants.user());
	    layout.setWidget(0, 1, userBox);
	    final PasswordTextBox  passBox = new PasswordTextBox ();
	    passBox.setText("imdev631");
	    
	    layout.setHTML(1, 0, constants.password());
	    layout.setWidget(1, 1, passBox);

	    // Create some advanced options
	    Grid advancedOptions = new Grid(5, 2);
	    advancedOptions.setCellSpacing(6);
	    
	    final TextBox hostBox = new TextBox();
	    final TextBox portBox = new TextBox();
	    final TextBox domainBox = new TextBox();
	    final CheckBox authCheck = new CheckBox("SASL");
	    authCheck.setChecked(false);
	    
	    hostBox.setEnabled(false);
		portBox.setEnabled(false);
		domainBox.setEnabled(false);
		authCheck.setEnabled(false);
	    	    
	    final CheckBox serverConfig = new CheckBox(constants.defineServerConfig());
	    advancedOptions.setWidget(0, 0, serverConfig);
	    serverConfig.addClickListener(new ClickListener()
	    {
			public void onClick(Widget sender) {
				if(serverConfig.isChecked())
				{
					hostBox.setEnabled(true);
					portBox.setEnabled(true);
					domainBox.setEnabled(true);
					authCheck.setEnabled(true);
				}
				else
				{
					hostBox.setEnabled(false);
					portBox.setEnabled(false);
					domainBox.setEnabled(false);
					authCheck.setEnabled(false);
				}
				
			}
	    	
	    });
	    
	    serverConfig.setChecked(false);
	    
	    advancedOptions.setHTML(1, 0, constants.domain());
	    advancedOptions.setWidget(1, 1, hostBox);
	    
	    advancedOptions.setHTML(2, 0, constants.host());
	    advancedOptions.setWidget(2, 1, portBox);
	    
	    advancedOptions.setHTML(3, 0, constants.port());
	    advancedOptions.setWidget(3, 1, domainBox);
	    
	    advancedOptions.setWidget(4, 0, authCheck);

	    // Add advanced options to form in a disclosure panel
	    DisclosurePanel advancedDisclosure = new DisclosurePanel(
	        constants.moreOptions());
	    advancedDisclosure.setAnimationEnabled(true);
	    advancedDisclosure.ensureDebugId("cwDisclosurePanel");
	    advancedDisclosure.setContent(advancedOptions);
	    layout.setWidget(2, 0, advancedDisclosure);
	    
	    Button loginButton = new Button(constants.login());
	    
	    layout.setWidget(3, 0,loginButton);
	    loginButton.addSelectionListener(new SelectionListener<ButtonEvent>()
	    {
			public void componentSelected(ButtonEvent ce) 
			{
				String user = userBox.getText();
	    		String pass = passBox.getText();
	    		String domain = domainBox.getText();
	    		String host = domainBox.getText();
	    		boolean sasl = authCheck.isChecked();
	    		if(serverConfig.isChecked())
	    		{
	    			int port = Integer.parseInt(portBox.getText());
	    			//JabberApp.instance().onLogin(host, port, domain, sasl, user, pass);
	    		}
	    		else
	    		{
	    			//JabberApp.instance().onLogin(user, pass);
	    		}
			}
	    	
	    });
	   
	    cellFormatter.setHorizontalAlignment(3, 0,
		        HasHorizontalAlignment.ALIGN_CENTER);
	    
	    cellFormatter.setColSpan(3, 0, 2);

	    return layout;
	  }
}
