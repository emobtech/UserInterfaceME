///*
// * GroupedList.java
// * 03/12/2006
// * JME Framework
// * Copyright(c) Ernandes Mourao Junior (ernandes@gmail.com)
// * All rights reserved
// */
//package br.framework.jme.ui;
//
//import java.io.IOException;
//import java.util.Vector;
//
//import javax.microedition.lcdui.Font;
//import javax.microedition.lcdui.Graphics;
//import javax.microedition.lcdui.Image;
//
//import br.framework.jme.util.StringUtil;
//import br.framework.jme.util.ui.Rect;
//import br.framework.jme.util.ui.UIUtil;
//
///**
// * <p>
// * Class that implements a list which items are grouped. Each group may have N
// * levels of depth. This groups can also be expanded in order to see their
// * children items.
// * </p>
// * 
// * <pre>
// * GroupedItem item1 = new GroupedItem("Item 1", "R$");
// * GroupedItem item11 = new GroupedItem("Item 1:Item1.1", "R$", 10);
// * GroupedItem item12 = new GroupedItem("Item 1:Item1.2", "R$", 20);
// * 
// * item1.add(item11);
// * item1.add(item12);
// *
// * GroupedItem item2 = new GroupedItem("Item 2", "R$");
// * GroupedItem item21 = new GroupedItem("Item 2:Item 2.1", "R$");
// * GroupedItem item211 = new GroupedItem("Item 2:Item 2.1:Item 2.1.1", "R$", 50);
// * GroupedItem item212 = new GroupedItem("Item 2:Item 2.1:Item 2.1.2", "R$", 25);
// * GroupedItem item22 = new GroupedItem("Item 2:Item 2.2", "R$", 30);
// * 
// * item2.add(item21);
// * item21.add(item211);
// * item21.add(item212);
// * item2.add(item22);
// * 
// * append(item1);
// * append(item2);
// * </pre>
// * 
// * @author Ernandes Mourao Junior (ernandes@gmail.com)
// * @version 1.0
// * @since 1.0
// * @see br.framework.jme.ui.GroupedItem
// */
//public class GroupedList extends Screen {
//	/**
//     * Custom opacity.
//     */
//    public static final int CUSTOM_OPACITY = 0xef000000;
//    
//    /**
//     * Object responsible to roll the value of the selected item in case it does
//     * not feet in the item width.
//     */
//    private RollerName roller;
//    
//	/**
//	 * Controls the time elapsed since the user kept the pen pressed until it is
//	 * released.
//	 */
//	private long penHoldTimeElapsed = -1;
//	
//	/**
//	 * Y coordinate where the pen was pressed initially for the scrollbar drag.
//	 */
//	private int penDraggedSquareY = -1;
//
//	/**
//	 * Vector that holds the added items.
//	 */
//	protected Vector items;
//	
//	/**
//	 * Expanded group image.
//	 */
//	protected Image imgGrpSignMin;
//
//	/**
//	 * Collapsed group image.
//	 */
//	protected Image imgGrpSignMax;
//	
//	/**
//	 * Index of the selected item.
//	 */
//	protected int selectedIndex;
//	
//	/**
//	 * Index of the first item being displayed.
//	 */
//	protected int idxFirstDisplayedItem;
//
//	/**
//	 * Items per page count.
//	 */
//	protected int itemsPerPage;
//	
//	/**
//	 * Grouping description. Displayed right below the screen title.
//	 */
//	protected String groupingDescription;
//    
//    /**
//     * Item opacity level.
//     */
//    protected int itemOpacityLevel = CUSTOM_OPACITY;
//	
//    /**
//     * Selection opacity level.
//     */
//    protected int selectionOpacityLevel = UIUtil.HALF_OPAQUE;
//
//    /**
//	 * Header color. Region displayed right below the screen title.
//	 */
//	protected int headerColor;
//	
//	/**
//	 * Detail color. Region displayed right below the item text.
//	 */
//	protected int detailColor = -1;
//
//	/**
//	 * Scrollbar color.
//	 */
//	protected int scrollBarColor;
//
//    /**
//     * Selection color.
//     */
//    protected int selectionColor;
//
//    /**
//     * Selection rgb data.
//     */
//    protected int[] selectionRGB;
//
//    /**
//	 * Header font color.
//	 */
//	protected int headerFontColor;
//
//	/**
//	 * Detail font color.
//	 */
//	protected int detailFontColor = bodyFontColor;
//
//    /**
//     * Detail rgb data.
//     */
//    protected int[] detailRGB;
//
//	/**
//	 * Header font.
//	 */
//	protected Font headerFont = bodyFont;
//    
//    /**
//     * Negative item font color. Displayed when the item is selected.
//     */
//    protected int negativeItemFontColor;
//	
//	/**
//	 * Colors to be applied to each depth level of the items.
//	 */
//	protected int[] depthsColor = new int[20];
//
//    /**
//     * Depths rgb data.
//     */
//    protected int[][] depthsRGB = new int[depthsColor.length][];
//    
//    /**
//     * Item height.
//     */
//    protected int itemHeight;
//    
//	/**
//	 * <p>
//	 * Header rectangle.
//	 * </p>
//	 */
//	protected Rect headerRect;
//	
//    /**
//     * <p>
//     * Vertical scrollbar rectangle.
//     * </p>
//     */
//	protected Rect scrollBarRect;
//    
//    /**
//     * <p>
//     * Vertical scrollbar square rectangle.
//     * </p>
//     */
//	protected Rect scrollBarSquareRect;
//
//    /**
//	 * <p>
//	 * Constructor.
//	 * </p>
//	 * @param title List title.
//	 * @param groupingDesc Grouping description.
//	 */
//	public GroupedList(String title, String groupingDesc) {
//		super(title);
//		this.groupingDescription = groupingDesc;
//		items = new Vector();
//		try {
//			imgGrpSignMin = Image.createImage("/images/icon_sig_min.png");
//			imgGrpSignMax = Image.createImage("/images/icon_sig_max.png");
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//		headerRect = new Rect();
//		scrollBarRect = new Rect();
//		scrollBarSquareRect = new Rect();
//		//
//       	skin.applyGroupedListSkin(this);
//	}
//	
//	/**
//	 * <p>
//	 * Set the grouping description. The text displayed right below the screen
//	 * title.
//	 * </p>
//	 * @param desc Description.
//	 */
//	public void setGroupingDescription(String desc) {
//		groupingDescription = desc;
//	}
//    
//    /**
//	 * <p>
//	 * Set the item opacity level.
//	 * </p>
//     * @param level Level.
//     * @see br.framework.jme.util.ui.UIUtil#FULLY_OPAQUE
//     * @see br.framework.jme.util.ui.UIUtil#HALF_OPAQUE
//     * @see br.framework.jme.util.ui.UIUtil#FULLY_TRANSPARENT
//     * @see br.framework.jme.ui.GroupedList#CUSTOM_OPACITY
//     */
//    public void setItemOpacityLevel(int level) {
//        if (itemOpacityLevel != level) {
//            depthsRGB = new int[depthsColor.length][]; //need to reprocess the rgb data.
//        }
//        itemOpacityLevel = level;
//    }
//	
//    /**
//	 * <p>
//	 * Set the selection opacity level.
//	 * </p>
//     * @param level Level.
//     * @see br.framework.jme.util.ui.UIUtil#FULLY_OPAQUE
//     * @see br.framework.jme.util.ui.UIUtil#HALF_OPAQUE
//     * @see br.framework.jme.util.ui.UIUtil#FULLY_TRANSPARENT
//     * @see br.framework.jme.ui.GroupedList#CUSTOM_OPACITY
//     */
//    public void setSelectionOpacityLevel(int level) {
//        if (selectionOpacityLevel != level) {
//            selectionRGB = null; //need to reprocess the rgb data.
//        }
//        selectionOpacityLevel = level;
//    }
//
//    /**
//	 * <p>
//	 * Set the header color.
//	 * </p>
//	 * @param r Red.
//	 * @param g Green.
//	 * @param b Blue.
//	 */
//	public void setHeaderColor(int r, int g, int b) {
//		headerColor = UIUtil.getHexColor(r, g, b);
//	}
//	
//	/**
//	 * <p>
//	 * Set the scrollbar color.
//	 * </p>
//	 * @param r Red.
//	 * @param g Green.
//	 * @param b Blue.
//	 */
//	public void setScrollBarColor(int r, int g, int b) {
//		scrollBarColor = UIUtil.getHexColor(r, g, b);
//	}
//
//    /**
//	 * <p>
//	 * Set the selection color.
//	 * </p>
//	 * @param r Red.
//	 * @param g Green.
//	 * @param b Blue.
//     */
//    public void setSelectionColor(int r, int g, int b) {
//        selectionColor = UIUtil.getHexColor(r, g, b);
//        selectionRGB = null; //need to reprocess the rgb data.
//        negativeItemFontColor = UIUtil.getNegativeColor(r, g, b);
//    }
//
//    /**
//	 * <p>
//	 * Set the detail color.
//	 * </p>
//	 * @param r Red.
//	 * @param g Green.
//	 * @param b Blue.
//	 */
//	public void setDetailColor(int r, int g, int b) {
//		detailColor = UIUtil.getHexColor(r, g, b);
//        detailRGB = null; //need to reprocess the rgb data.
//	}
//
//	/**
//	 * <p>
//	 * Set the header font color.
//	 * </p>
//	 * @param r Red.
//	 * @param g Green.
//	 * @param b Blue.
//	 */
//	public void setHeaderFontColor(int r, int g, int b) {
//		headerFontColor = UIUtil.getHexColor(r, g, b);
//	}
//
//	/**
//	 * <p>
//	 * Set the detail font color.
//	 * </p>
//	 * @param r Red.
//	 * @param g Green.
//	 * @param b Blue.
//	 */
//	public void setDetailFontColor(int r, int g, int b) {
//		detailFontColor = UIUtil.getHexColor(r, g, b);
//	}
//	
//	/**
//	 * @inheritDoc
//	 * @see br.framework.jme.ui.Screen#setBodyFont(javax.microedition.lcdui.Font)
//	 */
//	public void setBodyFont(Font font) {
//		super.setBodyFont(font);
//		itemHeight = bodyFont.getHeight() * 2;
//	}
//
//	/**
//	 * <p>
//	 * Set the color of a given depth.
//	 * </p>
//	 * @param depth Depth. Value must be between 1 and 20.
//	 * @param r Red.
//	 * @param g Green.
//	 * @param b Blue.
//	 * @throws IllegalArgumentException Invalid depth.
//	 */
//	public void setDepthColor(int depth, int r, int g, int b) {
//		if (depth <= 0 || depth > depthsColor.length) {
//			throw new IllegalArgumentException(
//				"Depth must be between 1 and " + depthsColor.length);
//		}
//		depthsColor[depth -1] = UIUtil.getHexColor(r, g, b);
//        if (itemOpacityLevel != UIUtil.FULLY_OPAQUE) {
//            depthsRGB[depth -1] = null; //need to reprocess the rgb data.
//        }
//	}
//
//	/**
//	 * <p>
//	 * Set the header font.
//	 * </p>
//	 * @param font Font.
//	 */
//	public void setHeaderFont(Font font) {
//		headerFont = font;
//	}
//	
//	/**
//	 * <p>
//	 * Append a given item into the list.
//	 * </p>
//	 * @param item Item.
//	 */
//	public synchronized void append(GroupedItem item) {
//		items.addElement(item);
//		GroupedItem[] children = item.getChildren();
//		for (int i = 0; i < children.length; i++) {
//			append(children[i]);
//		}
//		if (selectedIndex == -1) {
//			selectedIndex = 0; //select the first index.
//		}
//		requestRepaint();
//	}
//	
//	/**
//	 * <p>
//	 * Remove all the items from the list.
//	 * </p>
//	 */
//	public synchronized void deleteAll() {
//		items.removeAllElements();
//		selectedIndex = -1;
//		requestRepaint();
//	}
//	
//	/**
//	 * <p>
//	 * Get the index of the selected item.
//	 * </p>
//	 * @return Index.
//	 */
//	public synchronized int getSelectedIndex() {
//		return selectedIndex;
//	}
//	
//	/**
//	 * <p>
//	 * Get the item associated to the given index.
//	 * </p>
//	 * @param index Index.
//	 * @return Item object.
//	 */
//	public synchronized GroupedItem get(int index) {
//		return (GroupedItem)items.elementAt(index);
//	}
//	
//	/**
//	 * <p>
//	 * List size. This size is calculated based on the sum of all children of
//	 * the appended items inclusive.
//	 * </p>
//	 * @return Size.
//	 */
//	public synchronized int size() {
//		return items.size();
//	}
//
//	/**
//	 * @inheritDoc
//	 * @see javax.microedition.lcdui.Canvas#showNotify()
//	 */
//	protected void showNotify() {
//		super.showNotify();
//		//
//		roller = new RollerName(this);
//		Thread runner = new Thread(roller);
//		runner.start();
//	}
//
//	/**
//	 * @inheritDoc
//	 * @see javax.microedition.lcdui.Canvas#hideNotify()
//	 */
//	protected void hideNotify() {
//		super.hideNotify();
//		//
//		roller.stop(); //stops the thread.
//		roller = null;
//	}
//	
//	/**
//	 * @inheritDoc
//	 * @see javax.microedition.lcdui.Displayable#sizeChanged(int, int)
//	 */
//	protected void sizeChanged(int w, int h) {
//		super.sizeChanged(w, h);
//		//
//        bodyRect.h -= headerRect.h; //recalculate the body height.
//        //
//        if (itemHeight != 0) {
//            idxFirstDisplayedItem = selectedIndex;
//            itemsPerPage = (bodyRect.h / itemHeight) -1;
//            int i = 0;
//            //
//            //positioning the selected item.
//            while (idxFirstDisplayedItem > 0 && i < itemsPerPage &&
//            	   isDisplayable(
//            	       (GroupedItem)items.elementAt(idxFirstDisplayedItem),
//            	       null)) {
//            	idxFirstDisplayedItem--;
//            	i++;
//            }
//        }
//	}
//
//	/**
//	 * @inheritDoc
//	 * @see br.framework.jme.ui.Screen#drawBody(javax.microedition.lcdui.Graphics, int, int, int, int)
//	 */
//	protected synchronized void drawBody(Graphics g, int x, int y, int w,
//		int h) {
//		headerRect.x = x;
//		headerRect.y = y;
//		headerRect.w = w;
//		headerRect.h = bodyFont.getHeight();
//		drawHeader(g, x, y);
//        //
//        bodyRect.y += headerRect.h +1;
//        bodyRect.h -= headerRect.h;
//		//
//		if (items.size() > 0) {
//			itemHeight = bodyFont.getHeight() * 2;
//			itemsPerPage = bodyRect.h / itemHeight;
//			final boolean hasScroll = getDisplayableItemCount() > itemsPerPage;
//			if (hasScroll) {
//				if (hasPointerEvents()) {
//					scrollBarRect.w = (int)((w < h ? w : h) * 0.04);
//				} else {
//					scrollBarRect.w = (int)((w < h ? w : h) * 0.03);
//				}
//				scrollBarRect.x = x + w - scrollBarRect.w;
//				scrollBarRect.y = bodyRect.y;
//				scrollBarRect.h = bodyRect.h;
//				bodyRect.w -= scrollBarRect.w;
//				drawScrollBar(g, scrollBarRect.x, scrollBarRect.y);
//			} else {
//				scrollBarRect.x = 0;
//				scrollBarRect.y = 0;
//				scrollBarRect.w = 0;
//				scrollBarRect.h = 0;
//				bodyRect.w = w;
//			}
//			drawList(g, x, bodyRect.y);
//		}
//	}
//	
//	/**
//	 * <p>
//	 * Draw the header.
//	 * </p>
//	 * @param g Graphics object.
//	 * @param x X coordinate.
//	 * @param y Y coordinate.
//	 */
//	protected void drawHeader(Graphics g, int x, int y) {
//		if (groupingDescription != null) {
//			g.setColor(headerColor);
//            g.fillRect(x, y, headerRect.w, headerRect.h);
//			g.setColor(headerFontColor);
//			g.setFont(headerFont);
//			//
//            g.drawString(
//				groupingDescription,
//				x + offset,
//				y +1,
//				Graphics.TOP | Graphics.LEFT);
//            //
//			g.setColor(0, 0, 0); //black color.
//			g.drawLine(x, y + headerRect.h, x + headerRect.w, y + headerRect.h);
//		}
//	}
//
//	/**
//	 * <p>
//	 * Draw the list.
//	 * </p>
//	 * @param g Graphics object.
//	 * @param x X coordinate.
//	 * @param y Y coordinate.
//	 */
//	protected void drawList(Graphics g, int x, int y) {
//		final int size = items.size();
//		int i = idxFirstDisplayedItem;
//		int drawnItemsCount = itemsPerPage +1;
//		GroupedItem item = null;
//		while (drawnItemsCount > 0) {
//			item = (GroupedItem)items.elementAt(i);
//			y += drawItem(g, x, y, item, selectedIndex == i) +1;
//			drawnItemsCount--;
//			if (!item.isExpanded()) {
//				i = moveNextDisplayable(i +1);
//			} else {
//				i++;
//			}
//			if (i == size) {
//				break;
//			}
//		}
//	}
//	
//	/**
//	 * <p>
//	 * Draw a given item.
//	 * </p>
//	 * @param g Graphics object.
//	 * @param x X coordinate.
//	 * @param y Y coordinate.
//	 * @param item Item to be drawn.
//	 * @param selected If the given item is selected or not.
//	 * @return Item height.
//	 */
//	protected int drawItem(Graphics g, int x, int y, GroupedItem item,
//		boolean selected) {
//		final int fHeight = bodyFont.getHeight();
//		final int w = bodyRect.w;
//		final int width = getWidth();
//		final int itemDepth = getDepth(item) -1;
//		
//		//draw the background 1.
//        int[] p = depthsRGB[itemDepth];
//        if (p == null && itemOpacityLevel != UIUtil.FULLY_OPAQUE &&
//            itemOpacityLevel != UIUtil.FULLY_TRANSPARENT) {
//            p = //processing rgb.
//                UIUtil.getRGB(
//                    depthsColor[itemDepth], width, fHeight, itemOpacityLevel);
//            depthsRGB[itemDepth] = p;
//        }
//        if (p != null) {
//        	UIUtil.drawRGB(g, p, 0, w, x, y, w, fHeight, true);
//        } else if (itemOpacityLevel != UIUtil.FULLY_TRANSPARENT) {
//            g.setColor(depthsColor[itemDepth]);
//            g.fillRect(x, y, w, fHeight);
//        }
//
//		//draw the background 2.
//        int dColor;
//        if (detailColor != -1) {
//            dColor = detailColor;
//        } else {
//            dColor = depthsColor[itemDepth];
//            detailRGB = depthsRGB[itemDepth];
//        }
//        if (detailRGB == null && itemOpacityLevel != UIUtil.FULLY_OPAQUE &&
//            itemOpacityLevel != UIUtil.FULLY_TRANSPARENT) {
//            //processing rgb.
//            detailRGB = UIUtil.getRGB(dColor, width, fHeight, itemOpacityLevel);
//        }
//        if (detailRGB != null) {
//        	UIUtil.drawRGB(
//        		g, detailRGB, 0, w, x, y + fHeight, w, fHeight, true);
//        } else if (itemOpacityLevel != UIUtil.FULLY_TRANSPARENT) {
//            g.setColor(dColor);
//            g.fillRect(x, y + fHeight, w, fHeight);
//        }
//
//		if (selected) {
//            //draw the selection.
//            final int h = fHeight * 2;
//            if (selectionRGB == null &&
//            	selectionOpacityLevel != UIUtil.FULLY_OPAQUE &&
//                selectionOpacityLevel != UIUtil.FULLY_TRANSPARENT) {
//                selectionRGB =
//                    UIUtil.getRGB(
//                    	selectionColor, width, h, selectionOpacityLevel);
//            }
//            if (selectionRGB != null) {
//            	UIUtil.drawRGB(g, selectionRGB, 0, w, x, y, w, h, true);
//            } else if (selectionOpacityLevel != UIUtil.FULLY_TRANSPARENT) {
//    			UIUtil.fillRect(
//					g,
//					x,
//					y,
//					w,
//					h,
//					selectionColor,
//					skin.getSelectionStyle());
//            }
//            if (selectionOpacityLevel == UIUtil.FULLY_TRANSPARENT) {
//                g.setColor(selectionColor);
//                g.drawRect(x, y, w -1, h);
//            }
//		}
//
//		int xx = x;
//		//calculating the offset based on the item´s depth.
//		xx += itemDepth * imgGrpSignMax.getWidth();
//		//draw the grouping sign.
//		xx += drawGroupingSign(g, xx, y, fHeight, item);
//
//		//draw the item text 1.
//		String strValue = item.getDescription();
//        if (selected) {
//            g.setColor(negativeItemFontColor);
//    		roller.setText(strValue, bodyFont, w - xx - offset);
//    		strValue = roller.getText();
//        } else {
//            g.setColor(bodyFontColor);
//            strValue = UIUtil.shrinkString(strValue, w - xx - offset, bodyFont);
//        }
//		g.setFont(bodyFont);
//		g.drawString(strValue, xx + offset, y, Graphics.TOP | Graphics.LEFT);
//		
//		y += fHeight;
//		
//		//draw the item text 2.
//        if (selected) {
//            g.setColor(negativeItemFontColor);
//        } else {
//            g.setColor(detailFontColor);
//        }
//		g.setFont(bodyFont);
//        float floatValue = item.getValue();
//        if (floatValue != GroupedItem.NULL_VALUE) {
//            strValue =
//            	item.getDetail() + "" + StringUtil.formatNumber(floatValue);
//        } else {
//            strValue = item.getDetail();
//        }
//        g.drawString(
//            strValue,
//            w - bodyFont.stringWidth(strValue) - offset,
//            y,
//            Graphics.TOP | Graphics.LEFT);
//        
//		return itemHeight;
//	}
//	
//    /**
//	 * <p>
//	 * Draw the scrollbar.
//	 * </p>
//	 * @param g Graphics object.
//	 * @param x X coordinate.
//	 * @param y Y coordinate.
//     */
//    protected void drawScrollBar(Graphics g, int x, int y) {
//    	UIUtil.drawVerticalScrollbar(
//    		g, 
//    		x, 
//    		y, 
//    		scrollBarRect.w, 
//    		scrollBarRect.h, 
//    		getDisplayableItemCount(), 
//    		itemsPerPage, 
//    		idxFirstDisplayedItem, 
//    		scrollBarSquareRect, 
//    		scrollBarColor, 
//    		skin.getVerticalScrollBarStyle());
//    }
//	
//	/**
//	 * <p>
//	 * Draw the images located right before the item text.
//	 * </p>
//	 * @param g Graphics object.
//	 * @param x X coordinate.
//	 * @param y Y coordinate.
//	 * @param itemHeight Item height.
//	 * @param item Item.
//	 * @return Image height.
//	 */
//	protected int drawGroupingSign(
//		Graphics g, int x, int y, int itemHeight, GroupedItem item) {
//		final int sqSide = imgGrpSignMin.getHeight();
//		y += (itemHeight - sqSide) / 2;
//		x += offset;
//		if (item.getChildren().length > 0) {
//			if (item.isExpanded()) {
//				g.drawImage(imgGrpSignMax, x, y, Graphics.TOP | Graphics.LEFT);	
//			} else {
//				g.drawImage(imgGrpSignMin, x, y, Graphics.TOP | Graphics.LEFT);
//			}
//		}
//		return sqSide + offset;
//	}
//	
//	/**
//	 * <p>
//	 * Scroll up the selected item.
//	 * </p>
//	 */
//	protected void scrollUpSelectedItem() {
//		if (selectedIndex > idxFirstDisplayedItem) {
//			int idx = movePreviousDisplayable(selectedIndex -1);
//			if (idx < items.size()) {
//				selectedIndex = idx;
//				requestRepaint();
//			}
//		} else if (idxFirstDisplayedItem -1 >= 0) {
//			boolean repaint = false;
//			int idx = movePreviousDisplayable(idxFirstDisplayedItem -1);
//			if (idx < items.size()) {
//				idxFirstDisplayedItem = idx;
//				repaint = true;
//			}
//			idx = moveNextDisplayable(selectedIndex -1);
//			if (idx < items.size()) {
//				selectedIndex = idx;
//				repaint = true;
//			}
//			if (repaint) {
//				requestRepaint();
//			}
//		}
//	}
//	
//	/**
//	 * <p>
//	 * Scroll down the selected item.
//	 * </p>
//	 */
//	protected void scrollDownSelectedItem() {
//		final int lastItemIdx = getLastDisplayedItemIndex();
//		if (selectedIndex < lastItemIdx) {
//			int idx = moveNextDisplayable(selectedIndex +1);
//			if (idx < items.size()) {
//				selectedIndex = idx;
//				requestRepaint();
//			}
//		} else if (lastItemIdx < movePreviousDisplayable(items.size() -1)) {
//			boolean repaint = false;
//			int idx = moveNextDisplayable(idxFirstDisplayedItem +1);
//			if (idx < items.size()) {
//				idxFirstDisplayedItem = idx;
//				repaint = true;
//			}
//			idx = moveNextDisplayable(selectedIndex +1);
//			if (idx < items.size()) {
//				selectedIndex = idx;
//				repaint = true;
//			}
//			if (repaint) {
//				requestRepaint();
//			}
//		}
//	}
//	
//	/**
//	 * <p>
//	 * Scroll up the page.
//	 * </p>
//	 */
//	protected void scrollUpPage() {
//	    //dragged up.
//		idxFirstDisplayedItem =
//			movePreviousDisplayable(idxFirstDisplayedItem -1);
//		final int lastIdx = getLastDisplayedItemIndex();
//		if (selectedIndex > lastIdx) {
//			selectedIndex = lastIdx;
//		}
//		requestRepaint();
//	}
//
//	/**
//	 * <p>
//	 * Scroll down the page.
//	 * </p>
//	 */
//	protected void scrollDownPage() {
//	    //dragged down.
//		idxFirstDisplayedItem =
//			moveNextDisplayable(idxFirstDisplayedItem +1);
//		if (selectedIndex < idxFirstDisplayedItem) {
//			selectedIndex = idxFirstDisplayedItem;
//		}
//		requestRepaint();
//	}
//
//	/**
//	 * <p>
//	 * Get the item index of the last item being displayed.
//	 * </p>
//	 * @return Index.
//	 */
//	protected int getLastDisplayedItemIndex() {
//		final int size = items.size();
//		int drawnItemsCount = itemsPerPage -1;
//		int lastIndex = idxFirstDisplayedItem;
//		int idx;
//		while (drawnItemsCount > 0) {
//			idx = moveNextDisplayable(lastIndex +1);
//			drawnItemsCount--;
//			if (idx == size) {
//				break;
//			}
//			lastIndex = idx;
//		}
//		return lastIndex;
//	}
//	
//	/**
//	 * <p>
//	 * Verify if a given item is displayable at this moment or not.
//	 * </p>
//	 * @param item Item.
//	 * @param parent Item´s parent.
//	 * @return Displayable or not.
//	 */
//	protected boolean isDisplayable(GroupedItem item, GroupedItem parent) {
//		GroupedItem p = item.getParent();
//		if (p != null) {
//			if (p.isExpanded()) {
//				if (p.getParent() != null) {
//					return isDisplayable(p.getParent(), p);
//				}
//			} else {
//				return false;
//			}
//		}
//		return parent == null || item.isExpanded();
//	}
//	
//	/**
//	 * <p>
//	 * Get the depth of a given item.
//	 * </p>
//	 * @param item Item.
//	 * @return Depth.
//	 */
//	protected int getDepth(GroupedItem item) {
//		GroupedItem p = item.getParent();
//		if (p != null) {
//			return 1 + getDepth(p);
//		} else {
//			return 1;
//		}
//	}
//	
//	/**
//	 * <p>
//	 * Move to the next displayable item in the list.
//	 * </p>
//	 * @param startIndex Start index.
//	 * @return Index of the displayable item.
//	 */
//	protected int moveNextDisplayable(int startIndex) {
//		final int size = items.size();
//		while (startIndex < size
//			   && !isDisplayable(
//				       (GroupedItem)items.elementAt(startIndex), null)) {
//			startIndex++;
//		}
//		return startIndex;
//	}
//
//	/**
//	 * <p>
//	 * Move to the previous displayable item in the list.
//	 * </p>
//	 * @param startIndex Start index.
//	 * @return Index of the displayable item.
//	 */
//	protected int movePreviousDisplayable(int startIndex) {
//		while (startIndex >= 0 &&
//			   !isDisplayable((GroupedItem)items.elementAt(startIndex), null)) {
//			startIndex--;
//		}
//		return startIndex;
//	}
//	
//	/**
//	 * <p>
//	 * Get the virtual position of a given displayed item in the list of the
//	 * displayed items.
//	 * </p>
//	 * @param itemIndex Real item index.
//	 * @return Position.
//	 */
//	protected int getVirtualPosition(int itemIndex) {
//		int pos = 0;
//		int idx = 0;
//		while (true) {
//			idx = moveNextDisplayable(idx);
//			if (idx != itemIndex) {
//				pos++;
//				idx++;
//			} else {
//				break;
//			}
//		}
//		return pos;
//	}
//	
//	/**
//	 * <p>
//	 * Get the real position of a given displayed item in the list of the
//	 * displayed items.
//	 * </p>
//	 * @param virtualIndex Virtual item index.
//	 * @return Position.
//	 */
//	protected int getRealPosition(int virtualIndex) {
//		final int size = items.size();
//		int pos = 0;
//		int idx = 0;
//		while (true) {
//			idx = moveNextDisplayable(idx);
//			if (idx < size && pos != virtualIndex) {
//				pos++;
//				idx++;
//			} else {
//				break;
//			}
//		}
//		return idx;
//	}
//	
//	/**
//	 * <p>
//	 * Count of items that can be displayed.
//	 * </p>
//	 * @return Count.
//	 */
//	protected int getDisplayableItemCount() {
//		GroupedItem item = null;
//		int c = 0;
//		for (int i = items.size() -1; i >= 0; i--) {
//			item = (GroupedItem)items.elementAt(i);
//			if (isDisplayable(item, null)) {
//				c++;
//			}
//		}
//		return c;
//	}
//	
//	/**
//	 * <p>
//	 * Count of items being displayed.
//	 * </p>
//	 * @return Count.
//	 */
//	protected int getDisplayedItemCount() {
//		final int size = items.size();
//		int drawnItemsCount = itemsPerPage -1;
//		int index = idxFirstDisplayedItem;
//		while (drawnItemsCount > 0) {
//			index = moveNextDisplayable(index +1);
//			if (index == size) {
//				break;
//			}
//			drawnItemsCount--;
//		}
//		return itemsPerPage - drawnItemsCount;
//	}
//	
//	/**
//	 * <p>
//	 * Expand or collapse a given item. if <code>null</code> is 
//	 * passed, the item will be expanded only if it is minimized and vice-versa.
//	 * </p>
//	 * @param item Item.
//	 * @param status Expand status.
//	 */
//	protected void expandCollapseItem(GroupedItem item, Boolean status) {
//		final boolean hasChildren = item.getChildren().length > 0;
//		if ((status == null || status.booleanValue()) && 
//			hasChildren && !item.isExpanded()) {
//			item.setExpanded(true);
//			requestRepaint();
//		} else if ((status == null || !status.booleanValue()) &&
//			       hasChildren && item.isExpanded()) {
//			item.setExpanded(false);
//			//hold the current selection.
//			int selIndex = selectedIndex;
//			idxFirstDisplayedItem = selectedIndex;
//			int i = itemsPerPage - getDisplayedItemCount();
//			for (; i >= 0; i--) {
//				scrollUpSelectedItem();
//			}
//			selectedIndex = selIndex;
//			requestRepaint();
//		} else if (!status.booleanValue() && 
//				   (!hasChildren || !item.isExpanded())) {
//			if (item.getParent() != null) {
//				//set the parent item as selected.
//				selectedIndex = items.indexOf(item.getParent());
//				if (selectedIndex < idxFirstDisplayedItem) {
//					idxFirstDisplayedItem = selectedIndex;
//				}
//				requestRepaint();
//			}
//		}
//	}
//	
//	/**
//	 * @inheritDoc
//	 * @see br.framework.jme.ui.Screen#keyHold(int)
//	 */
//	protected synchronized void keyHold(int keyCode) {
//		keyDown(keyCode);
//	}
//
//	/**
//	 * @inheritDoc
//	 * @see br.framework.jme.ui.Screen#keyDown(int)
//	 */
//	protected synchronized void keyDown(int keyCode) {
//		keyCode = getGameAction(keyCode);
//		if (keyCode == UP) {
//			scrollUpSelectedItem();
//		} else if (keyCode == DOWN) {
//			scrollDownSelectedItem();
//		} else  if (keyCode == FIRE) {
//			if (selectedIndex != -1) {
//				expandCollapseItem(
//					(GroupedItem)items.elementAt(selectedIndex), null);
//			}
//		} else if (keyCode == RIGHT) {
//			if (selectedIndex != -1) {
//				expandCollapseItem(
//					(GroupedItem)items.elementAt(selectedIndex), 
//					new Boolean(true));
//			}
//		} else  if (keyCode == LEFT) {
//			if (selectedIndex != -1) {
//				expandCollapseItem(
//					(GroupedItem)items.elementAt(selectedIndex), 
//					new Boolean(false));
//			}
//		}
//	}
//	
//	/**
//	 * @inheritDoc
//	 * @see br.framework.jme.ui.Screen#penDown(int, int)
//	 */
//	protected synchronized void penDown(int x, int y) {
//		if (isItemsArea(x, y)) {
//			penHoldTimeElapsed = System.currentTimeMillis();
//		}
//	}
//	
//	/**
//	 * @inheritDoc
//	 * @see br.framework.jme.ui.Screen#penDragged(int, int)
//	 */
//	protected synchronized void penDragged(int x, int y) {
//		if (isScrlSquareArea(x, y)) {
//			penDraggedSquareY = y;
//		} else if (isScrollBarArea(x, y)) {
//			if (penDraggedSquareY != -1) {
//			    //pen left the square area but it is still in the scrollbar area.
//				if (y < penDraggedSquareY) {
//					scrollUpPage();
//				} else {
//					scrollDownPage();
//				}
//				penDraggedSquareY = -1;
//			}
//		}
//	}
//	
//	/**
//	 * @inheritDoc
//	 * @see br.framework.jme.ui.Screen#penUp(int, int)
//	 */
//	protected synchronized void penUp(int x, int y) {
//		if (isItemsArea(x, y) && penHoldTimeElapsed != -1) {
//			final int listHeight = y - bodyRect.y;
//			//virtual position of the first item displayed.
//			int vSelIndex = getVirtualPosition(idxFirstDisplayedItem);
//			//virtual position of the item that was selected.
//			vSelIndex += listHeight / itemHeight;
//			//real index of the selected item.
//			int rSelIndex = getRealPosition(vSelIndex);
//			//index of the last item displayed.
//			int lastDispIndex = getLastDisplayedItemIndex();
//			if (vSelIndex - getVirtualPosition(lastDispIndex) == 1 &&
//				vSelIndex < getDisplayableItemCount()) {
//				//displayed item right after the last displayed item.
//				selectedIndex = rSelIndex;
//				scrollDownSelectedItem();
//				selectedIndex = rSelIndex;
//			} else if (rSelIndex <= lastDispIndex) {
//				selectedIndex = rSelIndex;
//				//expand or collpase if the user kept the pen pressed
//				//during a period of time, otherwise, the item will be just
//				//selected.
//				if (System.currentTimeMillis() - penHoldTimeElapsed >= 250){
//					expandCollapseItem(
//						(GroupedItem)items.elementAt(selectedIndex), null);
//				}
//				requestRepaint();
//				penHoldTimeElapsed = -1; //reset.
//			}
//		} else if (!isScrlSquareArea(x, y) && isScrollBarArea(x, y)) {
//			if (y < scrollBarSquareRect.y) {
//				scrollUpPage();
//			} else {
//				scrollDownPage();
//			}
//		}
//		penDraggedSquareY = -1; //reset.
//	}
//	
//	/**
//	 * <p>
//	 * Verify if the given coordinates are in the items area.
//	 * </p>
//	 * @param x X coordinate.
//	 * @param y Y coordinate.
//	 * @return It is in the area (true) or not.
//	 */
//	protected boolean isItemsArea(int x, int y) {
//		return bodyRect.contains(x, y);
//	}
//	
//	/**
//	 * <p>
//	 * Verify if the given coordinates are in the scrollbar area.
//	 * </p>
//	 * @param x X coordinate.
//	 * @param y Y coordinate.
//	 * @return It is in the area (true) or not.
//	 */
//	protected boolean isScrollBarArea(int x, int y) {
//		return scrollBarRect.contains(x, y);
//	}
//    
//	/**
//	 * <p>
//	 * Verify if the given coordinates are in the scrollbar square area.
//	 * </p>
//	 * @param x X coordinate.
//	 * @param y Y coordinate.
//	 * @return It is in the area (true) or not.
//	 */
//	protected boolean isScrlSquareArea(int x, int y) {
//		return scrollBarSquareRect.contains(x, y);
//	}
//}