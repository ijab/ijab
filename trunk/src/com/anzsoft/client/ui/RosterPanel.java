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
import com.anzsoft.client.utils.ChatIcons;
import com.extjs.gxt.ui.client.Events;
import com.extjs.gxt.ui.client.Style.SelectionMode;
import com.extjs.gxt.ui.client.Style.SortDir;
import com.extjs.gxt.ui.client.data.BaseModel;
import com.extjs.gxt.ui.client.data.ModelData;
import com.extjs.gxt.ui.client.event.ComponentEvent;
import com.extjs.gxt.ui.client.event.GridEvent;
import com.extjs.gxt.ui.client.event.KeyListener;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.store.GroupingStore;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.store.Store;
import com.extjs.gxt.ui.client.store.StoreFilter;
import com.extjs.gxt.ui.client.util.Format;
import com.extjs.gxt.ui.client.util.Params;
import com.extjs.gxt.ui.client.widget.ContentPanel;
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
import com.google.gwt.user.client.ui.AbstractImagePrototype;
import com.google.gwt.user.client.ui.Image;

import com.anzsoft.client.XMPP.XmppID;
import com.anzsoft.client.XMPP.mandioca.XmppContact;
import com.anzsoft.client.XMPP.mandioca.XmppContactStatus;


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
    
    private GroupingStore<ContactData>  store;
    private Grid<ContactData> grid;
    //private StoreFilterField<ContactData> filterField;
       
    public RosterPanel(final String emptyText)
    {
    	setBorders(false);
    	setHeaderVisible(false);
    	setWidth("100%");
    	setHeight("100%");
    	setLayout(new FitLayout());
    	createGrid(emptyText);
    	setTopComponent(createFilterToolBar());
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
    
    public void addContact(XmppContact contact)
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
				if(alias.startsWith(filterText.toLowerCase())||jid.startsWith(filterText.toLowerCase()))
					return true;
				return false;
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
    				store.clearFilters();
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
    			return Format.substitute("<div style=\"direction: ltr; background-repeat: no-repeat; background-position: left center; background-image: url({0}); background-color: transparent; cursor: pointer; visibility: visible; width:32px;height:32px;\"/>", p);
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


    	   
    	final GroupingView  view = new GroupingView();
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
    		
    	
    	Grid<ContactData> grid = new Grid<ContactData>(store,columnModel);
    	grid.setWidth("100%");
    	grid.setHeight("100%");
    	grid.setView(view);
    	grid.setHideHeaders(true);
    	grid.setBorders(false);
    	grid.setStyleName("roster_list");
    	grid.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
    	grid.addListener(Events.RowClick , new Listener<GridEvent>()
    	{
			public void handleEvent(GridEvent be) 
			{
				List<ContactData> datas = store.getModels();
				ContactData data = datas.get(be.rowIndex);
				String jid = data.get(JID);
				ChatWindow.openChat(XmppID.parseId(jid));
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
}
