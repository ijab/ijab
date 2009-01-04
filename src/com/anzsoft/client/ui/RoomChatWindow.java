package com.anzsoft.client.ui;

import java.util.ArrayList;
import java.util.List;

import com.allen_sauer.gwt.voices.client.Sound;
import com.allen_sauer.gwt.voices.client.SoundController;
import com.anzsoft.client.JabberApp;
import com.anzsoft.client.XMPP.XmppID;
import com.anzsoft.client.XMPP.XmppMessage;
import com.anzsoft.client.XMPP.XmppMessageListener;
import com.anzsoft.client.XMPP.mandioca.VCardListener;
import com.anzsoft.client.XMPP.mandioca.XmppVCard;
import com.anzsoft.client.XMPP.mandioca.XmppVCardFactory;
import com.anzsoft.client.XMPP.mandioca.rooms.RoomPresenceListener;
import com.anzsoft.client.XMPP.mandioca.rooms.XmppRoom;
import com.anzsoft.client.utils.ChatTextFormatter;
import com.anzsoft.client.utils.emotions.EmoticonPaletteListener;
import com.anzsoft.client.utils.emotions.EmoticonPalettePanel;
import com.extjs.gxt.ui.client.Events;
import com.extjs.gxt.ui.client.GXT;
import com.extjs.gxt.ui.client.Style.Scroll;
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
import com.extjs.gxt.ui.client.event.ButtonEvent;

public class RoomChatWindow extends Window
{
	static private List<RoomChatWindow> roomWindows = new  ArrayList<RoomChatWindow>();
	static public RoomChatWindow openRoom(final XmppID id,final String nickName)
	{
		String roomId = id.toString();
		RoomChatWindow window = get(roomId);
		if(window == null)
		{
			XmppRoom room = new XmppRoom(JabberApp.instance().getSession(),id.getNode(),id.getDomain(),nickName);
			//XmppRoom room = JabberApp.instance().getSession().joinRoom(id.getDomain(), id.getNode(), nickName);
			window = new RoomChatWindow(roomId,room);
			
			registerRoomWindow(window);
		}
		
		if(!window.isVisible())
			window.setVisible(true);
		
		WindowManager.get().bringToFront(window);
		return window;
	}
	
	static public void clear()
	{
		int count = roomWindows.size();
	    for (int i = 0; i < count; i++) 
	    {
	      RoomChatWindow w = (RoomChatWindow) roomWindows.get(i);
	      w.close();
	      w.removeFromParent();
	    }
	    roomWindows.clear();
	}
	
	static void registerRoomWindow(final RoomChatWindow window)
	{
		roomWindows.add(window);
	}
	
	static void unRegisterChatWindow(final String jid)
	{
		RoomChatWindow window = get(jid);
		if(window == null)
			return;
		roomWindows.remove(window);
	}
	
	private static RoomChatWindow get(final String jid)
	{
		int count = roomWindows.size();
	    for (int i = 0; i < count; i++) 
	    {
	    	RoomChatWindow w = (RoomChatWindow)roomWindows.get(i);
	    	if (jid.equals(w.getJid())) 
	    	{
	    		return w;
	    	}
	    }
	    return null;
	}
	
	
	private final ContentPanel childPanel;
    private Button emoticonButton;
    private Button sendButton;
    private EmoticonPalettePanel emoticonPalettePanel;
    private String lastMessageNick = null;
	private String lastMsgID = null;
	private XmppVCard vcard = null;
   
    private TextField<String> input;
	
	private String jid;
	private XmppRoom room;
	
	private RoomChatWindow(final String roomJid,final XmppRoom room)
	{
		this.jid = roomJid;
		this.room = room;
		
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
		input.focus();
		
		XmppVCardFactory.instance().get(XmppID.parseId(this.jid),new VCardListener()
		{
			public void onVCard(XmppID jid, XmppVCard in_vcard) 
			{
				vcard = in_vcard;
			}
			
		});

		this.room.addMessageListener(new XmppMessageListener()
		{
			public void onMessageReceived(XmppMessage message) 
			{
				SoundController soundController = new SoundController();
				Sound sound = soundController.createSound(Sound.MIME_TYPE_AUDIO_MPEG,
						"sound/im.wav");
				sound.play();
				
				addMessage(message.getFromID().getResource(), message.getBody(),false);
			}
			public void onMessageSent(XmppMessage message) 
			{
			}
		});
		
		this.room.addRoomPresenceListener(new RoomPresenceListener()
		{
			public void onUserEntered(String alias, String status) 
			{
				
			}

			public void onUserLeft(String alias) 
			{
				
			}
			
		});
		
		room.join(JabberApp.instance().getSession().getUser());
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
		 //addMessage(nick,inputText,true);
		 /*
		 XmppMessage msg = JabberApp.instance().getSession().getFactory().createMessage();
		 msg.setType("groupchat");
		 msg.setTo(jid);
		 msg.setBody(inputText);
		 JabberApp.instance().getSession().send(msg);
		 */
		 room.sendMessage(inputText);
		 setInputText("");
		 input.focus();
	 }
	
	public String getInputText() 
	{
		return input.getRawValue();
	}
	
	private void  updateCaption()
	{
		String nick = XmppID.parseId(jid).getNode();
		if(nick == null || nick.isEmpty())
			nick = this.jid;
		setHeading("MUC - "+nick);
		setIconStyle("chat-icon");
	}
	
	public String getJid()
	{
		return this.jid;
	}
	
	public void setInputText(final String text) 
	{
		input.setRawValue(text);
	}
	
	public void addMessage(final String userAlias,final String message,boolean local) 
	{
		if(lastMsgID!=null&&lastMessageNick.equals(userAlias))
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
		lastMessageNick = userAlias;
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
			imageElement.setSrc("images/default_avatar.png");
		
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
}
