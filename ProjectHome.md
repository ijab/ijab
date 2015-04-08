# An ajax web jabber client #
[![](https://www.paypal.com/en_US/i/btn/btn_donateCC_LG.gif)](https://www.paypal.com/cgi-bin/webscr?cmd=_donations&business=3VPPFVMPRKZSE&lc=C2&item_name=ijab&currency_code=USD&bn=PP%2dDonationsBF%3abtn_donateCC_LG%2egif%3aNonHosted)



# Go to http://www.ijab.im/ for experiencing new iJab #
## iJab V1.0-beat3-2 Relesed ##

  1. Fixed some bugs with Tigase server
  1. Fixed 'refuse to set unsafe hearder' error under Webkit based browers
  1. maybe fixed resume connection bug

## iJab V1.0-beat3-1 Relesed ##

  1. Fixed some style bugs, long text wrap problem, transport menu layout and etc.
  1. Fixed onAvatarClick even open chat window bug
  1. Fixed some tooltip inconsitency bug
  1. Fixed can't open MUC window bug while no room there
  1. Fixed freezing bug(maybe)
  1. Some other improvements.

## iJab V1.0-beat3 Relesed ##

> Go to http://www.ijab.im for demo or visit [Demo](http://samespace.anzsoft.com/webim/ijab_bar/iJab.html) directly.

> iJab V1.0 beta-3 Changes:
    1. Support roster management
    1. Support vcard-search
    1. Support MUC
    1. Support register transport such as MSN, AOL transports
    1. Support config iJab not to load roster
    1. Support config to load roster delayed
    1. Support Black-List(XEP-0016)
    1. Support server-side chat history(XEP-136)
    1. Some bugs fixed


## iJab V1.0-beat2 Relesed ##

> Go to http://www.ijab.im for demo or visit [Demo](http://samespace.anzsoft.com/webim/ijab_bar/iJab.html) directly.

> iJab V1.0 beta-2 Changes:
    1. Add I18N support
    1. Add 'host' config in iJab's config, you could use third-party http-bind server
    1. iJab's bar could be collapsed or uncollapsed
    1. Add login window, you could enable it or not
    1. Fixed bug with openfire
    1. Add many options in iJab's config:
      * enable\_talkto\_stranger:true,   // Use could talk to user not in his roster or not, default true
      * expand\_bar\_default:false,      // Expand bar or not while initializing, defualt false
      * enable\_login\_dialog:true,      // Enable opening login dialog while clicking Chat block when log off, default true
      * hide\_online\_group:false,       // Hide online group or not, default false
      * disable\_option\_setting:false,  // Hide option block or not, default false
      * disable\_msg\_browser\_prompt:false, // Disalbe browser title notification while receiving message, default false
      * disable\_toolbox:false,            // Hide Tools box or not, default false
    1. Change user\_cookie\_field to username\_cookie\_field
    1. Change password\_cookie\_field to token\_cookie\_field

## iJab V1.0-beta Released ##
> in V1.0 we try to merge iJab and iJabbar into one application and you could see this in next version, maybe v1.1, soon.

> Try the new version here: http://www.ijab.im  Please register and login for experiencing the new features.

> in V1.0 version, changes:

  1. Redesign iJab codes.
  1. Merge chat bar, stand-alone and live-support three chat modes into one application.
  1. Redesign iJab styles and support theme.
  1. Support sound and user could enable/disable it easily.
  1. User could set his status and status message.
  1. Support receiving message notification. iJab would set window title while window is not the focus.
  1. Support pop-up tip notification of receiving message number.
  1. Scroll left and right easily while open too many chat windows.
  1. Toolbox and shortcut could be customized.
  1. Support recent chat message stored locally.
  1. Would restore chat status while refreshing page.
  1. Support POST and GET method.
  1. Support user avator within your website by config avator\_url parameter.
  1. Support user search.
  1. Typing notification.

Notes: This version can't work very well under IE6.


## Facebook style XMPP Chat - iJabBar ##
> iJabBar is a facebook style XMPP web chat.Chat window of iJabBar is very similar with gmail chat.[here is the Demo](http://samespace.anzsoft.com/webim/ijab_bar/iJab.html), you could login using anonymous.

> We're merging iJab and iJabBar and many people ask me to release iJabBar. Now iJabBar files were uploaded here. Go to Downloads and Source to check.

> iJabBar source files are here. http://code.google.com/p/ijab/source/browse/ Please use svn to get it.

> Go to Wiki pages for get to know how to integrate iJabBar into your own website.

> I hope next version iJab would be much more better.

> iJab has opened a testing website http://www.ijab.im

## Next release version of iJab ##
> iJab team is planning next version of iJab and features are being discussed!
> Welcome suggestions from you!

## iJab 0.1.7 released ##
> Changes:
  1. asic muc chat support

> [download iJab 0.1.7](http://ijab.googlecode.com/files/iJab-0.1.7.zip)

## iJab 0.1.6 released ##
> Changes:
  1. card support
  1. card search support

> [download iJab 0.1.6](http://ijab.googlecode.com/files/iJab-0.1.6.zip)

> MUC support has been delayed.

## iJab 0.1.5 released ##
> iJab team is glad to announce releasing version 0.1.5.
> Changes:
    1. support roster management
> > 2. some bug fix and clean.

> Next version would support MUC.

## iJab ##
> iJab is a web-based Jabber client. It is completly written in Javascript, and uses Ajax technology, such as gwt and jsJac xmpp library.

iJab provides basic instant messenging. You just have to use a compatible web browser (see [Wiki](http://code.google.com/p/ijab/w/list)), no need to install anything in your computer, for using iJab. It's comparable to [JWChat](http://blog.jwchat.org/jwchat/), but all windows stays inside one web page and don't need to worry about two many poped-up windows. You could configure iJab by your favorite themes for iJab is wholly based on CSS design.

## Features ##
Feautres list
  * Instant Messages and 1:1 Chats
  * Jabber presence types with support for away messages
  * Support for transport/gateways to other IM services like ICQ, AIM, MSN, etc.
  * Server side storage of preferences
  * Emotions
  * ...(It's just a 0.1.0 release and some other features would be implemented in later release version)

## Demo & Screenshots ##
See our [demo](http://samespace.anzsoft.com/webim/iJab/iJab.html)(our demo server support annoymous login) or a screenshots of what we've done:

<div>
Login Window<br />
<a href='http://code.google.com/p/ijab/'><img src='http://ijab.googlecode.com/svn/wiki/ijab_login.png' /></a>
</div>
<div>
Main Window<br />
<a href='http://code.google.com/p/ijab/'><img src='http://ijab.googlecode.com/svn/wiki/iJab_main.JPG' /></a>
</div>
<div>
Chat Window<br />
<a href='http://code.google.com/p/ijab/'><img src='http://ijab.googlecode.com/svn/wiki/ijab_chat_window.png' /></a>
</div>
<div>
Add user Window<br />
<a href='http://code.google.com/p/ijab/'><img src='http://ijab.googlecode.com/svn/wiki/ijab_add_user.png' /></a>
</div>


## Support & Discuss ##
Any question: contact me by zhancaibaoATgmail.com or zhongfanglin by zhongfanglin ATgmail.com.

or visit [iJab Forum](http://www.ijab.im/forum).


Supported by [AnzSoft](http://www.anzsoft.com)