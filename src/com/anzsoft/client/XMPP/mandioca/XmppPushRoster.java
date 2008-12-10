package com.anzsoft.client.XMPP.mandioca;

import java.util.HashMap;
import java.util.Map;

import com.anzsoft.client.JabberApp;
import com.anzsoft.client.XMPP.XmppInfoQueryListener;
import com.anzsoft.client.XMPP.XmppPacket;
import com.anzsoft.client.utils.XMLHelper;
import com.google.gwt.xml.client.Document;
import com.google.gwt.xml.client.Element;
import com.google.gwt.xml.client.NodeList;
import com.google.gwt.xml.client.XMLParser;

public class XmppPushRoster 
{
	private final XmppInfoQueryListener rosterListener;
    private final XmppSession session;
    public Map<String,XmppContact> contacts = new HashMap<String, XmppContact>();
    
    public XmppPushRoster(final XmppSession session)
    {
    	this.session = session;
    	rosterListener = new XmppInfoQueryListener()
    	{

			public void onInfoQueryReceived(XmppPacket packet) 
			{
				if(!packet.getType().equals("set")||packet.getID().equals("roster_1"))
					return;
				Document doc = XMLParser.parse(packet.toXML());
		    	Element query = XMLHelper.queryTag(doc.getDocumentElement());
		    	if(!query.getAttribute("xmlns").equals("jabber:iq:roster"))
		    		return;
		    	contacts.clear();
		    	
		    	NodeList itemList = query.getElementsByTagName("item");
	    		for(int index = 0;index<itemList.getLength();index++)
	    		{
	    			Element item = (Element)itemList.item(index);
	    			XmppContact contact = XmppContact.fromXml(item);
	    			contacts.put(contact.getJID().toString(), contact);
	    		}
	    		if(!contacts.isEmpty())
	    			JabberApp.instance().pushRosterIncoming(contacts);
			}

			public void onInfoQuerySent(XmppPacket packet) 
			{
				
			}
    	};
    	this.session.addInfoQueryListener(rosterListener);
    }

}
