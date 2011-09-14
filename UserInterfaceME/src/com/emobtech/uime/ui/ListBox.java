/*
 * ListBox.java
 * 31/08/2007
 * JME Framework
 * Copyright(c) Ernandes Mourao Junior (ernandes@gmail.com)
 * All rights reserved
 */
package br.framework.jme.ui;

import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;

import com.emobtech.uime.util.I18N;
import com.emobtech.uime.util.ui.UIUtil;


/**
 * <p>
 * This class implements a list box control.
 * </p>
 * 
 * @author Ernandes Mourao Junior (ernandes@gmail.com)
 * @version 1.0
 * @since 1.0
 */
public class ListBox extends Control {
    /**
     * <p>
     * Hold the form commands when the control is being edited.
     * </p>
     */
    private Command[] formCommands;
    
    /**
     * <p>
     * Hold the form command listener when the control is being edited.
     * </p>
     */
    private CommandListener formListener;
    
    /**
     * <p>
     * Previous selected index. It holds the selected index right before the 
     * list box enters in edit mode.
     * </p>
     */
    private int prevSelectedIndex;
    
    /**
     * <p>
     * Selection color.
     * </p>
     */
    private int selectionColor = -1;

    /**
     * <p>
     * Scrollbar color.
     * </p>
     */
    private int scrollBarColor = -1;

    /**
	 * <p>
     * Command Select.
     * </p>
	 */
	Command cmdSelect;

    /**
     * <p>
     * List screen that is used as model for the control.
     * </p>
     */
    List model;
    
    /**
     * <p>
     * Constructor.
     * </p>
     * @param label Label.
     */
    public ListBox(String label) {
        this(label, null, null, List.IMPLICIT);
    }

    /**
     * <p>
     * Constructor.
     * </p>
     * @param label Label.
     * @param items Items to be appended to the list.
     * @param images Items' images.
     * @param type Type.
     * @see br.framework.jme.ui.List#IMPLICIT
     * @see br.framework.jme.ui.List#MULTIPLE
     * @see br.framework.jme.ui.List#EXCLUSIVE
     */
    public ListBox(String label, Object[] items, Image[] images, int type) {
        super(label);
        //
        model = new List(label, type);
        model.hideScreenParts(true, true);
        model.setRoller(false);
        //
        if (selectionColor != -1) {
            model.selectionColor = selectionColor;
        }
        if (scrollBarColor != -1) {
            model.scrollBarColor = scrollBarColor;
        }
        if (backgroundColor != -1) {
            model.backgroundColor = backgroundColor;
        }
        //
        if (items != null) {
            for (int i = 0; i < items.length; i++) {
                model.append(
                    items[i],
                    images != null && images.length > i ? images[i] : null);
            }
        }
        //
        skin.applyListBoxSkin(this);
    }
    
    /**
     * @inheritDoc
     * @see br.framework.jme.ui.List#append(Object, Image)
     */
    public synchronized void append(Object item, Image image) {
        model.append(item, image);
    }
    
    /**
     * @inheritDoc
     * @see br.framework.jme.ui.List#set(int, Object, Image)
     */
    public synchronized void set(int itemIndex, Object item, Image image) {
        model.set(itemIndex, item, image);
    }
    
    /**
     * @inheritDoc
     * @see br.framework.jme.ui.List#insert(int, Object, Image)
     */
    public synchronized void insert(int itemIndex, Object item, Image image) {
        model.insert(itemIndex, item, image);
    }
    
    /**
     * @inheritDoc
     * @see br.framework.jme.ui.List#getSelectedIndex()
     */
    public synchronized int getSelectedIndex() {
        return model.getSelectedIndex();
    }
    
    /**
     * @inheritDoc
     * @see br.framework.jme.ui.List#setSelectedIndex(int)
     */
    public synchronized void setSelectedIndex(int itemIndex) {
        model.setSelectedIndex(itemIndex);
    }
    
    /**
     * @inheritDoc
     * @see br.framework.jme.ui.List#get(int)
     */
    public synchronized Object get(int itemIndex) {
        return model.get(itemIndex);
    }
    
    /**
     * @inheritDoc
     * @see br.framework.jme.ui.List#getSelectedValue()
     */
	public synchronized Object getSelectedValue() {
		return model.getSelectedValue();
	}
    
    /**
     * @inheritDoc
     * @see br.framework.jme.ui.List#getItems()
     */
    public synchronized Object[] getItems() {
        return model.getItems();
    }
    
    /**
     * @inheritDoc
     * @see br.framework.jme.ui.List#delete(int)
     */
    public synchronized void delete(int itemIndex) {
        model.delete(itemIndex);
    }
    
    /**
     * @inheritDoc
     * @see br.framework.jme.ui.List#deleteAll()
     */
    public synchronized void deleteAll() {
        model.deleteAll();
    }
    
    /**
     * @inheritDoc
     * @see br.framework.jme.ui.List#size()
     */
    public synchronized int size() {
        return model.size();
    }
    
    /**
     * @inheritDoc
     * @see br.framework.jme.ui.List#sort()
     */
    public synchronized void sort() {
        model.sort();
    }
    
    /**
     * @inheritDoc
     * @see br.framework.jme.ui.List#setChecked(int, boolean)
     */
	public synchronized void setChecked(int itemIndex, boolean checked) {
		model.setChecked(itemIndex, checked);
	}
	
    /**
     * @inheritDoc
     * @see br.framework.jme.ui.List#getCheckedItems()
     */
	public synchronized Object[] getCheckedItems() {
		return model.getCheckedItems();
	}
    
    /**
     * @inheritDoc
     * @see br.framework.jme.ui.Control#edit()
     */
    public synchronized void edit() {
        super.edit();
        //
        setTrappingEvent(true);
        //
        formCommands = parent.getAllCommands();
        parent.removeAllCommands();
        //
        if (model.type == List.IMPLICIT) {
            parent.addCommand(
                cmdSelect = 
                	new Command(
                		I18N.getFramework("framework.jme.ui.listbox.select"), 
                		Command.OK, 
                		0));
        	parent.addCommand(
            	new Command(
            		I18N.getFramework("framework.jme.ui.listbox.cancel"), 
            		Command.CANCEL, 
            		0));
        } else {
            parent.addCommand(
                cmdSelect = 
                	new Command(
                		I18N.getFramework("framework.jme.ui.listbox.ok"), 
                		Command.OK, 
                		0));
        }
        formListener = parent.getCommandListenerObject();
        parent.setCommandListener(new CommandListener() {
            public void commandAction(Command c, Displayable d) {
                performCommandAction(c, d);
            }
        });
        //
        model.setRoller(true);
        //
        //saving current selected index.
        prevSelectedIndex = model.getSelectedIndex();
    }
    
    /**
     * @inheritDoc
     * @see br.framework.jme.ui.Control#setWidth(int)
     */
    public void setWidth(int w) {
        super.setWidth(w);
        //
        model.setWidth(w);
    }
    
    /**
     * @inheritDoc
     * @see br.framework.jme.ui.Control#setHeight(int)
     */
    public void setHeight(int h) {
        super.setHeight(h);
        //
        model.setHeight(h);
    }
    
    /**
     * @inheritDoc
     * @see br.framework.jme.ui.List#getBestSizeForImage()
     */
    public int getBestSizeForImage() {
        return model.getBestSizeForImage();
    }
    
    /**
     * @inheritDoc
     * @see br.framework.jme.ui.List#setSelectionColor(int, int, int)
     */
    public void setSelectionColor(int r, int g, int b) {
    	selectionColor = UIUtil.getHexColor(r, g, b);
    	//
    	if (model != null) {
            model.setSelectionColor(r, g, b);
    	}
    }
    
    /**
     * @inheritDoc
     * @see br.framework.jme.ui.List#setScrollBarColor(int, int, int)
     */
    public void setScrollBarColor(int r, int g, int b) {
    	scrollBarColor = UIUtil.getHexColor(r, g, b);
    	//
    	if (model != null) {
            model.setScrollBarColor(r, g, b);
    	}
    }
    
    /**
     * @see br.framework.jme.ui.Control#setBackgroundColor(int, int, int)
     */
    public void setBackgroundColor(int r, int g, int b) {
    	super.setBackgroundColor(r, g, b);
    	//
    	if (model != null) {
        	model.setBackgroundColor(r, g, b);
    	}
    }
    
    /**
     * @inheritDoc
     * @see br.framework.jme.ui.List#indexOf(Object)
     */
    public int indexOf(Object item) {
    	return model.indexOf(item);
    }

    /**
     * @inheritDoc
     * @see br.framework.jme.ui.Control#drawBody(javax.microedition.lcdui.Graphics, int, int, int, int)
     */
    protected synchronized void drawBody(Graphics g, int x, int y, int w,
        int h) {
        model.paint(g, x, y);
        //
        g.setColor(bodyColor);
        g.drawRect(x, y, w, h); //drawing the border.
    }
    
    /**
     * @inheritDoc
     * @see br.framework.jme.ui.Control#showNotify()
     */
    protected void showNotify() {
        model.setShown(new Boolean(true));
        model.showNotify();
    }
    
    /**
     * @inheritDoc
     * @see br.framework.jme.ui.Control#hideNotify()
     */
    protected void hideNotify() {
        model.setShown(new Boolean(false));
        model.hideNotify();
    }
    
    /**
     * @inheritDoc
     * @see br.framework.jme.ui.Control#sizeChanged(int, int)
     */
    protected void sizeChanged(int w, int h) {
        model.sizeChanged(w, h);
    }
    
    /**
     * @inheritDoc
     * @see br.framework.jme.ui.Control#keyUp(int)
     */
    protected void keyUp(int keyCode) {
        model.keyUp(keyCode);
    }
    
    /**
     * @inheritDoc
     * @see br.framework.jme.ui.Control#keyDown(int)
     */
    protected void keyDown(int keyCode) {
        model.keyDown(keyCode);
    }
    
    /**
     * @inheritDoc
     * @see br.framework.jme.ui.Control#keyHold(int)
     */
    protected void keyHold(int keyCode) {
        model.keyHold(keyCode);
    }
    
    /**
     * @inheritDoc
     * @see br.framework.jme.ui.Control#penUp(int, int)
     */
    protected void penUp(int x, int y) {
    	model.penUp(x - rect.x, y - rect.y);
    }
    
    /**
     * @inheritDoc
     * @see br.framework.jme.ui.Control#penDown(int, int)
     */
    protected void penDown(int x, int y) {
    	model.penDown(x - rect.x, y - rect.y);
    }
    
    /**
     * @inheritDoc
     * @see br.framework.jme.ui.Control#penDragged(int, int)
     */
    protected void penDragged(int x, int y) {
    	model.penDragged(x - rect.x, y - rect.y);
    }

	/**
     * @inheritDoc
     * @see br.framework.jme.ui.Control#setParent(br.framework.jme.ui.Form)
     */
    void setParent(Form parent) {
        super.setParent(parent);
        model.setParent(parent);
        //
        if (parent != null) {
            //calculating the text field size.
			if (getWidth() == 0) {
	        	setWidth((int)((parent.getWidth() * 95) / 100)); //95%
			}
			if (getHeight() == 0) {
	            setHeight(bodyFont.getHeight() * 5);
			}
        }
    }
    
    /**
     * <p>
     * Handles the commands that are triggered by the control when it is being
     * edited.
     * </p>
     * @param c Triggered command.
     * @param d Displayable screen;
     */
    void performCommandAction(Command c, Displayable d) {
        parent.removeAllCommands();
        for (int i = formCommands.length -1; i >= 0; i--) {
            parent.addCommand(formCommands[i]);
        }
        parent.setCommandListener(formListener);
        //
    	if (c == cmdSelect) {
            if (prevSelectedIndex != model.getSelectedIndex()) {
                //selected index has been changed.
                if (parent != null) {
                    parent.notifyStateChanged(this);
                }
            }
        } else {
            model.setSelectedIndex(prevSelectedIndex);
        }
        //
        cmdSelect = null;
        formCommands = null;
        formListener = null;
        //
        model.setRoller(false);
        //
        setTrappingEvent(false);
    }
}