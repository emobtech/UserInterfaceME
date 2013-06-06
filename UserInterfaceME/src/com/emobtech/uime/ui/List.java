/*
 * List.java
 * 28/02/2007
 * JME Framework
 * Copyright(c) Ernandes Mourao Junior (ernandes@gmail.com)
 * All rights reserved
 */
package com.emobtech.uime.ui;

import java.util.Vector;

import javax.microedition.lcdui.Canvas;
import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;

import com.emobtech.uime.util.QSort;
import com.emobtech.uime.util.ui.Rect;
import com.emobtech.uime.util.ui.UIUtil;


/**
 * <p>
 * This class implements a basic list.
 * </p>
 * 
 * <pre>
 * List l = new List("Favorite Colors");
 * l.append("Red", null);
 * l.append("Yellow", null);
 * l.append("Black", null);
 * l.append("White", null);
 * </pre>
 * 
 * @author Ernandes Mourao Junior (ernandes@gmail.com)
 * @version 1.0
 * @since 1.0
 * @see br.framework.jme.ui.Grid
 * @see br.framework.jme.ui.DetailList
 */
public class List extends Screen {
	/**
	 * <p>
	 * Exclusive list.
	 * </p>
	 */
	public static final int EXCLUSIVE = 1;

	/**
	 * <p>
	 * Implicit list.
	 * </p>
	 */
	public static final int IMPLICIT = 2;

	/**
	 * <p>
	 * Multiple list.
	 * </p>
	 */
	public static final int MULTIPLE = 4;
	
	/**
	 * <p>
	 * Y coordinate where the pen was pressed initially for the vertical
	 * scrollbar drag.
	 * </p>
	 */
	private int penDraggedSquareY = -1;

	/**
	 * <p>
	 * X coordinate where the pen was pressed initially for the horizontal
	 * scrollbar drag.
	 * </p>
	 */
	private int penDraggedSquareX = -1;

    /**
	 * <p>
	 * Items area has been pressed or not.
	 * </p>
	 */
	boolean itemsAreaPressed;

	/**
	 * <p>
	 * List type.
	 * </p>
	 */
	int type;
	
	/**
	 * <p>
	 * Vector that holds the added items.
	 * </p>
	 */
	protected Vector items;
	
	/**
	 * <p>
	 * Vector that holds the checked items.
	 * </p>
	 */
	Vector checkedItems;

	/**
	 * <p>
	 * Vector that holds the added images.
	 * </p>
	 */
	protected Vector images;

	/**
	 * <p>
	 * Index of the selected item.
	 * </p>
	 */
	int selectedIndex = -1;

	/**
	 * <p>
	 * Index of the first item being displayed.
	 * </p>
	 */
	int idxFirstVisibleItem;

	/**
	 * <p>
	 * Scrollbar color.
	 * </p>
	 */
	int scrollBarColor;

	/**
	 * <p>
     * Selection color.
     * </p>
     */
    int selectionColor;
    
    /**
     * <p>
     * Negative item font color. Displayed when the item is selected.
     * </p>
     */
    int negativeItemFontColor;
    
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
     * Horizontal scrollbar rectangle.
     * </p>
     */
    Rect horiScrollBarRect;

    /**
	 * <p>
	 * Horizontal scrollbar square rectangle.
	 * </p>
	 */
	Rect horiScrollBarSquareRect;
	
	/**
	 * <p>
	 * Check box or radio button rect.
	 * </p>
	 */
	Rect checkBoxRect;

	/**
	 * <p>
	 * Object responsible to roll the value of the selected item in case it does
	 * not feet in the item width.
	 * </p>
	 */
	RollerName roller;
    
    /**
     * <p>
     * Enables the usage of the roller for the items name.
     * </p>
     */
    boolean rollerEnabled = true;
    
    /**
     * <p>
     * Enables the usage of the circular scroll.
     * </p>
     */
    boolean circularScrollEnabled = true;
    
	/**
	 * <p>
	 * Last key pressed from keypad.
	 * </p>
	 */
	int lastPressedKeyPadKey;
	
	/**
	 * <p>
	 * Keypad keys' offset.
	 * </p>
	 */
	int offKeyPadKey;
	
	/**
	 * <p>
	 * Last keypad keys' event.
	 * </p>
	 */
	long lastKeyPadEvent;
	
	/**
	 * <p>
	 * Cache resized images.
	 * </p>
	 */
	boolean cacheResizedImage = true;

	/**
	 * <p>
	 * Constructor.
	 * </p>
	 * @param title List title.
	 */
	public List(String title) {
		this(title, IMPLICIT);
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
	public List(String title, int type) {
		super(title);
		this.type = type;
		if ((type & IMPLICIT) == 0) {
			checkedItems = new Vector();
			checkBoxRect = new Rect();
		}
		items = new Vector(10);
		images = new Vector(10);
		//
		vertScrollBarRect = new Rect();
		vertScrollBarSquareRect = new Rect();
		horiScrollBarRect = new Rect();
		horiScrollBarSquareRect = new Rect();
		//
		skin.applyListSkin(this);
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
	 * Set the selection color.
	 * </p>
	 * @param r Red.
	 * @param g Green.
	 * @param b Blue.
     */
    public void setSelectionColor(int r, int g, int b) {
        selectionColor = UIUtil.getHexColor(r, g, b);
        negativeItemFontColor = UIUtil.getNegativeColor(r, g, b);
    }

	/**
	 * <p>
	 * Append a given item into the list.
	 * </p>
	 * @param item Item.
	 * @param img Image.
	 */
	public synchronized void append(Object item, Image img) {
		items.addElement(item);
		images.addElement(img);
		if (selectedIndex == -1) {
			selectedIndex = 0; //select the first index.
		}
        //
		requestRepaint();
	}
	
	/**
	 * <p>
	 * Set a new item in a given index in the list.
	 * </p>
	 * @param itemIndex Index.
	 * @param item New item.
	 * @param img New image.
	 * @throws IllegalArgumentException itemIndex is an invalid index.
	 */
	public synchronized void set(int itemIndex, Object item, Image img) {
		checkItemsBounds(itemIndex);
        //
		items.setElementAt(item, itemIndex);
		images.setElementAt(img, itemIndex);
        //
		requestRepaint();
	}
	
	/**
	 * <p>
	 * Insert a new item in a given index in the list. The item in the given
	 * index is shifted to the next index.
	 * </p>
	 * @param itemIndex Index.
	 * @param item New item.
	 * @param img New image.
	 * @throws IllegalArgumentException itemIndex is an invalid index.
	 */
	public synchronized void insert(int itemIndex, Object item, Image img) {
		checkItemsBounds(itemIndex);
        //
		items.insertElementAt(item, itemIndex);
		images.insertElementAt(img, itemIndex);
		//TODO: consertar o avanço do item selecionado ao inserir um novo.
        //
		requestRepaint();
	}
	
	/**
	 * <p>
	 * Get the index of the selected item.
	 * </p>
	 * @return Index.
	 */
	public synchronized int getSelectedIndex() {
		return selectedIndex;
	}
	
	/**
	 * <p>
	 * Set the index of the selected item.
	 * </p>
	 * @param itemIndex Index.
	 * @throws IllegalArgumentException itemIndex is an invalid index.
	 */
	public synchronized void setSelectedIndex(int itemIndex) {
		checkItemsBounds(itemIndex);
        //
        if (selectedIndex != itemIndex) {
            final int itemsPerPage = getItemsPerPage();
            //
            if (itemIndex < idxFirstVisibleItem || itemsPerPage < 0) {
                idxFirstVisibleItem = itemIndex;
                selectedIndex = itemIndex;
            } else {
                if (itemIndex >= idxFirstVisibleItem) {
                    if (itemIndex < idxFirstVisibleItem + itemsPerPage ||
                        itemsPerPage == 0) {
                        //item between the visible items.
                    	selectedIndex = itemIndex;
                    } else {
                    	selectedIndex = itemIndex;
                        //index after the last item shown. move items down.
                        idxFirstVisibleItem +=
                        	itemIndex -
                        		(idxFirstVisibleItem + itemsPerPage -1);
                    }
                } else {
                    //index before the first item shown. move items up.
                	idxFirstVisibleItem = itemIndex;
                	selectedIndex = itemIndex;
                }
            }
            //
            onSelectionChange();
            requestRepaint();
        }
	}

	/**
	 * <p>
	 * Get the item associated to the given index.
	 * </p>
	 * @param itemIndex Index.
	 * @return Item object.
	 * @throws IllegalArgumentException itemIndex is an invalid index.
	 */
	public synchronized Object get(int itemIndex) {
		checkItemsBounds(itemIndex);
        //
		return items.elementAt(itemIndex);
	}
	
	/**
	 * <p>
	 * Returns the value of the selected item. If there is no item selected,
	 * <code>null</code> will be returned.
	 * </p>
	 * @return Value.
	 */
	public synchronized Object getSelectedValue() {
		if (selectedIndex != -1) {
			return items.elementAt(selectedIndex);
		} else {
			return null;
		}
	}
	
	/**
	 * <p>
	 * Get all the items of the list.
	 * </p>
	 * @return Items.
	 */
	public synchronized Object[] getItems() {
		Object[] its = new Object[items.size()];
		items.copyInto(its);
		return its;
	}

	/**
	 * <p>
	 * Delete an item based on given index.
	 * </p>
	 * @param itemIndex Index.
	 * @throws IllegalArgumentException itemIndex is an invalid index.
	 */
	public synchronized void delete(int itemIndex) {
		checkItemsBounds(itemIndex);
        //
        items.removeElementAt(itemIndex);
        images.removeElementAt(itemIndex);
        if (itemIndex < selectedIndex) {
            setSelectedIndex(selectedIndex -1);
        }
        //
		requestRepaint();
	}

	/**
	 * <p>
	 * Remove all the items from the list.
	 * </p>
	 */
	public synchronized void deleteAll() {
		items.removeAllElements();
		images.removeAllElements();
		selectedIndex = -1;
        idxFirstVisibleItem = 0;
        //
		requestRepaint();
	}
	
	/**
	 * <p>
	 * Count of items in the list.
	 * </p>
	 * @return Size.
	 */
	public synchronized int size() {
		return items.size();
	}
	
	/**
	 * <p>
	 * Sort the items of the list.
	 * </p>
	 */
	public synchronized void sort() {
		sort(new ListQSort());
	}
	
	/**
	 * 
	 * @param sorter
	 */
	protected synchronized void sort(QSort sorter) {
		final int size = items.size();
		//
		if (size > 0) {
			Object[] itemsSort = new Object[size];
			items.copyInto(itemsSort);
			//
			sorter.quicksort(itemsSort, 0, size -1);
			//
			items.removeAllElements();
			//
	        for (int i = 0; i < size; i++) {
	        	items.addElement(itemsSort[i]);
	        }
	        //
	        idxFirstVisibleItem = 0;
	        selectedIndex = 0;
		}
	}
	
	/**
	 * <p>
	 * Set a given item as checked.
	 * </p>
	 * @param itemIndex Item index.
	 * @param checked Checked or not.
	 * @throws IllegalArgumentException itemIndex is an invalid index.
	 * @throws IllegalStateException List type is not EXCLUSIVE or MULTIPLE.
	 */
	public synchronized void setChecked(int itemIndex, boolean checked) {
		if ((type & IMPLICIT) == 0) {
			checkItemsBounds(itemIndex);
			//
			manageChecking(
				itemIndex, checked ? new Boolean(true) : new Boolean(false));
		} else {
			throw new IllegalStateException(
				"This method just can be invoked if the list type is " +
				"EXCLUSIVE or MULTIPLE.");
		}
	}
	
	/**
	 * <p>
	 * Verify if a given item is checked or not.
	 * </p>
	 * @param itemIndex Item index.
	 * @return Checked or not.
	 * @throws IllegalArgumentException itemIndex is an invalid index.
	 * @throws IllegalStateException List type is not EXCLUSIVE or MULTIPLE.
	 */
	public synchronized boolean isChecked(int itemIndex) {
		if ((type & IMPLICIT) == 0) {
			checkItemsBounds(itemIndex);
			//
			return checkedItems.indexOf(items.elementAt(itemIndex)) != -1;
		} else {
			throw new IllegalStateException(
				"This method just can be invoked if the list type is " +
				"EXCLUSIVE or MULTIPLE.");
		}
	}
	
	/**
	 * <p>
	 * Get the items marked as checked.
	 * </p>
	 * @return Checked items.
	 */
	public synchronized Object[] getCheckedItems() {
		if ((type & IMPLICIT) == 0) {
			Object[] ci = new Object[checkedItems.size()];
			checkedItems.copyInto(ci);
			return ci;
		} else {
			return new Object[0];
		}
	}
	
	/**
	 * <p>
	 * Get the best size for an image to be used by the list.
	 * </p>
	 * <p>
	 * <b>This method is import because you can save a reasonable amount of
	 * memory if you use images that are already of the size which the list
	 * uses.</b>
	 * </p>
	 * @return Best image size.
	 */
	public int getBestSizeForImage() {
		return getItemHeight();
	}
    
    /**
     * <p>
     * Set the usage of the roller responsible for rolling the items name
     * enabled or not.
     * </p>
     * @param enabled Enabled or not.
     */
    public synchronized void setRoller(boolean enabled) {
        if (!rollerEnabled && enabled) {
            startRoller(true);
        } else if (rollerEnabled && !enabled) {
            startRoller(false);
        }
        //
        rollerEnabled = enabled;
    }
    
    /**
     * <p>
     * Sets the usage of circular scroll enabled or not.
     * </p>
     * @param enabled Enabled or not.
     */
    public void setCircularScroll(boolean enabled) {
    	circularScrollEnabled = enabled;
    }
    
    /**
     * <p>
     * Finds the index of the given item. If the item is not found, -1 will be
     * returned.
     * </p>
     * @param item Item.
     * @return Item index.
     */
    public synchronized int indexOf(Object item) {
    	return items.indexOf(item);
    }
    
    /**
     * @inheritDoc
	 * @see javax.microedition.lcdui.Canvas#showNotify()
	 */
	protected void showNotify() {
		super.showNotify();
		//
        if (rollerEnabled) {
            startRoller(true);
        }
	}
	
	/**
	 * @inheritDoc
	 * @see javax.microedition.lcdui.Canvas#hideNotify()
	 */
	protected void hideNotify() {
		super.hideNotify();
		//
        if (rollerEnabled) {
            startRoller(false);
        }
	}

	/**
	 * @inheritDoc
	 * @see br.framework.jme.ui.Screen#drawBody(javax.microedition.lcdui.Graphics, int, int, int, int)
	 */
	protected synchronized void drawBody(Graphics g, int x, int y, int w,
		int h) {
		if (items.size() > 0) {
			final boolean hasVSB = hasVerticalScrollBar();
			final boolean hasHSB = hasHorizontalScrollBar();
			//
			if (hasVSB) {
				vertScrollBarRect.w = 
					hasVSB 
						? UIUtil.getScrollbarSize(w, h, hasPointerEvents()) : 0;
				vertScrollBarRect.x = x + w - vertScrollBarRect.w;
				vertScrollBarRect.y = y;
				vertScrollBarRect.h = h - horiScrollBarRect.h;
				//
				g.setClip(
					vertScrollBarRect.x,
					vertScrollBarRect.y,
					vertScrollBarRect.w,
					vertScrollBarRect.h);
				drawVerticalScrollBar(
					g,
					vertScrollBarRect.x,
					vertScrollBarRect.y,
					vertScrollBarRect.w,
					vertScrollBarRect.h);
				//
				bodyRect.w -= vertScrollBarRect.w;
			} else {
				vertScrollBarRect.x = 0;
				vertScrollBarRect.y = 0;
				vertScrollBarRect.w = 0;
				vertScrollBarRect.h = 0;
				//
				bodyRect.w = w;
			}
			//
			if (hasHSB) {
				horiScrollBarRect.h = 
					hasHSB 
						? UIUtil.getScrollbarSize(w, h, hasPointerEvents()) : 0;
				horiScrollBarRect.x = x;
				horiScrollBarRect.y = y + h - horiScrollBarRect.h;
				horiScrollBarRect.w = w - vertScrollBarRect.w;
				//
				g.setClip(
					horiScrollBarRect.x,
					horiScrollBarRect.y,
					horiScrollBarRect.w,
					horiScrollBarRect.h);
				drawHorizontalScrollBar(
					g,
					horiScrollBarRect.x,
					horiScrollBarRect.y,
					horiScrollBarRect.w,
					horiScrollBarRect.h);
				//
				bodyRect.h -= horiScrollBarRect.h;
			} else {
				horiScrollBarRect.x = 0;
				horiScrollBarRect.y = 0;
				horiScrollBarRect.w = 0;
				horiScrollBarRect.h = 0;
				//
				bodyRect.h = h;
			}
			//
			g.setClip(x, y, bodyRect.w, bodyRect.h);
			drawList(g, x, y, bodyRect.w, bodyRect.h);
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
		if (keyCode == Canvas.KEY_NUM2 || keyCode == Canvas.KEY_NUM3 || 
			keyCode == Canvas.KEY_NUM4 || keyCode == Canvas.KEY_NUM5 ||
			keyCode == Canvas.KEY_NUM6 || keyCode == Canvas.KEY_NUM7 ||
			keyCode == Canvas.KEY_NUM8 || keyCode == Canvas.KEY_NUM9) {
			int index = findItemStartsWith(getLetterFromPressedKey(keyCode));
			//
			if (index != -1) {
				setSelectedIndex(index);
			}
		} else {
			keyCode = getGameAction(keyCode);
			if (keyCode == UP) {
				scrollUpSelectedItem();
			} else if (keyCode == DOWN) {
	            scrollDownSelectedItem();
			} else if (keyCode == FIRE) {
				if ((type & IMPLICIT) == 0) {
					manageChecking(selectedIndex, null);
				}
			}
		}
	}
	
	/**
	 * @inheritDoc
	 * @see br.framework.jme.ui.Screen#penUp(int, int)
	 */
	protected void penUp(int x, int y) {
		if (isItemsArea(x, y) && itemsAreaPressed) {
			final int newSelIndex = getPressedItem(x, y);
			if (newSelIndex < items.size()) {
                setSelectedIndex(newSelIndex);
				if (newSelIndex >= idxFirstVisibleItem + getItemsPerPage()) {
					//item selected is not totally visible.
					scrollDownSelectedItem();
                    setSelectedIndex(newSelIndex);
				}
				if (isCheckBoxArea(x, y)) {
					manageChecking(newSelIndex, null);
				}
				requestRepaint();
			}
		} else if (!isVerticalScrollSquareArea(x, y) &&
				   isVerticalScrollBarArea(x, y)) {
			if (y < vertScrollBarSquareRect.y) {
				scrollUpPage();
			} else {
				scrollDownPage();
			}
		} else if (!isHorizontalScrollSquareArea(x, y) &&
				   isHorizontalScrollBarArea(x, y)) {
			if (x < horiScrollBarSquareRect.x) {
				scrollLeftPage();
			} else {
				scrollRightPage();
			}
		}
		//reset control variables.
		penDraggedSquareY = -1;
		penDraggedSquareX = -1;
		itemsAreaPressed = false;
	}
	
	/**
	 * @inheritDoc
	 * @see br.framework.jme.ui.Screen#penDragged(int, int)
	 */
	protected void penDragged(int x, int y) {
		if (isVerticalScrollSquareArea(x, y)) {
			penDraggedSquareY = y;
		} else if (isVerticalScrollBarArea(x, y)) {
			if (penDraggedSquareY != -1) {
			    //pen left the square area but it is still in the scrollbar area.
				if (y < penDraggedSquareY) {
					scrollUpPage();
				} else {
					scrollDownPage();
				}
				penDraggedSquareY = -1;
			}
		} else if (isHorizontalScrollSquareArea(x, y)) {
			penDraggedSquareX = x;
		} else if (isHorizontalScrollBarArea(x, y)) {
			if (penDraggedSquareX != -1) {
			    //pen left the square area but it is still in the scrollbar area.
				if (x < penDraggedSquareX) {
					scrollLeftPage();
				} else {
					scrollRightPage();
				}
				penDraggedSquareX = -1;
			}
		}
	}
	
	/**
	 * @inheritDoc
	 * @see br.framework.jme.ui.Screen#penDown(int, int)
	 */
	protected void penDown(int x, int y) {
		if (isItemsArea(x, y)) {
			itemsAreaPressed = true;
		}
	}

	/**
	 * <p>
	 * Trigger the event notifying that the current selection has been changed.
	 * </p>
	 */
	protected void onSelectionChange() {
	}
	
	/**
	 * @inheritDoc
	 * @see br.framework.jme.ui.Screen#sizeChanged(int, int)
	 */
	protected void sizeChanged(int w, int h) {
		super.sizeChanged(w, h);
		//
		//reorganizing the selected item.
		if (items.size() > 0) {
			int lastIdx = selectedIndex;
			setSelectedIndex(0);
			setSelectedIndex(lastIdx);
		}
	}
	
	/**
	 * <p>
	 * Draw the list.
	 * </p>
	 * @param g Graphics object.
	 * @param x X coordinate.
	 * @param y Y coordinate.
	 * @param w Width.
	 * @param h Height.
	 */
	protected void drawList(Graphics g, int x, int y, int w, int h) {
		final int size = items.size();
		//
		int i = idxFirstVisibleItem;
		int drawnItemsCount = getItemsPerPage() +1;
		int iHeight;
		int yy = y;
		//
		while (drawnItemsCount > 0) {
			drawnItemsCount--;
			iHeight = getItemHeight(i);
			//
			if (drawnItemsCount != 0) {
				g.setClip(x, yy, w, iHeight);
			} else {
				//set clip area height as the amount of pixels to reach bottom of the body.
				g.setClip(x, yy, w, y + h - yy);
			}
			drawItem(g, x, yy, w, iHeight, i, i == selectedIndex);
			//
			yy += iHeight;
			i++;
			if (i == size) {
				break;
			}
		}
	}

	/**
	 * <p>
	 * Draw the selection bar on the selected item.
	 * </p>
	 * @param g Graphics object.
	 * @param x X coordinate.
	 * @param y Y coordinate.
	 * @param w Width.
	 * @param h Height.
	 */
	protected void drawSelection(Graphics g, int x, int y, int w, int h) {
		UIUtil.fillRect(
			g,
			x,
			y,
			w,
			h,
			selectionColor,
			skin.getSelectionStyle());
	}

	/**
	 * <p>
	 * Draw a given item.
	 * </p>
	 * @param g Graphics object.
	 * @param x X coordinate.
	 * @param y Y coordinate.
	 * @param w Width.
	 * @param h Height.
	 * @param itemIndex Index of the item to be drawn.
	 * @param selected If the given item is selected or not.
	 */
	protected void drawItem(Graphics g, int x, int y, int w, int h,
		int itemIndex, boolean selected) {
		if (selected) {
			drawSelection(g, x, y, w, h);
		}
		//
		final int IMG_SIZE = getBestSizeForImage();
		//
		if ((type & MULTIPLE) != 0) {
			final boolean checked =
				checkedItems.indexOf(items.elementAt(itemIndex)) != -1;
			//
			drawCheckBox(g, x, y, IMG_SIZE, IMG_SIZE, checked);
			//
			x += IMG_SIZE;
			w -= IMG_SIZE;
		} else if ((type & EXCLUSIVE) != 0) {
			final boolean checked =
				checkedItems.indexOf(items.elementAt(itemIndex)) != -1;
			//
			drawRadioButton(g, x, y, IMG_SIZE, IMG_SIZE, checked);
			//
			x += IMG_SIZE;
			w -= IMG_SIZE;
		}
		//
		final boolean drawn =
			drawImage(
				g,
				x,
				y,
				IMG_SIZE,
				IMG_SIZE,
				itemIndex);
		//
		if (drawn) {
			x += IMG_SIZE;
			w -= IMG_SIZE + OFFSET;
		}
		//
		drawText(g, items.elementAt(itemIndex), x, y, w, h, selected);
	}
	
	/**
	 * <p>
	 * Draw a given text.
	 * </p>
	 * @param g Graphics object.
	 * @param item Item to be painted.
	 * @param x X coordinate.
	 * @param y Y coordinate.
	 * @param w Width.
	 * @param h Height.
	 * @param itemIndex Index of the item to be drawn.
	 * @param selected If the given item is selected or not.
	 */
	protected void drawText(Graphics g, Object item, int x, int y, int w, int h,
		boolean selected) {
		if (item != null) {
			g.setFont(bodyFont);
	        g.setColor(selected ? negativeItemFontColor : bodyFontColor);
	        //
	        String text;
	        //
	        if (selected && rollerEnabled) {
		        roller.setText(item.toString(), bodyFont, w);
		        text = roller.getText();
	        } else {
	        	text = UIUtil.shrinkString(item.toString(), w, bodyFont);
	        }
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
	 * Draw a given image.
	 * </p>
	 * @param g Graphics object.
	 * @param x X coordinate.
	 * @param y Y coordinate.
	 * @param w Width.
	 * @param h Height.
	 * @param imgIndex Index of the image to be drawn.
	 * @return Drawn or not.
	 */
	protected boolean drawImage(Graphics g, int x, int y, int w, int h,
		int imgIndex) {
		Image img = (Image)images.elementAt(imgIndex);
		//
		if (img != null) {
			//verify if the image needs to be resized.
			if (img.getWidth() > w || img.getHeight() > h) {
				img = UIUtil.resizeImage(img, w, h);
				//
				if (cacheResizedImage) {
					images.setElementAt(img, imgIndex);
				}
			}
			//
			g.drawImage(
				img,
				x + UIUtil.align(img.getWidth(), -1, w, UIUtil.ALIGN_CENTER),
				y + UIUtil.align(-1, img.getHeight(), h, UIUtil.ALIGN_MIDDLE),
				Graphics.TOP | Graphics.LEFT);
			//
			return true;
		} else {
			return false;
		}
	}
	
	/**
	 * <p>
	 * Draw a check box.
	 * </p>
	 * @param g Graphics object.
	 * @param x X coordinate.
	 * @param y Y coordinate.
	 * @param w Width.
	 * @param h Height.
	 * @param checked Checked or not.
	 */
	void drawCheckBox(Graphics g, int x, int y, int w, int h, boolean checked) {
		y += (h - (h - (OFFSET * 4))) / 2;
		x += (x - (x - (OFFSET * 4))) / 2;
		w -= OFFSET * 4;
		h -= OFFSET * 4;
		checkBoxRect.x = x;
		checkBoxRect.w = w;
		checkBoxRect.h = h;
		//drawing the background of the box.
		g.setColor(255, 255, 255); //white color.
		g.fillRect(x, y, w, h);
		//drawing the inner border.
		g.setColor(210, 210, 210); //brighter gray color.
		g.drawRect(x, y, w, h);
		//
		if (checked) {
			//drawing the X.
			final int borderCount = 2;
			g.setColor(selectionColor);
			//
			for (int i = 0; i < borderCount; i++) {
				g.drawLine(x + i, y, x + w, y + h - i);
				g.drawLine(x + i, y + h, x + w, y + i);
				//
				if (i > 0) {
					//used to make the "X" larger.
					g.drawLine(x, y + i, x + w - i, y + h);
					g.drawLine(x, y + h - i, x + w - i, y);
				}
			}
		}
		//
		//drawing outter border.
		g.setColor(90, 90, 90); //darker gray color
		g.drawRect(x -1, y -1, w +2, h +2);
	}

	/**
	 * <p>
	 * Draw a radio button.
	 * </p>
	 * @param g Graphics object.
	 * @param x X coordinate.
	 * @param y Y coordinate.
	 * @param w Width.
	 * @param h Height.
	 * @param checked Checked or not.
	 */
	void drawRadioButton(Graphics g, int x, int y, int w, int h,
		boolean checked) {
		y += (h - (h - (OFFSET * 4))) / 2;
		x += (x - (x - (OFFSET * 4))) / 2;
		w -= OFFSET * 4;
		h -= OFFSET * 4;
		checkBoxRect.x = x;
		checkBoxRect.w = w;
		checkBoxRect.h = h;
		//drawing the background of the circle.
		g.setColor(255, 255, 255); //white color.
		g.fillArc(x, y, w, h, 0, 360);
		//drawing the inner border.
		g.setColor(210, 210, 210); //brighter gray color.
		g.drawArc(x, y, w, h, 0, 360);
		//drawing outter border.
		g.setColor(90, 90, 90); //darker gray color
		g.drawArc(x -1, y -1, w +2, h +2, 0, 360);
		//
		if (checked) {
			x += OFFSET;
			y += OFFSET;
			w -= OFFSET * 2;
			h -= OFFSET * 2;
			//drawing the internal circle.
			g.setColor(selectionColor);
			g.fillArc(x, y, w, h, 0, 360);
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
			items.size(), 
			getItemsPerPage(), 
			idxFirstVisibleItem, 
			vertScrollBarSquareRect, 
			scrollBarColor, 
			skin.getVerticalScrollBarStyle());
	}

	/**
	 * <p>
	 * Draw the horizontal scrollbar.
	 * </p>
	 * @param g Graphics object.
	 * @param x X coordinate.
	 * @param y Y coordinate.
	 * @param w Width.
	 * @param h Height.
	 */
	protected void drawHorizontalScrollBar(Graphics g, int x, int y, int w,
		int h) {
	}

	/**
	 * <p>
	 * Get the height of a item in the list.
	 * </p>
	 * @return Height.
	 */
	protected int getItemHeight() {
		return bodyFont.getHeight() + OFFSET;
	}
	
	/**
	 * <p>
	 * Get the height of a given item including its details. If the given index
	 * is not current selected one, this method will always return only the item
	 * height.
	 * </p>
	 * @param itemIndex Item index.
	 * @return Item height plus its details.
	 */
	protected int getItemHeight(int itemIndex) {
		return getItemHeight();
	}
	
	/**
	 * <p>
	 * Get the items count that fits in the screen.
	 * </p>
	 * @return Items count.
	 */
	int getItemsPerPage() {
        if (isShown()) {
            return bodyRect.h / getItemHeight();
        } else {
            return -1;
        }
	}

	/**
	 * <p>
	 * Verify if the list needs a vertical scrollbar.
	 * <p>
	 * @return Need or not.
	 */
	boolean hasVerticalScrollBar() {
		return items.size() > getItemsPerPage();
	}

	/**
	 * <p>
	 * Verify if the list needs a horizontal scrollbar.
	 * <p>
	 * @return Need or not.
	 */
	boolean hasHorizontalScrollBar() {
		return false;
	}

	/**
	 * <p>
	 * Scroll up the page.
	 * </p>
	 */
	void scrollUpPage() {
	    //dragged up.
		idxFirstVisibleItem--;
		final int lastIdx = idxFirstVisibleItem + getItemsPerPage() -1;
		if (selectedIndex > lastIdx) {
			selectedIndex = lastIdx;
		}
		requestRepaint();
	}

	/**
	 * <p>
	 * Scroll down the page.
	 * </p>
	 */
	void scrollDownPage() {
	    //dragged down.
		idxFirstVisibleItem++;
		if (selectedIndex < idxFirstVisibleItem) {
			selectedIndex = idxFirstVisibleItem;
		}
		requestRepaint();
	}
	
	/**
	 * <p>
	 * Scroll left the page.
	 * </p>
	 */
	void scrollLeftPage() {
	}

	/**
	 * <p>
	 * Scroll right the page.
	 * </p>
	 */
	void scrollRightPage() {
	}

	/**
	 * <p>
	 * Scroll up the selected item.
	 * </p>
	 */
	void scrollUpSelectedItem() {
		if (items.size() == 0) {
			return;
		}
        if (selectedIndex > idxFirstVisibleItem) {
            //while the selection does not reach the first visible item, just
            //select the previous one.
        	selectedIndex--;
        } else {
            if (idxFirstVisibleItem -1 >= 0) {
                //while the first item is not visible, scroll it up.
            	idxFirstVisibleItem--;
            	selectedIndex--;
            } else if (circularScrollEnabled) {
                //the first item was reached, selected the last one.
                //circular scrolling.
                int size = items.size();
                int itemsPerPage = getItemsPerPage();
                idxFirstVisibleItem =
                    size <= itemsPerPage ? 0 : size - itemsPerPage;
                selectedIndex = size -1;
            } else {
            	return;
            }
        }
        //
        onSelectionChange();
        requestRepaint();
	}
	
	/**
	 * <p>
	 * Scroll down the selected item.
	 * </p>
	 */
	void scrollDownSelectedItem() {
		final int size = items.size();
		if (size == 0) {
			return;
		}
        int idxLastShownItem;
        int itemsPerPage = getItemsPerPage();
        if (itemsPerPage < size) {
            idxLastShownItem = idxFirstVisibleItem + itemsPerPage;
        } else {
            //there is less items than count visible.
            idxLastShownItem = idxFirstVisibleItem + size;
        }
        if (selectedIndex < idxLastShownItem -1) {
            //while the selection does not reach the last visible item, just
            //select the next one.
            selectedIndex++;
        } else {
            if (idxLastShownItem +1 <= size) {
                //while the last item is not visible, scroll it down.
            	idxFirstVisibleItem++;
                selectedIndex++;
            } else if (circularScrollEnabled) {
                //the last item was reached, selected the first one.
                //circular scrolling.
            	idxFirstVisibleItem = 0;
            	selectedIndex = 0;
            } else {
            	return;
            }
        }
        //
        onSelectionChange();
        requestRepaint();
	}
	
	/**
	 * <p>
	 * Manage the checking operation of the items. if <code>null</code> is 
	 * passed, the checkbox will be marked as checked only if it is unchecked
	 * and vice-versa.
	 * </p>
	 * @param itemIndex Item index.
	 * @param status Check status.
	 */
	void manageChecking(int itemIndex, Boolean status) {
		Object item = items.elementAt(itemIndex);
		boolean checked = checkedItems.indexOf(item) != -1;
		if (checked) {
			if (status == null || !status.booleanValue()) {
				if ((type & EXCLUSIVE) != 0) {
					if (checkedItems.elementAt(0) == item) {
						//checked item cannot be unchecked.
						return;
					}
				}
				checkedItems.removeElement(item);
				requestRepaint();
			}
		} else {
			if (status == null || status.booleanValue()) {
				if ((type & EXCLUSIVE) != 0) {
					checkedItems.removeAllElements();
				}
				checkedItems.addElement(item);
				requestRepaint();
			}
		}
	}

	/**
	 * <p>
	 * Get the index of the item that is located in the given coordinates.
	 * </p>
	 * @param x X coordinate.
	 * @param y Y coordinate.
	 * @return Index of the item.
	 */
	int getPressedItem(int x, int y) {
		final int newSelIndex =
			idxFirstVisibleItem + (y - bodyRect.y) / getItemHeight();
		return newSelIndex;
	}

	/**
	 * <p>
	 * Verify if the given coordinates are in the items area.
	 * </p>
	 * @param x X coordinate.
	 * @param y Y coordinate.
	 * @return It is in the area (true) or not.
	 */
	boolean isItemsArea(int x, int y) {
		if (bodyRect.contains(x, y)) {
			return y <= bodyRect.y + items.size() * getItemHeight();
		}
		return false;
	}
	
	/**
	 * <p>
	 * Verify if the given coordinates are in the check box or radio button
	 * area.
	 * </p>
	 * @param x X coordinate.
	 * @param y Y coordinate.
	 * @return It is in the area (true) or not.
	 */
	boolean isCheckBoxArea(int x, int y) {
		if ((type & IMPLICIT) == 0 && bodyRect.contains(x, y)) {
			return x > checkBoxRect.x && x < checkBoxRect.x + checkBoxRect.w;
		} else {
			return false;
		}
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
	 * Verify if the given coordinates are in the horizontal scrollbar area.
	 * </p>
	 * @param x X coordinate.
	 * @param y Y coordinate.
	 * @return It is in the area (true) or not.
	 */
	boolean isHorizontalScrollBarArea(int x, int y) {
		return horiScrollBarRect.contains(x, y);
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
	 * Verify if the given coordinates are in the vertical scrollbar square
	 * area.
	 * </p>
	 * @param x X coordinate.
	 * @param y Y coordinate.
	 * @return It is in the area (true) or not.
	 */
	boolean isHorizontalScrollSquareArea(int x, int y) {
		return horiScrollBarSquareRect.contains(x, y);
	}
	
	/**
	 * <p>
	 * Set the items' images must be cached when resized, so that next time they
	 * are drawn, another resize operation does not be necessary.
	 * </p>
	 * @param enabled Enabled or not.
	 */
	void setCacheResizedImage(boolean enabled) {
		cacheResizedImage = enabled;
	}

	/**
     * <p>
     * Method used to turn on or off the roller responsible for rolling the
     * items name.
     * </p>
     * @param turnon Turn on or off.
     */
    void startRoller(boolean turnon) {
        if (turnon) {
            if (roller == null) {
                roller = new RollerName(this);
                //
                Thread runner = new Thread(roller);
                runner.start();
            }
        } else {
            if (roller != null) {
                roller.stop(); //stops the thread.
                roller = null;
            }
        }
    }

    /**
	 * <p>
	 * Check if the given index respect the items list bounds.
	 * </p>
	 * @param itemIndex Index.
	 * @throws IllegalArgumentException Invalid index.
	 */
	void checkItemsBounds(int itemIndex) {
		if (itemIndex < 0 || itemIndex >= items.size()) {
			throw new IllegalArgumentException(
			    "Item index must be between 0 and " +
			        (items.size() -1) + ": " + itemIndex);
		}
	}
	
	/**
	 * <p>
	 * Find the first item in the list that starts with a given prefix. If -1
	 * is returned, no item has been found.
	 * </p>
	 * @param prefix Prefix.
	 * @return Item index.
	 */
	int findItemStartsWith(String prefix) {
		int size = items.size();
		Object item;
		prefix = prefix.toLowerCase();
		//
		for (int i = 0; i < size; i++) {
			item = items.elementAt(i);
			//
			if (item != null &&
				    item.toString().toLowerCase().startsWith(prefix)) {
				return i;
			}
		}
		//
		return -1;
	}
	
	/**
	 * <p>
	 * Get the letter from the keypad key pressed. If <code>null</code> is
	 * returned, the keyCode passed is not a keypad's key.
	 * </p>
	 * @param keyCode Keypad key.
	 * @return Letter from key.
	 */
	String getLetterFromPressedKey(int keyCode) {
		//ABC,DEF,GHI,JKL,MNO,PQRS,TUV,WXYZ
		String[][] keysChars = new String[][] {
			{"A", "B", "C"}, {"D", "E", "F"}, {"G", "H", "I"}, {"J", "K", "L"},
			{"M", "N", "O"}, {"P", "Q", "R", "S"}, {"T", "U", "V"},
			{"W", "X", "Y", "Z"}
		};
		//
		if (lastPressedKeyPadKey != keyCode ||
				(System.currentTimeMillis() - lastKeyPadEvent) > 1000) {
			offKeyPadKey = 0;
		} else {
			if (offKeyPadKey < 2 || 
					((keyCode == Canvas.KEY_NUM7 || 
							keyCode == Canvas.KEY_NUM9) && offKeyPadKey < 3)) {
				offKeyPadKey++;
			} else {
				offKeyPadKey = 0;
			}
		}
		//
		lastPressedKeyPadKey = keyCode;
		lastKeyPadEvent = System.currentTimeMillis();
		//
        switch (keyCode) {
	        case Canvas.KEY_NUM2:
	            return keysChars[0][offKeyPadKey];
	        case Canvas.KEY_NUM3:
	        	return keysChars[1][offKeyPadKey];
	        case Canvas.KEY_NUM4:
	        	return keysChars[2][offKeyPadKey];
	        case Canvas.KEY_NUM5:
	        	return keysChars[3][offKeyPadKey];
	        case Canvas.KEY_NUM6:
	        	return keysChars[4][offKeyPadKey];
	        case Canvas.KEY_NUM7:
	        	return keysChars[5][offKeyPadKey];
	        case Canvas.KEY_NUM8:
	        	return keysChars[6][offKeyPadKey];
	        case Canvas.KEY_NUM9:
	        	return keysChars[7][offKeyPadKey];
	        default:
	            return null;
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
	protected class ListQSort extends QSort {
		/**
		 * @see com.emobtech.uime.util.QSort#swap(java.lang.Object[], int, int)
		 */
		protected void swap(Object[] its, int i1, int i2) {
			super.swap(its, i1, i2);
			//
			//swaping the images.
			Object img = images.elementAt(i1);
			images.setElementAt(images.elementAt(i2), i1);
			images.setElementAt(img, i2);
		}
	}
}