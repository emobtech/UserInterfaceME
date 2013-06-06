/*
 * RSSList.java
 * 15/05/2008
 * JME Framework
 * Copyright(c) Ernandes Mourao Junior (ernandes@gmail.com)
 * All rights reserved
 */
package com.emobtech.uime.ui;

import javax.microedition.lcdui.Font;
import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;

import com.emobtech.uime.util.ui.UIUtil;


/**
 * <p>
 * </p>
 * 
 * @author Ernandes Mourao Junior (ernandes@gmail.com)
 * @version 1.0
 * @since 1.0
 */
//TODO: implementar suporte a stylus nesta classe.
public class RSSList extends List {
	/**
	 * 
	 */
	protected Font itemDescFont;
	
	/**
	 * 
	 */
	protected int itemDescColor;
	
	/**
	 * 
	 */
	protected int itemBorderColor;
	
	/**
	 * 
	 */
	protected int itemTitleBackgroundColor = -1;

	/**
	 * <p>
	 * </p>
	 * @param title
	 */
	public RSSList(String title) {
		super(title);
		//
		setBodyFont(
			Font.getFont(
				bodyFont.getFace(), Font.STYLE_BOLD, bodyFont.getSize()));
		setItemDescriptionFont(
			Font.getFont(bodyFont.getFace(), Font.STYLE_PLAIN,Font.SIZE_SMALL));
		//
		skin.applyRSSListSkin(this);
	}
	
	/**
	 * <p>
	 * </p>
	 * @param title
	 * @param desc
	 * @param image
	 */
	public synchronized void append(Object title, Object desc, Image image) {
		super.append(createItem(title, desc), image);
	}
	
	/**
	 * <p>
	 * </p>
	 * @param itemIndex
	 * @param title
	 * @param desc
	 * @param image
	 */
	public synchronized void set(int itemIndex, Object title, Object desc,
		Image image) {
		super.set(itemIndex, createItem(title, desc), image);
	}

	/**
	 * <p>
	 * </p>
	 * @param itemIndex
	 * @param title
	 * @param desc
	 * @param image
	 */
	public synchronized void insert(int itemIndex, Object title, Object desc,
		Image image) {
		super.insert(itemIndex, createItem(title, desc), image);
	}
	
    /**
	 * <p>
	 * Set the item's description color.
	 * </p>
	 * @param r Red.
	 * @param g Green.
	 * @param b Blue.
     */
    public void setItemDescriptionColor(int r, int g, int b) {
        itemDescColor = UIUtil.getHexColor(r, g, b);
    }
    
    /**
	 * <p>
	 * Set the item's border color.
	 * </p>
	 * @param r Red.
	 * @param g Green.
	 * @param b Blue.
     */
    public void setItemBorderColor(int r, int g, int b) {
        itemBorderColor = UIUtil.getHexColor(r, g, b);
    }

    /**
	 * <p>
	 * Set the item's title background color.
	 * </p>
	 * @param r Red.
	 * @param g Green.
	 * @param b Blue.
     */
    public void setItemTitleBackgroundColor(int r, int g, int b) {
        itemTitleBackgroundColor = UIUtil.getHexColor(r, g, b);
    }

    /**
	 * <p>
	 * Set the item's description font.
	 * </p>
	 * @param font Font.
	 */
	public void setItemDescriptionFont(Font font) {
		itemDescFont = font;
	}

	/**
	 * @see br.framework.jme.ui.List#getBestSizeForImage()
	 */
	public int getBestSizeForImage() {
		return (int)((getWidth() * 13) / 100);
	}
	
	/**
	 * @see br.framework.jme.ui.List#sort()
	 */
	public synchronized void sort() {
		sort(new RSSListSort());
	}
	
	/**
	 * @see br.framework.jme.ui.List#indexOf(java.lang.Object)
	 */
	public synchronized int indexOf(Object item) {
		for (int i = items.size() -1; i >= 0; i--) {
			if (((Object[])items.elementAt(i))[0].equals(item)) {
				return i;
			}
		}
		//
		return -1;
	}

	/**
	 * @see br.framework.jme.ui.List#getItemHeight()
	 */
	protected int getItemHeight() {
		return OFFSET * 5 + itemDescFont.getHeight() + bodyFont.getHeight();
	}
	
	/**
	 * @see br.framework.jme.ui.List#getItemHeight(int)
	 */
	protected int getItemHeight(int itemIndex) {
		return getItemHeight();
	}
	
	/**
	 * @see br.framework.jme.ui.List#drawList(javax.microedition.lcdui.Graphics, int, int, int, int)
	 */
	protected void drawList(Graphics g, int x, int y, int w, int h) {
		super.drawList(g, x + OFFSET, y, w - OFFSET -1, h);
	}
	
	/**
	 * @see br.framework.jme.ui.List#drawItem(javax.microedition.lcdui.Graphics, int, int, int, int, int, boolean)
	 */
	protected void drawItem(Graphics g, int x, int y, int w, int h,
		int itemIndex, boolean selected) {
		y += OFFSET;
		h -= OFFSET * 2;
		//
		if (selected) {
			drawSelection(g, x, y, w, h);
		}
		//
		final int IMG_SIZE = getBestSizeForImage();
		final boolean drawn = drawImage(g, x, y, IMG_SIZE, IMG_SIZE, itemIndex);
		//
		if (drawn) {
			x += IMG_SIZE;
			w -= IMG_SIZE;
		}
		//
		final int titleh = bodyFont.getHeight() + OFFSET;
		//
		if (!selected && itemTitleBackgroundColor != -1) {
			g.setColor(itemTitleBackgroundColor);
			g.fillRect(x, y, w, titleh);
		}
		//
		g.setColor(itemBorderColor);
		if (drawn) {
			g.drawRect(x - IMG_SIZE, y, w + IMG_SIZE -1, h);
		} else {
			g.drawRect(x, y, w -1, h);
		}
		//
		x += OFFSET;
		w -= OFFSET * 2;
		//
		Object[] item = (Object[])items.elementAt(itemIndex);
		//
		if (item[0] != null) {
			drawText(g, item[0], x, y, w, titleh, selected);
		}
		//
		if (item[1] != null) {
			y += titleh;
			h -= titleh;
			//
			UIUtil.drawString(
				g,
				x,
				y,
				w,
				h,
				item[1].toString(),
				itemDescFont,
				UIUtil.ALIGN_LEFT | UIUtil.ALIGN_MIDDLE,
				itemDescColor,
				true);
		}
	}
	
	/**
	 * <p>
	 * </p>
	 * @param title
	 * @param desc
	 * @return
	 */
	Object[] createItem(Object title, Object desc) {
		return new Object[] {title, desc};
	}
	
	/**
	 * <p>
	 * </p>
	 * 
	 * @author Ernandes Mourao Junior (ernandes@gmail.com)
	 * @version 1.0
	 * @since 1.0
	 */
	protected class RSSListSort extends ListQSort {
		/**
		 * @see com.emobtech.uime.util.QSort#getElement(java.lang.Object)
		 */
		protected Object getElement(Object ob) {
			return ((Object[])ob)[0];
		}
	}
}