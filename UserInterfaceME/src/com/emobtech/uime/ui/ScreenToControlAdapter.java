/*
 * ScreenToControlAdapter.java
 * 31/08/2007
 * JME Framework
 * Copyright(c) Ernandes Mourao Junior (ernandes@gmail.com)
 * All rights reserved
 */
package com.emobtech.uime.ui;

import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.Graphics;

import com.emobtech.uime.util.I18N;


/**
 * <p>
 * </p>
 * 
 * @author Ernandes Mourao Junior (ernandes@gmail.com)
 * @version 1.0
 * @since 1.0
 */
public class ScreenToControlAdapter extends Control {
    /**
     * <p> 
     * Command OK.
     * </p>
     */
    private Command cmdOK;
    
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
     * Screen object listener.
     * </p>
     */
    private CommandListener screenListener;
    
    /**
     * <p>
     * Edit mode state.
     * </p>
     */
    private boolean onEdit;

    /**
     * <p>
     * Indicates that the screen needs to be notified that the screen is about
     * to displayed.
     * </p>
     */
    private boolean needShowNotify;

    /**
     * <p>
     * Screen to displayed as a control.
     * </p>
     */
    Screen screen;
    
    /**
     * <p>
     * Popup mode. It is used to define if the screen will be displayed full
     * size or according to the control's size when it is edited.
     * </p>
     */
    boolean popupMode;
    
    /**
     * <p>
     * Constructor.
     * </p>
     * @param screen Screen.
     */
    public ScreenToControlAdapter(Screen screen) {
        super(screen.getTitle());
        //
        this.screen = screen;
        //
        if (screen instanceof List) {
            ((List)screen).setRoller(false);
        }
        //
        screen.hideScreenParts(true, true);
    }
    
    /**
     * <p>
     * Gets the screen.
     * </p>
     * @return Screen.
     */
    public Screen getScreen() {
    	return screen;
    }
    
    /**
     * @inheritDoc
     * @see br.framework.jme.ui.Control#setWidth(int)
     */
    public void setWidth(int w) {
    	super.setWidth(w);
    	//
    	screen.setWidth(w);
    }
    
    /**
     * @inheritDoc
     * @see br.framework.jme.ui.Control#setHeight(int)
     */
    public void setHeight(int h) {
    	super.setHeight(h);
    	//
    	screen.setHeight(h);
    }
    
    /**
     * <p>
     * Sets the popup mode. It is used to define if the screen will be displayed
     * full size or according to the control's size when it is edited.
     * </p>
     * @param enabled Enabled or not.
     */
    public synchronized void setPopUpMode(boolean enabled) {
        popupMode = enabled;
    }
    
    /**
     * @inheritDoc
     * @see br.framework.jme.ui.Control#edit()
     */
    public synchronized void edit() {
        super.edit();
        //
        onEdit = true;
        setTrappingEvent(true);
        //
        if (screen instanceof List) {
            ((List)screen).setRoller(true);
        }
        //
        if (popupMode) {
            screen.hideScreenParts(false, false);
            //
            screen.setWidth(-1);
            screen.setHeight(-1);
            screen.sizeChanged(screen.getWidth(), screen.getHeight());
            screen.setShown(null);
            //
            screen.addCommand(
            	cmdOK = 
            		new Command(
            			I18N.getFramework(
            				"framework.jme.ui.screentocontroladapter.ok"), 
            			Command.OK, 
            			-100));
            screenListener = screen.getCommandListenerObject();
            screen.setCommandListener(new CommandListener() {
                public void commandAction(Command c, Displayable d) {
                    performCommandAction(c, d);
                }
            });
            //
            screen.setParent(null);
            parent.showNext(screen);
        } else {
            formCommands = parent.getAllCommands();
            parent.removeAllCommands();
            //
            parent.addCommand(
            	cmdOK = 
            		new Command(
            			I18N.getFramework(
            				"framework.jme.ui.screentocontroladapter.ok"), 
            			Command.OK, 
            			-100));
            Command[] screenCmds = screen.getAllCommands();
            for (int i = screenCmds.length -1; i >= 0; i--) {
                parent.addCommand(screenCmds[i]);
            }
            formListener = parent.getCommandListenerObject();
            parent.setCommandListener(new CommandListener() {
                public void commandAction(Command c, Displayable d) {
                    performCommandAction(c, d);
                }
            });
        }
    }
    
    /**
     * <p>
     * Gets the heigth of a line of the wrapped list screen. If the wrapped
     * screen is not instance of List class, -1 will be returned.
     * </p>
     * @return Line height.
     */
    public int getListLineHeight() {
    	if (screen instanceof List) {
    		return ((List)screen).getItemHeight();
    	} else {
    		return -1;
    	}
    }

    /**
     * @inheritDoc
     * @see br.framework.jme.ui.Control#drawBody(javax.microedition.lcdui.Graphics, int, int, int, int)
     */
    protected synchronized void drawBody(Graphics g, int x, int y, int w,
        int h) {
        if (needShowNotify) {
            screen.showNotify();
            needShowNotify = false;
        }
        //
    	screen.paint(g, x, y);
        //
        g.setColor(bodyColor);
        g.drawRect(x, y, w, h); //drawing the border.
    }
    
    /**
     * @inheritDoc
     * @see br.framework.jme.ui.Control#showNotify()
     */
    protected void showNotify() {
        screen.setShown(new Boolean(true));
        screen.showNotify();
    }
    
    /**
     * @inheritDoc
     * @see br.framework.jme.ui.Control#hideNotify()
     */
    protected void hideNotify() {
        if (onEdit) {
            screen.setShown(null);
        } else {
            screen.setShown(new Boolean(false));
            screen.hideNotify();
        }
    }
    
    /**
     * @inheritDoc
     * @see br.framework.jme.ui.Control#sizeChanged(int, int)
     */
    protected void sizeChanged(int w, int h) {
        screen.sizeChanged(w, h);
    }
    
    /**
     * @inheritDoc
     * @see br.framework.jme.ui.Control#keyUp(int)
     */
    protected void keyUp(int keyCode) {
        screen.keyUp(keyCode);
    }
    
    /**
     * @inheritDoc
     * @see br.framework.jme.ui.Control#keyDown(int)
     */
    protected void keyDown(int keyCode) {
        screen.keyDown(keyCode);
    }
    
    /**
     * @inheritDoc
     * @see br.framework.jme.ui.Control#keyHold(int)
     */
    protected void keyHold(int keyCode) {
        screen.keyHold(keyCode);
    }
    
    /**
     * @inheritDoc
     * @see br.framework.jme.ui.Control#penUp(int, int)
     */
    protected void penUp(int x, int y) {
        screen.penUp(x - rect.x, y - rect.y);
    }
    
    /**
     * @inheritDoc
     * @see br.framework.jme.ui.Control#penDown(int, int)
     */
    protected void penDown(int x, int y) {
        screen.penDown(x - rect.x, y - rect.y);
    }
    
    /**
     * @inheritDoc
     * @see br.framework.jme.ui.Control#penDragged(int, int)
     */
    protected void penDragged(int x, int y) {
        screen.penDragged(x - rect.x, y - rect.y);
    }

    /**
     * @inheritDoc
     * @see br.framework.jme.ui.Control#setParent(br.framework.jme.ui.Form)
     */
    void setParent(Form parent) {
        super.setParent(parent);
        screen.setParent(parent);
        //
        if (parent != null && parent.isShown()) {
        	showNotify();
        }
        //
        if (parent != null) {
            //calculating the text field size.
            if (screen instanceof Calendar) {
            	if (getWidth() == 0) {
                    setWidth(((Calendar)screen).getPreferredWidth());
            	}
            	if (getHeight() == 0) {
                	setHeight(((Calendar)screen).getPreferredHeight());
            	}
            } else {
            	if (getWidth() == 0) {
                	setWidth((int)((parent.getWidth() * 95) / 100)); //95%
            	}
            	if (getHeight() == 0) {
                	setHeight(bodyFont.getHeight() * 5);
            	}
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
            if (screen instanceof List) {
                ((List)screen).setRoller(false);
            }
            //
            if (parent != null) {
                parent.notifyStateChanged(this); //notify about the changed text.
            }
            //
            if (popupMode) {
                screen.hideScreenParts(true, true);
                //
                screen.setWidth(getWidth());
                screen.setHeight(getHeight());
                screen.sizeChanged(getWidth(), getHeight());
                //
                screen.removeCommand(cmdOK);
                screen.setCommandListener(screenListener);
                //
                screen.setParent(parent);
                screen.showPrevious();
                //
                needShowNotify = true;
            } else {
                parent.removeAllCommands();
                for (int i = formCommands.length -1; i >= 0; i--) {
                    parent.addCommand(formCommands[i]);
                }
                parent.setCommandListener(formListener);
            }
            //
            cmdOK = null;
            formCommands = null;
            formListener = null;
            screenListener = null;
            //
            onEdit = false;
            setTrappingEvent(false);
        } else {
            //relaying the command event.
            if (popupMode) {
                if (screenListener != null) {
                    screenListener.commandAction(c, screen);
                }
            } else {
                CommandListener cl = screen.getCommandListenerObject();
                if (cl != null) {
                    cl.commandAction(c, parent);
                }
            }
        }
    }
}