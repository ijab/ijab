package com.anzsoft.client.XMPP.mandioca;

import com.anzsoft.client.iJabPrefs;
import com.anzsoft.client.XMPP.XmppPacket;
import com.anzsoft.client.XMPP.XmppQuery;
import com.anzsoft.client.utils.DOMHelper;
import com.anzsoft.client.utils.TextUtils;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.Node;
import com.google.gwt.dom.client.NodeList;

public class PrefsTask extends XmppTask
{
	public PrefsTask(XmppSession session) 
	{
		super(session);
	}

	public void take(XmppPacket packet) 
	{
		if(!packet.getType().equals("result")||!packet.getID().equals(id()))
			return;
		
		Element iqElement = (Element)packet.getNode();
		if(iqElement.getElementsByTagName("ijab").getItem(0) != null)
		{
			Node iNode = iqElement.getElementsByTagName("ijab").getItem(0);
			NodeList nodeList = iNode.getChildNodes();
			for(int index = 0;index<nodeList.getLength();index++)
			{
				Node node = nodeList.getItem(index);
				String name = node.getNodeName();
				if(name.equals("showOnlineOnly"))
				{
					iJabPrefs.instance().showOnlineOnly = TextUtils.str2bool(node.getNodeValue());
				}
			}
		}
		iJabPrefs.instance().prefsChanged();
		
		
	}
	
	public void get()
	{
		XmppQuery iq = session.getFactory().createQuery();
		iq.setIQ(null, XmppQuery.TYPE_GET, id());
		Element query = iq.setQuery("jabber:iq:private");
		Element prefsElement = iq.getDoc().createElement("ijab");
		prefsElement.setAttribute("xmlns", "ijab:prefs");
		query.appendChild(prefsElement);
		send(iq);		
	}
	
	public void set()
	{
		XmppQuery iq = session.getFactory().createQuery();
		iq.setIQ(null, XmppQuery.TYPE_SET, id());
		Element query = iq.setQuery("jabber:iq:private");
		Element prefsElement = iq.getDoc().createElement("ijab");
		prefsElement.setAttribute("xmlns", "ijab:prefs");
		
		prefsElement.appendChild(DOMHelper.textTag(iq.getDoc(), "showOnlineOnly", TextUtils.bool2str(iJabPrefs.instance().showOnlineOnly)));
		
		query.appendChild(prefsElement);
		send(iq);
	}

}
