/*
 * DetailList.java
 * 19/04/2007
 * JME Framework
 * Copyright(c) Ernandes Mourao Junior (ernandes@gmail.com)
 * All rights reserved
 */
package br.framework.jme.ui;

import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;

import com.emobtech.uime.util.ui.UIUtil;


/**
 * <p>
 * This class implements a list which items can have details in order to
 * complement their information.
 * </p>
 * 
 * <pre>
 * List l = new List("Phone Book");
 * l.append(new Object[] {"Jorge Adams", "+1-202-203-8765", "jorge@adams.com"}, null);
 * l.append(new Object[] {"Michael James", "+1-402-802-1832", "michel@james.uk"}, null);
 * l.append(new Object[] {"Peter Steves", "+1-792-563-8345", "peter@steves.com"}, null);
 * </pre>
 * 
 * @author Ernandes Mourao Junior (ernandes@gmail.com)
 * @version 1.0
 * @since 1.0
 */
public class DetailList extends List {
	{
       	skin.applyDetailListSkin(this);
	}
	
	/**
	 * <p>
	 * Constructor.
	 * </p>
	 * @param title List title.
	 */
	public DetailList(String title) {
		super(title);
	}
	
	/**
	 * <p>
	 * Constructor.
	 * </p>
	 * @param title List title.
	 * @param type List type.
	 * @see br.framework.jme.ui.List#EXCLUSIVE
	 * @see br.framework.jme.ui.List#IMPLICIT
	 * @see br.framework.jme.ui.List#MULTIPLE
	 */
	public DetailList(String title, int type) {
		super(title, type);
	}
	
	/**
	 * <p>
	 * Append a given item and its details into the list.
	 * </p>
	 * @param items Items and details.
	 * @param imgs Images of the item and its details.
	 */
	public synchronized void append(Object items, Image[] imgs) {
        this.items.addElement(items);
        images.addElement(imgs);
        if (selectedIndex == -1) {
            selectedIndex = 0; //select the first index.
        }
        //
        requestRepaint();
	}
    
    /**
	 * <p>
	 * Set a new item and its details in a given index in the list.
	 * </p>
	 * @param itemIndex Index.
	 * @param items New items and details.
	 * @param imgs New images of the item and its details.
	 * @throws IllegalArgumentException itemIndex is an invalid index.
     */
    public synchronized void set(int itemIndex, Object items, Image[] imgs) {
        checkItemsBounds(itemIndex);
        //
        this.items.setElementAt(items, itemIndex);
        images.setElementAt(imgs, itemIndex);
        //
        requestRepaint();
    }
    
    /**
	 * <p>
	 * Insert a new item and its details in a given index in the list. The item
	 * in the given index and its details is shifted to the next index.
	 * </p>
	 * @param itemIndex Index.
	 * @param items New items and details.
	 * @param imgs New images of the item and its details.
	 * @throws IllegalArgumentException itemIndex is an invalid index.
     */
    public synchronized void insert(int itemIndex, Object items, Image[] imgs) {
        checkItemsBounds(itemIndex);
        //
        this.items.insertElementAt(items, itemIndex);
        images.insertElementAt(imgs, itemIndex);
        //
        requestRepaint();
    }

    /**
     * @inheritDoc
     * @see br.framework.jme.ui.List#setSelectedIndex(int)
     */
    public synchronized void setSelectedIndex(int itemIndex) {
        checkItemsBounds(itemIndex);
        //
        if (selectedIndex != itemIndex) {
            final int itemsPerPage = super.getItemsPerPage();
            if (itemIndex < idxFirstVisibleItem) {
                idxFirstVisibleItem = itemIndex;
            } else {
                if (itemsPerPage >= 0) {
                    int c = getItemLinesCount(itemIndex, false);
                    int t =
                        (itemIndex + c) - (idxFirstVisibleItem + itemsPerPage);
                    if (t > 0) {
                        idxFirstVisibleItem += t;
                    }
                } else {
                    idxFirstVisibleItem = itemIndex;
                }
            }
            selectedIndex = itemIndex;
            //
            onSelectionChange();
            requestRepaint();
        }
    }
    
    /**
     * @see br.framework.jme.ui.List#sort()
     */
    public synchronized void sort() {
    	sort(new DetailListSort());
    }
	
	/**
	 * <p>
	 * Get the details count plus the item itself of a given item. If the given
	 * index is not current selected one, this method will always return 1.
	 * </p>
	 * @param itemIndex Item index.
	 * @return Details count plus item.
	 */
	int getItemLinesCount(int itemIndex) {
        return getItemLinesCount(itemIndex, true);
	}
	
	/**
	 * @inheritDoc
	 * @see br.framework.jme.ui.List#getItemsPerPage()
	 */
	int getItemsPerPage() {
        if (isShown()) {
            return super.getItemsPerPage() - (getItemLinesCount(selectedIndex) -1);
        } else {
            return -1;
        }
	}
	
	/**
	 * @inheritDoc
	 * @see br.framework.jme.ui.List#getItemHeight(int)
	 */
	protected int getItemHeight(int itemIndex) {
		return super.getItemHeight() *
			(selectedIndex == itemIndex ? getItemLinesCount(itemIndex) : 1);
	}
    
    /**
     * @inheritDoc
     * @see br.framework.jme.ui.List#getPressedItem(int, int)
     */
    int getPressedItem(int x, int y) {
        int s = bodyRect.y;
        int i = idxFirstVisibleItem;
        while (s < y) {
            s += getItemHeight(i);
            i++;
        }
        return i -1;
    }
	
	/**
	 * @inheritDoc
	 * @see br.framework.jme.ui.List#scrollUpSelectedItem()
	 */
	void scrollUpSelectedItem() {
		final int size = items.size();
		if (size == 0) {
			return;
		}
		//
		if (selectedIndex > idxFirstVisibleItem) {
			selectedIndex--;
			int t =
				(selectedIndex + getItemLinesCount(selectedIndex)) -
				(idxFirstVisibleItem + super.getItemsPerPage());
			if (t > 0) {
				idxFirstVisibleItem += t;
			}
		} else if (idxFirstVisibleItem -1 >= 0) {
			idxFirstVisibleItem--;
			selectedIndex--;
		} else {
			selectedIndex = size -1;
			int t =
				(selectedIndex + getItemLinesCount(selectedIndex)) -
				(idxFirstVisibleItem + super.getItemsPerPage());
			idxFirstVisibleItem = t >= 0 ? t : 0;
		}
		//
		onSelectionChange();
		requestRepaint();
	}
	
	/**
	 * @inheritDoc
	 * @see br.framework.jme.ui.List#scrollDownSelectedItem()
	 */
	void scrollDownSelectedItem() {
		final int size = items.size();
		if (size == 0) {
			return;
		}
		//
		if (selectedIndex +1 < size) {
			selectedIndex++;
		} else {
			idxFirstVisibleItem = 0;
			selectedIndex = 0;
		}
		//
		int c = getItemLinesCount(selectedIndex);
		int t =
			(selectedIndex + c) - (idxFirstVisibleItem+super.getItemsPerPage());
		if (t > 0) {
			idxFirstVisibleItem += t;
		}
		//
		onSelectionChange();
		requestRepaint();
	}
	
	/**
	 * @inheritDoc
	 * @see br.framework.jme.ui.List#scrollUpPage()
	 */
	void scrollUpPage() {
		idxFirstVisibleItem--;
		selectedIndex = idxFirstVisibleItem;
		requestRepaint();
	}

	/**
	 * @inheritDoc
	 * @see br.framework.jme.ui.List#drawItem(javax.microedition.lcdui.Graphics, int, int, int, int, int, boolean)
	 */
	protected void drawItem(Graphics g, int x, int y, int w, int h,
		int itemIndex, boolean selected) {
		Object itemObj = items.elementAt(itemIndex);
		if (itemObj == null) {
			return;
		}
		//
		final int linesCount;
		Object[] lines;
		if (itemObj instanceof Object[]) {
			lines = (Object[])itemObj;
			linesCount = selected ? lines.length : 1;
		} else {
			lines = new Object[] {itemObj};
			linesCount = 1;
		}
		//
		int imgsCount = 0;
		Object imgObj = images.elementAt(itemIndex);
		Image[] imgs = null;
		if (imgObj != null && imgObj instanceof Image[]) {
			imgs = (Image[])imgObj;
			imgsCount = imgs.length;
		} else if (imgObj != null) {
			imgs = new Image[] {(Image)imgObj};
			imgsCount = 1;
		}
		//
		if (selected) {
			drawSelection(g, x, y, w, h);
		}
		//
		final int itemHeight = super.getItemHeight();
		final int imgSize = getBestSizeForImage();
		Image img = null;
		//
		for (int i = 0; i < linesCount; i++) {
			if (i < imgsCount && (img = imgs[i]) != null) {
				if (img.getWidth() != imgSize || img.getHeight() != imgSize) { //need to be resized?
					//resizing.
					img = UIUtil.resizeImage(img, imgSize, imgSize);
					//caching.
					if (imgObj instanceof Object[]) {
						imgs[i] = img;
					} else {
						images.setElementAt(img, itemIndex);
					}
				}
			}
			//
			drawItemLine(g, x, y, w, itemHeight, itemIndex, i, lines[i], img);
			//
			y += itemHeight;
			img = null;
		}
	}
	
	/**
	 * <p>
	 * Draw the lines (item plus details) of a given item.
	 * </p>
	 * @param g Graphics object.
	 * @param x X coordinate.
	 * @param y Y coordinate.
	 * @param w Width.
	 * @param h Height.
	 * @param itemIndex Item index.
	 * @param lineIndex Line index.
	 * @param item Item.
	 * @param img Image.
	 */
	void drawItemLine(Graphics g, int x, int y, int w, int h, int itemIndex,
		int lineIndex, Object item, Image img) {
		final int imgSize = getBestSizeForImage();
		//
		if (lineIndex == 0) {
			//drawing the check box or radio button.
			if ((type & MULTIPLE) != 0) {
				drawCheckBox(
					g,
					x,
					y,
					imgSize,
					imgSize,
					checkedItems.indexOf(items.elementAt(itemIndex)) != -1);
				x += imgSize;
				w -= imgSize;
			} else if ((type & EXCLUSIVE) != 0) {
				drawRadioButton(
					g,
					x,
					y,
					imgSize,
					imgSize,
					checkedItems.indexOf(items.elementAt(itemIndex)) != -1);
				x += imgSize;
				w -= imgSize;
			}
		}
		//drawing the image.
		if (img != null) {
			g.drawImage(
				img, 
				x, 
				y + UIUtil.align(-1,imgSize,h,UIUtil.ALIGN_MIDDLE), 
				Graphics.TOP | Graphics.LEFT);
			x += imgSize;
			w -= imgSize + OFFSET;
		}
		//drawing the text.
		if (item != null) {
			final boolean selected = itemIndex == selectedIndex; //item selected?
	        String text;
	        //
	        if (lineIndex == 0 && selected && rollerEnabled) { //first line and selected item?
		        roller.setText(item.toString(), bodyFont, w);
		        text = roller.getText();
	        } else {
	        	text = UIUtil.shrinkString(item.toString(), w, bodyFont);
	        }
	        //
			g.setFont(bodyFont);
			g.setColor(selected ? negativeItemFontColor : bodyFontColor);
			//
			g.drawString(
				text, 
				x + OFFSET, 
				y + UIUtil.align(
					-1, bodyFont.getHeight(), h, UIUtil.ALIGN_MIDDLE), 
				Graphics.TOP | Graphics.LEFT);
		}
	}
    
    /**
	 * <p>
	 * Get the details count plus the item itself of a given item. If the given
	 * index is not current selected one and the parameter "considerSelection"
	 * is true, this method will always return 1.
	 * </p>
	 * @param itemIndex Item index.
     * @param considerSelection Consider if the item of the given index is
     *                          selected.
	 * @return Details count plus item.
     */
    private int getItemLinesCount(int itemIndex, boolean considerSelection) {
        checkItemsBounds(itemIndex);
        //
        Object item = items.elementAt(itemIndex);
        if (item != null && item instanceof Object[]) {
            if (considerSelection) {
                return selectedIndex == itemIndex ? ((Object[])item).length : 1;
            } else {
                return ((Object[])item).length;
            }
        } else {
            return 1;
        }
    }
    
	/**
	 * <p>
	 * </p>
	 * 
	 * @author Ernandes Mourao Junior (ernandes@gmail.com)
	 * @version 1.0
	 * @since 1.0
	 */
	protected class DetailListSort extends ListQSort {
		/**
		 * @see com.emobtech.uime.util.QSort#getElement(java.lang.Object)
		 */
		protected Object getElement(Object ob) {
			return ob instanceof Object[] ? ((Object[])ob)[0] : ob;
		}
	}
}