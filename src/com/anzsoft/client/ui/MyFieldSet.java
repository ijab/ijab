package com.anzsoft.client.ui;

import com.extjs.gxt.ui.client.Events;
import com.extjs.gxt.ui.client.core.El;
import com.extjs.gxt.ui.client.event.ComponentEvent;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.widget.Component;
import com.extjs.gxt.ui.client.widget.ComponentHelper;
import com.extjs.gxt.ui.client.widget.Container;
import com.extjs.gxt.ui.client.widget.Layout;
import com.extjs.gxt.ui.client.widget.button.ToolButton;
import com.google.gwt.dom.client.InputElement;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.Event;

public class MyFieldSet extends Container
{
	private El body;
	  private ToolButton collapseBtn;
	  private Element heading;
	  private String text;
	  private boolean collapsible;
	  private boolean collapsed;
	  private boolean checkboxToggle;
	  private String checkboxName;
	  private InputElement checkbox;

	  /**
	   * Creates a new fieldset.
	   */
	  public MyFieldSet() {
	    baseStyle = "x-fieldset";
	    enableLayout = true;
	  }

	  @Override
	  public boolean add(Component item) {
	    return super.add(item);
	  }

	  /**
	   * Collapses the fieldset.
	   */
	  public void collapse() {
	    if (rendered) {
	      if (collapsible && !collapsed) {
	        if (fireEvent(Events.BeforeCollapse)) {
	          onCollapse();
	        }
	      }
	    } else {
	      collapsed = true;
	    }
	  }

	  /**
	   * Expands the fieldset.
	   */
	  public void expand() {
	    if (rendered && collapsible && !isExpanded()) {
	      if (fireEvent(Events.BeforeExpand)) {
	        removeStyleName("x-fieldset-collpase");
	        onExpand();
	      }
	    }
	  }

	  /**
	   * Returns the checkbox name.
	   * 
	   * @return the checkbox name
	   */
	  public String getCheckboxName() {
	    return checkboxName;
	  }

	  @Override
	  public boolean insert(Component item, int index) {
	    return super.insert(item, index);
	  }

	  /**
	   * Returns true if checkbox toggle is enabled.
	   * 
	   * @return the checkbox toggle state
	   */
	  public boolean isCheckboxToggle() {
	    return checkboxToggle;
	  }

	  /**
	   * Returns true if the fieldset is collapsible.
	   * 
	   * @return true if callapsible
	   */
	  public boolean isCollapsible() {
	    return collapsible;
	  }

	  /**
	   * Returns <code>true</code> if the panel is expanded.
	   * 
	   * @return the expand state
	   */
	  public boolean isExpanded() {
	    return !collapsed;
	  }
	  
	  public boolean isChecked()
	  {
		  if(!isCheckboxToggle())
			  return false;
		  else
			  return checkbox.isChecked();
		  
	  }

	  @Override
	  public void onComponentEvent(ComponentEvent ce) {
	    super.onComponentEvent(ce);
	    if (ce.type == Event.ONCLICK) {
	      onClick(ce);
	    }
	  }

	  @Override
	  public boolean remove(Component item) {
	    return super.remove(item);
	  }

	  /**
	   * The name to assign to the fieldset's checkbox if
	   * {@link #setCheckboxToggle(boolean)} = true.
	   * 
	   * @param checkboxName the name
	   */
	  public void setCheckboxName(String checkboxName) {
	    this.checkboxName = checkboxName;
	  }

	  /**
	   * True to render a checkbox into the fieldset frame just in front of the
	   * legend (defaults to false, pre-render). The fieldset will be expanded or
	   * collapsed when the checkbox is toggled.
	   * 
	   * @param checkboxToggle true for checkbox toggle
	   */
	  public void setCheckboxToggle(boolean checkboxToggle) {
	    this.checkboxToggle = checkboxToggle;
	    this.collapsible = true;
	  }

	  /**
	   * Sets whether the fieldset is collapsible (defaults to false, pre-render).
	   * 
	   * @param collapsible true for collapse
	   */
	  public void setCollapsible(boolean collapsible) {
	    this.collapsible = collapsible;
	  }

	  /**
	   * Sets the panel's expand state.
	   * 
	   * @param expand <code>true<code> true to expand
	   */
	  public void setExpanded(boolean expand) {
	    if (expand) {
	      expand();
	    } else {
	      collapse();
	    }
	  }

	  /**
	   * Sets the panel heading.
	   * 
	   * @param text the heading text
	   */
	  public void setHeading(String text) {
	    this.text = text;
	    if (rendered) {
	      heading.setInnerHTML(text);
	    }
	  }

	  @Override
	  public void setLayout(Layout layout) {
	    super.setLayout(layout);
	  }

	  @Override
	  protected ComponentEvent createComponentEvent(Event event) {
	    return new MyFieldSetEvent(this, event);
	  }

	  @Override
	  protected void doAttachChildren() {
	    super.doAttachChildren();
	    ComponentHelper.doAttach(collapseBtn);
	  }

	  @Override
	  protected void doDetachChildren() {
	    super.doDetachChildren();
	    ComponentHelper.doDetach(collapseBtn);
	  }

	  @Override
	  protected El getLayoutTarget() {
	    return body;
	  }

	  protected void onClick(ComponentEvent ce) {
	    if (checkboxToggle && ce.getTarget() == (Element)checkbox.cast()) {
	      setExpanded(!isExpanded());
	    }
	  }

	  protected void onCollapse() {
	    addStyleName("x-panel-collapsed");
	    collapsed = true;
	    body.setVisible(false);
	    MyFieldSetEvent fe = new MyFieldSetEvent(this);
	    fireEvent(Events.Collapse, fe);
	    fireEvent(Events.Resize, fe);

	  }

	  protected void onExpand() {
	    collapsed = false;
	    body.setVisible(true);
	    removeStyleName("x-panel-collapsed");
	    layout();
	    MyFieldSetEvent fe = new MyFieldSetEvent(this);
	    fireEvent(Events.Expand, fe);
	    fireEvent(Events.Resize, fe);
	  }

	  @Override
	  protected void onRender(Element parent, int pos) {
	    super.onRender(parent, pos);
	    setElement(DOM.createFieldSet(), parent, pos);
	    sinkEvents(Event.ONCLICK);

	    Element legend = DOM.createLegend();
	    legend.setClassName("x-fieldset-header");

	    if (checkboxToggle) {
	      checkbox = DOM.createInputCheck().cast();
	      
	      if (checkboxName != null) {
	        checkbox.setAttribute("name", checkboxName);
	      }
	      legend.appendChild(checkbox);
	      checkbox.setChecked(false);
	    }

	    if (!checkboxToggle && collapsible) {
	      collapseBtn = new ToolButton("x-tool-toggle");
	      collapseBtn.addListener(Events.Select, new Listener<ComponentEvent>() {
	        public void handleEvent(ComponentEvent be) {
	          setExpanded(!isExpanded());
	        }
	      });
	      collapseBtn.render(legend);
	    }

	    heading = DOM.createSpan();
	    heading.setClassName("x-fieldset-header-text");
	    legend.appendChild(heading);
	    getElement().appendChild(legend);

	    body = el().createChild("<div class=''></div");
	    getElement().appendChild(body.dom);

	    if (text != null) {
	      setHeading(text);
	    }

	    if (collapsed) {
	      onCollapse();
	    }
	  }
}
