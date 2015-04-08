### Important #####

Some of the below contents is only for iJab v0.1.7 and older.

How to configure rewrite rules works for iJab v 1.0 and later.

######################

#How to install iJab with Openfire.

This tutorial is based on this [blog](http://chromus.kajigger.com/blog/2007/03/22/making-jwchat-work-with-openfire/) and it will guide you through the installation of the web-based Jabber client iJab 0.1.0 using a pre-generated package.

At the end, you will have iJab running using openfire's internal HTTP Bind support.

## 1.Configure openfire ##

> Enable the http-binding
    1. goto openfire admin console
> > 2) click on the "Server" tab
> > 3) at the left bar click over HTTPBinding
> > 4) enable http binding (caution: jetty uses by default the 8080 port, so probably its a good idea to choose another port i.e 8181)
With these options the URL for http-bind in your server will be:
http://jabber.mycompany.com:8181/http-bind/

## 2.Download iJab ##

Download a pre-generated package of [iJab](http://ijab.googlecode.com/files/iJab-0.1.zip) and unpack it.

## 3.Configure iJab ##

You could using iJab.html or just copy contents you needed to your own html page.
Config the variables according to your settings.
```
   var host = "samespace.anzsoft.com";
   var port = 5222;
   var domain = "anzsoft.com";
```
Copy iJab to www root directory.

Now you can copy the iJab directory to its final destination. On Linux it could be /var/www/iJab/  according to which web server you choose.

## 4.Setup webserver ##


> You have to setup your web server, for example Apache, so that it redirects requests from the http-bind URL to an HTTP-Bind capable Jabber server component.

Those instructions are for Apache 2.
### .htaccess ###

The easiest way to make redirection is creating a .htaccess file in the iJab directory with this content:
```
AddDefaultCharset UTF-8
Options +MultiViews
<IfModule mod_rewrite.c>
        RewriteEngine On
        RewriteRule http-bind/ http://jabber.mycompany.com:8181/http-bind/ [P]
</IfModule>
```
In your Apache configuration file (/etc/apache2/apache2.conf) you must enable .htaccess files and proxy options:
```
<Directory "/var/www/iJab">
        AllowOverride All
</Directory>
```
Finally, enable rewrite and proxy modules for Apache:
```
cd /etc/apache2
ln -s /etc/apache2/mods-available/rewrite.load /etc/apache2/mods-enabled/
ln -s /etc/apache2/mods-available/proxy.load /etc/apache2/mods-enabled/
ln -s /etc/apache2/mods-available/proxy.conf /etc/apache2/mods-enabled/
```
### virtual host ###

```
<VirtualHost *:8001>
  ServerName www.mycompany.com
  DocumentRoot /var/www/html
  <Directory /var/www/html/iJab>
    Options +Indexes +Multiviews
  </Directory>
  AddDefaultCharset UTF-8
  RewriteEngine on
  RewriteRule http-bind/ http://jabber.mycompany.com:8181/http-bind/ [P]
</VirtualHost>
```

Restart your web server so changes take effect:
apache2ctl restart

And now you can open your browser in the URL (maybe similar to http://www.mycompany.com/iJab/).

## Something About JsJac with openfire http-bind ##
> If it couldn't work, please search on openfire's forum.

  1. Christian Beckmann always got a "UserName or password error!" after install iJab. Using debug it found the problem was javax.security.sasl.SaslException: DIGEST-MD5: digest response format violation. Incompatible charset value: utf-8��  ".
> > http://www.igniterealtime.org/community/message/156357#185787 gives the info how to fix this problem.


> Welcome to share your experience configuring iJab.