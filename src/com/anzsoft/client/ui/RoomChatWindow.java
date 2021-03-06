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
import com.anzsoft.client.XMPP.mandioca.rooms.MUCItem;
import com.anzsoft.client.XMPP.mandioca.rooms.RoomPresenceListener;
import com.anzsoft.client.XMPP.mandioca.rooms.XmppRoom;
import com.anzsoft.client.utils.ChatTextFormatter;
import com.anzsoft.client.utils.emotions.EmoticonPaletteListener;
import com.anzsoft.client.utils.emotions.EmoticonPalettePanel;
import com.extjs.gxt.ui.client.Events;
import com.extjs.gxt.ui.client.GXT;
import com.extjs.gxt.ui.client.Style.LayoutRegion;
import com.extjs.gxt.ui.client.Style.Scroll;
import com.extjs.gxt.ui.client.Style.SelectionMode;
import com.extjs.gxt.ui.client.data.BaseModelData;
import com.extjs.gxt.ui.client.event.ComponentEvent;
import com.extjs.gxt.ui.client.event.GridEvent;
import com.extjs.gxt.ui.client.event.KeyListener;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.event.WindowEvent;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.util.Format;
import com.extjs.gxt.ui.client.util.Margins;
import com.extjs.gxt.ui.client.util.Params;
import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.Dialog;
import com.extjs.gxt.ui.client.widget.Html;
import com.extjs.gxt.ui.client.widget.MessageBox;
import com.extjs.gxt.ui.client.widget.Window;
import com.extjs.gxt.ui.client.widget.WindowManager;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.form.TextField;
import com.extjs.gxt.ui.client.widget.grid.ColumnConfig;
import com.extjs.gxt.ui.client.widget.grid.ColumnData;
import com.extjs.gxt.ui.client.widget.grid.ColumnModel;
import com.extjs.gxt.ui.client.widget.grid.Grid;
import com.extjs.gxt.ui.client.widget.grid.GridCellRenderer;
import com.extjs.gxt.ui.client.widget.layout.BorderLayout;
import com.extjs.gxt.ui.client.widget.layout.BorderLayoutData;
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
	public class Buddy extends BaseModelData
	{

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		public Buddy()
		{
			
		}
		
		public Buddy(final String nick,final String jid,final String affiliation,final String role)
		{
			setNick(nick);
			setJid(jid);
			setAffiliation(affiliation);
			setRole(role);
		}
		
		public void setRole(final String role)
		{
			set("role",role);
		}
		
		public void setStatus(final String status)
		{
			set("status",status);
		}
		
		public void setAffiliation(final String affiliation)
		{
			set("affiliation",affiliation);
		}
		
		public void setNick(final String nick)
		{
			set("nick",nick);
		}
		
		public void setJid(final String jid)
		{
			set("jid",jid);
		}
		
		public String jid()
		{
			return get("jid");
		}
		
		public String nick()
		{
			return get("nick");
		}
		
		public String affiliation()
		{
			return get("affiliation");
		}
		
		public String role()
		{
			return get("role");
		}
		
		public String status()
		{
			return get("status");
		}
		
	}
	
	
	
	
	
	
	
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
	
	//for roster
	ListStore<Buddy> store;
	private boolean asked = false;
	
	private RoomChatWindow(final String roomJid,final XmppRoom room)
	{
		this.jid = roomJid;
		this.room = room;
		
		setLayout(new BorderLayout());
		setClosable(true);
		setCloseAction(CloseAction.HIDE);
		setWidth(550);
		setHeight(300);
		updateCaption();
		setMinimizable(false);
		setCollapsible(true);
		setAnimCollapse(true);
		setMaximizable(true);
		setFocusWidget(input);
		
		ContentPanel center = new ContentPanel();
		center.setFrame(false);
		center.setBodyBorder(false);
		center.setHeaderVisible(false);
		BorderLayoutData centerData = new BorderLayoutData(LayoutRegion.CENTER,350);  
	    centerData.setMargins(new Margins(0));
	    
		ContentPanel east = new ContentPanel();
		east.setLayout(new FitLayout());
		east.setHeading("Buddys");
		east.setFrame(false);
		east.setBodyBorder(false);
		BorderLayoutData eastData = new BorderLayoutData(LayoutRegion.EAST, 200);  
	    eastData.setSplit(true);  
	    //eastData.setCollapsible(true);  
	    eastData.setMargins(new Margins(5)); 
		
		//the center widgets
		childPanel = new ContentPanel();
		childPanel.addStyleName("message_view");
		childPanel.setHeaderVisible(false);
		childPanel.setBorders(false);
		childPanel.setFrame(false);
		childPanel.setHeight("100%");
		childPanel.setScrollMode(Scroll.AUTO);
		
		center.add(childPanel);
		center.setBottomComponent(createBottomWidget());
		
		//the east widgets
		east.add(createRosterWidget());
		
		add(center, centerData); 
		add(east, eastData); 
		
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
				
				RoomChatWindow win = RoomChatWindow.get(jid);
				if(!win.isVisible())
					win.setVisible(true);
				
				WindowManager.get().bringToFront(win);
			}
			public void onMessageSent(XmppMessage message) 
			{
			}
		});
		
		this.room.addRoomPresenceListener(new RoomPresenceListener()
		{
			public void onUserLeft(String alias) 
			{
				Buddy buddy = store.findModel("nick",alias);
				if(buddy != null)
					store.remove(buddy);
			}

			public void onUserEntered(String alias, MUCItem item) 
			{
				Buddy buddy = new Buddy();
				buddy.setNick(alias);
				buddy.setJid(item.jid().toString());
				//buddy.setStatus(status);		
				store.add(buddy);
			}
			
		});
		
		this.addListener(Events.Hide, new Listener<WindowEvent>()
		{
			public void handleEvent(WindowEvent be) 
			{
				if(asked)
					return;
				MessageBox.confirm("Confirm","Do you want to quit this room when you close the window?", new Listener<WindowEvent>()
				{
					public void handleEvent(WindowEvent be) 
					{
						Dialog dialog = (Dialog) be.component;
						Button btn = dialog.getButtonPressed();
						if(btn.getItemId().equals(Dialog.YES))
						{
							room.logout();
							setCloseAction(CloseAction.CLOSE);
							unRegisterChatWindow(jid);
							close();
						}

					}

				});
				asked = true;
			}
			
		});
		
		room.join(JabberApp.instance().getSession().getUser());
	}
	
	private Grid<Buddy> createRosterWidget()
	{
		//create the grid to show the roster
		List<ColumnConfig> columnConfigs = new ArrayList<ColumnConfig>(); 
		
		ColumnConfig column = new ColumnConfig();  
		column.setId("status_icon");
		column.setWidth(20);
		column.setRenderer(new GridCellRenderer<Buddy>()
    	{
 			public String render(Buddy model, String property,
					ColumnData config, int rowIndex, int colIndex,
					ListStore<Buddy> store) 
			{
				Params p = new Params();
    			p.add("<img src=\"images/icons/fam/user.gif\" />");
    			return Format.substitute("{0}", p);
			}

    	});
		column.setFixed(true);
		columnConfigs.add(column);
		
		column = new ColumnConfig("nick","Nick",32);  
		column.setRenderer(new GridCellRenderer<Buddy>()
    	{
			public String render(Buddy model, String property,
					ColumnData config, int rowIndex, int colIndex,
					ListStore<Buddy> store) 
			{
				Params p = new Params();
    			p.add(model.get("nick"));
    			return Format.substitute("<span style=\"vertical-align: middle;color:black;\">{0}</span>", 
    					p);
			}

    	});
		columnConfigs.add(column);
		
	    ColumnModel cm = new ColumnModel(columnConfigs);
	    
	    store = new ListStore<Buddy>(); 
	    
	    Grid<Buddy> buddyGrid = new Grid<Buddy>(store, cm);  
	    buddyGrid.setStyleName("roster_list");
	    buddyGrid.setStyleAttribute("borderTop", "none");  
	    buddyGrid.setBorders(false);  
	    buddyGrid.setAutoExpandColumn("nick");
	    buddyGrid.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
	    buddyGrid.setHideHeaders(true);
	    buddyGrid.setWidth("100%");
	    buddyGrid.setHeight("100%");
	    
	    buddyGrid.getView().setForceFit(true);  
	    
	    buddyGrid.addListener(Events.RowDoubleClick , new Listener<GridEvent>()
	    {
	    	public void handleEvent(GridEvent be) 
	    	{
	    		List<Buddy> buddys =  store.getModels();
	    		Buddy buddy = buddys.get(be.rowIndex);
	    		String jid = buddy.jid();
	    		if(jid != null&&!jid.isEmpty())
	    			ChatWindow.openChat(XmppID.parseId(jid));
	    	}
	    });
	    return buddyGrid;
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
