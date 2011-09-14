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
 * This class implements a control that displays a given image.
 * </p>
 * 
 * @author Ernandes Mourao Junior (ernandes@gmail.com)
 * @version 1.0
 * @since 1.0
 */
public class ImageControl extends Control {
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
     * Command OK.
     * </p>
	 */
	Command cmdOK;

	/**
     * <p>
     * ImageViewer screen that is used as model for the control.
     * </p>
	 */
	ImageViewer model;
	
	/**
     * <p>
     * Constructor.
     * </p>
	 * @param label Label.
	 * @param image Image.
	 */
	public ImageControl(String label, Image image) {
		super(label);
		//
		model = new ImageViewer(label, image);
        model.hideScreenParts(true, true);
        model.backgroundColor = backgroundColor;
        //
        skin.applyImageControlSkin(this);
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
	 * @see br.framework.jme.ui.ImageViewer#setImage(Image)
	 */
	public synchronized void setImage(Image img) {
		model.setImage(img);
		//
		setWidth(img.getWidth());
		setHeight(img.getHeight());
		//
		parent.notifySizeChanged(this);
	}
	
	/**
	 * @inheritDoc
	 * @see br.framework.jme.ui.ImageViewer#getImage()
	 */
	public synchronized Image getImage() {
		return model.getImage();
	}
	
	/**
	 * @inheritDoc
	 * @see br.framework.jme.ui.ImageViewer#full()
	 */
	public synchronized void full() {
		model.full();
	}

	/**
	 * @inheritDoc
	 * @see br.framework.jme.ui.ImageViewer#fit()
	 */
	public synchronized void fit() {
		model.fit();
	}

	/**
	 * @inheritDoc
	 * @see br.framework.jme.ui.ImageViewer#stretch()
	 */
	public synchronized void stretch() {
		model.stretch();
	}
	
	/**
	 * @inheritDoc
	 * @see br.framework.jme.ui.ImageViewer#isFullyVisible()
	 */
	public synchronized boolean isFullyVisible() {
		return model.isFullyVisible();
	}
	
	/**
	 * @inheritDoc
	 * @see br.framework.jme.ui.ImageViewer#setImageAlignment(int)
	 */
	public void setImageAlignment(int align) {
		model.setImageAlignment(align);
	}
	
	/**
	 * @inheritDoc
     * @see br.framework.jme.ui.ImageViewer#setScrollBarColor(int, int, int)
	 */
	public void setScrollBarColor(int r, int g, int b) {
		model.setScrollBarColor(r, g, b);
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
        parent.addCommand(
        	cmdOK = 
        		new Command(
        			I18N.getFramework("framework.jme.ui.imagecontrol.ok"), 
        			Command.OK, 
        			0));
        formListener = parent.getCommandListenerObject();
        parent.setCommandListener(new CommandListener() {
            public void commandAction(Command c, Displayable d) {
                performCommandAction(c, d);
            }
        });
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
	 * @see br.framework.jme.ui.Control#drawBody(javax.microedition.lcdui.Graphics, int, int, int, int)
	 */
	protected synchronized void drawBody(Graphics g, int x, int y, int w, 
		int h) {
		Image img = model.getImageObject();
		//
        model.paint(
        	g,
        	x + UIUtil.align(img.getWidth(), -1, w, layout),
        	y + UIUtil.align(-1, img.getHeight(), h, layout));
        //
        if (bodyColor != -1) {
            g.setColor(bodyColor);
            g.drawRect(x, y, w, h); //drawing the border.
        }
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
     * @see br.framework.jme.ui.Control#isEditable()
     */
    protected boolean isEditable() {
    	//TODO: decide if the edit command is displayed or not.
    	return !model.isFullyVisible();
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
	            setWidth(model.getImageObject().getWidth());
			}
			if (getHeight() == 0) {
	            setHeight(model.getImageObject().getHeight());
			}
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
            if (parent != null) {
                parent.notifyStateChanged(this);
            }
            parent.removeAllCommands();
            for (int i = formCommands.length -1; i >= 0; i--) {
                parent.addCommand(formCommands[i]);
            }
            parent.setCommandListener(formListener);
            //
            cmdOK = null;
            formCommands = null;
            formListener = null;
            //
            setTrappingEvent(false);
        }
    }
}