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

import java.util.ArrayList;
import java.util.List;

import com.allen_sauer.gwt.voices.client.Sound;
import com.allen_sauer.gwt.voices.client.SoundController;
import com.anzsoft.client.JabberApp;
import com.anzsoft.client.XMPP.XmppID;
import com.anzsoft.client.XMPP.mandioca.VCardListener;
import com.anzsoft.client.XMPP.mandioca.XmppChat;
import com.anzsoft.client.XMPP.mandioca.XmppVCard;
import com.anzsoft.client.XMPP.mandioca.XmppVCardFactory;
import com.anzsoft.client.utils.ChatTextFormatter;
import com.anzsoft.client.utils.emotions.EmoticonPaletteListener;
import com.anzsoft.client.utils.emotions.EmoticonPalettePanel;
import com.extjs.gxt.ui.client.Events;
import com.extjs.gxt.ui.client.GXT;
import com.extjs.gxt.ui.client.Style.Scroll;
import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.ComponentEvent;
import com.extjs.gxt.ui.client.event.KeyListener;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.event.WindowEvent;
import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.Html;
import com.extjs.gxt.ui.client.widget.Window;
import com.extjs.gxt.ui.client.widget.WindowManager;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.form.TextField;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.extjs.gxt.ui.client.widget.layout.TableData;
import com.extjs.gxt.ui.client.widget.toolbar.AdapterToolItem;
import com.google.gwt.dom.client.DivElement;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.ImageElement;
import com.google.gwt.dom.client.Style;
import com.google.gwt.dom.client.TableElement;
import com.google.gwt.i18n.client.DateTimeFormat;

import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.Widget;


public class ChatWindow extends Window
{
	static private List<ChatWindow> chatWindows = new  ArrayList<ChatWindow>();
	static private Window lastActive = null;
	static public ChatWindow openChat(XmppID id)
	{
		String bareJid = id.toStringNoResource();
		ChatWindow window = get(bareJid);
		if(window == null)
		{
			XmppChat chat = JabberApp.instance().getSession().openChat(id.getNode(), id.getDomain());
			window = new ChatWindow(bareJid,chat);
			registerChatWindow(window);
		}
		
		
		/*
		if(lastActive != null&&lastActive != window)
			window.setPosition(lastActive.getPosition(true).x+20, lastActive.getPosition(true).y + 20);
		*/
		
		if(!window.isVisible())
			window.setVisible(true);
		
		WindowManager.get().bringToFront(window);
		lastActive = window;
		return window;
	}
	
	static public void clear()
	{
		int count = chatWindows.size();
	    for (int i = 0; i < count; i++) {
	      ChatWindow w = (ChatWindow) chatWindows.get(i);
	      w.close();
	      w.removeFromParent();
	    }
	    chatWindows.clear();
	    lastActive = null;
	}
	
	static void registerChatWindow(ChatWindow window)
	{
		chatWindows.add(window);
	}
	
	static void unRegisterChatWindow(String jid)
	{
		ChatWindow window = get(jid);
		if(window == null)
			return;
		chatWindows.remove(window);
	}
	
	private static ChatWindow get(String jid)
	{
		int count = chatWindows.size();
	    for (int i = 0; i < count; i++) {
	      ChatWindow w = (ChatWindow) chatWindows.get(i);
	      if (jid.equals(w.getJid())) {
	        return w;
	      }
	    }
	    return null;
	}
	
	private final ContentPanel childPanel;
    private Button emoticonButton;
    private Button sendButton;
    private EmoticonPalettePanel emoticonPalettePanel;
   
    private TextField<String> input;
    private String jid;
	
	private XmppChat chat;
	private boolean lastIsLocal = false;
	private String lastMsgID = null;
	private XmppVCard vcard = null;
	
	private ChatWindow(final String jid,XmppChat chat)
	{
		this.jid = jid;
		this.chat = chat;
		
		setLayout(new FitLayout());
		
		setClosable(true);
		//setCloseAction(CloseAction.HIDE);
		setCloseAction(CloseAction.HIDE);
		
		childPanel = new ContentPanel();
		childPanel.addStyleName("message_view");
		childPanel.setHeaderVisible(false);
		childPanel.setBorders(false);
		childPanel.setFrame(false);
		childPanel.setHeight("100%");
		childPanel.setScrollMode(Scroll.AUTO);
		
		add(childPanel);
				
		setBottomComponent(createBottomWidget());
		setWidth(400);
		setHeight(300);
		updateCaption();
		setMinimizable(false);
		setCollapsible(true);
		setAnimCollapse(true);
		setMaximizable(true);
		setFocusWidget(input);
		
		this.addListener(Events.Show, new Listener<WindowEvent>()
		{
			public void handleEvent(WindowEvent be) 
			{
				input.focus();
			}
			
		});

		this.addListener(Events.Close, new Listener<WindowEvent>()
		{

			public void handleEvent(WindowEvent be) 
			{
				if(lastActive == be.window)
					lastActive = null;
			}
			
		});
		input.focus();
		
		XmppVCardFactory.instance().get(XmppID.parseId(this.jid),new VCardListener()
		{
			public void onVCard(XmppID jid, XmppVCard in_vcard) 
			{
				vcard = in_vcard;
			}
			
		});
	}
	
	private InputContainer createBottomWidget()
	{
		InputContainer inputContainer = new InputContainer();
		//inputContainer.setWidth("99%");
		
		input = new TextField<String>();
		input.setWidth("100%");
		input.setEmptyText(JabberApp.getConstants().typeAMessage());
		input.addKeyListener(new KeyListener()
		{
			public void componentKeyPress(ComponentEvent event)
			{
				if(event.getKeyCode() == 13)
					doSend();
			}
		});
		TableData data = new TableData();
		data.setWidth("100%");
		inputContainer.addInputItem(new AdapterToolItem(input), data);
		
		emoticonButton = new Button();
		emoticonButton.setIconStyle("emoticon_button");
		emoticonButton.setStyleName("x-btn-icon x-btn-focus");
		emoticonButton.setToolTip("Insert a emoticon");
		emoticonButton.addSelectionListener(new SelectionListener<ButtonEvent>()
		{
			public void componentSelected(ButtonEvent ce) 
			{
				showEmoticonPalette(emoticonButton.getElement().getAbsoluteLeft(),emoticonButton.getElement().getAbsoluteTop());
			}
			
		});
		
		data = new TableData();
		data.setWidth("30px");
		inputContainer.addInputItem(new AdapterToolItem(emoticonButton), data);
		
		sendButton = new Button(JabberApp.getConstants().send());
		sendButton.addSelectionListener(new SelectionListener<ButtonEvent>()
		{
			public void componentSelected(ButtonEvent ce)
			{
				doSend();	
			}
		});
		
		data = new TableData();
		data.setWidth("30px");
		inputContainer.addInputItem(new AdapterToolItem(sendButton), data);
				
		return inputContainer;
	}
	
	private void showEmoticonPalette(final int x, final int y) 
	{
		if (emoticonPalettePanel == null) {
		    emoticonPalettePanel = new EmoticonPalettePanel(new EmoticonPaletteListener() {
			public void onEmoticonSelected(final String emoticonText) {
			    setInputText(getInputText() + " " + emoticonText + " ");
			    emoticonPalettePanel.hide();
			    input.focus();
			}
		    });
		}
		emoticonPalettePanel.showAt(x, y);
	}
	
	public String getInputText() 
	{
		return input.getRawValue();
	}
	
	private void  updateCaption()
	{
		String nick = JabberApp.instance().getContactNick(jid); 
		String status = JabberApp.instance().getContactStatusText(jid);
		if(nick == null || nick.isEmpty())
			nick = this.jid;
		if(status == null||status.isEmpty())
			setHeading(nick);
		else
			setHeading(nick+"("+status+")");
		setIconStyle("chat-icon");
	}
	
	public void setInputText(final String text) 
	{
		input.setRawValue(text);
	}
	
	public void addMessage(final String userAlias,final String message,boolean local) 
	{
		if(lastMsgID!=null&&lastIsLocal == local)
		{
			Element msgDiv = DOM.getElementById(lastMsgID);
			if(msgDiv != null)
			{
				Element messageElement = DOM.createSpan();
				messageElement.setInnerHTML("<br/>"+ChatTextFormatter.format(message == null ? "" : message).getHTML());
				msgDiv.appendChild(messageElement);
				if (childPanel.isRendered()) 
				{
					childPanel.layout();
				}
				childPanel.fly((com.google.gwt.user.client.Element) messageElement).scrollIntoView(childPanel.getLayoutTarget().dom, true);
				return;
			}
		}
		String messageString = createMessage(userAlias,message,local).getString();
		Html messageHtml = new Html(messageString);
		addWidget(messageHtml);
		lastIsLocal = local;
	}
	
	private DivElement createMessage(final String user,final String message,boolean local)
	{
		Element element = DOM.createDiv();
		DivElement messageDiv = DivElement.as(element);
		lastMsgID = DOM.createUniqueId();
		messageDiv.setId(lastMsgID);
		messageDiv.setClassName("selected-article");
		
		//create the avatar table
		element = DOM.createTable();
		TableElement avatarTable = TableElement.as(element);
		messageDiv.appendChild(avatarTable);
		avatarTable.setAttribute("hspace", "4");
		avatarTable.setAttribute("cellspacing","0");
		avatarTable.setAttribute("vspace", "2");
		avatarTable.setAttribute("border", "0");
		avatarTable.setAttribute("align", "left");
		
		Element tbodyElement = DOM.createTBody();
		avatarTable.appendChild(tbodyElement);
		
		Element trElement = DOM.createTR();
		tbodyElement.appendChild(trElement);
		
		Element tdElement = DOM.createTD();
		trElement.appendChild(tdElement);
		tdElement.setAttribute("height", "45");
		tdElement.setAttribute("width", "45");
		tdElement.setAttribute("align", "middle");
		Style style = tdElement.getStyle();
		style.setProperty("border", "1px solid black");
		style.setProperty("background-color", "white");
		
		Element imgElement = DOM.createImg();
		ImageElement imageElement = ImageElement.as(imgElement);
		tdElement.appendChild(imageElement);
		imageElement.setAttribute("height", "45");
		imageElement.setAttribute("widht", "45");
		XmppVCard vc = null;
		if(local)
			vc = JabberApp.instance().getSelfVCard();
		else
			vc = vcard;
		if(!GXT.isIE&&vc != null&&!vc.photo().isEmpty())
			imageElement.setSrc("data:image;base64,"+vc.photo());
		else
			imageElement.setSrc(JabberApp.instance().getAvatarUrl(jid));
		
		tdElement = DOM.createTD();
		tdElement.setInnerHTML("&nbsp&nbsp");
		trElement.appendChild(tdElement);
		
		//create the div for timestamp and nick
		element = DOM.createDiv();
		DivElement tnDiv = DivElement.as(element);
		tnDiv.setClassName("msg_header");
		messageDiv.appendChild(tnDiv);
		//style = tnDiv.getStyle();
		//style.setProperty("border-bottom", "1px solid black");
		
		element = DOM.createTable();
		TableElement tnTableElement = TableElement.as(element);
		tnDiv.appendChild(tnTableElement);
		tnTableElement.setAttribute("cellspacing", "0");
		tnTableElement.setAttribute("cellpadding", "0");
		tnTableElement.setAttribute("width", "80%");
		tnTableElement.setAttribute("border", "0");
		
		tbodyElement = DOM.createTBody();
		tnTableElement.appendChild(tbodyElement);
		
		trElement = DOM.createTR();
		tbodyElement.appendChild(trElement);
		
		Element nickElement = DOM.createTD();
		trElement.appendChild(nickElement);
		nickElement.setClassName("msg-nick");
		nickElement.setAttribute("valign", "bottom");
		nickElement.setAttribute("align", "left");
		nickElement.setInnerHTML("<b>"+user+"</b>");
		
		Element timeElement = DOM.createTD();
		trElement.appendChild(timeElement);
		timeElement.setClassName("msg-nick");
		timeElement.setAttribute("valign","bottom");
		timeElement.setAttribute("align", "right");
		DateTimeFormat timeFormat = DateTimeFormat.getMediumTimeFormat();
		String datetime = timeFormat.format(new java.util.Date());
		timeElement.setInnerHTML("<small>"+datetime+"</small>");
		
		Element messageElement = DOM.createSpan();
		messageElement.setInnerHTML(ChatTextFormatter.format(message == null ? "" : message).getHTML());
		
		messageDiv.appendChild(messageElement);		
		return messageDiv;
	}
	
	private void addWidget(final Widget widget) 
	{
		childPanel.add(widget);
		if (childPanel.isRendered()) 
		{
			childPanel.layout();
		}
		childPanel.scrollIntoView(widget);
	}
	
	 private void doSend()
	 {
		 final String inputText = getInputText();
		 if(inputText.isEmpty())
		 {
			 input.focus();
			 return;
		 }

		 SoundController soundController = new SoundController();
		 Sound sound = soundController.createSound(Sound.MIME_TYPE_AUDIO_MPEG,
				 "sound/im_send.wav");
		 sound.play();
		 
		 XmppVCard selfv = JabberApp.instance().getSelfVCard();
		 String nick = XmppID.parseId(JabberApp.instance().getSession().getUser().getID()).toStringNoResource();
		 if(selfv!=null)
		 {
			 if(!selfv.nickName().isEmpty())
				 nick = selfv.nickName();
			 else if(!selfv.fullName().isEmpty())
				 nick = selfv.fullName();
		 }
		 addMessage(nick,inputText,true);
		 chat.sendMessage(inputText);
		 setInputText("");
		 input.focus();
	 }
	
		
	public String getJid()
	{
		return this.jid;
	}
	
	public static String randomUUID()
	{
		char[] s = new char[36];
		char itoh[]={'0','1','2','3','4','5','6','7','8','9','A','B','C','D','E','F'};
		for (int i = 0; i < 36; i++)
		{
			s[i] = (char) Math.floor(Math.random()*0x10);
		}
		
		s[14] = 4;
		s[19] =(char) ((s[19] & 0x3) | 0x8);
		
		for (int i = 0; i < 36; i++)
			s[i] = itoh[s[i]];
		
		s[8] = s[13] = s[18] = s[23] = '-';
		
		return new String(s);
		
	}
}
