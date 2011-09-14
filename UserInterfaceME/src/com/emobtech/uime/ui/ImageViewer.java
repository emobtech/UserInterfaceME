/*
 * ImageViewer.java
 * 08/09/2007
 * JME Framework
 * Copyright(c) Ernandes Mourao Junior (ernandes@gmail.com)
 * All rights reserved
 */
package br.framework.jme.ui;

import javax.microedition.lcdui.Canvas;
import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;

import com.emobtech.uime.util.I18N;
import com.emobtech.uime.util.ui.Rect;
import com.emobtech.uime.util.ui.UIUtil;


/**
 * <p>
 * This class implements a screen that displays a given image.
 * </p>
 * 
 * @author Ernandes Mourao Junior (ernandes@gmail.com)
 * @version 1.0
 * @since 1.0
 */
public class ImageViewer extends Screen {
	/**
	 * <p>
     * Hold an out of memory message that is displayed when an out of memory
     * error happens.
     * </p>
	 */
	private String outOfMemoryMsg;
	
	/**
     * <p>
     * Notifies that a stretch operation is pending.
     * </p>
	 */
	private boolean pendingStretch;

	/**
     * <p>
     * Notifies that a fit operation is pending.
     * </p>
	 */
	private boolean pendingFit;
	
	/**
     * <p>
     * Original screen width.
     * </p>
	 */
	private int ow;
	
	/**
     * <p>
     * Original screen height.
     * </p>
	 */
	private int oh;
	
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
	 * Image area has been pressed or not.
	 * </p>
	 */
	boolean imageAreaPressed;

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
     * Scroll bars color.
     * </p>
	 */
	int scrollBarColor;
    
    /**
     * <p>
     * Coordinate X of the image's region being displayed.
     * </p>
     */
    int coord_x;
	
    /**
     * <p>
     * Coordinate Y of the image's region being displayed.
     * </p>
     */
    int coord_y;

    /**
     * <p>
     * Holds a temp image in result of a fit or stretch operation.
     * </p>
	 */
	Image tmpImage;

	/**
     * <p>
     * Image.
     * </p>
	 */
	Image image;

	/**
     * <p>
     * Image alignment.
     * </p>
	 */
	int alignment = UIUtil.ALIGN_LEFT | UIUtil.ALIGN_TOP;

	/**
     * <p>
     * Constructor.
     * </p>
	 * @param title Title.
	 */
	public ImageViewer(String title) {
		this(title, null);
	}
	
    /**
     * <p>
     * Constructor.
     * </p>
	 * @param title Title.
	 * @param image Image.
	 */
	public ImageViewer(String title, Image image) {
		super(title);
        //
        vertScrollBarRect = new Rect();
        vertScrollBarSquareRect = new Rect();
        horiScrollBarRect = new Rect();
        horiScrollBarSquareRect = new Rect();
        //
        setImage(image);
        //
        skin.applyImageViewerSkin(this);
	}

    /**
     * <p>
     * Set a given image to be displayed.
     * </p>
	 * @param img Image.
	 */
	public synchronized void setImage(Image img) {
		image = img;
		tmpImage = null;
        //
        coord_x = 0;
        coord_y = 0;
        //
        requestRepaint();
	}
	
    /**
     * <p>
     * Get the image being displayed.
     * </p>
	 * @return Image.
	 */
	public synchronized Image getImage() {
		return image;
	}
	
    /**
     * <p>
     * Displays the image on its original dimension.
     * </p>
	 */
	public synchronized void full() {
		tmpImage = null;
		outOfMemoryMsg = null;
        //
        coord_x = 0;
        coord_y = 0;
		//
        System.gc();
        //
		requestRepaint();
	}

    /**
     * <p>
     * Resizes the image to fit in the screen.
     * </p>
	 */
	public synchronized void fit() {
		if (!isShown()) {
			pendingFit = true;
			return;
		}
		//
		int w = image.getWidth();
		int h = image.getHeight();
		//
		w = w > ow ? ow : w;
		h = h > oh ? oh : h;
        //
        coord_x = 0;
        coord_y = 0;
		outOfMemoryMsg = null;
		//
		try {
			tmpImage = UIUtil.resizeImage(image, w, h);
		} catch (OutOfMemoryError e) {
			outOfMemoryMsg = 
			   I18N.getFramework("framework.jme.ui.calendarviewer.outofmemory");
			tmpImage = null;
		} finally {
	        System.gc();
		}
		//
		requestRepaint();
	}

    /**
     * <p>
     * Stretches the image.
     * </p>
	 */
	public synchronized void stretch() {
		if (!isShown()) {
			pendingStretch = true;
			return;
		}
		//
        coord_x = 0;
        coord_y = 0;
		outOfMemoryMsg = null;
        //
        try {
			tmpImage = UIUtil.resizeImage(image, ow, oh);
		} catch (OutOfMemoryError e) {
			outOfMemoryMsg = 
			   I18N.getFramework("framework.jme.ui.calendarviewer.outofmemory");
			tmpImage = null;
		} finally {
	        System.gc();
		}
		//
		requestRepaint();
	}
	
    /**
     * <p>
     * Verifies if the image being displayed is fully visible.
     * </p>
	 * @return Fully visible or not.
	 */
	public synchronized boolean isFullyVisible() {
		Image img = getImageObject();
		//
		if (img == null) {
			return false;
		}
		//
		return img.getWidth() <= bodyRect.w && img.getHeight() <= bodyRect.h;
	}
	
    /**
     * <p>
     * Sets the image alignment.
     * </p>
	 * @param align Alignment.
     * @see com.emobtech.uime.util.ui.UIUtil#ALIGN_LEFT
     * @see com.emobtech.uime.util.ui.UIUtil#ALIGN_CENTER
     * @see com.emobtech.uime.util.ui.UIUtil#ALIGN_RIGHT
     * @see com.emobtech.uime.util.ui.UIUtil#ALIGN_TOP
     * @see com.emobtech.uime.util.ui.UIUtil#ALIGN_MIDDLE
     * @see com.emobtech.uime.util.ui.UIUtil#ALIGN_BOTTOM
	 */
	public void setImageAlignment(int align) {
		alignment = align;
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
     * @inheritDoc
     * @see br.framework.jme.ui.Screen#keyHold(int)
     */
    protected void keyHold(int keyCode) {
        final int key = getGameAction(keyCode);
        //
        if (key == UP || key == DOWN || key == LEFT || key == RIGHT) {
            keyDown(keyCode);
        }
    }
    
    /**
     * @inheritDoc
     * @see br.framework.jme.ui.Screen#keyDown(int)
     */
    protected void keyDown(int keyCode) {
        final int key = getGameAction(keyCode);
        //
        if (key == UP || key == DOWN || key == LEFT || key == RIGHT) {
            if (image != null) {
                moveImage(key);
            }
        }
    }
    
	/**
	 * @inheritDoc
	 * @see br.framework.jme.ui.Screen#penUp(int, int)
	 */
	protected void penUp(int x, int y) {
		if (!imageAreaPressed) {
			if (!vertScrollBarSquareRect.contains(x, y) &&
				vertScrollBarRect.contains(x, y)) {
				if (y < vertScrollBarSquareRect.y) {
					moveImage(Canvas.UP);
				} else {
					moveImage(Canvas.DOWN);
				}
			} else if (!horiScrollBarSquareRect.contains(x, y) &&
					   horiScrollBarRect.contains(x, y)) {
				if (x < horiScrollBarSquareRect.x) {
					moveImage(Canvas.LEFT);
				} else {
					moveImage(Canvas.RIGHT);
				}
			}
		}
		//reset control variables.
		penDraggedSquareY = -1;
		penDraggedSquareX = -1;
		imageAreaPressed = false;
	}
    
	/**
	 * @inheritDoc
	 * @see br.framework.jme.ui.Screen#penDragged(int, int)
	 */
	protected void penDragged(int x, int y) {
		if (imageAreaPressed) {
			final int GAP = 5; //pixels.
			//scrolling the image dragging the image.
			hscroll: {
				if (penDraggedSquareX != -1) {
					if (Math.abs(x - penDraggedSquareX) < GAP) {
						break hscroll;
					}
					//
					if (x < penDraggedSquareX) {
						moveImage(Canvas.RIGHT);
					} else {
						moveImage(Canvas.LEFT);
					}
				}
				//
				penDraggedSquareX = x;
			}
			vscroll: {
				if (penDraggedSquareY != -1) {
					if (Math.abs(y - penDraggedSquareY) < GAP) {
						break vscroll;
					}
					//
					if (y < penDraggedSquareY) {
						moveImage(Canvas.DOWN);
					} else {
						moveImage(Canvas.UP);
					}
				}
				//
				penDraggedSquareY = y;
			}
		} else if (vertScrollBarSquareRect.contains(x, y)) {
			penDraggedSquareY = y;
		} else if (vertScrollBarRect.contains(x, y)) {
			if (penDraggedSquareY != -1) {
				if (y < penDraggedSquareY) {
					moveImage(Canvas.UP);
				} else {
					moveImage(Canvas.DOWN);
				}
				penDraggedSquareY = -1;
			}
		} else if (horiScrollBarSquareRect.contains(x, y)) {
			penDraggedSquareX = x;
		} else if (horiScrollBarRect.contains(x, y)) {
			if (penDraggedSquareX != -1) {
				if (x < penDraggedSquareX) {
					moveImage(Canvas.LEFT);
				} else {
					moveImage(Canvas.RIGHT);
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
		if (bodyRect.contains(x, y)) {
			imageAreaPressed = true;
		}
	}

	/**
	 * @inheritDoc
	 * @see br.framework.jme.ui.Screen#drawBody(javax.microedition.lcdui.Graphics, int, int, int, int)
	 */
	protected synchronized void drawBody(Graphics g, int x, int y, int w, 
		int h) {
		ow = w;
		oh = h;
		//
        if (image == null) {
            return; //no image defined.
        }
        //
		if (outOfMemoryMsg == null) {
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
            drawImage(g, x, y, bodyRect.w, bodyRect.h);
		} else {
			g.setFont(bodyFont);
			g.setColor(bodyFontColor);
			//
			//show the out of memory message.
			g.drawString(
				outOfMemoryMsg,
				x + UIUtil.alignString(
						outOfMemoryMsg, w, UIUtil.ALIGN_CENTER, bodyFont),
				y + UIUtil.alignString(
						outOfMemoryMsg, h, UIUtil.ALIGN_MIDDLE, bodyFont),
				Graphics.TOP | Graphics.LEFT);
		}
	}
	
    /**
     * <p>
     * Draws the image.
     * </p>
	 * @param g Graphics object.
	 * @param x X coordinate.
	 * @param y Y coordinate.
	 * @param w Width.
	 * @param h Height.
	 */
	void drawImage(Graphics g, int x, int y, int w, int h) {
		if (pendingFit) {
			fit();
			pendingFit = false;
		} else if (pendingStretch) {
			stretch();
			pendingStretch = false;
		}
		//
        final Image img = getImageObject();
        //
        w = img.getWidth() < w ? img.getWidth() : w;
        h = img.getHeight() < h ? img.getHeight() : h;
        //
        UIUtil.drawRegion(
        	g, 
        	img, 
        	coord_x, 
        	coord_y, 
        	w, 
        	h, 
        	x + UIUtil.align(w, -1, bodyRect.w, alignment), 
        	y + UIUtil.align(-1, h, bodyRect.h, alignment));
	}

    /**
     * <p>
     * Draws a vertical scroll bar.
     * </p>
	 * @param g Graphics object.
	 * @param x X coordinate.
	 * @param y Y coordinate.
	 * @param w Width.
	 * @param h Height.
	 */
	void drawVerticalScrollBar(Graphics g, int x, int y, int w, int h) {
		UIUtil.drawVerticalScrollbar(
			g, 
			x, 
			y, 
			w, 
			h, 
			getImageObject().getHeight(), 
			bodyRect.h, 
			coord_y, 
			vertScrollBarSquareRect, 
			scrollBarColor, 
			skin.getVerticalScrollBarStyle());
	}
	
    /**
     * <p>
     * Draws a horizontal scroll bar.
     * </p>
	 * @param g Graphics object.
	 * @param x X coordinate.
	 * @param y Y coordinate.
	 * @param w Width.
	 * @param h Height.
	 */
	void drawHorizontalScrollBar(Graphics g, int x, int y, int w, int h) {
		UIUtil.drawHorizontalScrollbar(
			g, 
			x, 
			y, 
			w, 
			h, 
			getImageObject().getWidth(), 
			bodyRect.w, 
			coord_x, 
			horiScrollBarSquareRect, 
			scrollBarColor, 
			skin.getHorizontalScrollBarStyle());
	}
	
    /**
     * <p>
     * Verifies if the screen needs a vertical scroll bar.
     * </p>
	 * @return Needs or not.
	 */
	boolean hasVerticalScrollBar() {
        return getImageObject().getHeight() > bodyRect.h;
	}

    /**
     * <p>
     * Verifies if the screen needs a horizontal scroll bar.
     * </p>
	 * @return Needs or not.
	 */
	boolean hasHorizontalScrollBar() {
		return getImageObject().getWidth() > bodyRect.w;
	}
    
    /**
     * <p>
     * Moves the image through the display.
     * </p>
     * @param direction Diretion movement.
     * @see javax.microedition.lcdui.Canvas#UP
     * @see javax.microedition.lcdui.Canvas#DOWN
     * @see javax.microedition.lcdui.Canvas#LEFT
     * @see javax.microedition.lcdui.Canvas#RIGHT
     */
    void moveImage(int direction) {
        final Image img = getImageObject();
        final int iw = img.getWidth();
        final int ih = img.getHeight();
        final int step = ((ih > iw ? ih : iw) * 5) / 100;
        boolean repaint = false;
        //
        if (direction == UP) {
            if (coord_y -1 >= 0) {
                coord_y -= step;
                if (coord_y < 0) {
                    coord_y = 0;
                }
                //
                repaint = true;
            }
        } else if (direction == DOWN) {
            if (ih - (coord_y +1) > bodyRect.h) {
                coord_y += step;
                if (ih - coord_y < bodyRect.h) {
                    coord_y = ih - bodyRect.h;
                }
                //
                repaint = true;
            }
        } else if (direction == LEFT) {
            if (coord_x -1 >= 0) {
                coord_x -= step;
                if (coord_x < 0) {
                    coord_x = 0;
                }
                //
                repaint = true;
            }
        } else { //if (direction == RIGHT)
            if (iw - (coord_x +1) > bodyRect.w) {
                coord_x += step;
                if (iw - coord_x < bodyRect.w) {
                    coord_x = iw - bodyRect.w;
                }
                //
                repaint = true;
            }
        }
        //
        if (repaint) {
            requestRepaint();
        }
    }
    
    /**
     * <p>
     * Gets the image object to be displayed.
     * </p>
     * @return Image object.
     */
    Image getImageObject() {
        return tmpImage != null ? tmpImage : image;
    }
}