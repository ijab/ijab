#How to install iJab using ejabberd

This tutorial is based on [guide file](http://www.ejabberd.im/jwchat-apache) on [ejabberd](http://www.ejabberd.im) and it will guide you through the installation of the web-based Jabber client iJab 0.1.0 using a pre-generated package.

At the end, you will have iJab running using ejabberd's internal HTTP Bind support.


## 1.Configure ejabberd ##

Make sure you have options similar to those on your ejabberd.cfg:
```
{host, "jabber.mycompany.com"}.

{listen,
  ...
  {5280, ejabberd_http,    [http_bind]}
```

Note:don't forget add mod\_httpd\_bind into modules section.
```
  {modules,
    ...
    {mod_http_bind, []}.
   }
```

With these options the URL for http-bind in your server will be:
http://jabber.mycompany.com:5280/http-bind/

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

Now you can copy the iJab directory to its final destination. On Linux it could be /var/www/iJab/ or /var/ejabberd-2.0.0/var/www according to which web server you choose.

## 4.Setup webserver ##

4.1 Using mod\_http\_fileserver modules in ejabberd

> ejabberd has an internal http module:mod\_http\_fileserver. You could config iJab
> there.

> First, config mod\_http\_fileserver module work in ejabberd.Something like this:
```
   {5280, ejabberd_http, [
                         http_poll,
                         http_bind,
                         web_admin,
                         {request_handlers, [
                            {["web"], mod_http_fileserver}
                         ]}
                         
    {mod_http_fileserver, [
                   {docroot, "/opt/ejabberd-2.0.0/var/www"},
                   {accesslog, "/opt/ejabberd-2.0.0/var/log/ejabberd/access.log"}
                  ]
```
> > Second, copy iJab to docroot of mod\_http\_fileserver


> At last, restart ejabberd. visit you web im page using http://jabber.mycompany.com:5280/web/iJab/iJab.html


4.2 Using Apache
> You have to setup your web server, for example Apache, so that it redirects requests from the http-bind URL to an HTTP-Bind capable Jabber server component.

Those instructions are for Apache 2.
### .htaccess ###

The easiest way to make redirection is creating a .htaccess file in the iJab directory with this content:
```
AddDefaultCharset UTF-8
Options +MultiViews
<IfModule mod_rewrite.c>
        RewriteEngine On
        RewriteRule http-bind/ http://jabber.mycompany.com:5280/http-bind/ [P]
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
  RewriteRule http-bind/ http://jabber.mycompany.com:5280/http-bind/ [P]
</VirtualHost>
```

Restart your web server so changes take effect:
apache2ctl restart

And now you can open your browser in the URL (maybe similar to http://www.mycompany.com/iJab/).