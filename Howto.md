# A quickstart for configuring iJab in your own web site

# Introduction #

This document helps you to embed iJab into your own html page.
To use this, you should have a http-bind supported jabber server and rewrite supported web server running.

# How-to #

You need to do something in your html page:
  * Declare the locale to decide which language you want to use.(We only support zh and en for now)
```
      <meta name="gwt:property" content="locale=zh"> 
      or 
     <meta name="gwt:property" content="locale=en">
```

  * Include the script in your head section.
```
      <script type="text/javascript" language="javascript" src="com.anzsoft.iJab.nocache.js"></script>
     <script type="text/javascript" src="jsjac.js">
```

  * Declare the variables about your xmpp server.
```
        var httpBind = "/http-bind/";
	var host = "samespace.anzsoft.com";
	var port = 5222;
	var domain = "anzsoft.com";
	var authType = "sasl";<!-- sasl,nosasl-->
```

There is a sample html file to show you how it work.
```
<html>
  <head>
    <meta http-equiv="content-type" content="text/html; charset=UTF-8">
    <meta name="gwt:property" content="locale=zh">

     <title>iJab</title>
     <script type="text/javascript" language="javascript" src="com.anzsoft.iJab.nocache.js"></script>
     <script type="text/javascript" src="jsjac.js">

     <script>
    function login()
    {
    	doJabberLogin();
    } 
    
      </head>

      <body style="overflow: hidden" onload=login()">

	<script>
	var httpBind = "/http-bind/";
	var host = "samespace.anzsoft.com";
	var port = 5222;
	var domain = "anzsoft.com";
	var authType = "sasl";<!-- sasl,nosasl-->

	</script>
    <!-- OPTIONAL: include this if you want history support -->
    <iframe src="javascript:''" id="__gwt_historyFrame" tabIndex='-1' style="position:absolute;width:0;height:0;border:0">
    </iframe>
   </body>
</html>
```

  * Notice:
> > using body onload event would not work under IE, you could try this:
```
   function login()
    {
    	
    	doJabberLogin();
    }
    function wait_for_script_load(look_for, callback) {
          var interval = setInterval(function() {
          if (eval("typeof " + look_for) != 'undefined') {
            clearInterval(interval);
            callback();
          }
        }, 50);
    }
    function onloadInit()
    {
        wait_for_script_load( 'doJabberLogin', login );
    } 
    </script>
  </head>

  <body style="overflow: hidden" onload="onloadInit()">
```

iJab provide some function you can use within your web site, please see the API document.

Any quesionts: contact me by zhancaibaoATgmail.com or zhongfanglin by zhongfanglin ATgmail.com