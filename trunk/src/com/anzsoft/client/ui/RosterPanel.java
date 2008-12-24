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
import java.util.Map;

import com.anzsoft.client.JabberApp;
import com.anzsoft.client.iJabPrefs;
import com.anzsoft.client.utils.ChatIcons;
import com.extjs.gxt.ui.client.Events;
import com.extjs.gxt.ui.client.GXT;
import com.extjs.gxt.ui.client.Style.SelectionMode;
import com.extjs.gxt.ui.client.Style.SortDir;
import com.extjs.gxt.ui.client.data.BaseModel;
import com.extjs.gxt.ui.client.data.ModelData;
import com.extjs.gxt.ui.client.event.ComponentEvent;
import com.extjs.gxt.ui.client.event.GridEvent;
import com.extjs.gxt.ui.client.event.KeyListener;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.event.MenuEvent;
import com.extjs.gxt.ui.client.event.MessageBoxEvent;
import com.extjs.gxt.ui.client.event.WindowEvent;
import com.extjs.gxt.ui.client.store.GroupingStore;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.store.Store;
import com.extjs.gxt.ui.client.store.StoreFilter;
import com.extjs.gxt.ui.client.util.Format;
import com.extjs.gxt.ui.client.util.Params;
import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.Dialog;
import com.extjs.gxt.ui.client.widget.MessageBox;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.form.TextField;
import com.extjs.gxt.ui.client.widget.grid.ColumnConfig;
import com.extjs.gxt.ui.client.widget.grid.ColumnData;
import com.extjs.gxt.ui.client.widget.grid.ColumnModel;
import com.extjs.gxt.ui.client.widget.grid.Grid;
import com.extjs.gxt.ui.client.widget.grid.GridCellRenderer;
import com.extjs.gxt.ui.client.widget.grid.GridGroupRenderer;
import com.extjs.gxt.ui.client.widget.grid.GroupColumnData;
import com.extjs.gxt.ui.client.widget.grid.GroupingView;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.extjs.gxt.ui.client.widget.menu.CheckMenuItem;
import com.extjs.gxt.ui.client.widget.menu.Menu;
import com.extjs.gxt.ui.client.widget.menu.MenuItem;
import com.extjs.gxt.ui.client.widget.menu.SeparatorMenuItem;
import com.google.gwt.user.client.ui.AbstractImagePrototype;
import com.google.gwt.user.client.ui.Image;

import com.anzsoft.client.XMPP.XmppID;
import com.anzsoft.client.XMPP.mandioca.VCardListener;
import com.anzsoft.client.XMPP.mandioca.XmppContact;
import com.anzsoft.client.XMPP.mandioca.XmppContactStatus;
import com.anzsoft.client.XMPP.mandioca.XmppVCard;
import com.anzsoft.client.XMPP.mandioca.XmppVCardFactory;


public class RosterPanel extends ContentPanel
{
	
	public class ContactData extends BaseModel
	{
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		public ContactData()
		{
			
		}
		
		public ContactData(final String img,final String jid,final String alias,final String statusImg,
				final String statusText,final String group,int statusValue)
		{
			set(IMG,img);
			set(JID,jid);
			set(ALIAS,alias);
			set(STATUSIMG,statusImg);
			set(STATUSTEXT,statusText);
			set(USER_GROUP_DD,group);
			set(STATUSVALUE,statusValue);
			
		}		
	}
	public static final String USER_GROUP_DD = "userGroupDD";
    public static final String JID = "jid";
    public static final int DEFAULT_INITIAL_WIDTH = 150;
    private static final String ALIAS = "alias";
    //private static final String COLOR = "color";
    private static final String IMG = "img";
    public static final String STATUSIMG = "status";
    public static final String STATUSTEXT = "statustext";
    public static final String STATUSVALUE = "statusvalue";
    
    private int currentItem = -1;
    
    private GroupingStore<ContactData>  store;
    private Grid<ContactData> grid;
    //private StoreFilterField<ContactData> filterField;
    
    private MenuItem chatItem;
    private MenuItem infoItem;
    private MenuItem requestAuth;
    private MenuItem deleteItem;
    private MenuItem renameItem;
    private CheckMenuItem onlineOnlyItem;
    private MenuItem groupMenuItem;
    private Menu groupMenu;
    private GroupingView view;
    
    private List<String> m_groups = new ArrayList<String>();
    //StoreFilter<ContactData> onlineOnlyFilter = null;
       
    public RosterPanel(final String emptyText)
    {
    	setBorders(false);
    	setHeaderVisible(false);
    	setWidth("100%");
    	setHeight("100%");
    	setLayout(new FitLayout());
    	createGrid(emptyText);
    	setTopComponent(createFilterToolBar());
    	
    	XmppVCardFactory.instance().addVCardListener(new VCardListener()
    	{
			public void onVCard(XmppID jid, XmppVCard vcard) 
			{
				if(iJabPrefs.instance().showOnlineOnly)
		    		store.clearFilters();
		    	final ContactData dataToUpdate = store.findModel(JID, jid.toStringNoResource());
		    	if(dataToUpdate != null)
		    	{
			    	if(!vcard.nickName().isEmpty())
			    		dataToUpdate.set(ALIAS, vcard.nickName());
			    	else if(!vcard.fullName().isEmpty())
			    		dataToUpdate.set(ALIAS, vcard.fullName());
			    	
			    	if(!vcard.photo().isEmpty()&&!GXT.isIE)
			    		dataToUpdate.set(IMG, "data:image;base64,"+vcard.photo());
			    	else
			    		dataToUpdate.set(IMG,"images/default_avatar.png");
			    	
			    	store.update(dataToUpdate);
			    	doLayoutIfNeeded();
		    	}
		    	if(iJabPrefs.instance().showOnlineOnly)
		    		store.applyFilters("");
			}
    		
    	});
    }
    
    public void setRoster(final Map<String,XmppContact> contacts)
    {    	
    	store.removeAll();
    	for(String jid : contacts.keySet())
    	{
    	    XmppContact contact = contacts.get(jid);
    	    addContact(contact);
    	}
    	store.groupBy(USER_GROUP_DD);
    	doLayoutIfNeeded();
    }
    
    public void pushRosterIncoming(final Map<String,XmppContact> contacts)
    {
    	for(String jid:contacts.keySet())
    	{
    		XmppContact contact = contacts.get(jid);
    		if(contact.getSubscription() == XmppContact.Subscription.remove)
    		{
    			removeContact(contact);
    		}
    		else
    		{
    			ContactData data = getContactData(contact.getJID().toString());
    			if(data != null)
    			{
    				String alias = contact.getName();
    		    	if(alias == null || alias.isEmpty())
    		    	{
    		    		alias = contact.getJID().getNode();
    		    	}
    		    	
    		    	String group = JabberApp.getConstants().buddies();
    		    	if(contact.getGroups().size()>0)
    		    		group = contact.getGroups().get(0);
    		    	if(group!=null&&group.equalsIgnoreCase("@everybody@"))
    		    		group = JabberApp.getConstants().buddies();
    		    	
    		    	if(!m_groups.contains(group))
    		    	{
    		    		m_groups.add(group);
    		    	}
    		    	
    				final String statusFormated = formatStatus(contact.getStatus());
    				data.set(STATUSTEXT, statusFormated);
    				data.set(ALIAS, alias);
    				data.set(STATUSIMG, formatStatusIcon(contact.getStatus()));
    				data.set(STATUSVALUE, contact.getStatus().type().ordinal());
    				if(!contact.getGroups().isEmpty()&&!contact.getGroups().get(0).isEmpty())
    				data.set(USER_GROUP_DD, group);
    		    	store.update(data);
    		    	sort();
    			}
    			else
    			{
    				addContact(contact);
    			}
    		}
    		
    	}
    }
    
    private void removeContact(final XmppContact contact)
    {
    	ContactData data = getContactData(contact.getJID().toString());
    	if(data != null)
    	store.remove(data);
    }
    
    public void addContact(final XmppContact contact)
    {
    	final String statusIcon = formatStatusIcon(contact.getStatus());
    	addRecord(contact,statusIcon);
    }
    
    private void addRecord(final XmppContact contact,final String statusIcon)
    {
    	String alias = contact.getName();
    	if(alias == null || alias.isEmpty())
    	{
    		alias = contact.getJID().getNode();
    	}
    	final String jid = contact.getJID().toString();
    	final String statusFormated = formatStatus(contact.getStatus());
    	String group = JabberApp.getConstants().buddies();
    	if(contact.getGroups().size()>0)
    		group = contact.getGroups().get(0);
    	if(group!=null&&group.equalsIgnoreCase("@everybody@"))
    		group = JabberApp.getConstants().buddies();
    	
    	if(!m_groups.contains(group))
    	{
    		m_groups.add(group);
    	}
    	
    	final ContactData data = new ContactData(contact.getAvatar(),jid, alias,
   			 statusIcon, statusFormated,group,contact.getStatus().type().ordinal());
    	store.add(data);
    	//sort();
    	//doLayoutIfNeeded();
    }
    
    public ContactData getContactData(final String bareJid)
    {
    	return store.findModel(JID, bareJid);
    }
    
    public String getContactNick(final String bareJid)
    {
    	final ContactData data = store.findModel(JID, bareJid);
    	if(data == null)
    		return null;
    	return data.get(ALIAS);
    }
    
    public String getContactStatusText(final String bareJid)
    {
    	final ContactData data = store.findModel(JID, bareJid);
    	if(data == null)
    		return null;
    	return data.get("statustext");
    }
    
    public String getAvatarUrl(final String bareJid)
    {
    	final ContactData data = store.findModel(JID, bareJid);
    	if(data == null)
    		return null;
    	return data.get(IMG);
    }
    
    public void updateContactStatus(final String jid,final XmppContactStatus status)
    {
    	if(iJabPrefs.instance().showOnlineOnly)
    		store.clearFilters();
    	final ContactData dataToUpdate = store.findModel(JID, jid);
    	if(dataToUpdate == null)
    		return;
    	final String statusFormated = formatStatus(status);
    	dataToUpdate.set(STATUSTEXT, statusFormated);
    	dataToUpdate.set(STATUSIMG, formatStatusIcon(status));
    	dataToUpdate.set(STATUSVALUE, status.type().ordinal());
    	store.update(dataToUpdate);
    	//store.commitChanges();
    	sort();
    	doLayoutIfNeeded();
    	if(iJabPrefs.instance().showOnlineOnly)
    		store.applyFilters("");
    }
    
    public void setWidth(final int width) 
    {
    	// Log.info("Grid width: " + width);
    	grid.setWidth(width - 27);
    	super.setWidth(width - 25);
    	doLayoutIfNeeded();
    }
    
    private void sort() 
    {
    	store.sort(STATUSVALUE,SortDir.DESC);
    }
    
    private void doLayoutIfNeeded() 
    {
    	if (isRendered()) 
    	{
    	    this.doLayout();
    	}
    }
    
    private TextField<String> createFilterToolBar()
    {
    	final TextField<String> field = new TextField<String>();
    	field.setEmptyText(JabberApp.getConstants().searchAllContacts());
    	field.setWidth("100%");
    	StoreFilter<ContactData> filter = new StoreFilter<ContactData>()
    	{
			public boolean select(Store store, ContactData parent,
					ContactData item, String property) 
			{
				String filterText = field.getRawValue();
				String alias = item.get(ALIAS);
				String jid = item.get(JID);
				if(filterText.length() >0 )
				{
					if(alias.startsWith(filterText.toLowerCase())||jid.startsWith(filterText.toLowerCase()))
						return true;
					return false;
				}
				else
				{
					if(iJabPrefs.instance().showOnlineOnly)
					{
						Integer status = item.get(STATUSVALUE);
						if(status.intValue() == 0)
							return false;
						return true;
					}
					else
						return true;
				}
			}
    		
    	};
    	store.addFilter(filter);
    	
    	field.addKeyListener(new KeyListener()
    	{
    		public void componentKeyUp(ComponentEvent event) 
    		{
    			if(field.getRawValue().length()>0)
    				store.applyFilters("");
    			else
    			{
    				if(iJabPrefs.instance().showOnlineOnly)
    				{
    					store.clearFilters();
    					store.applyFilters("");
    				}
    				else
    					store.clearFilters();
    			}
    		}
    	});
    	return field;
    }
        
    private void createGrid(String emptyText)
    {
    	store = new GroupingStore<ContactData> ();    	
    	store.setSortField(STATUSVALUE);
    	ColumnConfig avatarColumnConfig = new ColumnConfig(IMG,"Image",44);
    	avatarColumnConfig.setRenderer(new GridCellRenderer()
    	{

    		public String render(ModelData model, String property,
    				ColumnData config, int rowIndex, int colIndex,
    				ListStore store) 
    		{
    			Params p = new Params();
    			String img = model.get(IMG);
    			p.add(img);
    			return Format.substitute("<img src=\"{0}\" style=\"width=32px;height:32px;cursor: pointer;\" />",p);
    			//return Format.substitute("<div style=\"direction: ltr; background-repeat: no-repeat; background-position: left center; background-image: url({0}); background-color: transparent; cursor: pointer; visibility: visible; width:32px;height:32px;\"/>", p);
    		}

    	});
    	avatarColumnConfig.setFixed(true);

    	  
    	ColumnConfig statusImgColumnConfig = new ColumnConfig(STATUSIMG,"Status", 20);
    	statusImgColumnConfig.setRenderer(new GridCellRenderer()
    	{
    		public String render(ModelData model, String property,
    				ColumnData config, int rowIndex, int colIndex,
    				ListStore store)
    		{
    			Params p = new Params();
    			p.add(model.get(STATUSIMG));
    			return Format.substitute("{0}", p);
    		}

    	});
    	statusImgColumnConfig.setFixed(true);

    	ColumnConfig groupColumnConfig = new ColumnConfig(USER_GROUP_DD,"Group", 0);
    	groupColumnConfig.setHidden(true);   

    	ColumnConfig aliasColumnConfig = new ColumnConfig(ALIAS,"Alias",32);
    	aliasColumnConfig.setRenderer(new GridCellRenderer()
    	{
    		public String render(ModelData model, String property,
    				ColumnData config, int rowIndex, int colIndex,
    				ListStore store) 
    		{
    			Params p = new Params();
    			p.add(model.get(ALIAS));
    			p.add(model.get(STATUSTEXT));
    			return Format.substitute("<span style=\"vertical-align: middle;color:black;\">{0}</span><br/><span style=\"vertical-align: middle;color:gray;\">{1}</span>", 
    					p);
    		}

    	});
    	  
    	List<ColumnConfig> config = new ArrayList<ColumnConfig>();
    	config.add(statusImgColumnConfig);
    	config.add(aliasColumnConfig);
    	config.add(avatarColumnConfig);
    	config.add(groupColumnConfig);
    	final ColumnModel columnModel = new ColumnModel(config);
    	   
    	view = new GroupingView();
    	// i18n
    	view.setForceFit(true);  
    	view.setGroupRenderer(new GridGroupRenderer()
    	{
    		public String render(GroupColumnData data) 
    		{
    			int total = data.models.size();
    			int online = 0;
    			for(int index = 0;index<total;index++)
    			{
    				ModelData model = data.models.get(index);
    				int statusValue = model.get(STATUSVALUE);
    				if(statusValue != 0)
    					online++;
    			}
    			return data.group+"("+online+"/"+total+")";
    		}

    	});
    	view.setShowGroupedColumn(false);
    	
    	grid = new Grid<ContactData>(store,columnModel);
    	grid.setContextMenu(createContextMenu());
    	grid.setWidth("100%");
    	grid.setHeight("100%");
    	grid.setView(view);
    	grid.setHideHeaders(true);
    	grid.setBorders(false);
    	grid.setStyleName("roster_list");
    	grid.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
    	grid.addListener(Events.RowDoubleClick , new Listener<GridEvent>()
    	{
			public void handleEvent(GridEvent be) 
			{
				List<ContactData> datas = store.getModels();
				ContactData data = datas.get(be.rowIndex);
				String jid = data.get(JID);
				ChatWindow.openChat(XmppID.parseId(jid));
			}

    	});
    	
    	grid.addListener(Events.ContextMenu, new Listener<GridEvent>()
    	{
			public void handleEvent(GridEvent be) 
			{
				currentItem = be.rowIndex;
			}
    		
    	});

    	super.add(grid);
    }
        
    private String formatStatusIcon(XmppContactStatus status) 
    {
    	final AbstractImagePrototype icon = iconFromStatus(status);
    	final Image iconImg = new Image();
    	icon.applyTo(iconImg);
    	return iconImg.toString();
    }
    
    private String formatStatus(XmppContactStatus status)
    {
    	if(status.status() == null||status.status().isEmpty())
    		return status.type().toString();
    	else
    		return status.status();
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
    
    private Menu createContextMenu()
    {
    	Menu menu = new Menu();
    	chatItem = new MenuItem(JabberApp.getConstants().openChat());
    	chatItem.addListener(Events.Select, new Listener<MenuEvent>()
    	{
			public void handleEvent(MenuEvent me) 
			{
				List<ContactData> datas = store.getModels();
				ContactData data = datas.get(currentItem);
				if(data == null)
					return;
				String jid = data.get(JID);
				ChatWindow.openChat(XmppID.parseId(jid));
				
			}
    		
    	});
    	
    	infoItem = new MenuItem(JabberApp.getConstants().userInfo());
    	infoItem.addListener(Events.Select, new Listener<MenuEvent>()
    	{
			public void handleEvent(MenuEvent me) 
			{
				List<ContactData> datas = store.getModels();
				ContactData data = datas.get(currentItem);
				if(data == null)
					return;
				String jid = data.get(JID);
				JabberApp.instance().showInfo(XmppID.parseId(jid));
			}
    		
    	});
    	
    	requestAuth = new MenuItem(JabberApp.getConstants().Rerequset_authorization());
    	requestAuth.addListener(Events.Select, new Listener<MenuEvent>()
    	{
			public void handleEvent(MenuEvent me) 
			{
				List<ContactData> datas = store.getModels();
				ContactData data = datas.get(currentItem);
				if(data == null)
					return;
				String jid = data.get(JID);
				JabberApp.instance().dj_authReq(XmppID.parseId(jid),"");
			}
    		
    	});
    	
    	deleteItem = new MenuItem(JabberApp.getConstants().Delete());
    	deleteItem.addListener(Events.Select, new Listener<MenuEvent>()
    	{
			public void handleEvent(MenuEvent me) 
			{
				List<ContactData> datas = store.getModels();
				ContactData data = datas.get(currentItem);
				if(data == null)
					return;
				final String jid = data.get(JID);				
				String msg = JabberApp.getConstants().DeleteConfirm() +" "+jid+"?";
				MessageBox.confirm("Confirm",msg, new Listener<WindowEvent>()
				{
					public void handleEvent(WindowEvent be) 
					{
						Dialog dialog = (Dialog) be.component;
						Button btn = dialog.getButtonPressed();
						if(btn.getItemId().equals(Dialog.YES))
							JabberApp.instance().removeUser(XmppID.parseId(jid));
							
					}

				});
			}
    		
    	});
    	
    	onlineOnlyItem = new CheckMenuItem(JabberApp.getConstants().Show_online_only());
    	onlineOnlyItem.setChecked(iJabPrefs.instance().showOnlineOnly);
    	onlineOnlyItem.addListener(Events.Select, new Listener<MenuEvent>()
    	{
			public void handleEvent(MenuEvent me) 
			{
				CheckMenuItem item = (CheckMenuItem)me.item;
				if(item.isChecked())
					iJabPrefs.instance().showOnlineOnly = true;
				else
					iJabPrefs.instance().showOnlineOnly = false;
				grid.getSelectionModel().deselectAll();
				store.clearFilters();
				store.applyFilters("");
			}
    		
    	});
    	
    	renameItem = new MenuItem(JabberApp.getConstants().Rename());
    	renameItem.addListener(Events.Select, new Listener<MenuEvent>()
    	{
			public void handleEvent(MenuEvent me) 
			{
				List<ContactData> datas = store.getModels();
				ContactData data = datas.get(currentItem);
				if(data == null)
					return;
				final String jid = data.get(JID);		
				final String nick = data.get(ALIAS);
				final MessageBox box = MessageBox.prompt(JabberApp.getConstants().Rename(), JabberApp.getConstants().RenamePrompt()+" "+nick+":");
				box.addCallback(new Listener<MessageBoxEvent>() 
				{  
					public void handleEvent(MessageBoxEvent be) 
					{  
						if(!be.value.isEmpty())
						{
							if(!be.buttonClicked.getItemId().equals(MessageBox.OK))
								return;
							if(!nick.equals(be.value))
								JabberApp.instance().renameUser(XmppID.parseId(jid), be.value);
						}
					}  
				});
			}
    		
    	});
    	
    	groupMenuItem = new MenuItem(JabberApp.getConstants().Group());
    	groupMenu = new Menu();
    	groupMenuItem.setSubMenu(groupMenu);
    	groupMenuItem.addListener(Events.Activate, new Listener<MenuEvent>()
    	{
			public void handleEvent(MenuEvent be) 
			{
				List<ContactData> datas = store.getModels();
				ContactData data = datas.get(currentItem);
				if(data == null)
					return;
				final String jid = data.get(JID);
				final String group = data.get(USER_GROUP_DD);
				final String nick = data.get(ALIAS);
				
				Menu groupMenu = groupMenuItem.getSubMenu();
				groupMenu.removeAll();
				for(String g:m_groups)
				{
					CheckMenuItem gItem = new CheckMenuItem(g);
					gItem.setGroup("groups");
					if(g.equals(group))
						gItem.setChecked(true);
					groupMenu.add(gItem);
					gItem.addListener(Events.Select, new Listener<MenuEvent>()
					{
						public void handleEvent(MenuEvent be) 
						{
							MenuItem mItem = (MenuItem)be.item;
							String newGroup = mItem.getText();
							if(!newGroup.equals(group))
								JabberApp.instance().changeGroup(XmppID.parseId(jid),nick, newGroup);
						}
						
					});
				}
				groupMenu.add(new SeparatorMenuItem());
				MenuItem newGroupItem = new MenuItem(JabberApp.getConstants().New_Group());
				newGroupItem.addListener(Events.Select, new Listener<MenuEvent>()
				{
					public void handleEvent(MenuEvent be) 
					{
						final MessageBox box = MessageBox.prompt(JabberApp.getConstants().New_Group(), JabberApp.getConstants().NewGroupPrompt());
						box.addCallback(new Listener<MessageBoxEvent>() 
						{  
							public void handleEvent(MessageBoxEvent be) 
							{  
								if(!be.value.isEmpty())
								{
									if(!be.buttonClicked.getItemId().equals(MessageBox.OK))
										return;
									if(!group.equals(be.value))
										JabberApp.instance().changeGroup(XmppID.parseId(jid),nick, be.value);
								}
							}  
						});
					}
					
				});
				groupMenu.add(newGroupItem);
				
			}
    		
    	});
    	
    	menu.add(chatItem);
    	menu.add(infoItem);
    	menu.add(new SeparatorMenuItem());
    	menu.add(renameItem);
    	menu.add(groupMenuItem);
    	menu.add(requestAuth);
    	menu.add(deleteItem);
    	menu.add(new SeparatorMenuItem());
    	menu.add(onlineOnlyItem);
    	
		return menu;
    }
    
    public void doFilter()
    {
    	store.clearFilters();
		store.applyFilters("");
    }
}


