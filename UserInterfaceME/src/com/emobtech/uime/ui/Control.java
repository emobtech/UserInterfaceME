/*
 * Control.java
 * 10/08/2007
 * JME Framework
 * Copyright(c) Ernandes Mourao Junior (ernandes@gmail.com)
 * All rights reserved
 */
package com.emobtech.uime.ui;

import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Font;
import javax.microedition.lcdui.Graphics;

import com.emobtech.uime.util.ui.Rect;
import com.emobtech.uime.util.ui.UIUtil;


/**
 * <p>
 * This class defines a structure for a control that can be used along with a
 * form. It defines all the procedures and behaviors that a control must have.
 * </p>
 * 
 * @author Ernandes Mourao Junior (ernandes@gmail.com)
 * @version 1.0
 * @since 1.0
 * @see br.framework.jme.ui.CheckBox
 * @see br.framework.jme.ui.ComboBox
 * @see br.framework.jme.ui.DateField
 * @see br.framework.jme.ui.ImageControl
 * @see br.framework.jme.ui.ListBox
 * @see br.framework.jme.ui.TextArea
 * @see br.framework.jme.ui.RadioButton
 * @see br.framework.jme.ui.ScreenToControlAdapter
 * @see br.framework.jme.ui.Spacer
 * @see br.framework.jme.ui.StringControl
 * @see br.framework.jme.ui.TextField
 */
public abstract class Control {
	/**
	 * <p>
	 * Offset.
	 * </p>
	 */
	final static int OFFSET = UIUtil.OFFSET;
	
    /**
     * <p>
     * Skin.
     * </p>
     */
    static final Skin skin = Skin.getDefault();
    
    /**
     * <p>
     * Holds the prior body font color before the controls gets disabled.
     * </p>
     */
    private int priorBodyFontColor = -1;
    
    /**
     * <p>
     * Default command, which is specified by the control that has the focus.
     * </p>
     */
    Command defaultCommand;

	/**
     * <p>
     * It is used to notify the form that the control is trapping all the 
     * triggered events.
     * </p>
	 */
	boolean trappingEvent;
    
    /**
     * <p>
     * Focus color.
     * </p>
	 */
	int focusColor;
    
    /**
     * <p>
     * Background color.
     * </p>
     */
    int backgroundColor = -1;
	
    /**
     * <p>
     * Disabled effect color.
     * </p>
     */
    int disabledColor;

    /**
     * <p>
     * Label color.
     * </p>
	 */
	int labelColor;
	
	/**
     * <p>
     * Label font.
     * </p>
	 */
	Font labelFont;
	
	/**
     * <p>
     * Body font color.
     * </p>
	 */
	int bodyFontColor;
    
    /**
     * <p>
     * Body color.
     * </p>
     */
    int bodyColor;
	
	/**
     * <p>
     * Body font.
     * </p>
	 */
	Font bodyFont;

	/**
     * <p>
     * Label.
     * </p>
	 */
	String label;
	
	/**
     * <p>
     * Layout.
     * </p>
	 */
	int layout = UIUtil.ALIGN_LEFT;
	
	/**
     * <p>
     * Enabled state.
     * </p>
	 */
	boolean enabled = true;
	
	/**
     * <p>
     * Visible state.
     * </p>
	 */
	boolean visible = true;

	/**
     * <p>
     * Focusless state.
     * </p>
	 */
	boolean focusless;

	/**
     * <p>
     * Parent screen.
     * </p>
	 */
	Form parent;
	
	/**
     * <p>
     * Control rectangle.
     * </p>
	 */
	Rect rect;
	
	/**
	 * <p>
	 * Set background style.
	 * </p>
	 */
	int backgroundStyle = UIUtil.STYLE_GRADIENT | UIUtil.STYLE_VERTICAL |
    	UIUtil.STYLE_TRANS_SPEED_1;

	/**
     * <p>
     * Read only mode.
     * </p>
	 */
	boolean isReadOnly;
	
	/**
     * <p>
     * Constructor.
     * </p>
	 * @param label Label.
	 */
	public Control(String label) {
		rect = new Rect();
		setPosition(-1, -1);
		//
		this.label = label != null ? label.trim() : null;
		labelFont =
			Font.getFont(Font.FACE_SYSTEM, Font.STYLE_BOLD, Font.SIZE_MEDIUM);
		bodyFont =
			Font.getFont(Font.FACE_SYSTEM, Font.STYLE_PLAIN, Font.SIZE_MEDIUM);
		//
		skin.applyControlSkin(this);
	}

	/**
     * <p>
     * Get the control width.
     * </p>
	 * @return Width.
	 */
	public int getWidth() {
		return rect.w;
	}

	/**
     * <p>
     * Get the control height.
     * </p>
	 * @return Height.
	 */
	public int getHeight() {
		return rect.h;
	}

    /**
     * <p>
     * Set the control width.
     * </p>
	 * @param w Width.
	 */
	public void setWidth(int w) {
		rect.w = w;
		//
		sizeChanged(w, rect.h);
	}

    /**
     * <p>
     * Set the control height.
     * </p>
	 * @param h Height.
	 */
	public void setHeight(int h) {
		rect.h = h;
		//
		sizeChanged(rect.w, h);
	}
	
    /**
     * <p>
     * Verify if the control is visible.
     * </p>
	 * @return Visible or not.
	 */
	public boolean isVisible() {
		return visible;
	}
    
    /**
     * <p>
     * Verify if the control is enabled.
     * </p>
     * @return Enabled or not.
     */
    public boolean isEnabled() {
        return enabled;
    }

    /**
     * <p>
     * Set the control layout.
     * </p>
	 * @param layout Layout.
     * @see com.emobtech.uime.util.ui.UIUtil#ALIGN_LEFT
     * @see com.emobtech.uime.util.ui.UIUtil#ALIGN_CENTER
     * @see com.emobtech.uime.util.ui.UIUtil#ALIGN_RIGHT
	 */
	public void setLayout(int layout) {
		this.layout = layout;
	}
	
    /**
     * <p>
     * Set the control as enabled or disabled.
     * </p>
	 * @param enabled Enabled or disabled.
	 */
	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
		//
		if (enabled) {
			if (priorBodyFontColor != -1) {
				bodyFontColor = priorBodyFontColor;
			}
		} else {
			priorBodyFontColor = bodyFontColor;
			bodyFontColor = disabledColor;
		}
		//
		if (parent != null) {
			parent.onControlStateChanges("enabled", this);
		}
		//
		repaint();
	}

    /**
     * <p>
     * Set the control as visible or invisible.
     * </p>
	 * @param enabled Visible or invisible.
	 */
	public void setVisible(boolean enabled) {
		if (parent != null && visible || enabled) {
			parent.onControlStateChanges("visibility", this);
		}
		//
		visible = enabled;
		//
		repaint();
	}
	
	/**
	 * <p>
	 * Sets the control as focusless.
	 * </p>
	 * @param enabled Focusless or not.
	 */
	public void setFocusless(boolean enabled) {
		focusless = enabled;
	}
    
    /**
     * <p>
     * Set the focus color.
     * </p>
	 * @param r Red.
	 * @param g Green.
	 * @param b Blue.
	 */
	public void setFocusColor(int r, int g, int b) {
		focusColor = UIUtil.getHexColor(r, g, b);
	}
    
    /**
     * <p>
     * Set the background color.
     * </p>
     * @param r Red.
     * @param g Green.
     * @param b Blue.
     */
    public void setBackgroundColor(int r, int g, int b) {
        backgroundColor = UIUtil.getHexColor(r, g, b);
    }

    /**
     * <p>
     * Set the disabled effect color.
     * </p>
     * @param r Red.
     * @param g Green.
     * @param b Blue.
     */
    public void setDisabledColor(int r, int g, int b) {
        disabledColor = UIUtil.getHexColor(r, g, b);
    }

    /**
     * <p>
     * Set the label color.
     * </p>
     * @param r Red.
     * @param g Green.
     * @param b Blue.
	 */
	public void setLabelColor(int r, int g, int b) {
		labelColor = UIUtil.getHexColor(r, g, b);
	}
	
    /**
     * <p>
     * Set the label font.
     * </p>
	 * @param font Font.
	 */
	public void setLabelFont(Font font) {
		labelFont = font;
	}
	
    /**
     * <p>
     * Set the body font color.
     * </p>
     * @param r Red.
     * @param g Green.
     * @param b Blue.
	 */
	public void setBodyFontColor(int r, int g, int b) {
		bodyFontColor = UIUtil.getHexColor(r, g, b);
	}
	
    /**
     * <p>
     * Set the body color.
     * </p>
     * @param r Red.
     * @param g Green.
     * @param b Blue.
     */
    public void setBodyColor(int r, int g, int b) {
        bodyColor = UIUtil.getHexColor(r, g, b);
    }

    /**
     * <p>
     * Set the body font.
     * </p>
	 * @param font Font.
	 */
	public void setBodyFont(Font font) {
		bodyFont = font;
	}
	
	/**
	 * <p>
	 * Get body font.
	 * </p>
	 * @return Font.
	 */
	public Font getBodyFont() {
		return bodyFont;
	}
	
	/**
	 * <p>
	 * Set background style.
	 * </p>
	 * @param style Style.
	 */
	public void setBackgroundStyle(int style) {
		backgroundStyle = style;
	}

    /**
     * <p>
     * Get the control label.
     * </p>
	 * @return Label.
	 */
	public String getLabel() {
		return label;
	}
	
    /**
     * <p>
     * Get the control layout.
     * </p>
	 * @return Layout.
	 */
	public int getLayout() {
		return layout;
	}
	
    /**
     * <p>
     * Set a command that will be inserted into the form, as the main command,
     * when the control gets the focus.
     * </p>
	 * @param cmd Command.
	 */
	public void setDefaultCommand(Command cmd) {
		defaultCommand = cmd;
	}
	
    /**
     * <p>
     * Get the control default command.
     * </p>
	 * @return Command.
	 */
	public Command getDefaultCommand() {
		return defaultCommand;
	}
    
    /**
     * <p>
     * It starts the control to be edited. Every control that extends the
     * Control class must implement this method to provide a way to edit it.
     * </p>
     * @throws IllegalStateException The control is not appended to a form.
     * @throws IllegalStateException The control is disabled, so it can't be
     *                               edited.
     * @throws IllegalStateException The control is not on focus.
     */
    public synchronized void edit() {
        if (parent == null) {
            throw new IllegalStateException(
                "The control is not appended to a form.");
        }
        if (!enabled) {
            throw new IllegalStateException(
                "The control is disabled, so it can't be edited.");
        }
    }
    
    /**
     * <p>
     * Set the label.
     * </p>
     * @param label New label.
     */
    public void setLabel(String label) {
    	this.label = label;
    	//
    	repaint();
    }
    
	/**
	 * <p>
	 * Sets the text field read only.
	 * </p> 
	 * @param enabled Read only or not.
	 */
	public void setReadOnly(boolean enabled) {
		isReadOnly = enabled;
	}
    
    /**
     * <p>
     * Draw the control body.
     * </p>
     * @param g Graphics object.
     * @param x X coordinate.
     * @param y Y coordinate.
     * @param w Width.
     * @param h Height.
	 */
	protected abstract void drawBody(Graphics g, int x, int y, int w, int h);

    /**
     * <p>
     * Draw the control focus.
     * </p>
     * @param g Graphics object.
     * @param x X coordinate.
     * @param y Y coordinate.
     * @param w Width.
     * @param h Height.
	 */
	protected void drawFocus(Graphics g, int x, int y, int w, int h) {
		g.setColor(focusColor);
		g.drawRect(x, y, w, h);
//		if (!isTrappingEvent()) {
//			g.drawRect(x +1, y +1, w -2, h -2);
//		}
	}
    
    /**
     * <p>
     * Draw the control background.
     * </p>
     * @param g Graphics object.
     * @param x X coordinate.
     * @param y Y coordinate.
     * @param w Width.
     * @param h Height.
     */
    protected void drawBackground(Graphics g, int x, int y, int w, int h) {
    	if (backgroundColor != -1) {
            UIUtil.fillRect(
                g,
                x,
                y,
                w,
                h,
                backgroundColor,
                backgroundStyle);
    	}
    }

    /**
     * <p>
     * Draw the control label.
     * </p>
     * @param g Graphics object.
     * @param x X coordinate.
     * @param y Y coordinate.
     * @param w Width.
     * @param h Height.
	 */
	protected void drawLabel(Graphics g, int x, int y, int w, int h) {
		if (isLabelEntered()) {
			g.setFont(labelFont);
            final String l = UIUtil.shrinkString(label, w, labelFont);
            //
            g.setColor(190, 190, 190); //dark gray color.
            //drawing the label's shadow.
            g.drawString(l, x -1, y +1, Graphics.LEFT | Graphics.TOP);
            //
			g.setColor(labelColor);
            g.drawString(l, x, y, Graphics.LEFT | Graphics.TOP);
		}
	}
	
	/**
	 * <p>
	 * Method that handles the event when a key is pressed. If you intend to
	 * extend this class, all your key pressed events handling must be done
	 * from this method.
	 * </p>
	 * @param keyCode Key code.
	 */
	protected void keyDown(int keyCode) {
	}

	/**
	 * <p>
	 * Method that handles the event when a key is released. If you intend to
	 * extend this class, all your key released events handling must be done
	 * from this method.
	 * </p>
	 * @param keyCode Key code.
	 */
	protected void keyUp(int keyCode) {
	}
	
	/**
	 * <p>
	 * Method that handles the event when a key is hold. If you intend to
	 * extend this class, all your key hold events handling must be done
	 * from this method.
	 * </p>
	 * @param keyCode Key code.
	 */
	protected void keyHold(int keyCode) {
	}
	
	/**
	 * <p>
	 * Method that handles the event when the pen is pressed. If you intend to
	 * extend this class, all your pen pressed events handling must be done
	 * from this method.
	 * </p>
	 * @param x X coordinate.
	 * @param y Y coordinate.
	 */
	protected void penDown(int x, int y) {
	}

	/**
	 * <p>
	 * Method that handles the event when the pen is released. If you intend to
	 * extend this class, all your pen released events handling must be done
	 * from this method.
	 * </p>
	 * @param x X coordinate.
	 * @param y Y coordinate.
	 */
	protected void penUp(int x, int y) {
	}

	/**
	 * <p>
	 * Method that handles the event when the pen is dragged. If you intend to
	 * extend this class, all your pen dragged events handling must be done
	 * from this method.
	 * </p>
	 * @param x X coordinate.
	 * @param y Y coordinate.
	 */
	protected void penDragged(int x, int y) {
	}

    /**
     * <p>
     * This method is triggered when the control is about to be removed from
     * the display.
     * </p>
	 */
	protected void hideNotify() {
	}

    /**
     * <p>
     * This method is triggered when the control is about to be visible on the
     * display.
     * </p>
	 */
	protected void showNotify() {
	}

    /**
     * <p>
     * This method is triggered when the control dimensions were changed.
     * </p>
	 * @param w New width.
	 * @param h New height.
	 */
	protected void sizeChanged(int w, int h) {
	}
	
    /**
     * <p>
     * Verify if the control is visible on the display.
     * </p>
	 * @return Visible or not.
	 */
	protected boolean isShown() {
		return parent != null && parent.isShown();
	}
	
    /**
     * <p>
     * Verify if the control is able to receive the focus.
     * </p>
	 * @return Able to receive the focus.
	 */
	protected boolean isFocusless() {
		return focusless;
	}
    
    /**
     * <p>
     * Verify if the control can be editable.
     * </p>
     * @return Editable or not.
     */
    protected boolean isEditable() {
        return true;
    }
    
    /**
     * <p>
     * This method is triggered when the control receives the focus.
     * </p>
     */
    protected void onFocus() {
    }
	
    /**
     * <p>
     * This method is triggered when the control loses the focus.
     * </p>
     */
    protected void onBlur() {
    }

    /**
     * <p>
     * Set the state that the control is trapping all the triggered events.
     * </p>
     * @param enabled Trapping or not.
     */
    void setTrappingEvent(boolean enabled) {
        trappingEvent = enabled;
    }

    /**
     * <p>
     * Notify to the parent screen that the control has triggered a command.
     * </p>
     * @param evtCmd Command.
     */
    void notifyEvent(Command evtCmd) {
        //relaying the command event.
        CommandListener cl = parent.getCommandListenerObject();
        if (cl != null) {
            cl.commandAction(evtCmd, parent);
        }
    }
	
    /**
     * <p>
     * Verify if the control is trapping the events.
     * </p>
	 * @return Trapping or not.
	 */
	boolean isTrappingEvent() {
		return trappingEvent;
	}

    /**
     * <p>
     * Set the parent screen.
     * </p>
	 * @param parent Screen.
	 */
	void setParent(Form parent) {
		this.parent = parent;
	}

    /**
     * <p>
     * Set the control position.
     * </p>
	 * @param x X coordinate.
	 * @param y Y coordinate.
	 */
	void setPosition(int x, int y) {
		rect.x = x;
		rect.y = y;
	}
	
    /**
     * <p>
     * Verify if the label is entered or not.
     * </p>
	 * @return Entered or not.
	 */
	boolean isLabelEntered() {
		return label != null && label.length() > 0;
	}
	
    /**
     * <p>
     * Get the label height.
     * </p>
	 * @return Height.
	 */
	int getLabelHeight() {
		return labelFont.getHeight();
	}
	
    /**
     * <p>
     * Get the control rectangle.
     * </p>
	 * @return Rectangle.
	 */
	Rect getRect() {
		return rect;
	}
	
    /**
     * <p>
     * Requests a repaint for the entire control.
     * </p>
	 */
	void repaint() {
		if (parent != null) {
			parent.requestRepaint();
		}
	}
}