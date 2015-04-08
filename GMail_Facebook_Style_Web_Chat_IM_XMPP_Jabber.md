GMail/Facebook style web chat bar for XMPP/Jabber.

# Introduction #

> iJabBar is a facebook style web chat bar base on XMPP/Jabber protocol which could be integrated into your website for letting your user chatting while online.

> You could learn more about iJabBar from [here](http://www.ijab.im/) (This site would be open later).

# How To #

  1. You should install a XMPP server with HTTP-BIND feature.

> 2. Deploy iJabBar to your web site.

> 3. Add the following codes to your pages for integrating iJabBar into your website.

> 

&lt;script type="text/javascript" language="javascript" src="http://example.com/ijabbar/ijabbar.nocache.js"&gt;



&lt;/script&gt;



> 

&lt;script&gt;


> <!-- ijab\_httpbind,ijab\_port,ijab\_host,ijab\_domain,ijab\_servertype,ijab\_debug -->
> var ijab\_httpbind = "http://xmpp.example.com:5280/http-bind/";
> var ijab\_port = 5222;
> var ijab\_domain = "example.com";
> var ijab\_servertype = "EJabberd";<!-- EJabberd,Openfire,Others-->
> var ijab\_rostermode = "Online";<!-- All,Online,None -->
> var ijab\_debug = false;
> var ijab\_enableLoginBox = true;
> var ijab\_autoLogin = false;
> var ijab\_auto\_user = "username";         <!-- for SSO login with your website -->
> var ijab\_auto\_password = "auth\_token";   <!-- for SSO login with your website -->
> var ijab\_anonymous\_prefix = "";<!-- empty or some username prefix -->
> var ijab\_reconnect\_count = 3;
> > 

&lt;/script&gt;




> 

&lt;script type="text/javascript"&gt;


> function testHandler()
> {
> > var handler =
> > {
> > > onBeforeLogin:function()
> > > {
> > > > alert("On Before login");

> > > },
> > > onEndLogin:function()
> > > {
> > > > alert("On end login");

> > > },
> > > onError:function(message)
> > > {
> > > > iJab.login('imdev','imdev631');

> > > },
> > > onLogout:function()
> > > {
> > > > alert("On logout");

> > > }

> > };
> > iJab.addHandler(handler);

> }
> 

&lt;/script&gt;

