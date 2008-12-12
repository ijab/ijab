package com.anzsoft.client;

public class iJabPrefs 
{
	private static iJabPrefs _instance = null;
	
	public boolean showOnlineOnly = false;
	private iJabPrefs()
	{
	}
	
	public static iJabPrefs instance()
	{
		if(_instance == null)
			_instance = new iJabPrefs();
		return _instance;
	}
	
	public void prefsChanged()
	{
		
	}
	
	public void deinit()
	{
		showOnlineOnly = false;
	}
}
