/*
 * StringControl.java
 * 03/09/2007
 * JME Framework
 * Copyright(c) Ernandes Mourao Junior (ernandes@gmail.com)
 * All rights reserved
 */
package br.framework.jme.ui;

import javax.microedition.lcdui.Font;
import javax.microedition.lcdui.Graphics;

import com.emobtech.uime.util.ui.UIUtil;


/**
 * <p>
 * This class implements a control that displays a string in the screen.
 * </p>
 * 
 * @author Ernandes Mourao Junior (ernandes@gmail.com)
 * @version 1.0
 * @since 1.0
 */
public class StringControl extends Control {
    /**
     * <p>
	 * Constant that defines that the control will display a plain string.
     * </p>
     */
    public static final int PLAIN = 1;
    
    /**
     * <p>
	 * Constant that defines that the control will display a string as a link.
     * </p>
     */
    public static final int HYPERLINK = 2;

    /**
     * <p>
     * Text.
     * </p>
     */
    String text;
   
    /**
     * <p>
     * Type.
     * </p>
     */
    int type;
    
	/**
	 * <p>
	 * Text alignment.
	 * </p>
	 */
	int textAlignment = UIUtil.ALIGN_LEFT;
	
	/**
	 * <p>
	 * Visible line count.
	 * </p>
	 */
	int vLineCount = -1;

    /**
     * <p>
     * Constructor.
     * </p>
     * @param text Text.
     */
    public StringControl(String text) {
        this(null, text, PLAIN);
    }

	/**
     * <p>
     * Constructor.
     * </p>
     * @param text Text.
     * @param type Type.
     * @see StringControl#PLAIN
     * @see StringControl#HYPERLINK
     */
    public StringControl(String text, int type) {
        this(null, text, type);
    }
    
    /**
     * <p>
     * Constructor.
     * </p>
     * @param label Label.
     * @param text Text.
     */
    public StringControl(String label, String text) {
        this(label, text, PLAIN);
    }

    /**
     * <p>
     * Constructor.
     * </p>
     * @param label Label.
     * @param text Text.
     * @param type Type.
     * @see br.framework.jme.ui.StringControl#PLAIN
     * @see br.framework.jme.ui.StringControl#HYPERLINK
     */
    public StringControl(String label, String text, int type) {
        super(label);
        //
        setText(text);
        this.type = type;
        //
        setBodyFont(bodyFont);
        //
        skin.applyStringControlSkin(this);
    }
    
    /**
     * @inheritDoc
     * @see br.framework.jme.ui.Control#setBodyFont(javax.microedition.lcdui.Font)
     */
    public void setBodyFont(Font font) {
        if ((type & HYPERLINK) != 0 && !font.isUnderlined()) {
        	font =
        		Font.getFont(
        			font.getFace(),
        			font.getStyle() | Font.STYLE_UNDERLINED,
        			font.getSize());
        }
        //
    	super.setBodyFont(font);
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
     * Sets the text.
     * </p>
     * @param text Text.
     */
    public synchronized void setText(String text) {
        this.text = text != null ? text.trim() : null;
        //
        if (isShown()) {
            calcTextHeight(getWidth());
        }
        //
        if (parent != null) {
            parent.notifyStateChanged(this); //notify about the changed text.
        }
        //
        repaint();
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
     * Sets the count of visible lines. If the count is equal to -1, all the
     * lines of the text will be displayed.
     * </p>
     * @param count Count.
     */
    public synchronized void setVisibleLineCount(int count) {
    	vLineCount = count < 1 ? -1 : count;
    	//
    	calcTextHeight(rect.w);
    }
    
    /**
     * @inheritDoc
     * @see br.framework.jme.ui.Control#showNotify()
     */
    protected void showNotify() {
    	calcTextHeight(rect.w);
    }
    
    /**
     * @inheritDoc
     * @see br.framework.jme.ui.Control#sizeChanged(int, int)
     */
    protected void sizeChanged(int w, int h) {
        if (isShown()) {
        	calcTextHeight(w);
        }
    }
    
    /**
     * @inheritDoc
     * @see br.framework.jme.ui.Control#drawBody(javax.microedition.lcdui.Graphics, int, int, int, int)
     */
    protected synchronized void drawBody(Graphics g, int x, int y, int w,
        int h) {
		if (text != null) {
			UIUtil.drawString(
				g,
				x + OFFSET,
				y + OFFSET,
				w - OFFSET * 2,
				h,
				text,
				bodyFont,
				textAlignment,
				bodyFontColor,
				true);
		}
    }
    
    /**
     * @inheritDoc
     * @see br.framework.jme.ui.Control#isEditable()
     */
    protected boolean isEditable() {
        return false;
    }
    
	/**
	 * <p>
	 * Calcs the area in which the text will ocuppy.
	 * </p>
	 * @param w Width.
	 */
	void calcTextHeight(int w) {
		if (parent == null) {
			return;
		}
		//
		String[] lines;
		//
		if (text != null) {
			lines =	UIUtil.splitString(text, w - OFFSET * 2, bodyFont);
		} else {
			lines = new String[] {""};
		}
		//
    	final int lineCount = 
    		vLineCount == -1 
				? lines.length 
				: vLineCount > lines.length ? lines.length : vLineCount;
		//
		rect.h = (lineCount * bodyFont.getHeight()) + (OFFSET * lineCount);
		//
		if (rect.h == 0) {
			rect.h = bodyFont.getHeight() + OFFSET;
		}
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
			if (rect.w == 0) {
				rect.w = (int)((parent.getWidth() * 95) / 100); //95%
			}
			if (rect.h == 0) {
				rect.h = bodyFont.getHeight() + OFFSET;
			}
        }
    }
}