package com.anzsoft.client.XMPP.mandioca;

import com.anzsoft.client.XMPP.XmppID;
import com.anzsoft.client.XMPP.XmppInfoQueryListener;
import com.anzsoft.client.XMPP.XmppPacket;
import com.anzsoft.client.utils.TextUtils;
import com.anzsoft.client.utils.XMLHelper;
import com.google.gwt.xml.client.Element;

public abstract class XmppTask 
{
	protected final XmppSession session;
	protected final XmppInfoQueryListener queryListener;
	protected final String m_id;
	public XmppTask(final XmppSession session)
	{
		this.session = session;
		this.m_id = TextUtils.genUniqueId();
		queryListener = new XmppInfoQueryListener()
		{
			public void onInfoQueryReceived(XmppPacket packet) 
			{
				take(packet);
			}
			public void onInfoQuerySent(XmppPacket packet) 
			{
				
			}
			
		};
	}
	
	public abstract void take(XmppPacket packet);
	
	public String id()
	{
		return m_id;
	}
	
	protected void send(final XmppPacket packet)
	{
		packet.setID(m_id);
		session.send(packet);
	}
	
	protected boolean iqVerify(final Element x,final XmppID to,final String id,final String xmlns)
	{
		if(!x.getTagName().equals("iq"))
			return false;
		XmppID from = XmppID.parseId(x.getAttribute("from"));
		XmppID local = XmppID.parseId(session.getUser().getID());
		XmppID server = XmppID.parseId(XmppID.parseId(session.getUser().getID()).getDomain());
		
		if(from == null)
		{
			if(to != null&&!to.equals(server))
			{
				return false;
			}
		}
		else if(from.compareNoResouce(local)||from.compareNoResouce(local.getDomain()))
		{
			if(to!=null&&!to.compareNoResouce(local)&&!to.compareNoResouce(server))
				return false;
		}
		else
		{
			if(!from.compareNoResouce(to))
				return false;
		}
		
		if(id != null&&!id.isEmpty())
		{
			if(!x.getAttribute("id").equals(id))
				return false;
		}
		
		if(xmlns != null&&!xmlns.isEmpty())
		{
			if(!XMLHelper.queryNS(x).equals(xmlns))
				return false;
		}
		return true;		
	}
}
