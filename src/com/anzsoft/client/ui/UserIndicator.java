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
import com.anzsoft.client.XMPP.PresenceShow;
import com.anzsoft.client.XMPP.XmppID;
import com.anzsoft.client.XMPP.XmppPresence;
import com.anzsoft.client.XMPP.mandioca.VCardListener;
import com.anzsoft.client.XMPP.mandioca.XmppContactStatus;
import com.anzsoft.client.XMPP.mandioca.XmppSession;
import com.anzsoft.client.XMPP.mandioca.XmppVCard;
import com.anzsoft.client.XMPP.mandioca.XmppVCardFactory;
import com.anzsoft.client.XMPP.mandioca.XmppUser.XmppUserListener;
import com.anzsoft.client.utils.ChatIcons;
import com.extjs.gxt.ui.client.GXT;
import com.extjs.gxt.ui.client.event.MenuEvent;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.menu.Menu;
import com.extjs.gxt.ui.client.widget.menu.MenuItem;
import com.google.gwt.dom.client.ImageElement;
import com.google.gwt.i18n.client.HasDirection.Direction;
import com.google.gwt.user.client.ui.AbstractImagePrototype;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.FocusListener;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.KeyboardListener;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;


public class UserIndicator extends FlexTable
{
	private Image statusImg;
	private Image avatarImg;
	private Label nickName;
	private Label statusLabel;
	//private Label statusMenuLabel;
	private TextBox statusEditor;
	private XmppContactStatus status;
	//private HorizontalPanel linksPanel;
	private Button statusButton;
	
	private Menu statusMenu;
	private MenuItem onlineItem,chatItem,awayItem,dndItem,xaItem;
	public UserIndicator(final String nick)
	{
		createStatusMenu();
		setWidth("100%");
		setCellPadding(0);
	    setCellSpacing(0);
	    setStyleName("indicator");
	    FlexCellFormatter formatter = getFlexCellFormatter();
	    
	    // Setup the links cell
	    /*
	    linksPanel = new HorizontalPanel();
	    setWidget(0, 0, linksPanel);
	    formatter.setStyleName(0, 0, "indicator-links");
	    formatter.setHorizontalAlignment(0, 0, HasHorizontalAlignment.ALIGN_RIGHT);
	    formatter.setColSpan(0, 0, 2);
	    */
	    
	 // Setup the title cell
	    setTitleWidget(null);
	    formatter.setStyleName(0, 0, "indicator-title");
	    
	    getRowFormatter().setVerticalAlign(0,HasVerticalAlignment.ALIGN_TOP);
	    getRowFormatter().setVerticalAlign(1,HasVerticalAlignment.ALIGN_TOP);


		
		final ChatIcons icons = ChatIcons.App.getInstance();
		statusImg = new Image();
		statusImg.setWidth("16px");
		statusImg.setHeight("16px");
		icons.online().applyTo(statusImg);
		
		avatarImg = new Image("images/default_avatar.png");
		avatarImg.setWidth("32px");
		avatarImg.setHeight("32px");
		avatarImg.setStyleName("handler");
		avatarImg.addClickListener(new ClickListener()
		{
			public void onClick(Widget sender)
			{
				JabberApp.instance().showInfoSelf();
			}
			
		});
		
		nickName = new Label(nick);
		nickName.setDirection(Direction.LTR);
		nickName.setWidth("100%");
		 
		statusLabel = new Label();
		statusLabel.setStyleName("status_label");
		statusLabel.setWidth("100%");

		statusEditor = new TextBox();
		statusEditor.setVisible(false);
		statusEditor.setWidth("100%");

		statusButton = new Button();
		statusButton.setMenu(statusMenu);
		statusButton.setStyleName("Status-Menu-Button");
		
		//statusMenuLabel = new Label();
		//statusMenuLabel.setStyleName("status_menu_label");
		
		 //addLink(new HTML("<a href=\"http://samespace.anzsoft.com\">SameSpace</a>"));
		// Add the title and some images to the title bar
		 HorizontalPanel titlePanel = new HorizontalPanel();
		 titlePanel.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
		 titlePanel.setWidth("100%");
		 titlePanel.setSpacing(3);
		 
		 VerticalPanel statusPanel = new VerticalPanel();
		 statusPanel.setWidth("100%");
		 statusPanel.add(nickName);
		 
		 HorizontalPanel hPanel = new HorizontalPanel();
		 hPanel.setWidth("100%");
		 hPanel.setSpacing(2);
		 hPanel.setVerticalAlignment(HasVerticalAlignment.ALIGN_TOP);
		 hPanel.add(statusLabel);
		 hPanel.add(statusEditor);
		 hPanel.add(statusButton);
		 
		 statusPanel.add(hPanel);
		 
		 titlePanel.add(statusImg);
		 titlePanel.add(statusPanel);
		 titlePanel.add(avatarImg);
		 
		 titlePanel.setCellWidth(statusImg, "20px");
		 titlePanel.setCellWidth(statusPanel, "100%");
		 titlePanel.setCellWidth(avatarImg, "32px");
		 setTitleWidget(titlePanel);
		 
		
		JabberApp.instance().getSession().getUser().addUserListener(new XmppUserListener()
		{
			public void onPresenceChanged(XmppPresence presence) 
			{
				String show = new String("");
				PresenceShow presenceShow = presence.getShow();
				if(presenceShow!=null)
					show = presenceShow.toString();
				String statusString = presence.getStatus();
				int priority = presence.getPriority();
				boolean avaiable = true;
				String type = presence.getType();
				if(type != null&&!type.isEmpty())
				{
					if(type.equalsIgnoreCase("unavailable"))
						avaiable = false;
				}
				status = new XmppContactStatus(show,statusString,priority,avaiable);
				statusLabel.setText(status.status());
				iconFromStatus(status).applyTo(statusImg);
			}
		});
		
		statusLabel.addClickListener(new ClickListener()
		{
			public void onClick(Widget sender) 
			{
				statusLabel.setVisible(false);
				statusEditor.setVisible(true);
				statusEditor.setText(statusLabel.getText());
			}
			
		});
		
		statusEditor.addKeyboardListener(new KeyboardListener()
		{

			public void onKeyDown(Widget sender, char keyCode, int modifiers) 
			{
				
			}
			public void onKeyPress(Widget sender, char keyCode, int modifiers) 
			{
				
			}

			public void onKeyUp(Widget sender, char keyCode, int modifiers) 
			{
				if(keyCode == 13)
					doneChangeStatusString();
			}
			
		});
		
		statusEditor.addFocusListener(new FocusListener()
		{
			public void onFocus(Widget sender) 
			{
			}

			public void onLostFocus(Widget sender) 
			{
				doneChangeStatusString();
			}
			
		});
		
		XmppVCardFactory.instance().addVCardListener(new VCardListener()
		{
			public void onVCard(XmppID jid, XmppVCard vcard) 
			{
				if(jid.toStringNoResource().equalsIgnoreCase(JabberApp.instance().getJid().toStringNoResource()))
				{
					if(!vcard.nickName().isEmpty())
						nickName.setText(vcard.fullName());
					else if(!vcard.fullName().isEmpty())
						nickName.setText(vcard.fullName());
					String photoData = vcard.photo();
					if(!photoData.isEmpty()&&!GXT.isIE)
					{
						ImageElement imgEl = avatarImg.getElement().cast();
						imgEl.removeAttribute("src");
						imgEl.setSrc("data:image;base64,"+photoData);
					}
					
				}
			}
			
		});
	}
	
	public void setNickName(final String nick)
	{
		nickName.setText(nick);
	}
	
	 private AbstractImagePrototype iconFromStatus(XmppContactStatus status)
	 {

		 final ChatIcons icons = ChatIcons.App.getInstance();
		 switch(status.type())
		 {
		 case Online:
			 return icons.online();
		 case Offline:
			 return icons.offline();
		 case Invisible:
			 return icons.offline();
		 case DND:
			 return icons.busy();
		 case XA:
			 return icons.xa();
		 case Away:
			 return icons.away();
		 case FFC:
			 return icons.chat();
		 }
		 return icons.offline();
	 }
	 
	 private void doneChangeStatusString()
	 {
		 String newStatus = statusEditor.getText();
		 if(!newStatus.equalsIgnoreCase(status.status()))
		 {
			 String show = status.show();
			 XmppSession session = JabberApp.instance().getSession();
			 if(show == null||show.isEmpty())
			 {
				 
				 session.getUser().sendPresence(PresenceShow.emptyShow(), newStatus);
			 }
			 else
			 {
				 session.getUser().sendPresence(PresenceShow.get(show), newStatus);
			 }
		 }
		 statusLabel.setVisible(true);
		 statusEditor.setVisible(false);
	 }
	 
	 private void setTitleWidget(Widget title) 
	 {
		 setWidget(1, 0, title);
	 }

	 /*
	 private void addLink(Widget link) 
	 {
		 if (linksPanel.getWidgetCount() > 0) 
		 {
			 linksPanel.add(new HTML("&nbsp;|&nbsp;"));
		 }
		 linksPanel.add(link);
	 }
	 */
	 
	 private void createStatusMenu()
	 {
		 iJabConstants constants = JabberApp.getConstants();
		 statusMenu = new Menu();
		 
		 onlineItem = new MenuItem();
		 onlineItem.setText(constants.online());
		 onlineItem.addSelectionListener(new SelectionListener<MenuEvent>()
		 {
			public void componentSelected(MenuEvent ce) {
				changeShow("");
				
			} 
		 });

		 onlineItem.setIconStyle("contact-icon-online");
		 statusMenu.add(onlineItem);
		 
		 chatItem = new MenuItem();
		 chatItem.setText(constants.freeChat());
		 chatItem.addSelectionListener(new SelectionListener<MenuEvent>()
		 {
			public void componentSelected(MenuEvent ce) {
				changeShow("chat");
			}			 
		 });

		 chatItem.setIconStyle("contact-icon-online");
		 statusMenu.add(chatItem);
		 
		 awayItem = new MenuItem();
		 awayItem.setText(constants.away());
		 awayItem.addSelectionListener(new SelectionListener<MenuEvent>()
		 {
			public void componentSelected(MenuEvent ce) {
				changeShow("away");	
			}			 
		 });
		 awayItem.setIconStyle("contact-icon-away");
		 statusMenu.add(awayItem);
		 
		 dndItem = new MenuItem();
		 dndItem.setText(constants.dnd());
		 dndItem.setIconStyle("contact-icon-busy");
		 dndItem.addSelectionListener(new SelectionListener<MenuEvent>()
		 {
			public void componentSelected(MenuEvent ce) {
				changeShow("dnd"); 				
			}
		 });
		 statusMenu.add(dndItem);
		 
		 xaItem = new MenuItem();
		 xaItem.setText(constants.xa());
		 xaItem.addSelectionListener(new SelectionListener<MenuEvent>()
		 {
			public void componentSelected(MenuEvent ce) {
				 changeShow("xa");
			}
		 });
		 xaItem.setIconStyle("contact-icon-busy");
		 statusMenu.add(xaItem); 
	 }
	 
	 private void changeShow(final String show)
	 {
		 if(!show.equals(status.show()))
		 {

			 XmppSession session = JabberApp.instance().getSession();
			 if(show == null||show.isEmpty())
				 session.getUser().sendPresence(PresenceShow.emptyShow(),status.status());
			 else
				 session.getUser().sendPresence(PresenceShow.get(show),status.status());
		 }
	 }

}
