Opensource Ajax web IM for XMPP/Jabber.

# Introduction #

> iJab is an Ajax-based web based instant messaging program for XMPP/Jabber. It is free software and users can run their own instant messaging without limitations or software cost, complimenting the XMPP philosophy of a distributed network. iJab is interpreted purely by the client webbrowser. It runs solely in a browser and does not require downloading or installation of additional software.


# Details #

Features

In-browser, non pop-up browser window
Sound
Multi User Chat
Vcard Search
Roster management
HTTP Binding thereby allowing near realtime messaging.

Proxying

Because of JavaScript's same origin policy a JavaScript program (such as iJab) can only communicate with the hosting webserver. All traffic must therefore be proxied by the webserver to the XMPP server. A webserver such as Apache can be configured to act as a reverse proxy. An alternative to this relaying of traffic is to use a minimal webserver in the XMPP server such as the module included in ejabberd.