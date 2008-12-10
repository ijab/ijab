/*
    iJab , The Ajax web jabber client
    Copyright (c) 2006-2008 by AnzSoft
   
    Author:Fanglin Zhong <zhongfanglin@anzsoft.com>

    Started at 2008-08-20, Beijing of China

    iJab    (c) 2006-2008 by the ijab developers  

    *************************************************************************
    *                                                                       *
    * This program is free software; you can redistribute it and/or modify  *
    * it under the terms of the GNU General Public License as published by  *
    * the Free Software Foundation; either version 2 of the License, or     *
    * (at your option) any later version.                                   *
    *                                                                       *
    *************************************************************************
*/


package com.anzsoft.client.utils;

public class TextUtils {

    // FIXME this utils are commont to kune, emiteui and emitelib

    // Original regexp from http://snippets.dzone.com/posts/show/452
    public static final String URL_REGEXP = "((ftp|http|https):\\/\\/(\\w+:{0,1}\\w*@)?(\\S+)(:[0-9]+)?(\\/|\\/([\\w#!:.?+=&%@!\\-\\/]))?)";

    // Original regexp from http://www.regular-expressions.info/email.html
    public static final String EMAIL_REGEXP = "[-!#$%&\'*+/=?_`{|}~a-z0-9^]+(\\.[-!#$%&\'*+/=?_`{|}~a-z0-9^]+)*@(localhost|([a-z0-9]([-a-z0-9]*[a-z0-9])?\\.)+[a-z0-9]([-a-z0-9]*[a-z0-9]))?";

    /*
     * This method escape only some dangerous html chars
     */
    public static String escape(final String source) {
        if (source == null) {
            return null;
        }
        String result = source;
        result = result.replaceAll("&", "&amp;");
        result = result.replaceAll("\"", "&quot;");
        // text = text.replaceAll("\'", "&#039;");
        result = result.replaceAll("<", "&lt;");
        result = result.replaceAll(">", "&gt;");
        return result;
    }

    /*
     * This method unescape only some dangerous html chars for use in GWT Html
     * widget for instance
     */
    public static String unescape(final String source) {
        if (source == null) {
            return null;
        }
        String result = source;
        result = result.replaceAll("&amp;", "&");
        result = result.replaceAll("&quot;", "\"");
        result = result.replaceAll("&#039;", "\'");
        result = result.replaceAll("&lt;", "<");
        result = result.replaceAll("&gt;", ">");
        return result;
    }
    
    public static String genUniqueId()
    {
  	  char[] s = new char[5];
  	  char itoh[]={'0','1','2','3','4','5','6','7','8','9','A','B','C','D','E','F'};
  	  for (int i = 0; i < 5; i++)
  	  {
  		  s[i] = (char) Math.floor(Math.random()*0x10);
  	  }

  	  //s[14] = 4;
  	  //s[19] =(char) ((s[19] & 0x3) | 0x8);

  	  for (int i = 0; i < 5; i++)
  		  s[i] = itoh[s[i]];

  	  //s[8] = s[13] = s[18] = s[23] = '-';

  	  return new String(s);
    }
}
