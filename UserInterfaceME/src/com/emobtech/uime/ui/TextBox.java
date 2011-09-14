/*
 * TextBox.java
 * 04/06/2007
 * JME Framework
 * Copyright(c) Ernandes Mourao Junior (ernandes@gmail.com)
 * All rights reserved
 */
package br.framework.jme.ui;

import javax.microedition.lcdui.Canvas;
import javax.microedition.lcdui.Font;
import javax.microedition.lcdui.Graphics;

import com.emobtech.uime.util.StringUtil;
import com.emobtech.uime.util.ui.Rect;
import com.emobtech.uime.util.ui.UIUtil;


/**
 * <p>
 * This class implements a screen that represents a text box.
 * </p>
 * 
 * @author Ernandes Mourao Junior (ernandes@gmail.com)
 * @version 1.0
 * @since 1.0
 * @see br.framework.jme.ui.GroupedList
 */
public class TextBox extends Screen {
	/**
	 * Line count for a line block scrolling.
	 */
	static final int SCROLL_BLOCK = 3;
	
	/**
	 * <p>
	 * Normal layout.
	 * </p>
	 */
	public static final int NORMAL_LAYOUT = 1;
	
	/**
	 * <p>
	 * Printing layout.
	 * </p>
	 */
	public static final int PRINTING_LAYOUT = 2;
	
	/**
	 * <p>
	 * Y coordinate where the pen was pressed initially for the vertical
	 * scrollbar drag.
	 * </p>
	 */
	private int penDraggedSquareY = -1;
	
    /**
	 * <p>
	 * Text area has been pressed or not.
	 * </p>
	 */
	boolean textAreaPressed;

	/**
	 * <p>
	 * Text to be displayed on the text box.
	 * </p>
	 */
	StringBuffer text;
	
	/**
	 * <p>
	 * Text splitted up in lines, where each line fits on the screen.
	 * </p>
	 */
	Object[] textLines;
	
	/**
	 * <p>
	 * Index of the first visible line.
	 * </p>
	 */
	int idxFirstVisibleLine;
	
    /**
     * <p>
     * Vertical scrollbar rectangle.
     * </p>
     */
    Rect vertScrollBarRect;
    
    /**
     * <p>
     * Vertical scrollbar square rectangle.
     * </p>
     */
    Rect vertScrollBarSquareRect;

    /**
     * <p>
     * Text area rectangle.
     * </p>
     */
    Rect textRect;
    
	/**
	 * <p>
	 * Scrollbar color.
	 * </p>
	 */
	int scrollBarColor;
	
	/**
	 * <p>
	 * Page margin.
	 * </p>
	 */
	int pageMargin;

	/**
	 * <p>
	 * Text margin.
	 * </p>
	 */
	int textMargin;
	
	/**
	 * <p>
	 * Text alignment.
	 * </p>
	 */
	int textAlignment;

    /**
	 * <p>
	 * Constructor.
	 * </p>
	 * @param title Text box title.
	 */
	public TextBox(String title) {
		super(title);
		//
		setLayout(PRINTING_LAYOUT);
		setTextAlignment(UIUtil.ALIGN_LEFT);
		//
		vertScrollBarRect = new Rect();
		vertScrollBarSquareRect = new Rect();
		textRect = new Rect();
		//
		skin.applyTextBoxSkin(this);
	}
	
	/**
	 * <p>
	 * Set the text to be displyed on the screen.
	 * </p>
	 * @param text Text.
	 */
	public synchronized void setText(String text) {
		if (text != null && (text = text.trim()).length() > 0) {
			if (this.text != null) {
				this.text.setLength(0);
				this.text.append(text);
			} else {
				this.text = new StringBuffer(text);
			}
		} else {
			this.text = null;
		}
		//
		textLines = null; //split up the new text.
		//
		idxFirstVisibleLine = 0;
		requestRepaint();
	}
	
	/**
	 * <p>
	 * Append a given text to the text.
	 * </p>
	 * @param text Text to be appended.
	 */
	public synchronized void append(String text) {
		if (this.text != null) {
			if (text != null) {
				this.text.append(text);
				textLines = null;
				//
				requestRepaint();
			}
		} else {
			setText(text);
		}
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
	 * <p>
	 * Set the page layout.
	 * </p>
	 * @param layout Layout type.
	 * @see br.framework.jme.ui.TextBox#NORMAL_LAYOUT
	 * @see br.framework.jme.ui.TextBox#PRINTING_LAYOUT
	 */
	public synchronized void setLayout(int layout) {
		if ((layout & PRINTING_LAYOUT) != 0) {
			pageMargin = OFFSET;
			textMargin = OFFSET * 2;
		} else {
			pageMargin = 0;
			textMargin = OFFSET;
		}
		//
		textLines = null; //recalculate the lines after page resizing.
		requestRepaint();
	}

	/**
	 * <p>
	 * Set the text alignment.
	 * </p>
	 * @param align Alignment type.
     * @see com.emobtech.uime.util.ui.UIUtil#ALIGN_LEFT
     * @see com.emobtech.uime.util.ui.UIUtil#ALIGN_CENTER
     * @see com.emobtech.uime.util.ui.UIUtil#ALIGN_RIGHT
     * @see com.emobtech.uime.util.ui.UIUtil#ALIGN_JUSTIFIED
	 */
	public synchronized void setTextAlignment(int align) {
		textAlignment = align;
		//
		textLines = null; //rebuild the lines.
		requestRepaint();
	}
	
	/**
	 * <p>
	 * Get the text of the text box.
	 * </p>
	 * @return Text.
	 */
	public synchronized String getText() {
		return text != null ? text.toString() : null;
	}

	/**
	 * @inheritDoc
	 * @see br.framework.jme.ui.Screen#drawBody(javax.microedition.lcdui.Graphics, int, int, int, int)
	 */
	protected synchronized void drawBody(Graphics g, int x, int y, int w,
		int h) {
		//
		final int pageMarginWH = pageMargin * 2;
		final int textMarginWH = textMargin * 2;
		//
		bodyRect.x += pageMargin; //left margin.
		bodyRect.y += pageMargin; //top margin.
		bodyRect.w -= pageMarginWH; //right margin.
		bodyRect.h -= pageMarginWH; //bottom margin.
		//
		textRect.x = bodyRect.x + textMargin; //left margin.
		textRect.y = bodyRect.y + textMargin; //top margin.
		textRect.w = bodyRect.w - textMarginWH; //right margin.
		textRect.h = bodyRect.h - textMarginWH; //bottom margin.
		//
		if (text != null) {
			boolean firstTime = false;
			if (textLines == null) {
				textLines = splitText(text.toString(), textRect.w);
				firstTime = true;
			}
			if (textLines.length > getLinesPerPage()) {
				vertScrollBarRect.w = 
					UIUtil.getScrollbarSize(w, h, hasPointerEvents());
				vertScrollBarRect.x = x + w - vertScrollBarRect.w;
				vertScrollBarRect.y = y;
				vertScrollBarRect.h = h;
				//
				bodyRect.w -= vertScrollBarRect.w; //leaving space for the scrollbar.
				textRect.w -= vertScrollBarRect.w; //new right margin.
				//
				if (firstTime) {
					textLines = splitText(text.toString(), textRect.w);
				}
			}
			//
			drawVerticalScrollBar(
				g,
				vertScrollBarRect.x,
				vertScrollBarRect.y,
				vertScrollBarRect.w,
				vertScrollBarRect.h);
			//
			if (isFirstLineVisible()) {
				//show the page top margin.
				y = bodyRect.y;
				h -= pageMargin;
			} else {
				if (pageMargin != 0) {
					//avoids to show the line of the page top.
					y--;
					h++;
				}
			}
			if (isLastLineVisible()) {
				//show the bottom page margin.
				h -= pageMargin;
			} else {
				if (pageMargin != 0) {
					//avoids to show the line of the page bottom.
					h++;
				}
			}
		}
		//
		g.setClip(bodyRect.x, y, bodyRect.w, h);
		drawPage(g, bodyRect.x, y, bodyRect.w, h);
		//
		if (text != null) {
			g.setClip(textRect.x, y + textMargin, textRect.w, h - textMarginWH);
			drawText(g, textRect.x, y + textMargin, textRect.w, h-textMarginWH);
		}
	}
	
	/**
	 * @inheritDoc
	 * @see br.framework.jme.ui.Screen#drawBackground(javax.microedition.lcdui.Graphics, int, int, int, int)
	 */
	protected void drawBackground(Graphics g, int x, int y, int w, int h) {
		if (pageMargin != 0) { //is printing layout?
			g.setColor(192, 192, 192);
			g.fillRect(x, y, w, h);
		}
	}
	
	/**
	 * @inheritDoc
	 * @see br.framework.jme.ui.Screen#keyHold(int)
	 */
	protected void keyHold(int keyCode) {
		int gameKey = getGameAction(keyCode);
		if (gameKey == Canvas.UP || gameKey == Canvas.DOWN) {
			keyDown(keyCode);
		}
	}
	
	/**
	 * @inheritDoc
	 * @see br.framework.jme.ui.Screen#keyDown(int)
	 */
	protected void keyDown(int keyCode) {
	    keyCode = getGameAction(keyCode);
	    if (keyCode == UP) {
	        scrollLines(true, 1);
	    } else if (keyCode == DOWN) {
	        scrollLines(false, 1);
	    }
	}
	
	/**
	 * @inheritDoc
	 * @see br.framework.jme.ui.Screen#penUp(int, int)
	 */
	protected void penUp(int x, int y) {
		if (!isVerticalScrollSquareArea(x, y) && isVerticalScrollBarArea(x, y)){
			scrollLines(y < vertScrollBarSquareRect.y, SCROLL_BLOCK);
		}
		//reset control variables.
		penDraggedSquareY = -1;
		textAreaPressed = false;
	}
	
	/**
	 * @inheritDoc
	 * @see br.framework.jme.ui.Screen#penDown(int, int)
	 */
	protected void penDown(int x, int y) {
		if (isTextArea(x, y)) {
			textAreaPressed = true;
			penDraggedSquareY = y;
		}
	}
	
	/**
	 * @inheritDoc
	 * @see br.framework.jme.ui.Screen#penDragged(int, int)
	 */
	protected void penDragged(int x, int y) {
		if (textAreaPressed) {
			if (isTextArea(x, y)) {
				if (Math.abs(y - penDraggedSquareY) > bodyFont.getHeight()) {
					//drags when the dragged distance is greater than a line height.
					scrollLines(y > penDraggedSquareY, 1);
					//
					penDraggedSquareY = y;
				}
			}
		} else {
			if (isVerticalScrollSquareArea(x, y)) {
				penDraggedSquareY = y;
			} else if (isVerticalScrollBarArea(x, y)) {
				if (penDraggedSquareY != -1) {
				    //pen left the square area but it is still in the scrollbar area.
					scrollLines(y < penDraggedSquareY, 1);
					penDraggedSquareY = -1;
				}
			}
		}
	}

	/**
	 * <p>
	 * Draw the page.
	 * </p>
	 * @param g Graphics object.
	 * @param x X coordinate.
	 * @param y Y coordinate.
	 * @param w Width.
	 * @param h Height.
	 */
	protected void drawPage(Graphics g, int x, int y, int w, int h) {
		super.drawBackground(g, x, y, w, h);
		//
		if (pageMargin != 0) { //is printing layout?
			//drawing the black border.
			g.setColor(0, 0, 0);
			g.drawRect(x, y, w -1, h -1);
		}
	}

	/**
	 * <p>
	 * Draw the text.
	 * </p>
	 * @param g Graphics object.
	 * @param x X coordinate.
	 * @param y Y coordinate.
	 * @param w Width.
	 * @param h Height.
	 */
	void drawText(Graphics g, int x, int y, int w, int h) {
		final int fontHeight = bodyFont.getHeight();
		final int count = textLines.length;
		int i = idxFirstVisibleLine;
		int drawnLinesCount = getLinesPerPage() +1;
		//
		if (count < drawnLinesCount) {
			y += 
				UIUtil.align(
					-1, count * fontHeight, h, textAlignment);
		}
		//
		g.setFont(bodyFont);
		g.setColor(bodyFontColor);
		//
		while (drawnLinesCount > 0) {
			drawLine(g, x, y, w, h, i);
			//
			drawnLinesCount--;
			y += fontHeight;
			i++;
			//
			if (i == count) {
				break;
			}
		}
	}
	
	/**
	 * <p>
	 * Draw a given line of text.
	 * </p>
	 * @param g Graphics object.
	 * @param x X coordinate.
	 * @param y Y coordinate.
	 * @param w Width.
	 * @param h Height.
	 * @param lineIndex Line index.
	 */
	void drawLine(Graphics g, int x, int y, int w, int h, int lineIndex) {
		if ((textAlignment & UIUtil.ALIGN_JUSTIFIED) != 0) { //is text justified?
			String[] words = (String[])textLines[lineIndex];
			final int len = words.length;
			final int space =
				(w - getWordsWidth(words, bodyFont)) / (len > 1 ? len -1 : 1);
			String word;
			//
	    	for (int i = 0; i < len; i++) {
	    		word = words[i];
	    		//
				g.drawString(word, x, y, Graphics.TOP | Graphics.LEFT);
				x += bodyFont.stringWidth(word) + space;
			}
		} else {
			String line = textLines[lineIndex].toString();
			g.drawString(
				line,
				x + UIUtil.align(bodyFont.stringWidth(line), -1, w, textAlignment),
				y,
				Graphics.TOP | Graphics.LEFT);
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
	protected void drawVerticalScrollBar(Graphics g, int x, int y, int w, int h) {
		UIUtil.drawVerticalScrollbar(
			g, 
			x, 
			y, 
			w, 
			h, 
			textLines.length, 
			getLinesPerPage(), 
			idxFirstVisibleLine, 
			vertScrollBarSquareRect, 
			scrollBarColor, 
			skin.getVerticalScrollBarStyle());
	}
	
	/**
	 * <p>
	 * Verify if the given coordinates are in the vertical scrollbar area.
	 * </p>
	 * @param x X coordinate.
	 * @param y Y coordinate.
	 * @return It is in the area (true) or not.
	 */
	boolean isVerticalScrollBarArea(int x, int y) {
		return vertScrollBarRect.contains(x, y);
	}

	/**
	 * <p>
	 * Verify if the given coordinates are in the vertical scrollbar square
	 * area.
	 * </p>
	 * @param x X coordinate.
	 * @param y Y coordinate.
	 * @return It is in the area (true) or not.
	 */
	boolean isVerticalScrollSquareArea(int x, int y) {
		return vertScrollBarSquareRect.contains(x, y);
	}
	
	/**
	 * <p>
	 * Verify if the given coordinates are in the text area.
	 * </p>
	 * @param x X coordinate.
	 * @param y Y coordinate.
	 * @return It is in the area (true) or not.
	 */
	boolean isTextArea(int x, int y) {
		return bodyRect.contains(x, y);
	}

	/**
	 * <p>
	 * Get the lines count that fits in the text box.
	 * </p>
	 * @return Lines count.
	 */
	int getLinesPerPage() {
        if (isShown()) {
            return textRect.h / bodyFont.getHeight();
        } else {
            return -1;
        }
	}
	
	/**
	 * <p>
	 * Split the given text up where each line fits in the given width.
	 * </p>
	 * @param text Text to be spplited.
	 * @param w Width.
	 * @return Lines.
	 */
	Object[] splitText(String text, int w) {
		String[] lines = UIUtil.splitString(text, w, bodyFont);
		//
		if ((textAlignment & UIUtil.ALIGN_JUSTIFIED) != 0) {
			String[][] linesAndWords = new String[lines.length][];
			for (int i = lines.length -1; i >= 0; i--) {
				linesAndWords[i] = StringUtil.split(lines[i], ' ');
			}
			return linesAndWords;
		} else {
			return lines;
		}
	}
	
	/**
	 * <p>
     * Scroll the line.
     * </p>
     * @param goup Scroll up (true) or down.
     * @param lcount Line count to scroll.
     */
    void scrollLines(boolean goup, int lcount) {
        if (goup) {
            if (!isFirstLineVisible()) {
                //while the first line is not visible, scroll it up.
            	idxFirstVisibleLine -= lcount;
            	if (idxFirstVisibleLine < 0) {
            		idxFirstVisibleLine = 0;
            	}
            	//
                requestRepaint();
            }
        } else {
            if (!isLastLineVisible()) {
                //while the last line is not visible, scroll it down.
            	final int idxfi = textLines.length - lcount; //idx first first line in the last page.
            	//
            	idxFirstVisibleLine += lcount;
            	if (idxFirstVisibleLine > idxfi) {
            		idxFirstVisibleLine = idxfi;
            	}
            	//
            	requestRepaint();
            }
        }
    }
    
	/**
     * <p>
     * Verify if the first line of the text is displyed on the screen.
     * </p>
     * @return Displayed or not.
     */
    boolean isFirstLineVisible() {
    	return idxFirstVisibleLine == 0;
    }

    /**
     * <p>
     * Verify if the last line of the text is displyed on the screen.
     * </p>
     * @return Displayed or not.
     */
    boolean isLastLineVisible() {
    	return idxFirstVisibleLine + getLinesPerPage() +1 > textLines.length;
    }
    
    /**
     * <p>
     * Get the sum of the width of all the given words.
     * </p>
     * @param words Words.
     * @param font Font.
     * @return Width.
     */
    private int getWordsWidth(String[] words, Font font) {
    	int w = 0;
    	for (int i = words.length -1; i >= 0; i--) {
			w += font.stringWidth(words[i]);
		}
    	return w;
    }
}