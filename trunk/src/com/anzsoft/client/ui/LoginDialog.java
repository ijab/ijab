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
import com.anzsoft.client.XMPP.XmppError;
import com.anzsoft.client.XMPP.XmppEventAdapter;
import com.extjs.gxt.ui.client.Events;
import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.ComponentEvent;
import com.extjs.gxt.ui.client.event.KeyListener;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.widget.Dialog;
import com.extjs.gxt.ui.client.widget.MessageBox;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.button.StatusButtonBar;
import com.extjs.gxt.ui.client.widget.form.TextField;
import com.extjs.gxt.ui.client.widget.layout.FormLayout;

public class LoginDialog extends Dialog {

  protected StatusButtonBar buttonBar;
  protected TextField<String> userName;
  protected TextField<String> password;
  protected Button reset;
  protected Button login;

  public LoginDialog() {
    FormLayout layout = new FormLayout();
    layout.setLabelWidth(90);
    layout.setDefaultWidth(175);
    setLayout(layout);

    setButtons("");
    setIconStyle("user");
    setHeading(JabberApp.getConstants().iJabLogin());
    setModal(false);
    setBodyBorder(true);
    setInsetBorder(true);
    setBodyStyle("padding: 8px;background: none");
    setWidth(350);
    setResizable(false);
    setClosable(false);
    setCollapsible(true);

    KeyListener keyListener = new KeyListener() {
      public void componentKeyPress(ComponentEvent event) 
      {
        validate();
        if(event.getKeyCode() == 13&&login.isEnabled())
        {
        	onSubmit();
        }
      }

    };

    userName = new TextField<String>();
    userName.setMinLength(1);
    userName.setFieldLabel(JabberApp.getConstants().user());
    userName.addKeyListener(keyListener);
    add(userName);

    password = new TextField<String>();
    password.setMinLength(1);
    password.setPassword(true);
    password.setFieldLabel(JabberApp.getConstants().password());
    password.addKeyListener(keyListener);
    add(password);

    setFocusWidget(userName);

    buttonBar = new StatusButtonBar();
    setButtonBar(buttonBar);

  }

  @Override
  protected void createButtons() {
    reset = new Button(JabberApp.getConstants().reset());
    reset.addSelectionListener(new SelectionListener<ButtonEvent>() {
      public void componentSelected(ButtonEvent ce) {
        userName.reset();
        password.reset();
        validate();
        userName.focus();
        buttonBar.getStatusBar().clear();
        buttonBar.enable();
      }

    });

    login = new Button(JabberApp.getConstants().login());
    login.disable();
    login.addSelectionListener(new SelectionListener<ButtonEvent>() {
      public void componentSelected(ButtonEvent ce) {
        onSubmit();
      }
    });

    buttonBar.add(reset);
    buttonBar.add(login);
  }

  protected void onSubmit() 
  {
    buttonBar.getStatusBar().showBusy(JabberApp.getConstants().loginPrompt());
    buttonBar.disable();
    XmppEventAdapter xmppEventAdapter = new XmppEventAdapter()
    {
    	public void onConnect()
		{
    		buttonBar.getStatusBar().showBusy(JabberApp.getConstants().getRosterPrompt());
		}
		
		public void onDisconnect()
		{
			
		}
		
		public void onError(final XmppError error) 
		{
			//buttonBar.getStatusBar().showBusy("Login error:"+error.toString());
			MessageBox.alert(JabberApp.getConstants().error(), JabberApp.getConstants().user_or_pwd_error(), null);
			reset.fireEvent(Events.Select);
	    }

	    public void onResume() 
	    {
	    	//buttonBar.getStatusBar().showBusy("On Resume");
	    }

	    public void onStatusChanged(final String status) 
	    {
	    	//buttonBar.getStatusBar().showBusy(status);
	    }
    };
    JabberApp.instance().onLogin(userName.getValue(),password.getValue(), xmppEventAdapter);
  }

  protected boolean hasValue(TextField<String> field) {
    return field.getValue() != null && field.getValue().length() > 0;
  }

  protected void validate() {
    login.setEnabled(hasValue(userName) && hasValue(password) && password.getValue().length() > 3);
  }

}
