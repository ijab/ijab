package com.anzsoft.client.ui;

import java.util.ArrayList;
import java.util.List;

import com.anzsoft.client.JabberApp;
import com.anzsoft.client.XMPP.XmppID;
import com.anzsoft.client.XMPP.XmppPacket;
import com.anzsoft.client.XMPP.XmppPacketListener;
import com.anzsoft.client.XMPP.XmppQuery;
import com.anzsoft.client.XMPP.mandioca.ServiceDiscovery;
import com.anzsoft.client.XMPP.mandioca.XmppSession;
import com.anzsoft.client.XMPP.mandioca.ServiceDiscovery.Service;
import com.anzsoft.client.utils.TextUtils;
import com.extjs.gxt.ui.client.Events;
import com.extjs.gxt.ui.client.Style.Orientation;
import com.extjs.gxt.ui.client.Style.SelectionMode;
import com.extjs.gxt.ui.client.data.BaseModelData;
import com.extjs.gxt.ui.client.event.GridEvent;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.event.SelectionChangedEvent;
import com.extjs.gxt.ui.client.event.SelectionChangedListener;
import com.extjs.gxt.ui.client.event.SelectionEvent;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.widget.Dialog;
import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.button.ButtonAdapter;
import com.extjs.gxt.ui.client.widget.button.ButtonBar;
import com.extjs.gxt.ui.client.widget.button.FillButton;
import com.extjs.gxt.ui.client.widget.form.ComboBox;
import com.extjs.gxt.ui.client.widget.grid.ColumnConfig;
import com.extjs.gxt.ui.client.widget.grid.ColumnModel;
import com.extjs.gxt.ui.client.widget.grid.Grid;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.extjs.gxt.ui.client.widget.layout.RowData;
import com.extjs.gxt.ui.client.widget.layout.RowLayout;
import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.NodeList;

public class RoomDialog extends Dialog
{
	public class Room extends BaseModelData
	{
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		public Room(String jid,String name)
		{
			setJid(jid);
			setName(name);
		}
		
		public Room()
		{
			
		}
		
		public String jid()
		{
			return get("jid");
		}
		
		public void setJid(final String jid)
		{
			set("jid", jid);
		}
		
		public String name()
		{
			return get("name");
		}
		
		public void setName(final String name)
		{
			set("name",name);
		}
	}
	
	private final ServiceDiscovery serviceDisco;
	
	//widgets
	private ComboBox<Service> roomField;
	private Grid<Room> roomGrid;
	private ListStore<Room> store = null;
	private List<ColumnConfig> columnConfigs = null;
	
	private String currentJid = null;
	
	public RoomDialog(final ServiceDiscovery serviceDisco)
	{
		this.serviceDisco = serviceDisco;
		initUI();
	}
	
	private void initUI()
	{
		setButtons("");
		setHeading(JabberApp.getConstants().Chat_Rooms());
		setModal(false);
		setBodyBorder(true);
		//setInsetBorder(true);
		setBodyStyle("padding: 0px;background: none");
		setWidth(350);
		setHeight(300);
		setResizable(false);
		setClosable(true);
		setCollapsible(false);
		
		setLayout(new FitLayout());
		
		LayoutContainer main = new LayoutContainer();  
	    main.setLayout(new RowLayout(Orientation.VERTICAL));
		
		ListStore<Service> rooms = new ListStore<Service>();  
		rooms.add(serviceDisco.getMUCRoomServices());  

		roomField = new ComboBox<Service>();
		roomField.setFieldLabel(JabberApp.getConstants().Chat_Rooms());
		roomField.setStore(rooms);
		roomField.setDisplayField("name");
		roomField.setTypeAhead(true);
		roomField.addSelectionChangedListener(new SelectionChangedListener<Service>()
		{
			public void selectionChanged(SelectionChangedEvent<Service> se) 
			{
				Service service = se.getSelectedItem();
				getRooms(service.getJid());
			}
		});
		roomField.setValue(rooms.getAt(0));
		
		//create the grid to show the room list
		columnConfigs = new ArrayList<ColumnConfig>(); 
	    ColumnModel cm = new ColumnModel(columnConfigs);
	    
	    ColumnConfig column = new ColumnConfig();  
	    column.setId("name");  
	    column.setHeader("Room Name");  
	    columnConfigs.add(column);
	    
	    store = new ListStore<Room>(); 
	    
	    roomGrid = new Grid<Room>(store, cm);  
	    roomGrid.setStyleAttribute("borderTop", "none");  
	    roomGrid.setBorders(true);  
	    roomGrid.setAutoExpandColumn("name");
	    roomGrid.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
	    roomGrid.setHideHeaders(true);
	    
	    roomGrid.addListener(Events.RowDoubleClick , new Listener<GridEvent>()
	    {
	    	public void handleEvent(GridEvent be) 
	    	{
	    		List<Room> rooms =  store.getModels();
	    		Room room = rooms.get(be.rowIndex);
	    		String jid = room.jid();
	    		if(jid != null&&!jid.isEmpty())
	    			joinRoom(jid);
	    	}
	    });
	    
	    roomGrid.getSelectionModel().addListener(Events.SelectionChange, new Listener<SelectionEvent<Room>>()
	    {
	    	public void handleEvent(SelectionEvent<Room> be) 
	    	{
	    		if(!be.selection.isEmpty())
	    			currentJid = be.selection.get(0).jid();
	    		else
	    			currentJid = null;
	    	}

	    });
		
	    ButtonBar topBar = new ButtonBar();
	    Button refreshButton = new Button("Refresh");
	    refreshButton.addSelectionListener(new SelectionListener<ButtonEvent>()
	    {
			public void componentSelected(ButtonEvent ce) 
			{
				refreshRooms();
			}
	    	
	    });
	    roomField.setWidth(250);
	    topBar.add(new ButtonAdapter(roomField));
	    topBar.add(new FillButton());
	    topBar.add(refreshButton);
	    this.setTopComponent(topBar);
		//main.add(roomField,new RowData(1,-1));
		main.add(roomGrid,new RowData(1,1));		
		add(main);
		
		ButtonBar bar = new ButtonBar();
		Button createButton = new Button("Create Room");
		createButton.addSelectionListener(new SelectionListener<ButtonEvent>()
		{
			public void componentSelected(ButtonEvent ce) 
			{
				joinRoom(null);
			}
		});
		
		Button joinButton = new Button("Join");
		joinButton.addSelectionListener(new SelectionListener<ButtonEvent>()
		{
			public void componentSelected(ButtonEvent ce) 
			{
				joinRoom(currentJid);
			}
			
		});
		
		Button closeButton = new Button(JabberApp.getConstants().close());
		closeButton.addSelectionListener(new SelectionListener<ButtonEvent>()
		{
			public void componentSelected(ButtonEvent ce) 
			{
				close();
			}
			
		});
		
		bar.add(joinButton);
		bar.add(createButton);
		bar.add(closeButton);
		setButtonBar(bar);
	}
	
	private void getRooms(final String jid)
	{
		XmppSession session = JabberApp.instance().getSession();
		XmppQuery query = session.getFactory().createQuery();
		query.setIQ(jid, XmppQuery.TYPE_GET, TextUtils.genUniqueId());
		query.setQuery("http://jabber.org/protocol/disco#items");
		session.send(query,new XmppPacketListener()
		{
			public void onPacketReceived(XmppPacket packet) 
			{
				onRooms(packet);
			}

			public void onPacketSent(XmppPacket packet) 
			{
				
			}
			
		});
	}
	
	private void onRooms(final XmppPacket iq)
	{
		if(!iq.getType().equals("result"))
			return;
		store.removeAll();
		Element iqElement = (Element) iq.getNode();
		Element queryElement = iqElement.getElementsByTagName("query").getItem(0);
		if(queryElement == null)
			return;
		
		NodeList<Element> items = queryElement.getElementsByTagName("item");
		for(int index=0;index<items.getLength();index++)
		{
			Element item = items.getItem(index);
			final String jid = item.getAttribute("jid");
			final String name = item.getAttribute("name");
			Room room = new Room(jid,name);
			store.add(room);
		}		
	}
	
	private void joinRoom(final String roomJid)
	{
		RoomChatWindow.openRoom(XmppID.parseId(roomJid), JabberApp.instance().getNick());
	}
	
	public void refreshRooms()
	{
		Service service = roomField.getValue();
		if(service != null)
			getRooms(service.getJid());
	}
}
