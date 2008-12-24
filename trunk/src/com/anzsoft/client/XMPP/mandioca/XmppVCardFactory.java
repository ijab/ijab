package com.anzsoft.client.XMPP.mandioca;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import com.anzsoft.client.JabberApp;
import com.anzsoft.client.XMPP.HandlerCollection;
import com.anzsoft.client.XMPP.XmppID;
import com.anzsoft.client.XMPP.XmppPacket;
import com.anzsoft.client.XMPP.XmppPacketListener;
import com.anzsoft.client.XMPP.XmppQuery;
import com.anzsoft.client.utils.TextUtils;
import com.google.gwt.dom.client.Element;

public class XmppVCardFactory 
{	
	private Map<String, XmppVCard> vcards = new HashMap<String,XmppVCard>();
	private final HandlerCollection vcardListeners = new HandlerCollection();
	private static XmppVCardFactory  instance = null;
	
	private XmppVCardFactory()
	{
		
	}
	
	public void set(final XmppID id,final XmppVCard vcard)
	{
		vcards.put(id.toStringNoResource(), vcard);
		fireOnVCard(id,vcard);
	}
	
	public XmppVCard get(final XmppID id,final VCardListener listener)
	{
		if(vcards.containsKey(id.toStringNoResource()))
			return vcards.get(id.toStringNoResource());
		
		XmppQuery iq = JabberApp.instance().getSession().getFactory().createQuery();
		iq.setIQ(id.toStringNoResource(), XmppQuery.TYPE_GET, TextUtils.genUniqueId());
		Element vcardEl = iq.getDoc().createElementNS("vcard-temp", "vCard");
		vcardEl.setAttribute("version", "2.0");
		vcardEl.setAttribute("prodid", "-//HandGen//NONSGML vGen v1.0//EN");
		iq.getNode().appendChild(vcardEl);
		JabberApp.instance().getSession().send(iq, new XmppPacketListener()
		{
			public void onPacketReceived(XmppPacket packet) 
			{
				XmppID id = packet.getFromID();
				XmppVCard vcard = new XmppVCard();
				if(vcard.formXml(packet.toXML()))
				{
					vcards.put(id.toStringNoResource(), vcard);
					if(listener != null)
						listener.onVCard(id,vcard);
					fireOnVCard(id,vcard);
				}
			}

			public void onPacketSent(XmppPacket packet) 
			{
			}
		});
		return null;
	}
	
	static public XmppVCardFactory instance()
	{
		if(instance == null)
			instance = new XmppVCardFactory();
		return instance;
	}
	
	public void addVCardListener(VCardListener listener)
    {
    	vcardListeners.add(listener);
    }
    
    public void removeVCardListener(VCardListener listener) 
    {
    	vcardListeners.remove(listener);
    }
    
    private void fireOnVCard(final XmppID id,final XmppVCard vcard)
    {
    	Iterator iter = vcardListeners.iterator();
    	while (iter.hasNext())
    	{
    		VCardListener listener = (VCardListener) iter.next();
    		listener.onVCard(id, vcard);
    	}	
    }
	
	public void clear()
	{
		vcardListeners.clear();
		vcards.clear();
	}
}
