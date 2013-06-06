/*
 * DateField.java
 * 22/09/2007
 * JME Framework
 * Copyright(c) Ernandes Mourao Junior (ernandes@gmail.com)
 * All rights reserved
 */
package com.emobtech.uime.ui;

import java.io.IOException;
import java.util.Date;

import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;

import com.emobtech.uime.util.I18N;
import com.emobtech.uime.util.StringUtil;
import com.emobtech.uime.util.ui.UIUtil;


/**
 * <p>
 * This class implements a date field control.
 * </p>
 * 
 * @author Ernandes Mourao Junior (ernandes@gmail.com)
 * @version 1.0
 * @since 1.0
 */
public class DateField extends Control {
    /**
     * <p>
     * Holds the form commands when the control is being edited.
     * </p>
     */
    private Command[] formCommands;
    
    /**
     * <p>
     * Holds the form command listener when the control is being edited.
     * </p>
     */
    private CommandListener formListener;

	/**
     * <p>
     * Edit state.
     * </p>
     */
    boolean onEdit;
    
    /**
	 * <p>
     * Command Select.
     * </p>
	 */
	Command cmdSelect;

    /**
	 * <p>
     * Command Simple date.
     * </p>
	 */
	Command cmdSimpDate;

	/**
     * <p>
     * Calendar image.
     * </p>
     */
    Image imgCal;
    
	/**
     * <p>
     * Size of a side of the box in which the calendar is drawn.
     * </p>
     */
    int sideSize;

	/**
     * <p>
     * Hold the current date as string.
     * </p>
	 */
	String dateStr;

    /**
     * <p>
     * Date.
     * </p>
	 */
	Date date;
	
	/**
     * <p>
     * Calendar screen that is used as model for the control.
     * </p>
	 */
	Calendar model;
	
	/**
     * <p>
     * Constructor.
     * </p>
	 * @param label Label.
	 * @param date Initial date.
	 */
	public DateField(String label, Date date) {
		super(label);
		//
		setDate(date);
		//
		model = new Calendar(getLabel());
		model.hideScreenParts(true, true);
		model.setHeight(0);
		//
		skin.applyDateFieldSkin(this);
	}
	
    /**
     * <p>
     * Set the date.
     * </p>
	 * @param date Date.
	 */
	public synchronized void setDate(Date date) {
		this.date = date;
		dateStr = date != null ? StringUtil.formatDate(date) : null;
		//
		repaint();
	}
	
    /**
     * <p>
     * Get the date.
     * </p>
	 * @return Date.
	 */
	public synchronized Date getDate() {
		return date;
	}
	
    /**
     * <p>
     * Get the date as string.
     * </p>
	 * @return Date as string.
	 */
	public synchronized String getDateAsString() {
		return dateStr;
	}
	
    /**
     * @inheritDoc
     * @see br.framework.jme.ui.Control#setHeight(int)
     */
    public void setHeight(int h) {
    	super.setHeight(h);
    	if (onEdit) {
        	model.setHeight(h);
    	} else {
    		model.setHeight(0);
    	}
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
	 * @see br.framework.jme.ui.Control#edit()
	 */
	public synchronized void edit() {
		super.edit();
		//
        setTrappingEvent(true);
    	onEdit = true;
        //
    	//TODO: bug em pequenas resoluções na hora de mostrar o calendário.
        setHeight(model.getPreferredHeight());
    	model.setHeight(model.getPreferredHeight());
        //
        parent.notifySizeChanged(this);
        //
        formCommands = parent.getAllCommands();
        parent.removeAllCommands();
        //
        parent.addCommand(
            cmdSelect = 
            	new Command(
            		I18N.getFramework("framework.jme.ui.datefield.select"), 
            		Command.OK, 
            		0));
        parent.addCommand(
            cmdSimpDate = 
            	new Command(
            		I18N.getFramework("framework.jme.ui.datefield.changeview"), 
            		Command.ITEM, 
            		0));
        parent.addCommand(
        	new Command(
        		I18N.getFramework("framework.jme.ui.datefield.cancel"), 
        		Command.CANCEL, 
        		0));
        formListener = parent.getCommandListenerObject();
        parent.setCommandListener(new CommandListener() {
            public void commandAction(Command c, Displayable d) {
                performCommandAction(c, d);
            }
        });
        //
        model.setDate(date);
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
     * @see br.framework.jme.ui.Control#drawBody(javax.microedition.lcdui.Graphics, int, int, int, int)
 	 */
    protected synchronized void drawBody(Graphics g, int x, int y, int w, 
    	int h) {
    	if (onEdit) {
    		model.paint(g, x, y);
            //
            g.setColor(bodyColor);
            g.drawRect(x, y, w, h); //drawing the border.
    	} else {
            g.setColor(bodyColor);
            //
            g.drawRect(x, y, w, h); //draw the rectangle;
            //
            drawCalendar(g, x + w - sideSize, y, sideSize, h); //draw the calendar.
            //
            g.setFont(bodyFont);
            g.setColor(bodyFontColor);
            //
            w -= sideSize + OFFSET * 2; //displayed text area.
            //
            if (dateStr != null) {
                g.drawString(
                    UIUtil.shrinkString(dateStr, w, bodyFont),
                    x + OFFSET, //left margin.
                    y + UIUtil.alignString(
                    		dateStr, h, UIUtil.ALIGN_MIDDLE, bodyFont),
                    Graphics.TOP | Graphics.LEFT);
            }
    	}
    }
    
    /**
     * <p>
     * Draw the calendar icon.
     * </p>
     * @param g Graphics object.
     * @param x X coordinate.
     * @param y Y coordinate.
     * @param w Width.
     * @param h Height.
     */
    void drawCalendar(Graphics g, int x, int y, int w, int h) {
    	if (imgCal == null) {
    		try {
    			Image img = Image.createImage("/images/icon_calendar.png");
    			imgCal = UIUtil.resizeImage(img, w, h);
    		} catch (OutOfMemoryError e) {
			} catch (IOException e) {
			}
			//
			if (imgCal == null) {
				//alternative calendar symbol.
				g.setColor(bodyColor);
				g.drawRect(x, y, w, h);
				return;
			}
    	}
    	//
    	g.drawImage(imgCal, x, y, Graphics.TOP | Graphics.LEFT);
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
				setHeight(getBestHeight());
			}
		}
    	//
        sideSize = (parent.getWidth() * 12) / 100; //12%
    }
    
    /**
     * <p>
     * Handle the commands that are triggered by the control when it is being
     * edited.
     * </p>
     * @param c Triggered command.
     * @param d Displayable screen;
     */
    void performCommandAction(Command c, Displayable d) {
    	if (c == cmdSimpDate) {
    		model.setSimpleDateMode(!model.simpleDateMode);
    		//
    		return;
    	}
    	//
        if (c == cmdSelect) {
        	Date sd = model.getDate();
        	//
            if (date == null || !date.equals(sd)) {
            	setDate(sd);
                //selected date has been changed.
                if (parent != null) {
                    parent.notifyStateChanged(this);
                }
            }
        }
        //
        setHeight(bodyFont.getHeight() + OFFSET * 2);
        //
        parent.removeAllCommands();
        for (int i = formCommands.length -1; i >= 0; i--) {
            parent.addCommand(formCommands[i]);
        }
        parent.setCommandListener(formListener);
        //
        cmdSelect = null;
        cmdSimpDate = null;
        formCommands = null;
        formListener = null;
        //
        onEdit = false;
        setTrappingEvent(false);
    }
    
    /**
     * <p>
     * Gets best datefield height.
     * </p>
     * @return Height.
     */
    private int getBestHeight() {
    	return bodyFont.getHeight() + OFFSET * 2;
    }
}