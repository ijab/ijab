package com.anzsoft.client.ui;

import com.anzsoft.client.JabberApp;
import com.anzsoft.client.XMPP.XmppID;
import com.anzsoft.client.XMPP.XmppPacket;
import com.anzsoft.client.XMPP.XmppPacketListener;
import com.anzsoft.client.XMPP.XmppQuery;
import com.anzsoft.client.XMPP.mandioca.VCardListener;
import com.anzsoft.client.XMPP.mandioca.XmppVCard;
import com.anzsoft.client.XMPP.mandioca.XmppVCardFactory;
import com.anzsoft.client.XMPP.mandioca.XmppVCard.Address;
import com.anzsoft.client.XMPP.mandioca.XmppVCard.Email;
import com.anzsoft.client.XMPP.mandioca.XmppVCard.Org;
import com.anzsoft.client.XMPP.mandioca.XmppVCard.Phone;
import com.anzsoft.client.utils.TextUtils;
import com.extjs.gxt.ui.client.GXT;
import com.extjs.gxt.ui.client.Style.HorizontalAlignment;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.widget.Dialog;
import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.extjs.gxt.ui.client.widget.MessageBox;
import com.extjs.gxt.ui.client.widget.TabItem;
import com.extjs.gxt.ui.client.widget.TabPanel;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.button.ButtonBar;
import com.extjs.gxt.ui.client.widget.form.AdapterField;
import com.extjs.gxt.ui.client.widget.form.FormPanel;
import com.extjs.gxt.ui.client.widget.form.TextArea;
import com.extjs.gxt.ui.client.widget.form.TextField;
import com.extjs.gxt.ui.client.widget.layout.CenterLayout;
import com.extjs.gxt.ui.client.widget.layout.ColumnData;
import com.extjs.gxt.ui.client.widget.layout.ColumnLayout;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.extjs.gxt.ui.client.widget.layout.FormLayout;
import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.ImageElement;
import com.google.gwt.user.client.ui.Image;

public class InfoDialog extends Dialog
{
	//General panel
	private TextField<String> fullNameField;
	private TextField<String> nickNameField;
	private TextField<String> birthdayField;
	private TextField<String> phoneField;
	private TextField<String> homepageField;
	private TextField<String> emailField;
	private Image avatarImg;
	
	//Work panel
	private TextField<String> companyField;
	private TextField<String> departmentField;
	private TextField<String> positionField;
	private TextField<String> roleField;
	
	//Location panel
	private TextField<String> streetField;
	private TextField<String> cityField;
	private TextField<String> stateField;
	private TextField<String> pCodeField;
	private TextField<String> countryField;
	
	//About panel
	private TextArea aboutTextArea;  
	
	private Button publishButton;
	private XmppVCard vcard;
	public InfoDialog()
	{
		initUI();
	}
	
	private void initUI()
	{
		setButtons("");
		setHeading(JabberApp.getConstants().userInfo());
		setIconStyle("userInfo-Icon");
		setModal(false);
		setBodyBorder(true);
		//setInsetBorder(true);
		setBodyStyle("padding: 4px;background: none");
		setWidth(500);
		setHeight(300);
		setResizable(false);
		setClosable(true);
		setCollapsible(false);
		
		setLayout(new FitLayout());
		
		TabPanel panel = new TabPanel();  
	    panel.setPlain(true);
	    panel.setWidth("490");
	    
	    TabItem general = new TabItem(JabberApp.getConstants().General()); 
	    general.setLayout(new FitLayout());
	    general.add(createGeneralPanel());
	    panel.add(general);
	    
	    TabItem work = new TabItem(JabberApp.getConstants().Work()); 
	    work.setLayout(new FitLayout());
	    work.add(createWorkPanel());
	    panel.add(work);
	    
	    TabItem location = new TabItem(JabberApp.getConstants().Location()); 
	    location.setLayout(new FitLayout());
	    location.add(createLocationPanel());
	    panel.add(location);
	    
	    TabItem about = new TabItem(JabberApp.getConstants().About()); 
	    about.setLayout(new FitLayout());
	    about.add(createAboutPanel());
	    panel.add(about);
	    
	    add(panel);
	    
	    ButtonBar buttonBar = new ButtonBar();
	    Button closeButton = new Button(JabberApp.getConstants().close());
	    closeButton.addSelectionListener(new SelectionListener<ButtonEvent>()
	    {
			public void componentSelected(ButtonEvent ce)
			{
				close();
			}
	    	
	    });
	    
	    publishButton = new Button(JabberApp.getConstants().Publish());
	    publishButton.addSelectionListener(new SelectionListener<ButtonEvent>()
	    {
			public void componentSelected(ButtonEvent ce) 
			{
				publish();
			}
	    	
	    });
	    publishButton.hide();
		publishButton.setEnabled(false);
		buttonBar.add(publishButton);
	    buttonBar.add(closeButton);
	    setButtonBar(buttonBar);
	}
	
	/**
	 * 
	 */
	private void publish()
	{
		publishButton.setEnabled(false);
		final XmppVCard v = makeVCard();
		XmppQuery iq = JabberApp.instance().getSession().getFactory().createQuery();
		iq.setIQ("", XmppQuery.TYPE_SET, TextUtils.genUniqueId());
		Element vcardEl = v.toXml(iq.getDoc());
		iq.getNode().appendChild(vcardEl);
		
		JabberApp.instance().getSession().send(iq, new XmppPacketListener()
		{
			public void onPacketReceived(XmppPacket packet) 
			{
				publishButton.setEnabled(true);
				if(packet.getType().equals("result"))
				{
					XmppVCardFactory.instance().set(JabberApp.instance().getJid(), v);
					MessageBox.alert(JabberApp.getConstants().Success(), JabberApp.getConstants().VCard_Success_Prompt(),null);
				}
				else
				{
					MessageBox.alert(JabberApp.getConstants().Failed(), JabberApp.getConstants().VCard_Failed_Prompt(),null);
				}
			}

			public void onPacketSent(XmppPacket packet) 
			{
			}
			
		});
	}
	
	private FormPanel createAboutPanel()
	{
		final FormPanel panel = new FormPanel();
		panel.setFrame(false);
		panel.setBodyBorder(false);
		panel.setHeaderVisible(false);
		panel.setButtonAlign(HorizontalAlignment.CENTER);
		panel.setLayout(new FitLayout());
		
		aboutTextArea = new TextArea();
		aboutTextArea.setHideLabel(true);
		panel.add(aboutTextArea);
		
		return panel;
	}
	
	private FormPanel createLocationPanel()
	{
		final FormPanel panel = new FormPanel();
		panel.setFrame(false);
		panel.setBodyBorder(false);
		panel.setHeaderVisible(false);
		panel.setButtonAlign(HorizontalAlignment.CENTER);
		
		streetField = new TextField<String>();
		streetField.setFieldLabel(JabberApp.getConstants().Street());
		panel.add(streetField);
		
		cityField = new TextField<String>();
		cityField.setFieldLabel(JabberApp.getConstants().City());
		panel.add(cityField);
		
		stateField = new TextField<String>();
		stateField.setFieldLabel(JabberApp.getConstants().State());
		panel.add(stateField);
		
		pCodeField = new TextField<String>();
		pCodeField.setFieldLabel(JabberApp.getConstants().Postal_Code());
		panel.add(pCodeField);
		
		countryField = new TextField<String>();
		countryField.setFieldLabel(JabberApp.getConstants().Country());
		panel.add(countryField);
		return panel;
	}
	
	private FormPanel createWorkPanel()
	{
		final FormPanel panel = new FormPanel();
		panel.setFrame(false);
		panel.setBodyBorder(false);
		panel.setHeaderVisible(false);
		panel.setButtonAlign(HorizontalAlignment.CENTER);
		
		companyField = new TextField<String>();
		companyField.setFieldLabel(JabberApp.getConstants().Company());
		panel.add(companyField);
		
		departmentField = new TextField<String>();
		departmentField.setFieldLabel(JabberApp.getConstants().Department());
		panel.add(departmentField);
		
		positionField = new TextField<String>();
		positionField.setFieldLabel(JabberApp.getConstants().Position());
		panel.add(positionField);
		
		roleField = new TextField<String>();
		roleField.setFieldLabel(JabberApp.getConstants().Role());
		panel.add(roleField);
		
		return panel;
	}
	
	private FormPanel createGeneralPanel()
	{
		final FormPanel panel = new FormPanel();
		panel.setFrame(false);
		panel.setBodyBorder(false);
		panel.setHeaderVisible(false);
		panel.setButtonAlign(HorizontalAlignment.CENTER);  
	    panel.setLayout(new FitLayout());
		
		LayoutContainer main = new LayoutContainer();  
	    main.setLayout(new ColumnLayout());  
	  
	    LayoutContainer left = new LayoutContainer();  
	  
	    FormLayout layout = new FormLayout();  
	    left.setLayout(layout);  
		
		fullNameField = new TextField<String>();
		fullNameField.setFieldLabel(JabberApp.getConstants().Full_Name());
		left.add(fullNameField);
		
		nickNameField = new TextField<String>();
		nickNameField.setFieldLabel(JabberApp.getConstants().Nickname());
		left.add(nickNameField);
		
		birthdayField = new TextField<String>();
		birthdayField.setFieldLabel(JabberApp.getConstants().Birthday());
		left.add(birthdayField);
		
		phoneField = new TextField<String>();
		phoneField.setFieldLabel(JabberApp.getConstants().Phone());
		left.add(phoneField);
		
		homepageField = new TextField<String>();
		homepageField.setFieldLabel(JabberApp.getConstants().Homepage());
		left.add(homepageField);
		
		emailField = new TextField<String>();
		emailField.setFieldLabel(JabberApp.getConstants().E_Mail());
		left.add(emailField);
		
		LayoutContainer right = new LayoutContainer();  

		//layout = new FormLayout();  
		right.setLayout(new CenterLayout()); 
		
		avatarImg = new Image("images/default_avatar.png");
		avatarImg.setWidth("64px");
		avatarImg.setHeight("64px");
		
		right.add(new AdapterField(avatarImg));
		
		main.add(left, new ColumnData(.7));  
	    main.add(right, new ColumnData(.3));  
	    panel.add(main);
		return panel;
	}
	
	public void getInfo(final XmppID jid)
	{
		setHeading(jid.toStringNoResource());
		XmppVCard vcard = XmppVCardFactory.instance().get(jid, new VCardListener()
		{
			public void onVCard(final XmppID id,final XmppVCard card) 
			{
				setData(card);
			}
			
		});
		if(vcard != null)
		{
			setData(vcard);
		}
		if(jid.toStringNoResource().equals(JabberApp.instance().getJid().toStringNoResource()))
		{
			publishButton.show();
			publishButton.setEnabled(true);
		}
		else
		{
			publishButton.hide();
			publishButton.setEnabled(false);
		}
	}
	
	private void setData (final XmppVCard vcard)
	{
		this.vcard = vcard;
		fullNameField.setValue(vcard.fullName());
		nickNameField.setValue(vcard.nickName());
		birthdayField.setValue(vcard.bday());
		if(vcard.phoneList.size() > 0)
			phoneField.setValue(vcard.phoneList.get(0).number);
		homepageField.setValue(vcard.url());
		if(vcard.emailList.size() > 0)
			emailField.setValue(vcard.emailList.get(0).userid);
		if(!vcard.photo().isEmpty()&&!GXT.isIE)
		{
			ImageElement imgEl = avatarImg.getElement().cast();
			String photoData = vcard.photo();
			imgEl.removeAttribute("src");
			imgEl.setSrc("data:image;base64,"+photoData);	
		}
		else
			avatarImg.setUrl("images/default_avatar.png");
		
		if(vcard.addressList.size()>0)
		{
			Address address = vcard.addressList.get(0);
			streetField.setValue(address.street);
			stateField.setValue(address.region);
			pCodeField.setValue(address.pcode);
			countryField.setValue(address.country);
		}
		companyField.setValue(vcard.org().name);

		if(vcard.org().unit.size()>0)
		{
			String unit = vcard.org().unit.get(0);
			departmentField.setValue(unit);
		}
		roleField.setValue(vcard.role());
		positionField.setValue(vcard.title());
		
		aboutTextArea.setValue(vcard.desc());
		
		if(!vcard.nickName().isEmpty())
			setHeading(vcard.nickName());
		else if(!vcard.fullName().isEmpty())
			setHeading(vcard.fullName());
	}
	
	public void clear()
	{
		fullNameField.setValue("");
		nickNameField.setValue("");
		birthdayField.setValue("");
		phoneField.setValue("");
		homepageField.setValue("");
		emailField.setValue("");
				
		//Work panel
		companyField.setValue("");
		departmentField.setValue("");
		positionField.setValue("");
		roleField.setValue("");

		//Location panel
		streetField.setValue("");
		cityField.setValue("");
		stateField.setValue("");
		stateField.setValue("");
		pCodeField.setValue("");
		countryField.setValue("");
		
		//About panel
		aboutTextArea.setValue("");
		
		setHeading("User Info");
		avatarImg.getElement().removeAttribute("src");
		avatarImg.setUrl("images/default_avatar.png");
	}
	
	private void setEditEnabled(boolean enabled)
	{
		fullNameField.setEnabled(enabled);
		nickNameField.setEnabled(enabled);
		birthdayField.setEnabled(enabled);
		phoneField.setEnabled(enabled);
		homepageField.setEnabled(enabled);
		emailField.setEnabled(enabled);
				
		//Work panel
		companyField.setEnabled(enabled);
		departmentField.setEnabled(enabled);
		positionField.setEnabled(enabled);
		roleField.setEnabled(enabled);

		//Location panel
		streetField.setEnabled(enabled);
		cityField.setEnabled(enabled);
		stateField.setEnabled(enabled);
		pCodeField.setEnabled(enabled);
		countryField.setEnabled(enabled);
		
		//About panel
		aboutTextArea.setEnabled(enabled);
	}
	
	private XmppVCard makeVCard()
	{
		XmppVCard v = new XmppVCard();
		v.setFullName(fullNameField.getValue());
		v.setNickName(nickNameField.getValue());
		v.setBday(birthdayField.getValue());
		
		if(this.vcard != null&&this.vcard.photo()!=null&&!this.vcard.photo().isEmpty())
		{
			v.setPhoto(this.vcard.photo());
		}
		
		if(emailField.getValue()!=null&&!emailField.getValue().isEmpty())
		{
			Email email = v.newEmail();
			email.internet = true;
			email.userid = emailField.getValue();
			
			v.emailList.add(email);
		}
		
		v.setUrl(homepageField.getValue());
		
		if(phoneField.getValue()!=null&&!phoneField.getValue().isEmpty())
		{
			Phone p = v.newPhone();
			p.home = true;
			p.voice = true;
			p.number = phoneField.getValue();
			
			v.phoneList.add(p);
		}
		
		if((streetField.getValue()!=null&&!streetField.getValue().isEmpty())||
				(cityField.getValue()!=null&&!cityField.getValue().isEmpty())||
				(stateField.getValue()!=null&&!stateField.getValue().isEmpty())||
				(pCodeField.getValue()!=null&&!pCodeField.getValue().isEmpty())||
				(countryField.getValue()!=null&&!countryField.getValue().isEmpty()))
		{
			Address addr = v.newAddress();
			addr.home = true;
			addr.street = streetField.getValue();
			addr.locality = cityField.getValue();
			addr.region = stateField.getValue();
			addr.pcode = pCodeField.getValue();
			addr.country = countryField.getValue();
			
			v.addressList.add(addr);
		}
		
		Org org = v.newOrg();
		org.name = companyField.getValue();
		
		if(departmentField.getValue()!=null&&!departmentField.getValue().isEmpty())
		{
			org.unit.add(departmentField.getValue());
		}
		v.setOrg(org);
		
		
		v.setTitle(positionField.getValue());
		v.setRole(roleField.getValue());
		v.setDesc(aboutTextArea.getValue());
		return v;
	}
}
