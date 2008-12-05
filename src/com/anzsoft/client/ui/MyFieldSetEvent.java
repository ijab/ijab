package com.anzsoft.client.ui;

import com.extjs.gxt.ui.client.event.ContainerEvent;
import com.google.gwt.user.client.Event;

public class MyFieldSetEvent extends ContainerEvent
{
	public MyFieldSet fieldSet;
	  
	  public MyFieldSetEvent(MyFieldSet fieldSet) {
	    super(fieldSet);
	    this.fieldSet = fieldSet;
	  }
	  
	  public MyFieldSetEvent(MyFieldSet fieldSet, Event event) {
	    this(fieldSet);
	    this.event = event;
	    
	  }

}
