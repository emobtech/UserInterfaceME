/*
 * TextArea.java
 * 30/08/2007
 * JME Framework
 * Copyright(c) Ernandes Mourao Junior (ernandes@gmail.com)
 * All rights reserved
 */
package com.emobtech.uime.ui;

import javax.microedition.lcdui.Canvas;
import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.Graphics;

import com.emobtech.uime.util.I18N;
import com.emobtech.uime.util.ui.Rect;
import com.emobtech.uime.util.ui.UIUtil;


/**
 * <p>
 * This class implements a multiline text field.
 * </p>
 * 
 * @author Ernandes Mourao Junior (ernandes@gmail.com)
 * @version 1.0
 * @since 1.0
 */
public class TextArea extends TextField {
    /**
     * <p>
     * Vertical scrollbar square rectangle.
     * </p>
     */
    Rect sqRect;

    /**
     * <p>
     * Hold the form commands when the control is being edited.
     * </p>
     */
    Command[] formCommands;
    
    /**
     * <p>
     * Hold the form command listener when the control is being edited.
     * </p>
     */
    CommandListener formListener;
    
	/**
     * <p>
     * Text splitted in lines.
     * </p>
	 */
	String[] textLines;
	
	/**
	 * <p>
	 * Index of the first visible line.
	 * </p>
	 */
	int idxFirstLine;
	
	/**
	 * 
	 */
	int scrollBarColor;
	
    /**
     * <p>
     * Visible line count.
     * </p>
	 */
	int lineCount;
	
	/**
	 * <p>
	 * Max height defined by the Form.
	 * </p>
	 */
	int maxHeight;

	/**
	 * <p>
	 * Constructor.
	 * </p>
	 * @param label Label.
	 * @param text Text.
	 * @param lineCount Count line.
	 */
	public TextArea(String label, String text, int lineCount) {
		super(label, text);
		//
		this.lineCount = lineCount;
		sqRect = new Rect();
		//
		skin.applyTextAreaSkin(this);
	}
	
	/**
	 * <p>
	 * Set the scrollbar color.
	 * </p>
	 * @param r Red.
	 * @param g Green.
	 * @param b Blue.
	 */
	public void setScrollBarColor(int r, int g, int b) {
		scrollBarColor = UIUtil.getHexColor(r, g, b);
	}

	/**
	 * @see br.framework.jme.ui.TextField#setText(java.lang.String)
	 */
	public synchronized void setText(String text) {
		super.setText(text);
		//
		if (isShown()) {
			processTextLines();
		}
	}

	/**
	 * @see br.framework.jme.ui.TextField#edit()
	 */
	public synchronized void edit() {
		if (isReadOnly) {
	    	setTrappingEvent(true);
	    	//
	        formCommands = parent.getAllCommands();
	        formListener = parent.getCommandListenerObject();
	        parent.removeAllCommands();
	        //
            cmdOK = 
            	new Command(
            		I18N.getFramework("framework.jme.ui.textfield.ok"), 
            		Command.OK, 
            		0);
            //
	    	parent.addCommand(cmdOK);
	        parent.setCommandListener(new CommandListener() {
	            public void commandAction(Command c, Displayable d) {
	                performCommandAction(c, d);
	            }
	        });
		} else {
			super.edit();
		}
	}
	
	/**
	 * @inheritDoc
	 * @see br.framework.jme.ui.TextField#showNotify()
	 */
	protected void showNotify() {
		super.showNotify();
		//
		processTextLines();
	}

	/**
	 * @inheritDoc
	 * @see br.framework.jme.ui.TextField#sizeChanged(int, int)
	 */
	protected void sizeChanged(int w, int h) {
		super.sizeChanged(w, h);
		//
		maxHeight = h;
		//
		if (isShown()) {
			processTextLines();
		}
	}

	/**
	 * @see br.framework.jme.ui.Control#keyDown(int)
	 */
	protected void keyDown(int keyCode) {
		keyCode = parent.getGameAction(keyCode);
		//
		if (keyCode == Canvas.DOWN) {
			scrollDown();
		} else if (keyCode == Canvas.UP) {
			scrollUp();
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
		if (isTrappingEvent()) {
			final int sbw =
				UIUtil.getScrollbarSize(
					parent.getWidth(),
					parent.getHeight(),
					parent.hasPointerMotionEvents());
			//
			drawVerticalScrollBar(g, x + w - sbw, y, sbw, h);
		}
		//
		if (text != null && !text.equals("")) {
			final boolean isPassword = //is password field?
				(constraints & javax.microedition.lcdui.TextField.PASSWORD)!=0;
			//
			String line;
			int lineH = getLineHeight();
			//
			g.setFont(bodyFont);
			g.setColor(bodyFontColor);
			y += 1;
			x += OFFSET;
			//
			for (int i = idxFirstLine, c = 0;
					c < lineCount && i < textLines.length; i++, c++) {
				line = isPassword ? passwordText : textLines[i];
				//
				g.drawString(
					line,
					x + UIUtil.alignString(line, w, textAlignment,bodyFont), 
					y, 
					Graphics.TOP | Graphics.LEFT);
				//
				y += lineH;
			}
		}
	}

	/**
	 * <p>
	 * Draw the vertical scrollbar.
	 * </p>
	 * @param g Graphics object.
	 * @param x X coordinate.
	 * @param y Y coordinate.
	 * @param w Width.
	 * @param h Height.
	 */
	protected void drawVerticalScrollBar(Graphics g, int x, int y, int w,
		int h) {
		UIUtil.drawVerticalScrollbar(
			g, 
			x, 
			y, 
			w, 
			h, 
			textLines.length, 
			getVisibleLinesCount(), 
			idxFirstLine, 
			sqRect, 
			scrollBarColor, 
			skin.getVerticalScrollBarStyle());
	}
	
	/**
	 * @see br.framework.jme.ui.Control#isEditable()
	 */
	protected boolean isEditable() {
		if (isReadOnly) {
			String[] tempLines = null;
			//
			if (textLines == null) {
				processTextLines();
				//
				if (textLines != null) {
					tempLines = textLines;
					textLines = null;
				} else {
					return false;
				}
			} else {
				tempLines = textLines;
			}
			//
			return tempLines.length * getLineHeight() > rect.h;
		} else {
			return true;
		}
	}

	/**
	 * <p>
	 * Scrolls the text down.
	 * </p>
	 * @return Scrolled or not.
	 */
	boolean scrollDown() {
        int idxLastShownItem;
        //
        if (getVisibleLinesCount() < textLines.length) {
            idxLastShownItem = idxFirstLine + getVisibleLinesCount();
        } else {
            idxLastShownItem = idxFirstLine + textLines.length;
        }
        //
        if (idxLastShownItem +1 <= textLines.length) {
        	idxFirstLine++;
            //
        	repaint();
        	//
            return true;
        } else {
        	return false;	
        }
	}

	/**
	 * <p>
	 * Scrolls the text up.
	 * </p>
	 * @return Scrolled or not.
	 */
	boolean scrollUp() {
        if (idxFirstLine -1 >= 0) {
        	idxFirstLine--;
            //
        	repaint();
        	//
        	return true;
        } else {
        	return false;
        }
	}
	
	/**
	 * <p>
	 * Get the number of visible lines in the text field area.
	 * </p>
	 * @return Number.
	 */
	int getVisibleLinesCount() {
		return rect.h / getLineHeight();
	}

	/**
	 * <p>
	 * Gets the line height.
	 * </p>
	 * @return Height.
	 */
	int getLineHeight() {
		return bodyFont.getHeight() + OFFSET;
	}
	
	/**
	 * @see br.framework.jme.ui.TextField#performCommandAction(javax.microedition.lcdui.Command, javax.microedition.lcdui.Displayable)
	 */
	void performCommandAction(Command c, Displayable d) {
		if (isReadOnly && c == cmdOK) {
			setTrappingEvent(false);
			//
			parent.setCommandListener(formListener);
			parent.removeAllCommands();
			//
			for (int i = formCommands.length -1; i >= 0; i--) {
				parent.addCommand(formCommands[i]);
			}
			//
			formListener = null;
			formCommands = null;
			cmdOK = null;
			idxFirstLine = 0;
		} else {
			super.performCommandAction(c, d);
		}
	}
	
	/**
     * <p>
     * Splitts the text in lines. Each line must fit the screen width.
     * </p>
	 */
	void processTextLines() {
		if (text != null) {
			final int sbw =
				UIUtil.getScrollbarSize(
					parent.getWidth(),
					parent.getHeight(),
					parent.hasPointerMotionEvents());
			//process the lines of the text.
			textLines =
				UIUtil.splitString(text, rect.w - OFFSET * 2 - sbw, bodyFont);
			//
			int nh;
			if (textLines.length <= lineCount) {
				nh = getLineHeight() * textLines.length;
			} else {
				nh = getLineHeight() * lineCount;
			}
			//
			if (nh <= maxHeight) {
				rect.h = nh;
			}
		} else {
			rect.h = getLineHeight();
		}
	}
	
	/**
	 * @see br.framework.jme.ui.TextField#setParent(br.framework.jme.ui.Form)
	 */
	void setParent(Form parent) {
		super.setParent(parent);
		//
		rect.h = getLineHeight() * lineCount;
	}
}