package com.anzsoft.client.XMPP.mandioca.rooms;

import com.anzsoft.client.XMPP.XmppID;
import com.anzsoft.client.utils.DOMHelper;
import com.anzsoft.client.utils.XmlDocument;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.Node;
import com.google.gwt.dom.client.NodeList;

public class MUCItem 
{
	public enum Affiliation { UnknownAffiliation, Outcast, NoAffiliation, Member, Admin, Owner }
	public enum Role { UnknownRole, NoRole, Visitor, Participant, Moderator }
	
	private Role role;
	private Affiliation affiliation;
	
	private String nick;
	private XmppID jid;
	private XmppID actor;
	private String reason;
	
	public MUCItem(final Role role,final Affiliation affiliation)
	{
		this.role = role;
		this.affiliation = affiliation;
	}
	
	public MUCItem(final Element item)
	{
		fromXml(item);
	}
	
	public void setNick(final String nick)
	{
		this.nick = nick;
	}
	
	public void setJid(final XmppID jid)
	{
		this.jid = jid;
	}
	
	public void setAffiliation(final Affiliation affiliation)
	{
		this.affiliation = affiliation;
	}
	
	public void setRole(final Role role)
	{
		this.role = role;
	}
	
	public void setReason(final String reason)
	{
		this.reason = reason;
	}
	
	public void setActor(final XmppID actor)
	{
		this.actor = actor;
	}
	
	public String nick()
	{
		return this.nick;
	}
	
	public XmppID jid()
	{
		return this.jid;
	}
	
	public XmppID actor()
	{
		return this.actor;
	}
	
	public Affiliation affiliation()
	{
		return this.affiliation;
	}
	
	public Role role()
	{
		return this.role;
	}
	
	public String reason()
	{
		return this.reason;
	}
	
	public void fromXml(final Element item)
	{
		if(!item.getTagName().equals("item"))
			return;
		
		jid = XmppID.parseId(item.getAttribute("jid"));
		nick = item.getAttribute("nick");
		
		//Affiliation
		String strAffiliation = item.getAttribute("affiliation");
		if(strAffiliation != null&&!strAffiliation.isEmpty())
		{
			if (strAffiliation.equals("owner"))
			{
				affiliation = Affiliation.Owner;
			}
			else if (strAffiliation.equals("admin")) 
			{
				affiliation = Affiliation.Admin;
			}
			else if (strAffiliation.equals("member")) 
			{
				affiliation = Affiliation.Member;
			}
			else if (strAffiliation.equals("outcast")) 
			{
				affiliation = Affiliation.Outcast;
			}
			else if (strAffiliation.equals("none")) 
			{
				affiliation = Affiliation.NoAffiliation;
			}
		}
		else
			affiliation = Affiliation.NoAffiliation;
		
		//Role
		String strRole = item.getAttribute("role");
		if(strRole != null&&!strRole.isEmpty())
		{
			if (strRole.equals("moderator"))
			{
				role = Role.Moderator;
			}
			else if (strRole.equals("participant"))
			{
				role = Role.Participant;
			}
			else if (strRole.equals("visitor")) 
			{
				role = Role.Visitor;
			}
			else if (strRole.equals("none")) 
			{
				role = Role.NoRole;
			}
		}
		else
			role = Role.NoRole;
		
		NodeList<Node> childs = item.getChildNodes();
		for(int index=0;index<childs.getLength();index++)
		{
			Element e = (Element) childs.getItem(index);
			
			if(e.getTagName().equals("actor"))
				actor = XmppID.parseId(e.getAttribute("jid"));
			else if(e.getTagName().equals("reason"))
				reason = e.getInnerText();
		}
	}
	
	public Element toXml(final XmlDocument doc)
	{
		Element e = doc.createElement("item");
		
		if(nick!=null&&!nick.isEmpty())
			e.setAttribute("nick", nick);
		
		if(jid!=null&&!jid.toString().isEmpty())
			e.setAttribute("jid", jid.toString());
		
		if(reason!=null&&!reason.isEmpty())
			e.appendChild(DOMHelper.textTag(doc, "reason", reason));
		
		switch (affiliation) 
		{
		case NoAffiliation:
			e.setAttribute("affiliation","none");
			break;
		case Owner:
			e.setAttribute("affiliation","owner");
			break;
		case Admin:
			e.setAttribute("affiliation","admin");
			break;
		case Member:
			e.setAttribute("affiliation","member");
			break;
		case Outcast:
			e.setAttribute("affiliation","outcast");
			break;
		default:
			break;
		}
		
		switch (role) 
		{
		case NoRole:
			e.setAttribute("role","none");
			break;
		case Moderator:
			e.setAttribute("role","moderator");
			break;
		case Participant:
			e.setAttribute("role","participant");
			break;
		case Visitor:
			e.setAttribute("role","visitor");
			break;
		default:
			break;
		}
		return e;
	}
	
}
