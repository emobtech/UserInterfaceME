/*
 * DocList.java
 * 03/03/2008
 * JME Framework
 * Copyright(c) Ernandes Mourao Junior (ernandes@gmail.com)
 * All rights reserved
 */
package br.framework.jme.ui;

import javax.microedition.lcdui.Canvas;
import javax.microedition.lcdui.Graphics;

import com.emobtech.uime.util.ui.UIUtil;


/**
 * <p>
 * This class implements a list which items are displayed at the screen bottom
 * and the selected item is displayed in the middle of the screen ia larger
 * size.
 * </p>
 * 
 * @author Ernandes Mourao Junior (ernandes@gmail.com)
 * @version 1.0
 * @since 1.0
 */
//TODO: implementar suporte a stylus nesta classe.
public class DockList extends List {
	/**
	 * 
	 * @param title
	 */
	public DockList(String title) {
		super(title);
	}
	
	/**
	 * @see br.framework.jme.ui.Screen#setBodyFontColor(int, int, int)
	 */
	public void setBodyFontColor(int r, int g, int b) {
		bodyFontColor = UIUtil.getHexColor(r, g, b);
	}
	
	/**
	 * @see br.framework.jme.ui.List#setSelectionColor(int, int, int)
	 */
	public void setSelectionColor(int r, int g, int b) {
		selectionColor = UIUtil.getHexColor(r, g, b);
	}
	
	/**
	 * @see br.framework.jme.ui.List#keyHold(int)
	 */
	protected void keyHold(int keyCode) {
		int gameKey = getGameAction(keyCode);
		if (gameKey == Canvas.LEFT || gameKey == Canvas.RIGHT) {
			keyDown(keyCode);
		}
	}

	/**
	 * @see br.framework.jme.ui.List#keyDown(int)
	 */
	protected void keyDown(int keyCode) {
		keyCode = getGameAction(keyCode);
		//
		if (keyCode == LEFT || keyCode == UP) {
			scrollUpSelectedItem();
		} else if (keyCode == RIGHT || keyCode == DOWN) {
			scrollDownSelectedItem();
		}
	}

	/**
	 * @see br.framework.jme.ui.List#getItemHeight()
	 */
	protected int getItemHeight() {
		return (int)(bodyRect.w * 15) / 100;
	}
	
	/**
	 * @see br.framework.jme.ui.List#getItemsPerPage()
	 */
	int getItemsPerPage() {
		if (isShown()) {
			return bodyRect.w / getItemHeight();
		} else {
			return -1;
		}
	}
	
	/**
	 * @see br.framework.jme.ui.List#hasVerticalScrollBar()
	 */
	boolean hasVerticalScrollBar() {
		return false;
	}
	
	/**
	 * @see br.framework.jme.ui.List#drawList(javax.microedition.lcdui.Graphics, int, int, int, int)
	 */
	protected void drawList(Graphics g, int x, int y, int w, int h) {
		final int OFFSET2 = OFFSET * 2;
		final int size = items.size();
		final int itemSize = getItemHeight() - OFFSET2;
		//
		int i = idxFirstVisibleItem;
		int itemsToDraw = size > getItemsPerPage() ? getItemsPerPage() : size;
		int xx = x;
		int yy = y;
		//
		xx += //aligning images at the center.
			UIUtil.align(
				itemsToDraw * itemSize + OFFSET2 * (itemsToDraw -1),
				-1,
				w,
				UIUtil.ALIGN_CENTER);
		//aligning images at the bottom.
		yy += UIUtil.align(-1, itemSize, h, UIUtil.ALIGN_BOTTOM);
		yy -= OFFSET;
		//
		int iSizeTmp;
		//
		drawSelection(g, x, yy - OFFSET, w, itemSize + OFFSET +1);
		//
		while (itemsToDraw > 0) {
			itemsToDraw--;
			iSizeTmp = itemSize;
			//
			if (i == selectedIndex) {
				setCacheResizedImage(true);
				//
				drawItem(g, x, y, w, h - (y + h - yy) - OFFSET, i, true);
			}
			//
			if (i +1 == selectedIndex || selectedIndex +1 == i) { //is it right before or after the selected item?
				iSizeTmp -= (int)(iSizeTmp * 20) / 100; //decrease 20% of the size.
			} else if (i != selectedIndex) {
				iSizeTmp -= (int)(iSizeTmp * 40) / 100; //decrease 40% of the size.
			}
			//
			setCacheResizedImage(false);
			//
			drawImage(
				g, 
				xx + UIUtil.align(iSizeTmp, -1, itemSize, UIUtil.ALIGN_CENTER), 
				yy + UIUtil.align(-1, iSizeTmp, itemSize, UIUtil.ALIGN_MIDDLE), 
				iSizeTmp, 
				iSizeTmp, 
				i);
			//
			xx += itemSize + OFFSET2;
			//
			i++;
			if (i == size) {
				break;
			}
		}
	}
	
	/**
	 * @see br.framework.jme.ui.List#drawItem(javax.microedition.lcdui.Graphics, int, int, int, int, int, boolean)
	 */
	protected void drawItem(Graphics g, int x, int y, int w, int h,
		int itemIndex, boolean selected) {
		int textHeight = bodyFont.getHeight() + OFFSET;
		int imageSize = h - OFFSET * 2 - textHeight;
		//
		drawImage(
			g,
			x, 
			y,
			w,
			h,
			itemIndex);
		//
		Object item = items.elementAt(itemIndex);
		if (item != null) {
			String text = item.toString();
			//
			if (bodyFont.stringWidth(text) < w) {
				x += UIUtil.alignString(text, w, UIUtil.ALIGN_CENTER, bodyFont);
			}
			//
			drawText(
				g,
				item,
				x,
				y + OFFSET + imageSize + OFFSET,
				w,
				textHeight, 
				selected);
		}
	}
}