package com.anzsoft.client.XMPP.mandioca;

import java.util.ArrayList;
import java.util.List;

import com.anzsoft.client.utils.XMLHelper;
import com.google.gwt.xml.client.Document;
import com.google.gwt.xml.client.Element;
import com.google.gwt.xml.client.Node;
import com.google.gwt.xml.client.NodeList;
import com.google.gwt.xml.client.XMLParser;

public class XmppVCard 
{
	public class Address 
	{
		public boolean home;
		public boolean work;
		public boolean postal;
		public boolean parcel;

		public boolean dom;
		public boolean intl;

		public boolean pref;

		public String pobox;
		public String extaddr;
		public String street;
		public String locality;
		public String region;
		public String pcode;
		public String country;
		public Address()
		{
		}
	}
	
	public class Label 
	{
		public boolean home;
		public boolean work;
		public boolean postal;
		public boolean parcel;

		public boolean dom;
		public boolean intl;

		public boolean pref;

		public List<String> lines = new ArrayList<String>();
		public Label()
		{
		}
	}
	
	public class Phone 
	{
		public boolean home;
		public boolean work;
		public boolean voice;
		public boolean fax;
		public boolean pager;
		public boolean msg;
		public boolean cell;
		public boolean video;
		public boolean bbs;
		public boolean modem;
		public boolean isdn;
		public boolean pcs;
		public boolean pref;

		public String number;
		public Phone()
		{
		}
	};
	
	public class Email 
	{
		public boolean home;
		public boolean work;
		public boolean internet;
		public boolean x400;

		public String userid;
		public Email()
		{
		}
	}
	
	public class Geo 
	{
		public String lat;
		public String lon;
		public Geo()
		{
		}			
	}
	
	public class Org 
	{

		public String name;
		public List<String> unit = new ArrayList<String>();
		public Org()
		{
		}
	}
		
	private String version;
	private String fullName;
	private String nickName;
	private String photo;
	private String bday;
	private String jid;
	private String mailer;
	private String timezone;
	private String title;
	private String role;
	private String logo;
	private String logoURI;
	private String prodId;
	private String rev;
	private String sortString;
	private String uid;
	private String url;
	private String desc;
	private String note;
	
	private Geo geo = new Geo();
	private Org org = new Org();
	
	
	public List<Address> addressList = new ArrayList<Address>();
	public List<Label> labelList = new ArrayList<Label>();
	public List<Phone> phoneList = new ArrayList<Phone>();
	public List<Email> emailList = new ArrayList<Email>();
	public XmppVCard()
	{
		
	}
	
	public boolean formXml(final String xml)
	{
		Document doc = XMLParser.parse(xml);
		Element vcardElement = XMLHelper.findSubTag(doc.getDocumentElement(), "vCard");
		if(vcardElement == null)
			return false;
		
		NodeList childs = vcardElement.getChildNodes();
		for(int index = 0;index<childs.getLength();index++)
		{
			Node child = childs.item(index);
			String tagName = child.getNodeName().toUpperCase();
			String value = XMLHelper.getTagContent((Element) child); 
			if(tagName.equals("VERSION"))
				version = value.trim();
			else if(tagName.equals("FN"))
				fullName = value.trim();
			else if(tagName.equals("NICKNAME"))
				nickName = value.trim();		
			else if(tagName.equals("PHOTO"))
				photo = XMLHelper.subTagText((Element) child, "BINVAL");
			else if(tagName.equals("BDAY"))
				bday = value.trim();
			else if(tagName.equals("ADR"))
			{
				Address a = new Address();
				
				a.home = XMLHelper.hasSubTag((Element) child, "HOME");
				a.work = XMLHelper.hasSubTag((Element) child, "WORK");
				a.postal = XMLHelper.hasSubTag((Element) child, "POSTAL");
				a.parcel = XMLHelper.hasSubTag((Element) child, "PARCEL");
				a.dom    = XMLHelper.hasSubTag((Element) child, "DOM");
				a.intl   = XMLHelper.hasSubTag((Element) child, "INTL");
				a.pref   = XMLHelper.hasSubTag((Element) child,"PREF");
				
				a.pobox    = XMLHelper.subTagText((Element) child, "POBOX");
				a.extaddr  = XMLHelper.subTagText((Element) child, "EXTADR");
				a.street   = XMLHelper.subTagText((Element) child, "STREET");
				a.locality = XMLHelper.subTagText((Element) child, "LOCALITY");
				a.region   = XMLHelper.subTagText((Element) child, "REGION");
				a.pcode    = XMLHelper.subTagText((Element) child, "PCODE");
				a.country  = XMLHelper.subTagText((Element) child, "CTRY");
				
				if(a.country.isEmpty())
				{
					if(XMLHelper.hasSubTag((Element) child, "COUNTRY"))
						a.country = XMLHelper.subTagText((Element) child, "COUNTRY");
				}
				
				if(a.extaddr.isEmpty())
				{
					if(XMLHelper.hasSubTag((Element) child, "EXTADD"))
						a.extaddr = XMLHelper.subTagText((Element) child, "EXTADD");
				}
				
				addressList.add(a);
			}
			else if(tagName.equals("LABEL"))
			{
				Label l = new Label();
				l.home   = XMLHelper.hasSubTag((Element) child, "HOME");
				l.work   = XMLHelper.hasSubTag((Element) child, "WORK");
				l.postal = XMLHelper.hasSubTag((Element) child, "POSTAL");
				l.parcel = XMLHelper.hasSubTag((Element) child, "PARCEL");
				l.dom    = XMLHelper.hasSubTag((Element) child, "DOM");
				l.intl   = XMLHelper.hasSubTag((Element) child, "INTL");
				l.pref   = XMLHelper.hasSubTag((Element) child, "PREF");
				
				NodeList lines = child.getChildNodes();
				for(int i = 0;i<lines.getLength();i++)
				{
					Element line = (Element) lines.item(i);
					if(line.getTagName().toUpperCase().equals("LINE"))
					{
						if(!XMLHelper.getTagContent(line).isEmpty())
							l.lines.add(XMLHelper.getTagContent(line));
					}
				}
				labelList.add(l);
			}
			else if(tagName.equals("TEL"))
			{
				Phone p = new Phone();
				
				p.home  = XMLHelper.hasSubTag((Element) child, "HOME");
				p.work  = XMLHelper.hasSubTag((Element) child, "WORK");
				p.voice = XMLHelper.hasSubTag((Element) child, "VOICE");
				p.fax   = XMLHelper.hasSubTag((Element) child, "FAX");
				p.pager = XMLHelper.hasSubTag((Element) child, "PAGER");
				p.msg   = XMLHelper.hasSubTag((Element) child, "MSG");
				p.cell  = XMLHelper.hasSubTag((Element) child, "CELL");
				p.video = XMLHelper.hasSubTag((Element) child, "VIDEO");
				p.bbs   = XMLHelper.hasSubTag((Element) child, "BBS");
				p.modem = XMLHelper.hasSubTag((Element) child, "MODEM");
				p.isdn  = XMLHelper.hasSubTag((Element) child, "ISDN");
				p.pcs   = XMLHelper.hasSubTag((Element) child, "PCS");
				p.pref  = XMLHelper.hasSubTag((Element) child, "PREF");
				
				p.number = XMLHelper.subTagText((Element) child, "NUMBER");
				if(p.number.isEmpty())
				{
					if(XMLHelper.hasSubTag((Element) child, "VOICE"))
						p.number = XMLHelper.subTagText((Element) child, "VOICE");
				}
				phoneList.add(p);
			}
			else if(tagName.equals("EMAIL"))
			{
				Email m = new Email();

				m.home = XMLHelper.hasSubTag((Element) child, "HOME");
				m.work = XMLHelper.hasSubTag((Element) child, "WORK");
				m.internet = XMLHelper.hasSubTag((Element) child, "INTERNET");
				m.x400 = XMLHelper.hasSubTag((Element) child, "X400");

				m.userid = XMLHelper.subTagText((Element) child, "USERID");

				if (m.userid.isEmpty())
				{
					if(!XMLHelper.getTagContent((Element)child).isEmpty())
					{
						m.userid = XMLHelper.getTagContent((Element)child);
					}
				}
				emailList.add(m);
			}
			else if(tagName.equals("JABBERID"))
				jid = value.trim();
			else if(tagName.equals("MAILER"))
				mailer = value.trim();
			else if(tagName.equals("TZ"))
				timezone = value.trim();
			else if(tagName.equals("GEO"))
			{
				geo.lat = XMLHelper.subTagText((Element) child, "LAT");
				geo.lon = XMLHelper.subTagText((Element) child, "LON");
			}
			else if(tagName.equals("TITLE"))
				title = value.trim();
			else if(tagName.equals("ROLE"))
				role = value.trim();
			else if(tagName.equals("LOGO"))
			{
				logo = XMLHelper.subTagText((Element)child, "BINVAL");
				logoURI = XMLHelper.subTagText((Element)child, "EXTVAL");
			}
			else if(tagName.equals("AGENT"))
			{
				
			}
			else if(tagName.equals("ORG"))
			{
				org.name = XMLHelper.subTagText((Element)child, "ORGNAME");
				NodeList units = child.getChildNodes();
				for(int iunit = 0;iunit<units.getLength();iunit++)
				{
					Element unit = (Element) units.item(iunit);
					if(!XMLHelper.getTagContent(unit).isEmpty())
						org.unit.add(XMLHelper.getTagContent(unit));
				}
			}
			else if(tagName.equals("NOTE"))
				note = value.trim();
			else if(tagName.equals("PRODID"))
				prodId = value.trim();
			else if(tagName.equals("REV"))
				rev = value.trim();
			else if(tagName.equals("SORT-STRING"))
				sortString = value.trim();
			else if(tagName.equals("SOUND"))
			{
				
			}
			else if(tagName.equals("UID"))
				uid = value.trim();
			else if(tagName.equals("URL"))
				url = value.trim();
			else if(tagName.equals("DESC"))
				desc = value.trim();
			else if(tagName.equals("CLASS"))
			{
				
			}
			else if(tagName.equals("CLASS"))
			{
				
			}
		}
		return true;
	}
	
	
	// PUBLIC method
	public final String version()
	{
		return this.version;
	}
	
	public void setVersion(final String version)
	{
		this.version = version;
	}
	
	public final String fullName()
	{
		return this.fullName;
	}
	
	public void setFullName(final String fn)
	{
		this.fullName = fn;
	}
	
	public final String nickName()
	{
		return this.nickName;
	}
	
	public void setNickName(final String nick)
	{
		this.nickName = nick;
	}
	
	public final String photo()
	{
		return this.photo;
	}
	
	public final String bday()
	{
		return this.bday;
	}
	
	public void setBday(final String bd)
	{
		this.bday = bd;
	}
	
	public final String jid()
	{
		return this.jid;
	}
	
	public void setJid(final String j)
	{
		this.jid = j;
	}
	
	public final String mailer()
	{
		return this.mailer;
	}
	
	public void setMailer(final String mail)
	{
		this.mailer = mail;
	}
	
	public final String timezone()
	{
		return this.timezone;
	}
	
	public void setTimezone(final String zone)
	{
		this.timezone = zone;
	}
	
	public final Geo geo()
	{
		return this.geo;
	}
	
	public void setGeo(final Geo g)
	{
		this.geo.lat = g.lat;
		this.geo.lon = g.lon;
	}
	
	public final String title()
	{
		return this.title;
	}
	
	public void setTitle(final String title)
	{
		this.title = title;
	}
	
	public final String role()
	{
		return this.role;
	}
	
	public void setRole(final String role)
	{
		this.role = role;
	}
	
	public final String logo()
	{
		return this.logo;
	}
	
	public void setLogo(final String logo)
	{
		this.logo = logo;
	}
	
	public final String logoURI()
	{
		return this.logoURI;
	}
	
	public void setLogoURI(final String uri)
	{
		this.logoURI = uri;
	}
	
	public final Org org()
	{
		return this.org;
	}
	
	public void setOrg(final Org org)
	{
		this.org.name = org.name;
		this.org.unit.clear();
		this.org.unit.addAll(org.unit);
	}
	
	public final String note()
	{
		return this.note;
	}
	
	public void setNote(final String n)
	{
		this.note = n;
	}
	
	public final String prodId()
	{
		return this.prodId;
	}
	
	public void setProdId(final String id)
	{
		this.prodId = id;
	}
	
	public final String rev()
	{
		return this.rev;
	}
	
	public void setRev(final String r)
	{
		this.rev = r;
	}
	
	public final String sortString()
	{
		return this.sortString;
	}
	
	public void setSortString(final String str)
	{
		this.sortString = str;
	}
	
	public final String uid()
	{
		return this.uid;
	}
	
	public void setUid(final String u)
	{
		this.uid = u;
	}
	
	public final String url()
	{
		return this.url;
	}
	
	public void setUrl(final String u)
	{
		this.url = u;
	}
	
	public final String desc()
	{
		return this.desc;
	}
	
	public void setDesc(final String d)
	{
		this.desc = d;
	}
}
