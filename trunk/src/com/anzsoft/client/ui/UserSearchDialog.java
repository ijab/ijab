package com.anzsoft.client.ui;

import java.util.ArrayList;
import java.util.List;

import com.anzsoft.client.JabberApp;
import com.anzsoft.client.XMPP.XmppPacket;
import com.anzsoft.client.XMPP.XmppPacketListener;
import com.anzsoft.client.XMPP.XmppQuery;
import com.anzsoft.client.XMPP.mandioca.ServiceDiscovery;
import com.anzsoft.client.XMPP.mandioca.XmppSession;
import com.anzsoft.client.XMPP.mandioca.XmppTask;
import com.anzsoft.client.XMPP.mandioca.ServiceDiscovery.Service;
import com.anzsoft.client.utils.JabberXData;
import com.anzsoft.client.utils.TextUtils;
import com.anzsoft.client.utils.XmlDocument;
import com.extjs.gxt.ui.client.Style.HorizontalAlignment;
import com.extjs.gxt.ui.client.Style.Orientation;
import com.extjs.gxt.ui.client.Style.SelectionMode;
import com.extjs.gxt.ui.client.data.BaseModelData;
import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.SelectionChangedEvent;
import com.extjs.gxt.ui.client.event.SelectionChangedListener;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.Dialog;
import com.extjs.gxt.ui.client.widget.HtmlContainer;
import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.button.ButtonBar;
import com.extjs.gxt.ui.client.widget.form.ComboBox;
import com.extjs.gxt.ui.client.widget.form.FormPanel;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.extjs.gxt.ui.client.widget.layout.FlowLayout;
import com.extjs.gxt.ui.client.widget.layout.RowData;
import com.extjs.gxt.ui.client.widget.layout.RowLayout;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.Node;
import com.google.gwt.dom.client.NodeList;
import com.extjs.gxt.ui.client.widget.grid.ColumnConfig;
import com.extjs.gxt.ui.client.widget.grid.ColumnModel;
import com.extjs.gxt.ui.client.widget.grid.Grid;

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
		
		public void searchUser(final String service,final XmlDocument xmlDoc,final SearchListener listener)
		{
			this.listener = listener;
			XmppQuery iq = session.getFactory().createQuery();
			iq.setIQ(service, XmppQuery.TYPE_SET, id());
			Element query = iq.setQuery("jabber:iq:search");
			
			NodeList<Node> nodeList = xmlDoc.documentElement().getChildNodes();
			for(int index = 0;index < nodeList.getLength();index++)
			{
				query.appendChild(xmlDoc.firstChild().getChildNodes().getItem(index).cloneNode(true));
			}
			//query.appendChild(xmlDoc.firstChild().cloneNode(true));
			send(iq);
		}
		
		public void searchUser(final String service,final SearchListener listener)
		{
			this.listener = listener;
			XmppQuery iq = session.getFactory().createQuery();
			iq.setIQ(service+"/users", XmppQuery.TYPE_GET, id());
			iq.setQuery("jabber:iq:browse");
			send(iq);
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
	
	
	//data model
	public class User extends BaseModelData
	{
		private static final long serialVersionUID = 4245153877312175801L;

		public User()
		{
		}		
	}
	
	//UI code
	final private ServiceDiscovery serviceDisco;
	final private XmppSession session;
	private ComboBox<Service> serviceField;
	private HtmlContainer searchForm;
	private FormPanel searchPanel;
	private ContentPanel resultPanel;
	private Button searchButton;
	private Grid<User> resultGrid;
	
	private ListStore<User> store = null;
	private List<ColumnConfig> columnConfigs = null;
	
	private UserSearchTask searchTask = null;
	
	public UserSearchDialog(final ServiceDiscovery disco,final XmppSession session)
	{
		this.session = session;
		searchTask = new UserSearchTask(this.session);
		this.serviceDisco = disco;
		initUI();
	}
	
	private void initUI()
	{
		setButtons("");
		setHeading(JabberApp.getConstants().User_Search());
		setModal(false);
		setBodyBorder(true);
		//setInsetBorder(true);
		setBodyStyle("padding: 8px;background: none");
		setWidth(700);
		setHeight(300);
		setResizable(true);
		setClosable(true);
		setCollapsible(false);
		
		//FlowLayout layoutMain = new  FlowLayout();
		setLayout(new FitLayout());
		
		LayoutContainer main = new LayoutContainer();  
	    main.setLayout(new RowLayout(Orientation.HORIZONTAL));
		
		createSearchPanel();
		createResultPanel();
		
		main.add(searchPanel,new RowData(.4, 1));
		main.add(resultPanel,new RowData(.6, 1));
		
		add(main);
		
		Button closeButton = new Button(JabberApp.getConstants().close());
		closeButton.addSelectionListener(new SelectionListener<ButtonEvent>()
		{
			public void componentSelected(ButtonEvent ce) 
			{
				close();
			}
			
		});
		
		ButtonBar buttonBar = new ButtonBar();
		buttonBar.setButtonAlign(HorizontalAlignment.RIGHT);
		setButtonBar(buttonBar); 
		buttonBar.add(closeButton);
	}
	
	private void createResultPanel()
	{
		resultPanel = new ContentPanel();  
		resultPanel.setBodyBorder(false);  
		resultPanel.setHeaderVisible(false);
		resultPanel.setButtonAlign(HorizontalAlignment.CENTER);  
		resultPanel.setLayout(new FitLayout());  
	  
	    columnConfigs = new ArrayList<ColumnConfig>(); 
	    ColumnModel cm = new ColumnModel(columnConfigs);
	    
	    ColumnConfig column = new ColumnConfig();  
	    column.setId("nickname");  
	    column.setHeader("Nickname");  
	    column.setWidth(80);  
	    columnConfigs.add(column);
	    
	    column = new ColumnConfig();  
	    column.setId("fn");  
	    column.setHeader("First Name");  
	    column.setWidth(80);  
	    columnConfigs.add(column);
	    
	    column = new ColumnConfig();  
	    column.setId("ln");  
	    column.setHeader("Last Name");  
	    column.setWidth(80);  
	    columnConfigs.add(column);
	    
	    column = new ColumnConfig();  
	    column.setId("email");  
	    column.setHeader("EMail Address");  
	    column.setWidth(80);  
	    columnConfigs.add(column);
	    
	    column = new ColumnConfig();  
	    column.setId("jid");  
	    column.setHeader("Jabber ID");  
	    column.setWidth(80);  
	    columnConfigs.add(column);
	    
	    store = new ListStore<User>(); 
	    
	    resultGrid = new Grid<User>(store, cm);  
	    resultGrid.setStyleAttribute("borderTop", "none");  
	    resultGrid.setBorders(true);  
	    //resultGrid.setAutoHeight(true);
	    resultGrid.setAutoExpandColumn("nickname");
	    resultGrid.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
	    resultPanel.add(resultGrid);
	    
	    
	    Button addButton = new Button(JabberApp.getConstants().add());
	    addButton.addSelectionListener(new SelectionListener<ButtonEvent>()
	    {
			public void componentSelected(ButtonEvent ce) 
			{
			}
	    });
	    
	    Button infoButton = new Button(JabberApp.getConstants().userInfo());
	    infoButton.addSelectionListener(new SelectionListener<ButtonEvent>()
	    {
			public void componentSelected(ButtonEvent ce) 
			{
			}
	    });
	    
	    ButtonBar buttonBar = new ButtonBar();
	    buttonBar.setButtonAlign(HorizontalAlignment.RIGHT);
	    resultPanel.setButtonBar(buttonBar);
	    buttonBar.add(addButton);
	    buttonBar.add(infoButton);
	    
	}
	
	private void createSearchPanel()
	{
		searchPanel = new FormPanel();  
		searchPanel.setFrame(false);  
		searchPanel.setHeaderVisible(false); 
		searchPanel.setBodyBorder(false); 
		searchPanel.setButtonAlign(HorizontalAlignment.CENTER);  
		searchPanel.setLayout(new FlowLayout());  
	  
	    LayoutContainer main = new LayoutContainer();  
	    main.setLayout(new RowLayout());  
	    
	    ListStore<Service> services = new ListStore<Service>();  
		services.add(serviceDisco.getSearchServices());  
		
		serviceField = new ComboBox<Service>();
		serviceField.setAutoWidth(true);
		serviceField.setFieldLabel(JabberApp.getConstants().Choose_Directory());
		serviceField.setStore(services);
		serviceField.setDisplayField("name");
		serviceField.setTypeAhead(true);
		serviceField.addSelectionChangedListener(new SelectionChangedListener<Service>()
		{
			public void selectionChanged(SelectionChangedEvent<Service> se) 
			{
				Service service = se.getSelectedItem();
				
				XmppQuery iq = session.getFactory().createQuery();
				iq.setIQ(service.getJid(), XmppQuery.TYPE_GET, TextUtils.genUniqueId());
				iq.setQuery("jabber:iq:search");
				session.send(iq, new XmppPacketListener()
				{
					public void onPacketReceived(XmppPacket iq)
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
								NodeList<Node> childs = queryElement.getChildNodes();
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
						searchForm.setHtml(html);
					}
					public void onPacketSent(XmppPacket packet) 
					{
						
					}
					
				});
			}
		});
		serviceField.setValue(serviceDisco.getSearchServices().get(0));
		main.add(serviceField);   
		
		searchForm = new HtmlContainer();
		main.add(searchForm);
	    
		searchPanel.add(main);
		
		searchButton = new Button(JabberApp.getConstants().search());
		searchButton.addSelectionListener(new SelectionListener<ButtonEvent>()
		{
			public void componentSelected(ButtonEvent be) 
			{
				searchUser();
			}
		});
		ButtonBar buttonBar = new ButtonBar();
		buttonBar.setButtonAlign(HorizontalAlignment.RIGHT);
		searchPanel.setButtonBar(buttonBar);
		buttonBar.add(searchButton);
	}
	
	public void reloadServices()
	{
		ListStore<Service> services = new ListStore<Service>();  
		services.add(serviceDisco.getSearchServices());  
		serviceField.setStore(services);
		serviceField.setDisplayField("name");
	}
	
	private void searchUser()
	{
		String searchstring = "";
		boolean jabberXData = false;
		Element searchFormElement = searchForm.getElement();
		NodeList<Node> nodes = searchFormElement.getChildNodes();
		for(int index=0;index<nodes.getLength();index++)
		{
			Element el = (Element) nodes.getItem(index);
			if(el.getAttribute("name").equals("jwchat_form_type")&&el.getAttribute("value").equals("jabber:x:data"))
			{
				jabberXData = true;
				break;
			}
		}
		
		if(jabberXData)
		{
			searchstring = JabberXData.genJabberXDataReply(searchFormElement);
		}
		//System.out.println(searchstring);
		
		String service = serviceField.getValue().getJid();
		if(searchstring.isEmpty())
		{
			searchTask.searchUser(service, new SearchListener()
			{
				public void onResult(XmppPacket packet) 
				{
					onSearchResult(packet);
				}
				
			});
		}
		else
		{
			XmppQuery iq = session.getFactory().createQuery();
			iq.setIQ(service, XmppQuery.TYPE_SET, TextUtils.genUniqueId());
			Element query = iq.setQuery("jabber:iq:search");
			XmlDocument xmlDoc = XmlDocument.create("body", "foo");
			xmlDoc.loadXML("<body>"+searchstring+"</body>");
			query.appendChild(xmlDoc.documentElement().getFirstChildElement().cloneNode(true));
			session.send(iq, new XmppPacketListener()
			{
				public void onPacketReceived(XmppPacket packet) 
				{
					onSearchResult(packet);
				}
				public void onPacketSent(XmppPacket packet) 
				{
				}
				
			});
		}
		
	}
	
	private void onSearchResult(final XmppPacket packet)
	{
		if(!packet.getType().equals("result"))
		{
			return;
		}
		
		Element iq = (Element) packet.getNode();
		Element query = iq.getElementsByTagName("query").getItem(0);
		if(!query.getAttribute("xmlns").equals("jabber:iq:search"))
			return;
		Element x = query.getElementsByTagName("x").getItem(0);
		Element reportedElement = x.getElementsByTagName("reported").getItem(0);
		if(reportedElement != null)
		{
			columnConfigs.clear();
			
			NodeList<Element> fields = reportedElement.getElementsByTagName("field");
			for(int index=0;index<fields.getLength();index++)
			{
				Element field = fields.getItem(index); 
				String label = field.getAttribute("label");
				String id = field.getAttribute("var"); 
				
				ColumnConfig column = new ColumnConfig();  
			    column.setId(id);  
			    column.setHeader(label);  
			    column.setWidth(80);  
			    columnConfigs.add(column);
			}
		}
		store = new ListStore<User>();
		
		resultGrid.getSelectionModel().deselectAll();
		NodeList<Element> items = x.getElementsByTagName("item"); 
		for(int index=0;index<items.getLength();index++)
		{
			Element item = items.getItem(index);
			User user = new User();
			NodeList<Element> fields = item.getElementsByTagName("field");
			for(int i = 0;i<fields.getLength();i++)
			{
				Element field = fields.getItem(i);
				String id = field.getAttribute("var");
				Element valueEl = field.getFirstChildElement();
				String value = "";
				if(valueEl.hasChildNodes())
					value = valueEl.getFirstChild().getNodeValue();
				user.set(id, value);
			}
			store.add(user);
		}		ColumnModel cm = new ColumnModel(columnConfigs);
		try
		{
			resultGrid.reconfigure(store, cm);
		}
		catch(Exception ce)
		{
			
		}
	}
}
