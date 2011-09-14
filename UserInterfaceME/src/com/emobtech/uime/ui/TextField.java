/*
 * TextField.java
 * 10/08/2007
 * JME Framework
 * Copyright(c) Ernandes Mourao Junior (ernandes@gmail.com)
 * All rights reserved
 */
package br.framework.jme.ui;

import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.TextBox;

import com.emobtech.uime.midlet.MIDlet;
import com.emobtech.uime.util.I18N;
import com.emobtech.uime.util.ui.UIUtil;


/**
 * <p>
 * This class implements a text field.
 * </p>
 * 
 * @author Ernandes Mourao Junior (ernandes@gmail.com)
 * @version 1.0
 * @since 1.0
 */
public class TextField extends Control {
	/**
     * <p> 
     * Command OK.
     * </p>
     */
    Command cmdOK;
    
    /**
     * <p>
     * TextBox used to edit the field's text.
     * </p>
     */
    TextBox tbox;
    
    /**
     * <p>
     * Text to be displayed when the text field type is PASSWORD.
     * </p>
     */
    String passwordText;
    
    /**
     * <p>
     * Indicates if the parent screen was on fullscreen mode before launching
     * the text box screen.
     * </p>
     */
    boolean wasFullScreeMode;

	/**
	 * <p>
	 * Text alignment.
	 * </p>
	 */
	int textAlignment = UIUtil.ALIGN_LEFT;
	
    /**
     * <p>
     * Input constraints.
     * </p>
     */
    int constraints = javax.microedition.lcdui.TextField.ANY;

	/**
     * <p>
     * Max size.
     * </p>
	 */
	int maxSize = 5000;
	
	/**
     * <p>
     * Text.
     * </p>
	 */
	String text;
	
	/**
     * <p>
     * Constructor.
     * </p>
	 * @param label Label.
	 * @param text Text,
	 */
	public TextField(String label, String text) {
		super(label);
		//
		setText(text);
		//
		skin.applyTextFieldSkin(this);
	}

	/**
     * <p>
     * Gets the text.
     * </p>
	 * @return Text.
	 */
	public synchronized String getText() {
		return text;
	}

	/**
     * <p>
     * Sets the text.
     * </p>
	 * @param text Text.
	 */
	public synchronized void setText(String text) {
		boolean stateChanged = 
			(this.text != null && text != null && !this.text.equals(text)) || 
			(this.text == null && text != null) || 
			(this.text != null && text == null);
		//
		this.text = text != null ? text.trim() : null;
		//
		if (stateChanged && parent != null) {
	    	parent.notifyStateChanged(this); //notify about the changed text.
		}
		//
		repaint();
	}
	
	/**
	 * <p>
	 * Set the text alignment.
	 * </p>
	 * @param align Alignment type.
     * @see com.emobtech.uime.util.ui.UIUtil#ALIGN_LEFT
     * @see com.emobtech.uime.util.ui.UIUtil#ALIGN_CENTER
     * @see com.emobtech.uime.util.ui.UIUtil#ALIGN_RIGHT
	 */
	public void setTextAlignment(int align) {
		textAlignment = align;
		//
		repaint();
	}
	
    /**
     * <p>
     * Sets the maximum size (number of characters) that can be contained in
     * this TextField.
     * </p>
     * @param size Size.
     * @throws IllegalArgumentException Size must be greater than zero.
     */
    public synchronized void setMaxSize(int size) {
    	if (size <= 0) {
    		throw new IllegalArgumentException(
    			"Size must be greater than zero: " + size);
    	}
    	//
        maxSize = size; 
    }
    
    /**
     * <p>
     * Set the input constraints.
     * </p>
     * @param constraints Constraints.
     * @see javax.microedition.lcdui.TextField#ANY
     * @see javax.microedition.lcdui.TextField#EMAILADDR
     * @see javax.microedition.lcdui.TextField#NUMERIC
     * @see javax.microedition.lcdui.TextField#URL
     * @see javax.microedition.lcdui.TextField#PHONENUMBER
     * @see javax.microedition.lcdui.TextField#PASSWORD
     */
    public synchronized void setConstraints(int constraints) {
        this.constraints = constraints;
        //
    	if (isShown()
    			&& (constraints
    					& javax.microedition.lcdui.TextField.PASSWORD) != 0) {
			preparePasswordText();
		}
    }
	
	/**
	 * @inheritDoc
     * @see br.framework.jme.ui.Control#edit()
     */
    public synchronized void edit() {
        super.edit();
        //
    	tbox = new TextBox(label, text, maxSize, constraints);
    	//
    	cmdOK = 
			new Command(
				I18N.getFramework("framework.jme.ui.textfield.ok"), 
				Command.OK, 
				0);
    	tbox.addCommand(cmdOK);
    	//
    	if (isReadOnly) {
    		//#ifdef MIDP20
    		tbox.setConstraints(
    			constraints | javax.microedition.lcdui.TextField.UNEDITABLE);
    		//#endif
    	} else {
        	Command cmdCancel =
        		new Command(
            		I18N.getFramework("framework.jme.ui.textfield.cancel"), 
            		Command.CANCEL, 
            		0);
            tbox.addCommand(cmdCancel);
    	}
        //
        tbox.setCommandListener(new CommandListener() {
            public void commandAction(Command c, Displayable d) {
                performCommandAction(c, d);
            }
        });
        //
        wasFullScreeMode = parent.fullScreenMode;
        //
        MIDlet.getDisplayInstance().setCurrent(tbox);
    }
    
    /**
     * @see br.framework.jme.ui.Control#showNotify()
     */
    protected void showNotify() {
    	super.showNotify();
    	//
    	if ((constraints & javax.microedition.lcdui.TextField.PASSWORD) != 0) {
			preparePasswordText();
		}
    }
    
    /**
     * @see br.framework.jme.ui.Control#isEditable()
     */
    protected boolean isEditable() {
    	return !isReadOnly
    		|| text == null
    		|| bodyFont.stringWidth(text) > rect.w - OFFSET * 2;
    }
    
    /**
     * @see br.framework.jme.ui.Control#sizeChanged(int, int)
     */
    protected void sizeChanged(int w, int h) {
    	super.sizeChanged(w, h);
    	//
    	if ((constraints & javax.microedition.lcdui.TextField.PASSWORD) != 0) {
			preparePasswordText();
		}
    }

	/**
	 * @inheritDoc
	 * @see br.framework.jme.ui.Control#drawBody(javax.microedition.lcdui.Graphics, int, int, int, int)
	 */
	protected synchronized void drawBody(Graphics g, int x, int y, int w,
        int h) {
		g.setFont(bodyFont);
		g.setColor(bodyColor);
		//
		g.drawRect(x, y, w, h); //draw the rectangle.
		//
		if (text != null && !text.equals("")) {
			final boolean isPassword = //is password field?
				(constraints & javax.microedition.lcdui.TextField.PASSWORD)!=0;
			//
            UIUtil.drawString(
            	g,
            	x + OFFSET,
            	y,
            	w - OFFSET,
            	h,
            	isPassword ? passwordText : text,
            	bodyFont,
            	textAlignment | UIUtil.ALIGN_MIDDLE,
            	bodyFontColor,
            	true);
		}
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
        if (c == cmdOK) {
        	if (!isReadOnly) {
                setText(tbox.getString().trim());
        	}
        }
        //
        tbox = null;
        cmdOK = null;
        //
        if (wasFullScreeMode) {
        	//it solves the problems, on emulator, which used to reduce the
        	//screen size when the text box screen was closed.
            parent.setFullScreenMode(true);
        }
        //
        MIDlet.getDisplayInstance().setCurrent(parent);
    }
    
    /**
     * <p>
     * Creates a password text as long as control's text. 
     * </p>
     */
    void preparePasswordText() {
        //generating the password text, used in password text field.
        StringBuffer sb = new StringBuffer();
        //visible character count in one line.
        int c = (getWidth() - OFFSET * 2) / bodyFont.charWidth('*');
        //
        while (c-- > 0) {
            sb.append('*');
        }
        //
        passwordText = sb.toString();
    }
	
	/**
	 * @inheritDoc
	 * @see br.framework.jme.ui.Control#setParent(br.framework.jme.ui.Form)
	 */
	void setParent(Form parent) {
		super.setParent(parent);
		//
		if (parent != null) {
			//calculating the text field size.
			if (getWidth() == 0) {
				rect.w = (int)((parent.getWidth() * 95) / 100); //95%
			}
			if (getHeight() == 0) {
				rect.h = bodyFont.getHeight() + OFFSET * 2;
			}
		}
	}
}
