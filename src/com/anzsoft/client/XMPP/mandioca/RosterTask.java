package com.anzsoft.client.XMPP.mandioca;

import java.util.List;

import com.anzsoft.client.XMPP.XmppID;
import com.anzsoft.client.XMPP.XmppPacket;
import com.anzsoft.client.XMPP.XmppQuery;
import com.anzsoft.client.utils.DOMHelper;
import com.extjs.gxt.ui.client.widget.menu.Item;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.Node;

public class RosterTask extends XmppTask
{

	public RosterTask(XmppSession session) 
	{
		super(session);
	}

	public void take(XmppPacket packet) 
	{
		
	}
	
	public void set(final XmppID jid,final String name,final List<String> groups)
	{
		XmppQuery iq = session.getFactory().createQuery();
		iq.setType(XmppQuery.TYPE_SET);
		iq.setID(id());
		Node query = iq.createQueryNode("jabber:iq:roster");
		Element item = (Element)query.appendChild(iq.getDoc().createElement("item"));
		item.setAttribute("jid", jid.toStringNoResource());
		if(name != null&&!name.isEmpty())
			item.setAttribute("name", name);
		for(String group:groups)
		{
			item.appendChild(DOMHelper.textTag(iq.getDoc(), "group", group));
		}
		send(iq);
	}
	
	public void remove(final XmppID jid)
	{
		XmppQuery iq = session.getFactory().createQuery();
		iq.setType(XmppQuery.TYPE_SET);
		iq.setID(id());
		Node query = iq.createQueryNode("jabber:iq:roster");
		Element item = (Element)query.appendChild(iq.getDoc().createElement("item"));
		item.setAttribute("jid", jid.toStringNoResource());
		send(iq);
	}

}
