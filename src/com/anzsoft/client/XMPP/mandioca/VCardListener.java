package com.anzsoft.client.XMPP.mandioca;

import com.anzsoft.client.XMPP.XmppID;

public interface VCardListener 
{
	void onVCard(final XmppID jid,final XmppVCard vcard);
}
