package com.anzsoft.client.XMPP.mandioca;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.anzsoft.client.XMPP.XmppID;
import com.anzsoft.client.XMPP.XmppInfoQueryListener;
import com.anzsoft.client.XMPP.XmppPacket;
import com.anzsoft.client.XMPP.XmppQuery;
import com.anzsoft.client.utils.XMLHelper;
import com.google.gwt.xml.client.Document;
import com.google.gwt.xml.client.Element;
import com.google.gwt.xml.client.Node;
import com.google.gwt.xml.client.NodeList;
import com.google.gwt.xml.client.XMLParser;
import com.extjs.gxt.ui.client.data.BaseModelData;

public class ServiceDiscovery
{
	

	public class Service extends BaseModelData 
	{

		public Service() 
		{

		}
		
		public Service(String jid, String name) 
		{
			setJid(jid);
			setName(name);
		}

		public String getName() 
		{
			return get("name");
		}

		public void setName(String name) 
		{
			set("name", name);
		}

		public String getJid()
		{
			return get("jid");
		}

		public void setJid(String jid) 
		{
			set("jid", jid);
		}

	}
	
	
	private final XmppInfoQueryListener discoListener;
	private final XmppSession session;
	public Map<String,Element> disco = new HashMap<String,Element>();
	private int info_id = 0;
	public ServiceDiscovery(final XmppSession session)
	{
		this.session = session;
		discoListener = new XmppInfoQueryListener()
		{
			public void onInfoQueryReceived(XmppPacket packet) 
			{
				if(packet.getType().equals("result"))
				{
					if(packet.getID().equals("disco_item_1"))
					{
						onDiscoItems(packet);
					}
					else if(packet.getID().startsWith("disco_info_"))
					{
						onDiscoInfo(packet);
					}
				}
			}

			public void onInfoQuerySent(XmppPacket packet) 
			{
				
			}
		};
		this.session.addInfoQueryListener(discoListener);
	}
	
	public void getDiscoItems()
	{
		XmppQuery query = session.getFactory().createQuery();
		query.setType(XmppQuery.TYPE_GET);
		query.setID("disco_item_1");
		XmppID jid = XmppID.parseId(session.getUser().getID());
		query.setTo(jid.getDomain());
		query.setQuery("http://jabber.org/protocol/disco#items");
		session.send(query);
	}
	
	public void getDiscoItemInfo(final String jid)
	{
		XmppQuery query = session.getFactory().createQuery();
		query.setType(XmppQuery.TYPE_GET);
		query.setID("disco_info_"+info_id);
		query.setTo(jid);
		query.setQuery("http://jabber.org/protocol/disco#info");
		session.send(query);
		info_id++;
	}
	
	private void onDiscoItems(final XmppPacket query)
	{
		Document doc = XMLParser.parse(query.toXML());
    	Element queryEl = XMLHelper.queryTag(doc.getDocumentElement());
    	NodeList items = queryEl.getChildNodes();
    	disco.clear();
    	for(int index = 0;index < items.getLength();index++)
    	{
    		Element item = (Element)items.item(index);
    		if(!item.getTagName().equals("item")||item.getAttribute("jid") == null||item.getAttribute("node") != null)
    			continue;
    		getDiscoItemInfo(item.getAttribute("jid"));
    	}
	}
	
	private void onDiscoInfo(final XmppPacket query)
	{
		final String jid = query.getFrom();
		Document doc = XMLParser.parse(query.toXML());
    	Element queryEl = XMLHelper.queryTag(doc.getDocumentElement());
    	disco.put(jid, queryEl);
	}
	
	public List<Service> getGateWays()
	{
		List<Service> services = new ArrayList<Service>();
		services.add(0,new Service("","Local jabber user"));
		for(String jid:disco.keySet())
		{
			Element el = disco.get(jid);
			if(el == null)
				continue;
			Node item = el.getElementsByTagName("identity").item(0);
			if(item == null)
				continue;
			Element elItem = (Element)item;
			if(elItem.getAttribute("category").equals("gateway"))
			{
				services.add(new Service(jid,elItem.getAttribute("name")));
			}
		}
		return services;
	}
	
	public List<Service> getSearchServices()
	{
		List<Service> services = new ArrayList<Service>();
		for(String jid:disco.keySet())
		{
			Element el = disco.get(jid);
			if(el == null)
				continue;
			NodeList items = el.getElementsByTagName("feature");
			for(int index = 0;index<items.getLength();index++)
			{
				Element item = (Element)items.item(index);
				if(item.getAttribute("var").equals("jabber:iq:search"))
				{
					Element identity = (Element) el.getElementsByTagName("identity").item(0);
					services.add(new Service(jid,identity.getAttribute("name")));
					break;
				}
			}
		}
		return services;
	}
}
