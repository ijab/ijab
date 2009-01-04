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

package com.anzsoft.client;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.allen_sauer.gwt.voices.client.Sound;
import com.allen_sauer.gwt.voices.client.SoundController;
import com.anzsoft.client.XMPP.Debugger;
import com.anzsoft.client.XMPP.PresenceShow;
import com.anzsoft.client.XMPP.XmppConnection;
import com.anzsoft.client.XMPP.XmppError;
import com.anzsoft.client.XMPP.XmppEventAdapter;
import com.anzsoft.client.XMPP.XmppFactory;
import com.anzsoft.client.XMPP.XmppID;
import com.anzsoft.client.XMPP.XmppMessage;
import com.anzsoft.client.XMPP.XmppMessageListener;
import com.anzsoft.client.XMPP.XmppPresence;
import com.anzsoft.client.XMPP.XmppPresenceListener;
import com.anzsoft.client.XMPP.XmppUserSettings;
import com.anzsoft.client.XMPP.XmppUserSettings.AuthType;
import com.anzsoft.client.XMPP.impl.JsJacFactory;
import com.anzsoft.client.XMPP.log.DebugPanel;
import com.anzsoft.client.XMPP.log.GWTLoggerOutput;
import com.anzsoft.client.XMPP.mandioca.RosterTask;
import com.anzsoft.client.XMPP.mandioca.ServiceDiscovery;
import com.anzsoft.client.XMPP.mandioca.VCardListener;
import com.anzsoft.client.XMPP.mandioca.XmppContact;
import com.anzsoft.client.XMPP.mandioca.XmppContactStatus;
import com.anzsoft.client.XMPP.mandioca.XmppPushRoster;
import com.anzsoft.client.XMPP.mandioca.XmppRosterListener;
import com.anzsoft.client.XMPP.mandioca.XmppSession;
import com.anzsoft.client.XMPP.mandioca.XmppVCard;
import com.anzsoft.client.XMPP.mandioca.XmppVCardFactory;
import com.anzsoft.client.ui.ChatWindow;
import com.anzsoft.client.ui.InfoDialog;
import com.anzsoft.client.ui.LoginDialog;
import com.anzsoft.client.ui.MainWindow;
import com.anzsoft.client.ui.RoomDialog;
import com.anzsoft.client.ui.RosterPanel;
import com.anzsoft.client.ui.UserAddDialog;
import com.anzsoft.client.ui.UserSearchDialog;
import com.extjs.gxt.ui.client.widget.Dialog;
import com.extjs.gxt.ui.client.widget.MessageBox;
import com.extjs.gxt.ui.client.widget.Window;
import com.extjs.gxt.ui.client.widget.WindowManager;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.RootPanel;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.event.WindowEvent;

public class JabberApp 
{
	public interface LoginListener
	{
		public void onLogined();
	}
	private static JabberApp _instance = null;
	private XmppSession session = null;
	private ServiceDiscovery disco;
	private XmppConnection connection = null;
	private XmppPresenceListener presenceListener = null;
	private XmppEventAdapter eventAdapter = null;
	private XmppMessageListener messageListener = null;
	private LoginListener loginListener = null;
	
	private boolean silent = false;
	//private boolean serviceMode = false;
	private String httpBind = "/http-bind/";
	private String host = "samespace.anzsoft.com";
	private int port = 5222;
	public String domain = "anzsoft.com";
	private String authType = "sasl";
	
	private Window debugWindow =  null;
	private DebugPanel debugPanel = null;
	private boolean Debug = false;
	
	private LoginDialog loginDlg = new LoginDialog();
	private UserAddDialog userAddDlg = null;
	private UserSearchDialog searchDialog = null;
	private InfoDialog infoDlg = null;
	private RoomDialog roomDlg = null;
	
	static iJabConstants constants = null;
	private RosterPanel rosterPanel = null;
	private MainWindow mainWindow = null;
	private Map<String,XmppContact> contactDatas = null; 
	private HashMap<String,XmppContactStatus> statusMap = new HashMap<String,XmppContactStatus>();
	public static JabberApp instance()
	{
		if(_instance == null)
			_instance = new JabberApp();
		return _instance;
	}
	
	private JabberApp()
	{
		initParament();
	}
	
	private native void initParament()
	/*-{
		this.@com.anzsoft.client.JabberApp::httpBind = $wnd.httpBind;
		this.@com.anzsoft.client.JabberApp::host = $wnd.host;
		this.@com.anzsoft.client.JabberApp::port = $wnd.port;
		this.@com.anzsoft.client.JabberApp::domain = $wnd.domain;
		this.@com.anzsoft.client.JabberApp::authType = $wnd.authType;
		this.@com.anzsoft.client.JabberApp::Debug = $wnd.debug;
	}-*/;

	
	
	public void run(boolean silent)
	{
		this.silent = silent;
		doLogin();
	}
	
	public void run(final String userName,final String password,boolean slient,LoginListener loginListener)
	{
		this.silent = slient;
		this.loginListener = loginListener;
		onLogin(userName,password,null);
	}
	
	public void doLogin()
	{		
		XmppVCardFactory.instance().clear();
		ChatWindow.clear();
		cleanDialog();	    
	    if(loginDlg == null)
	    	loginDlg = new LoginDialog();
	    loginDlg.show();
	}
	
	public void onLogin(final String user,final String pass,XmppEventAdapter eventAdapter)
	{
		initConnection();
		if(eventAdapter != null)
			connection.addEventListener(eventAdapter);
		session = new XmppSession(connection, true);
		disco = new ServiceDiscovery(session);
		new XmppPushRoster(session);
		XmppUserSettings userSetting = new XmppUserSettings(host,port,domain, user, pass, AuthType.fromString(authType));
		session.login(userSetting);
		session.getUser().getRoster().addRosterListener(createRosterListener());
	}
	
	public void onLogin(final String host,final int port,final String domain,boolean sasl,final String user,final String pass,XmppEventAdapter eventAdapter)
	{
		this.domain = domain;
		initConnection();
		if(eventAdapter != null)
			connection.addEventListener(eventAdapter);
		session = new XmppSession(connection, true);
		disco = new ServiceDiscovery(session);
		new XmppPushRoster(session);
		XmppUserSettings userSetting;
		if(sasl)
			userSetting = new XmppUserSettings(host,port,domain, user, pass, XmppUserSettings.SASL);
		else
			userSetting = new XmppUserSettings(host,port,domain, user, pass, XmppUserSettings.NON_SASL);
		session.login(userSetting);
		session.getUser().getRoster().addRosterListener(createRosterListener());
	}
	
	public void logout()
	{
		cleanDialog();
		savePrefs();
		iJabPrefs.instance().deinit();
		ChatWindow.clear();
		session.logout();
	}
	
	public String getContactNick(final String bareJid)
	{
		if(silent)
		{
			XmppContact contact = contactDatas.get(bareJid);
			if(contact != null)
				return contact.getName();
			else
				return "";
		}
		else
			return rosterPanel.getContactData(bareJid).get("alias");
	}
	
	public String getContactStatusText(final String bareJid)
	{
		if(silent)
		{
			XmppContactStatus status = statusMap.get(bareJid);
			if(status!=null)
				return status.status();
			else
				return "";
		}
		else
			return rosterPanel.getContactData(bareJid).get("statustext");
	}
	
	public XmppSession getSession()
	{
		return this.session;
	}
	
	private void createDebugWindow()
	{
		if(!Debug)
			return;
		debugWindow = new Window();
		debugWindow.setLayout(new FitLayout());
		
		debugPanel = new DebugPanel();
		debugWindow.add(debugPanel);
		debugWindow.setButtonBar(debugPanel.getBar());
		
		debugWindow.setWidth(500);
		debugWindow.setHeight(300);
		debugWindow.show();
		debugWindow.setTitle("Debugger");
		Debugger.debug(connection,debugPanel);
	}
	
	public static iJabConstants getConstants()
	{
		if(constants == null)
			constants = (iJabConstants)GWT.create(iJabConstants.class);
		return constants;
	}
	
	private void initConnection()
	{
		XmppFactory xmppFactory = JsJacFactory.getInstance();
		connection = xmppFactory.createBindingConnection(httpBind, 2000, GWTLoggerOutput.instance);
		connection.addEventListener(createEventAdapter());
		connection.addPreseceListener(createPresenceListener());
		connection.addMessageListener(createMessageListener());
		createDebugWindow();
	}
	
	private XmppEventAdapter createEventAdapter()
	{
		 eventAdapter = new XmppEventAdapter()
		{
			public void onConnect()
			{
				getPrefs();
				session.getUser().getRoster().sendQuery();
			}
			
			public void onDisconnect()
			{
				if(!silent)
					doLogin();
			}
			
			public void onError(final XmppError error) 
			{
				System.out.println("onError...");
				cleanDialog();
				if(!silent)
					doLogin();
		    }

		    public void onResume() 
		    {
		    	if(!silent)
		    		doLogin();
		    }

		    public void onStatusChanged(final String status) 
		    {
				System.out.println("OnStatusChanged..................");
				System.out.println(status);
		    }
		};
		return eventAdapter;
	}
	
	private XmppPresenceListener createPresenceListener()
	{
		presenceListener = new XmppPresenceListener()
		{
			public void onPresenceReceived(XmppPresence presence) 
			{
				try
				{
					XmppID id = presence.getFromID();
					String jid = id.toStringNoResource();
					String type = presence.getType();
					if(type != null&&(type.equals("subscribe")||type.equals("subscribed")||type.equals("unsubscribe")||type.equals("unsubscribed")))
					{
						onSubscription(id,type,presence.getNick());
						return;
					}
					String show = new String("");
					PresenceShow presenceShow = presence.getShow();
					if(presenceShow!=null)
						show = presenceShow.toString();
					String statusString = presence.getStatus();
					int priority = presence.getPriority();
					boolean avaiable = true;
					if(type != null&&!type.isEmpty())
					{
						if(type.equalsIgnoreCase("unavailable"))
							avaiable = false;
					}
					XmppContactStatus status = new XmppContactStatus(show,statusString,priority,avaiable);
					if(rosterPanel != null&&!silent)
						rosterPanel.updateContactStatus(jid, status);
					
					if(silent)
						statusMap.put(jid, status);
				}
				catch(Exception e)
				{
					MessageBox.alert(e.toString(), null, null);
				}
			}

			public void onPresenceSent(XmppPresence presence) 
			{
				
			}
			
		};
		return presenceListener;
	}
	
	private XmppMessageListener createMessageListener()
	{
		messageListener = new XmppMessageListener()
		{
			public void onMessageReceived(XmppMessage message) 
			{
				if(!message.getType().equals("chat"))
					return;
				ChatWindow window = ChatWindow.openChat(message.getFromID());
				
				SoundController soundController = new SoundController();
				Sound sound = soundController.createSound(Sound.MIME_TYPE_AUDIO_MPEG,
						"sound/im.wav");
				sound.play();
				
				window.addMessage(getContactNick(message.getFromID().toStringNoResource()), message.getBody(),false);
			}
			public void onMessageSent(XmppMessage message) 
			{
				
			}
			
		};
		return messageListener;
	}
	
	private XmppRosterListener createRosterListener()
	{
		XmppRosterListener listener = new XmppRosterListener()
		{

			public void onRoster(Map<String, XmppContact> contacts)
			{
				if(silent)
					contactDatas = contacts;
				else
				{
					try
					{
						mainWindow = new MainWindow(session);
						rosterPanel = mainWindow.getRosterPanel();
						rosterPanel.setRoster(contacts);
						mainWindow.layout();
						loginDlg.close();
						loginDlg = null;

						mainWindow.show();

						RootPanel root = RootPanel.get();
						int  x = root.getOffsetWidth() - mainWindow.getOffsetWidth();
						mainWindow.setPosition(x, 0);
					}
					catch(Exception e)
					{
						MessageBox.alert(e.toString(), null, null);
					}

					if(loginListener != null)
						loginListener.onLogined();
				}
				disco.getDiscoItems();
				XmppVCardFactory.instance().get(getJid(), new VCardListener()
				{
					public void onVCard(XmppID jid, XmppVCard in_vcard) 
					{
					}
					
				});
			}			
		};
		return listener;
	}
	
	public String getAvatarUrl(final String bareJid)
	{
		if(silent)
		{
			XmppContact contact = contactDatas.get(bareJid);
			if(contact!=null)
				return contact.getAvatar();
			else
				return "images/default_avatar.png";
		}
		else
			return rosterPanel.getAvatarUrl(bareJid);
	}
	
	public boolean connected()
	{
		if(connection == null)
			return false;
		return connection.isConnected();
	}
	
	private void onSubscription(final XmppID id,final String subStr,final String nick)
	{
		if(subStr.equals("unsubscribed"))
		{
			//TODO: delete the roster item from the panel
			return;
		}
		else if(subStr.equals("subscribe"))
		{
			//TODO: deal the incoming auth request
			String userString;
			if(nick != null && !nick.isEmpty())
				userString = nick +"<"+id.toStringNoResource()+">";
			else
				userString = id.toStringNoResource();
			MessageBox.confirm(JabberApp.getConstants().Auth_Confirm(),userString + constants.auth_requst() ,new Listener<WindowEvent>()
			{
				public void handleEvent(WindowEvent be) 
				{
					Dialog dialog = (Dialog) be.component;
					Button btn = dialog.getButtonPressed();
					if(btn.getItemId().equals(Dialog.YES))
					{
						dj_auth(id);
					}
					else
					{
						dj_deny(id);
					}
				}

			});
			
		}
		else if(subStr.equals("subscribed"))
		{
			//do nothing
		}
		else if(subStr.equals("unsubscribe"))
		{
			//do nothing
		}
	}
	
	public void dj_authReq(final XmppID id,final String status)
	{
		session.getUser().sendSubScription(id, "subscribe", "",status);
	}
	
	private void dj_auth(final  XmppID id)
	{
		session.getUser().sendSubScription(id, "subscribed", "","");
	}
	
	private void dj_deny(final XmppID id)
	{
		session.getUser().sendSubScription(id, "unsubscribed", "","");
	}
	
	public void doAddUser(final XmppID id)
	{
		if(userAddDlg == null)
			userAddDlg = new UserAddDialog(disco);
		else
			userAddDlg.reloadServices();
		userAddDlg.show();
		WindowManager.get().bringToFront(userAddDlg);
		userAddDlg.setJid(id);
	}
	
	public void doAddUser()
	{
		if(userAddDlg == null)
			userAddDlg = new UserAddDialog(disco);
		else
			userAddDlg.reloadServices();
		userAddDlg.show();
	}
	
	public void removeUser(final XmppID id)
	{
		RosterTask task = new RosterTask(session);
		task.remove(id);
	}
	
	public void showInfoSelf()
	{
		showInfo(getJid());
	}
	
	public void showInfo(final XmppID id)
	{
		if(infoDlg == null)
			infoDlg = new InfoDialog();
		
		infoDlg.clear();
		infoDlg.show();
		WindowManager.get().bringToFront(infoDlg);
		infoDlg.getInfo(id);
	}
	
	public void renameUser(final XmppID id,final String newName)
	{
		RosterTask task = new RosterTask(session);
		task.set(id, newName, null);
	}
	
	public void changeGroup(final XmppID id,final String name,final String newGroup)
	{
		RosterTask task = new RosterTask(session);
		List<String> groups = new ArrayList<String>();
		groups.add(newGroup);
		task.set(id,name, groups);
		
	}
	
	public void pushRosterIncoming(final Map<String,XmppContact> roster)
	{
		rosterPanel.pushRosterIncoming(roster);
	}
	
	public void getPrefs()
	{
		/*
		PrefsTask task = new PrefsTask(session);
		task.get();
		*/
	}
	
	public void savePrefs()
	{
		/*
		PrefsTask task = new PrefsTask(session);
		task.set();
		*/
	}
	
	public void doSearchUser()
	{
		if(searchDialog == null)
			searchDialog = new UserSearchDialog(disco,session);
		else
			searchDialog.reloadServices();
		searchDialog.show();
	}
	
	private void cleanDialog()
	{
		if(mainWindow != null)
		{
			mainWindow.close();
			mainWindow = null;
		}
		
		if(debugWindow != null)
		{
			debugWindow.close();
			debugWindow = null;
		}
		
		if(userAddDlg != null)
		{
			userAddDlg.close();
			userAddDlg = null;
		}
		
		if(searchDialog != null)
		{
			searchDialog.close();
			searchDialog = null;
		}
		
		if(infoDlg != null)
		{
			infoDlg.close();
			infoDlg = null;
		}
		
		if(roomDlg != null)
		{
			roomDlg.close();
			roomDlg = null;
		}
	}
	
	public XmppID getJid()
	{
		return XmppID.parseId(session.getUser().getID());
	}
	
	public String getNick()
	{
		String nick = getJid().getNode();
		XmppVCard v = getSelfVCard();
		if(v != null)
		{
			if(v.nickName()!=null&&!v.nickName().isEmpty())
				nick = v.nickName();
			else if(v.fullName()!=null&&!v.fullName().isEmpty())
				nick = v.fullName();
		}
		return nick;
	}
	
	public XmppVCard getSelfVCard()
	{
		return XmppVCardFactory.instance().get(getJid(), null);
		//return vcard;
	}
	
	public void showRooms()
	{
		if(roomDlg == null)
			roomDlg = new RoomDialog(disco);
		
		roomDlg.show();
		WindowManager.get().bringToFront(roomDlg);
	}
}
