package com.anzsoft.client.ui;

import com.anzsoft.client.JabberApp;
import com.anzsoft.client.XMPP.XmppPacket;
import com.anzsoft.client.XMPP.XmppQuery;
import com.anzsoft.client.XMPP.mandioca.ServiceDiscovery;
import com.anzsoft.client.XMPP.mandioca.XmppSession;
import com.anzsoft.client.XMPP.mandioca.XmppTask;
import com.anzsoft.client.XMPP.mandioca.ServiceDiscovery.Service;
import com.anzsoft.client.utils.JabberXData;
import com.extjs.gxt.ui.client.event.SelectionChangedEvent;
import com.extjs.gxt.ui.client.event.SelectionChangedListener;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.widget.Dialog;
import com.extjs.gxt.ui.client.widget.form.ComboBox;
import com.extjs.gxt.ui.client.widget.layout.FlowLayout;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.Node;
import com.google.gwt.dom.client.NodeList;

public class UserSearchDialog extends Dialog
{
	public interface SearchListener
	{
		public void onResult(XmppPacket packet);
	}
	
	//Search Task
	public class UserSearchTask extends XmppTask 
	{
		private SearchListener listener = null;
		public UserSearchTask(XmppSession session)
		{
			super(session);
		}

		public void take(XmppPacket packet) 
		{
			if(listener != null)
				listener.onResult(packet);
		}
		
		public void searchUser(final String searchString,final String service)
		{
			XmppQuery iq = session.getFactory().createQuery();
			if(searchString.isEmpty())
			{
				iq.setIQ(service+"/users", XmppQuery.TYPE_GET, id());
				iq.setQuery("jabber:iq:browse");
			}
			else
			{
				iq.setIQ(service, XmppQuery.TYPE_SET, id());
				Element query = iq.setQuery("jabber:iq:search");
				
			}
		}
		
		public void getSearchForm(final String service,final SearchListener listener)
		{
			this.listener = listener;
			XmppQuery iq = session.getFactory().createQuery();
			iq.setIQ(service, XmppQuery.TYPE_GET, id());
			iq.setQuery("jabber:iq:search");
			send(iq);
		}

	}
	//Task end
	
	//UI code
	final private ServiceDiscovery serviceDisco;
	final private XmppSession session;
	private ComboBox<Service> serviceField;
	
	public UserSearchDialog(final ServiceDiscovery disco,final XmppSession session)
	{
		this.session = session;
		this.serviceDisco = disco;
		initUI();
	}
	
	private void initUI()
	{
		setButtons("");
		setHeading(JabberApp.getConstants().User_Search());
		setModal(false);
		setBodyBorder(true);
		setInsetBorder(true);
		setBodyStyle("padding: 0px;background: none");
		setWidth(350);
		setResizable(false);
		setClosable(true);
		setCollapsible(false);
		
		FlowLayout layoutMain = new  FlowLayout();
		setLayout(layoutMain);
		
		ListStore<Service> services = new ListStore<Service>();  
		services.add(serviceDisco.getSearchServices());  
		
		serviceField = new ComboBox<Service>();
		serviceField.setFieldLabel(JabberApp.getConstants().Service());
		serviceField.setStore(services);
		serviceField.setDisplayField("name");
		serviceField.setTypeAhead(true);
		serviceField.addSelectionChangedListener(new SelectionChangedListener<Service>()
		{
			public void selectionChanged(SelectionChangedEvent<Service> se) 
			{
				Service service = se.getSelectedItem();
				UserSearchTask task = new UserSearchTask(session);
				task.getSearchForm(service.getJid(), new SearchListener()
				{
					public void onResult(XmppPacket iq) 
					{
						if(!iq.getType().equals("result"))
							return;
						Element el = (Element) iq.getNode();
						String html;
						if(el.getElementsByTagName("x").getLength()>0&&el.getElementsByTagName("x").getItem(0).getAttribute("xmlns").equals("jabber:x:data"))
						{
							html = JabberXData.genJabberXDataTable(el.getElementsByTagName("x").getItem(0));
						}
						else
						{
							html = "<table>";
							if (el.getElementsByTagName("instructions").getItem(0) != null)
								html += "<tr><th colspan=2>"+el.getElementsByTagName("instructions").getItem(0).getFirstChild().getNodeValue()+"</th></tr>";
							
							Element queryElement = el.getElementsByTagName("query").getItem(0);
							if(queryElement != null)
							{
								NodeList childs = queryElement.getChildNodes();
								for(int i = 0;i<childs.getLength();i++)
								{
									Node node = (Node) childs.getItem(i);
									if(node.getNodeName().equals("instructions"))
										continue;
									if(node.getNodeName().equals("key"))
										html += "<tr><td colspan=2><input type=hidden value=\""+node.getFirstChild().getNodeValue()+"\"></td></tr>";
									else
									{
										if(node.getFirstChild() != null)
											html += "<tr><td>"+node.getNodeName()+"&nbsp;</td><td><input type=\"text\" name=\""+node.getNodeName()+"\" value=\""+node.getFirstChild().getNodeValue()+"\"></td></tr>";
										else
											html += "<tr><td>"+node.getNodeName()+"&nbsp;</td><td><input type=\"text\" name=\""+node.getNodeName()+"\"></td></tr>";
									}
								}
							}
							html += "</table>";
						}
					}
					
				});
				
			}
		});
		//serviceField.setValue(serviceDisco.getSearchServices().get(0));
		add(serviceField);   
	}
	
	public void reloadServices()
	{
		ListStore<Service> services = new ListStore<Service>();  
		services.add(serviceDisco.getSearchServices());  
		serviceField.setStore(services);
		serviceField.setDisplayField("name");
	}
}
