package com.anzsoft.client.ui;

import com.anzsoft.client.JabberApp;
import com.anzsoft.client.XMPP.XmppID;
import com.anzsoft.client.XMPP.mandioca.ServiceDiscovery;
import com.anzsoft.client.XMPP.mandioca.ServiceDiscovery.Service;
import com.extjs.gxt.ui.client.Style.HorizontalAlignment;
import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.SelectionChangedEvent;
import com.extjs.gxt.ui.client.event.SelectionChangedListener;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.widget.Dialog;
import com.extjs.gxt.ui.client.widget.MessageBox;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.button.ButtonBar;
import com.extjs.gxt.ui.client.widget.button.FillButton;
import com.extjs.gxt.ui.client.widget.form.ComboBox;
import com.extjs.gxt.ui.client.widget.form.TextArea;
import com.extjs.gxt.ui.client.widget.form.TextField;
import com.extjs.gxt.ui.client.widget.layout.FormLayout;

public class UserAddDialog extends Dialog
{
	final private ServiceDiscovery serviceDisco;
	private TextField<String> jidField;
	private ComboBox<Service> serviceField;
	TextArea msgField;
	private Button closeButton;
	private Button addButton;
	private Button searchButton;
	public UserAddDialog(final ServiceDiscovery serviceDisco)
	{
		this.serviceDisco = serviceDisco;
		setHeading(JabberApp.getConstants().addUser());
		createUI();		
	}
	
	private void createUI()
	{
		setButtons("");
		setModal(false);
		setBodyBorder(true);
		setInsetBorder(true);
		setBodyStyle("padding: 0px;background: none");
		setSize(310,230);
		setResizable(false);
		setClosable(true);
		setCollapsible(false);

		FormLayout layout = new FormLayout();
		layout.setLabelWidth(90);
	    layout.setDefaultWidth(175);
		setLayout(layout);

		jidField = new TextField<String>();
		jidField.setFieldLabel("JID");
		add(jidField);

		ListStore<Service> services = new ListStore<Service>();  
		services.add(serviceDisco.getGateWays());  

		serviceField = new ComboBox<Service>();
		serviceField.setFieldLabel("Service");
		serviceField.setStore(services);
		serviceField.setDisplayField("name");
		serviceField.setTypeAhead(true);
		add(serviceField);   
		
		serviceField.addSelectionChangedListener(new SelectionChangedListener<Service>()
		{

			public void selectionChanged(SelectionChangedEvent<Service> se) 
			{
				Service gateWay = se.getSelectedItem();
				String domain = gateWay.getJid();
				if(domain == null)
					domain = "";
				String value = jidField.getValue();
				if(value != null)
				{
					int i=  value.indexOf("@");
					if(i != -1)
						value = value.substring(0, i);
					if(!domain.isEmpty())
						value += "@" + domain;
					jidField.setValue(value);
				}
				else
				{
					if(!domain.isEmpty())
						jidField.setValue("@"+domain);
				}
			}
		});
		serviceField.setValue(serviceDisco.getGateWays().get(0));

		msgField = new TextArea();  
		msgField.setPreventScrollbars(true);
		msgField.setFieldLabel("MSG");
		msgField.setHeight(100);
		//msgField.setHideLabel(true);
	    add(msgField);
		
		closeButton = new Button(JabberApp.getConstants().close());
		closeButton.addSelectionListener(new SelectionListener<ButtonEvent>()
		{
			public void componentSelected(ButtonEvent ce) 
			{
				jidField.setValue("");
				serviceField.setValue(serviceDisco.getGateWays().get(0));
				close();
			}
		});
		
		addButton = new Button(JabberApp.getConstants().add());
		addButton.addSelectionListener(new SelectionListener<ButtonEvent>()
		{
			public void componentSelected(ButtonEvent ce) 
			{
				onAdd();
			}
		});
		
		searchButton = new Button(JabberApp.getConstants().search());
		searchButton.addSelectionListener(new SelectionListener<ButtonEvent>()
		{
			public void componentSelected(ButtonEvent ce) 
			{
				
			}
			
		});

		buttonBar = new ButtonBar();
		buttonBar.setButtonAlign(HorizontalAlignment.RIGHT);
		setButtonBar(buttonBar);
		buttonBar.add(searchButton);
		buttonBar.add(new FillButton());
		buttonBar.add(closeButton);
		buttonBar.add(addButton);
	}
	
	public void reloadServices()
	{
		ListStore<Service> services = new ListStore<Service>();  
		services.add(serviceDisco.getGateWays());  
		serviceField.setStore(services);
		serviceField.setDisplayField("name");
	}
	
	private void onAdd()
	{
		String to = jidField.getValue();
		if(to == null||to.isEmpty())
		{
			MessageBox.alert("JID missing", "Please enter the jid you want to invite", null);
			return;
		}
		
		if(to.indexOf("@") == -1)
		{
			to += "@" + JabberApp.instance().domain;
		}
		
		JabberApp.instance().dj_authReq(XmppID.parseId(to),msgField.getValue());
		jidField.setValue("");
		serviceField.setValue(serviceDisco.getGateWays().get(0));
		close();		
	}
}
