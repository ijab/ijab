/*
    iJab , The Ajax web jabber client
    Copyright (c) 2006-2008 by AnzSoft
   
    Author:Fanglin Zhong <zhongfanglin@anzsoft.com>

    Started at 2008-08-20, Beijing of China

    iJab    (c) 2006-2008 by the ijab developers
    
    Some code copied form gwtjsjac

    *************************************************************************
    *                                                                       *
    * This program is free software; you can redistribute it and/or modify  *
    * it under the terms of the GNU General Public License as published by  *
    * the Free Software Foundation; either version 2 of the License, or     *
    * (at your option) any later version.                                   *
    *                                                                       *
    *************************************************************************
*/

package com.anzsoft.client.XMPP.log;

public class LogHelper {
	
	private LogHelper() {
	}

	/**
	 * Convert to param method
	 * @param integer
	 * @return as string
	 */
	public static String p(int value) {
		return new Integer(value).toString();
	}

	public static void log(String pattern, String value1, String value2, LoggerOuput logger) {
		pattern = pattern.replaceFirst("#", value1);
		pattern = pattern.replaceFirst("#", value2);
		logger.log(pattern);
	}
	

}
