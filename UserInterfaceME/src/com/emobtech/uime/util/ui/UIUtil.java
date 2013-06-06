/*
 * UIUtil.java
 * 03/12/2006
 * JME Framework
 * Copyright(c) Ernandes Mourao Junior (ernandes@gmail.com)
 * All rights reserved
 */
package com.emobtech.uime.util.ui;

import java.util.Vector;

import javax.microedition.lcdui.Font;
import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;
//#ifdef MIDP20
import javax.microedition.lcdui.game.Sprite;
//#endif
import com.emobtech.uime.util.StringUtil;


/**
 * <p>
 * Utility class responsible to provide some useful functions related to work
 * with UI components.
 * </p>
 * 
 * @author Ernandes Mourao Junior (ernandes@gmail.com)
 * @version 1.0
 * @since 1.0
 */
public final class UIUtil {
	/**
	 * <p>
	 * Holds an array with some useful information that are used to turn RGB
	 * values into a single hexadecimal value.
	 * </p>
	 */
	private static String[] RGB;
	
	/**
	 * <p>
	 * Holds an array with all the possible digits to represent a hexadecimal
	 * number.
	 * </p>
	 */
	private static char[] hex;
	
	/**
	 * <p>
	 * Scrollbar minimum size.
	 * </p>
	 */
	private static final int SCROLLBAR_MIN_SIZE = 10;

	/**
	 * <p>
	 * Default achor for drawing operations.
	 * </p>
	 */
	public static final int ANCHOR = Graphics.TOP | Graphics.LEFT;
	
	/**
	 * <p>
	 * General offset used in drawing operations.
	 * </p>
	 */
	public static final int OFFSET = 2;
	
	/**
     * <p>
     * Fully opaque opacity.
     * </p>
     */
    public static final int FULLY_OPAQUE = 0xff000000;

    /**
     * <p>
     * Half opaque opacity.
     * </p>
     */
    public static final int HALF_OPAQUE = 0x7f000000;

    /**
     * <p>
     * Fully transparent opacity.
     * </p>
     */
    public static final int FULLY_TRANSPARENT = 0x00000000;

	/**
	 * <p>
	 * Left alignment.
	 * </p>
	 */
	public static final int ALIGN_LEFT = 1;
	
	/**
	 * <p>
	 * Center alignment.
	 * </p>
	 */
	public static final int ALIGN_CENTER = 2;

	/**
	 * <p>
	 * Right alignment.
	 * </p>
	 */
	public static final int ALIGN_RIGHT = 4;

	/**
	 * <p>
	 * Top alignment.
	 * </p>
	 */
	public static final int ALIGN_TOP = 8;

	/**
	 * <p>
	 * Middle alignment.
	 * </p>
	 */
	public static final int ALIGN_MIDDLE = 16;

	/**
	 * <p>
	 * Bottom alignment.
	 * </p>
	 */
	public static final int ALIGN_BOTTOM = 32;
	
	/**
	 * <p>
	 * Justify the words of the lines so that all of them have the same size.
	 * </p>
	 */
	public static final int ALIGN_JUSTIFIED = 64;

	/**
	 * <p>
	 * A layout directive indicating that this OBJECT's height may be increased 
	 * to fill available space.
	 * </p>
	 */
	public static final int LAYOUT_VEXPAND = 128;

	/**
	 * <p>
	 * Plain style.
	 * </p>
	 */
	public static final int STYLE_PLAIN = 1;

	/**
	 * <p>
	 * Pipe style.
	 * </p>
	 */
	public static final int STYLE_PIPE = 2;

	/**
	 * <p>
	 * Vertical orientation style. Use this property to define the course of
	 * drawing operation of a given style.
	 * </p>
	 */
	public static final int STYLE_VERTICAL = 4;

	/**
	 * <p>
	 * Horizontal orientation style. Use this property to define the course of
	 * drawing operation of a given style.
	 * </p>
	 */
	public static final int STYLE_HORIZONTAL = 8;

	/**
	 * <p>
	 * Gradient style.
	 * </p>
	 */
	public static final int STYLE_GRADIENT = 16;

	/**
	 * <p>
	 * Left orientation style. Use this property to define the course of
	 * drawing operation of a given style.
	 * </p>
	 */
	public static final int STYLE_LEFT = 32;

	/**
	 * <p>
	 * Right orientation style. Use this property to define the course of
	 * drawing operation of a given style.
	 * </p>
	 */
	public static final int STYLE_RIGHT = 64;
	
	/**
	 * <p>
	 * Top orientation style. Use this property to define the course of
	 * drawing operation of a given style.
	 * </p>
	 */
	public static final int STYLE_TOP = 128;
	
	/**
	 * <p>
	 * Bottom orientation style. Use this property to define the course of
	 * drawing operation of a given style.
	 * </p>
	 */
	public static final int STYLE_BOTTOM = 256;

	/**
	 * <p>
	 * Color orientation. Turn a color into darker.
	 * </p>
	 */
	public static final int STYLE_DARK = 512;

	/**
	 * <p>
	 * Color orientation. Turn a color into brighter.
	 * </p>
	 */
	public static final int STYLE_BRIGTH = 1024;
	
	/**
	 * <p>
	 * Transition speed 1. Use this property to change the step of the
	 * transition of given style.
	 * </p>
	 */
	public static final int STYLE_TRANS_SPEED_1 = 131072;

	/**
	 * <p>
	 * Transition speed 2. Use this property to change the step of the
	 * transition of given style.
	 * </p>
	 */
	public static final int STYLE_TRANS_SPEED_2 = 2048;

	/**
	 * <p>
	 * Transition speed 3. Use this property to change the step of the
	 * transition of given style.
	 * </p>
	 */
	public static final int STYLE_TRANS_SPEED_3 = 4096;

	/**
	 * <p>
	 * Transition speed 4. Use this property to change the step of the
	 * transition of given style.
	 * </p>
	 */
	public static final int STYLE_TRANS_SPEED_4 = 8192;

	/**
	 * <p>
	 * Transition speed 5. Use this property to change the step of the
	 * transition of given style.
	 * </p>
	 */
	public static final int STYLE_TRANS_SPEED_5 = 16384;

	/**
	 * <p>
	 * Transition speed 6. Use this property to change the step of the
	 * transition of given style.
	 * </p>
	 */
	public static final int STYLE_TRANS_SPEED_6 = 32768;

	/**
	 * <p>
	 * Transition speed 7. Use this property to change the step of the
	 * transition of given style.
	 * </p>
	 */
	public static final int STYLE_TRANS_SPEED_7 = 65536;

	/**
	 * <p>
	 * Transition speed 8. Use this property to change the step of the
	 * transition of given style.
	 * </p>
	 */
	public static final int STYLE_TRANS_SPEED_8 = 262144;

	/**
	 * <p>
	 * Transition speed 9. Use this property to change the step of the
	 * transition of given style.
	 * </p>
	 */
	public static final int STYLE_TRANS_SPEED_9 = 524288;

	/**
	 * <p>
	 * Transition speed 10. Use this property to change the step of the
	 * transition of given style.
	 * </p>
	 */
	public static final int STYLE_TRANS_SPEED_10 = 1048576;

	/**
     * <p>
     * Resize a given image to a given size.
     * </p>
     * @param src Image object.
     * @param w Target width.
     * @param h Target height.
     * @return Resized image.
     */
    public static final Image resizeImage(Image src, int w, int h) {
        int srcW = src.getWidth();
        int srcH = src.getHeight();
        
        if (srcH == h && srcW == w) {
            return src; //does not need resize.
        }

        Image tmp = Image.createImage(w, srcH);
        Graphics g = tmp.getGraphics();

        int delta = (srcW << 16) / w;
        int pos = delta / 2;

        for (int x = 0; x < w; x++) {
            g.setClip(x, 0, 1, srcH);
            g.drawImage(src, x - (pos >> 16), 0, Graphics.LEFT | Graphics.TOP);
            pos += delta;
        }

        Image dst = Image.createImage(w, h);
        g = dst.getGraphics();

        delta = (srcH << 16) / h;
        pos = delta / 2;

        for (int y = 0; y < h; y++) {
            g.setClip(0, y, w, 1);
            g.drawImage(tmp, 0, y - (pos >> 16), Graphics.LEFT | Graphics.TOP);
            pos += delta;
        }

        return dst;
    }
    
    /**
     * <p>
     * Resizes an image to a specific dimension if necessary.
     * </p>
     * @param src Image source.
     * @param w Destination width.
     * @param h Destination height.
     * @param keepAlpha Keep alpha channel.
     * @return Resized image.
     */
    public static final Image resizeImageIfNecessary(Image src, int w, int h) {
    	int iw = -1;
    	int ih = -1;
    	//
    	if (src.getWidth() > w) {
    		iw = w;
    	}
    	if (src.getHeight() > h) {
    		ih = h;
    	}
    	//
    	if (iw != -1 || ih != -1) {
    		src =
    			resizeImage(
    				src,
    				iw != -1 ? iw : src.getWidth(),
    				ih != -1 ? ih : src.getHeight());
    	}
    	//
    	return src;
    }
    
    /**
     * <p>
     * Copies a region of the specified source image to a location within the 
     * destination.
     * </p>
     * @param g Graphics object.
     * @param image Source image to copy from.
     * @param x_src X coordinate of the upper left corner of the region within 
     *                the source image to copy.
     * @param y_src Y coordinate of the upper left corner of the region within 
     *                the source image to copy.
     * @param w Width of the region to copy.
     * @param h Height of the region to copy.
     * @param x_dest X coordinate of the anchor point in the destination drawing
     *               area.
     * @param y_dest Y coordinate of the anchor point in the destination drawing
     *               area.
     */
    public static final void drawRegion(Graphics g, Image image, int x_src, 
    	int y_src, int w, int h, int x_dest, int y_dest) {
    	//#ifdef MIDP20
    	g.drawRegion(
    		image,
    		x_src,
    		y_src,
    		w,
    		h,
    		Sprite.TRANS_NONE,
    		x_dest,
    		y_dest,
    		Graphics.TOP | Graphics.LEFT);
    	//#else
//@    	int oldx = g.getClipX();
//@    	int oldy = g.getClipY();
//@    	int oldw = g.getClipWidth();
//@    	int oldh = g.getClipHeight();
//@    	//
//@   	g.setClip(x_dest, y_dest, w, h);  
//@   	g.drawImage(image, x_dest - x_src, y_dest - y_src, 0);
//@     g.setClip(oldx, oldy, oldw, oldh);          
    	//#endif
    }
    
    /**
     * <p>
     * Split a text up in as many lines as need in order to fit each one of
     * them to a given width based on a given font.
     * </p>
     * @param text Text.
     * @param w Target width.
     * @param font Font.
     * @return Lines.
     */
    public static final String[] splitString(String text, int w, Font font) {
		if (text == null || (text = text.trim()).length() == 0) {
			return new String[0];
		}
		//
		char[] chars = text.toCharArray();
		Vector lines = new Vector(3);
		int offset = 0; //start of the line.
		int ww = 0; //line width.
		int ls = -1; //last space.
		int cw = 0;
		//
		for (int i = 0; i < chars.length; i++) {
			cw = font.charWidth(chars[i]);
			//
			if (ww + cw >= w) {
				if (ls != -1) { //is there a space char in this line?
					i = ls +1;
				}
				//
				lines.addElement(new String(chars, offset, i - offset));
				offset = i;
				ww = 0;
				ls = -1;
			}
			//
			if (StringUtil.isSpaceChar(chars[i])) {
				ls = i;
				//
				if (chars[i] == '\n') {
					cw = w;
				}
			}
			//
			ww += cw;
		}
		//
		if (ww > 0) {
			//reach the end of the text, but the last line is not inserted.
			lines.addElement(new String(chars, offset, chars.length - offset));
		}
		//
		String[] l = new String[lines.size()];
		lines.copyInto(l);
		//
		return l;
    }
    
    /**
     * <p>
     * Shrinks a given text so that it fits in a given width. If the text is
     * really shrinked a postfix ".." is put at its tail.
     * </p>
     * @param text Text to be shrinked.
     * @param width Width.
     * @param font Font.
     * @return Text shrinked.
     */
    public static final String shrinkString(String text, int width, Font font) {
    	return shrinkString(text, 0, text.length() -1, width, font, true);
    }
    
    /**
     * <p>
     * Shrinks a given subtext so that it fits in a given width. If the subtext
     * is really shrinked a postfix ".." is put at its tail.
     * </p>
     * @param text Text to be shrinked.
     * @param start Start index of the text.
     * @param end End index of the text.
     * @param width Width.
     * @param font Font.
     * @param usePostfix Add post the postfix ".." at the end or not, in case of
     *                   text be shrinked.
     * @return Subtext shrinked.
     */
    public static final String shrinkString(String text, int start, int end,
    	int width, Font font, boolean usePostfix) {
    	if (width <= 0) {
    		return "";
    	}
    	//
    	text = text.substring(start, end +1);
    	//
    	if (font.stringWidth(text) > width) {
    		if (usePostfix) {
        		width -= font.stringWidth("..");
    		}
    		//
    		if (width == 0) {
    			return "..";
    		} else if (width < 0) {
    			return "";
    		}
    		//
			char[] chars = text.toCharArray();
			int i = 0;
			int sum = 0;
			while (sum < width) {
				sum += font.charWidth(chars[i++]);
			}
			if (usePostfix) {
				return new String(chars, 0, --i) + "..";
			} else {
				return new String(chars, 0, --i);
			}
    	}else {
    	    return text;
    	}
    }

    /**
     * <p>
     * Get the offset to be used on the alignment of a given text. This offset
     * must be added to X coordinate during the text drawing so that the
     * alignment works.
     * </p>
     * @param text Text to be aligned.
     * @param area Width or height of the area which the text will be drawn on.
     * @param align Align.
     * @return Offset.
	 * @see javax.microedition.lcdui.Graphics#drawString(java.lang.String, int, int, int)
     * @see com.emobtech.uime.util.ui.UIUtil#ALIGN_LEFT
     * @see com.emobtech.uime.util.ui.UIUtil#ALIGN_CENTER
     * @see com.emobtech.uime.util.ui.UIUtil#ALIGN_RIGHT
     * @see com.emobtech.uime.util.ui.UIUtil#ALIGN_TOP
     * @see com.emobtech.uime.util.ui.UIUtil#ALIGN_MIDDLE
     * @see com.emobtech.uime.util.ui.UIUtil#ALIGN_BOTTOM
     */
    public static final int alignString(String text, int area, int align,
    	Font font) {
    	if (align == ALIGN_LEFT || align == ALIGN_TOP || text == null) {
    		return 0;
    	}
    	//
    	if (align == ALIGN_MIDDLE) {
    		return (area - font.getHeight()) / 2;
    	} else if (align == ALIGN_BOTTOM) {
    		return area - font.getHeight();
    	}
    	//
		final int textWidth = font.stringWidth(text);
    	if (align == ALIGN_CENTER) {
    		return (area - textWidth) / 2;
    	} else if (align == ALIGN_RIGHT) {
    		return area - textWidth;
    	} else {
        	return 0;
    	}
    }
    
    /**
     * <p>
     * Get the offset to be used on the alignment of a given area. This offset
     * must be added to X coordinate during the area drawing so that the
     * alignment works.
     * </p>
     * <p>
     * The alignment can be both vertical or horizontal. If you want a vertical
     * alignment, you must specify the parameter h, passing the parementer w as 
     * -1. For horizontal alignment, you specify the parementer w, leaving the
     * parameter h as -1.
     * </p>
     * @param w Horizontal area.
     * @param h Vertical area.
     * @param areaSize Area size which the alignment is applied.
     * @param align Align.
     * @return Offset.
	 * @see javax.microedition.lcdui.Graphics#drawString(java.lang.String, int, int, int)
     * @see com.emobtech.uime.util.ui.UIUtil#ALIGN_LEFT
     * @see com.emobtech.uime.util.ui.UIUtil#ALIGN_CENTER
     * @see com.emobtech.uime.util.ui.UIUtil#ALIGN_RIGHT
     * @see com.emobtech.uime.util.ui.UIUtil#ALIGN_TOP
     * @see com.emobtech.uime.util.ui.UIUtil#ALIGN_MIDDLE
     * @see com.emobtech.uime.util.ui.UIUtil#ALIGN_BOTTOM
     */
    public static final int align(int w, int h, int areaSize, 
    	int align) {
    	if (w != -1) {
    		if ((align & ALIGN_RIGHT) != 0) {
    			return areaSize - w;
    		} else if ((align & ALIGN_CENTER) != 0) {
    			return (areaSize - w) / 2;
    		} else { //LEFT
    			return 0;
    		}
    	} else {
    		if ((align & ALIGN_BOTTOM) != 0) {
    			return areaSize - h;
    		} else if ((align & ALIGN_MIDDLE) != 0) {
    			return (areaSize - h) / 2;
    		} else { //TOP
    			return 0;
    		}
    	}
    }

    /**
	 * <p>
	 * Get a hexadecimal representation to a given rgb.
	 * </p>
	 * @param r Red.
	 * @param g Green.
	 * @param b Blue.
	 * @return Hexadecimal.
	 */
	public static final int getHexColor(int r, int g, int b) {
		return getHexColor(r, g, b, 0);
	}

    /**
	 * <p>
	 * Get a hexadecimal representation to a given rgb.
	 * </p>
	 * @param r Red.
	 * @param g Green.
	 * @param b Blue.
	 * @param brightness Brightness factor to be applyed on given color.
	 *                   For brighter colores use positive values from 0 up to
	 *                   255, otherwise, use from 0 up to -255.
	 * @return Hexadecimal.
	 */
	public static final int getHexColor(int r, int g, int b, int brightness) {
		if (r == -1 && g == -1 && b == -1) {
			return -1;
		}
		//
		if (RGB == null) {
			//caching auxiliar data.
			hex = new char[] {
				'0','1','2','3','4',
				'5','6','7','8','9',
				'A','B','C','D','E','F'};
			//
			int k = 0;
			RGB = new String[256];
			for (int i = 0 ; i < 16; i++) {
			    for (int j = 0; j < 16; j++) {
				    RGB[k] = "" + hex[i] + hex[j];
				    k++;
			    }
			}
		}
		//
		if (brightness != 0) {
			//applying brightness on a given color.
	        r += brightness;
	        g += brightness;
	        b += brightness;
	        //checking RGB bounds.
	        r = r > 255 ? 255 : r < 0 ? 0 : r;
	        g = g > 255 ? 255 : g < 0 ? 0 : g;
	        b = b > 255 ? 255 : b < 0 ? 0 : b;
		}
		//
		return Integer.parseInt(RGB[r] + RGB[g] + RGB[b], 16);
	}
    
    /**
     * <p>
     * Get the negative color from a given rgb.
     * </p>
	 * @param r Red.
	 * @param g Green.
	 * @param b Blue.
     * @return Negative color.
     */
    public static final int getNegativeColor(int r, int g, int b) {
        final int alpha = ((r << 5) + (g << 6) + (b << 2)) / 100;
        return alpha > 128 ? darkerColor(r, g, b) : brighterColor(r, g, b);
    }
    
    /**
	 * <p>
	 * Get the RGB data from a given color.
	 * </p>
	 * @param color Color.
	 * @param w Area width.
	 * @param h Area height.
	 * @param opacity Opacity level.
	 * @return RGB data.
     * @see com.emobtech.uime.util.ui.UIUtil#FULLY_OPAQUE
     * @see com.emobtech.uime.util.ui.UIUtil#HALF_OPAQUE
     * @see com.emobtech.uime.util.ui.UIUtil#FULLY_TRANSPARENT
	 */
	public static final int[] getRGB(int color, int w, int h, int opacity) {
        int [] pixelArray = new int[w * h];
		color = opacity + color;
		//
        for(int i = 0; i < pixelArray.length; i++) {
            pixelArray[i] = color;
        }
        //
        return pixelArray;
	}
	
//	public static final int[] getRoundRGB(int color, int w, int h, int arcw, int arch, int opacity) {
//        Image img = Image.createImage(w, h);
//        //
//        Graphics g = img.getGraphics();
//        g.setColor(color);
//        g.fillRoundRect(0, 0, w, h, arcw, arch);
//		
//		
//		int [] pixelArray = new int[w * h];
//		
//		img.getRGB(pixelArray, 0, w, 0, 0, w, h);
//		
//		int newColor = opacity + color;
//		//
//        for(int i = 0; i < pixelArray.length; i++) {
//        	if (pixelArray[i] != -1) {
//                pixelArray[i] = newColor;
//        	}
//        }
//        //
//        return pixelArray;
//	}

	/**
	 * <p>
	 * Draw a given equilateral triangle.
	 * </p>
	 * @param g Graphic object.
	 * @param x X coordinate.
	 * @param y Y coordinate.
	 * @param sidew Side width.
	 * @param upsideDown Draw the an upside down triangle or not.
	 */
	public static final void drawTriangle(Graphics g, int x, int y, int sidew,
		boolean upsideDown) {
		int h = (sidew) / 2;
		int d = 0;
		//
		if (!upsideDown) {
			y += sidew;
		}
		//
		while (h-- >= 0) {
			g.drawLine(x + d, y, x + sidew - d, y);
			//
			d++;
			y += upsideDown ? +1 : -1;
		}
	}

	/**
	 * <p>
	 * Fill a given rectangle with a stylized drawing in a given graphics.
	 * </p>
	 * @param g Graphic object.
	 * @param x X coordinate.
	 * @param y Y coordinate.
	 * @param w Width.
	 * @param h Height.
	 * @param color Color.
	 * @param style Style.
	 * @see com.emobtech.uime.util.ui.UIUtil#STYLE_PIPE
     * @see com.emobtech.uime.util.ui.UIUtil#STYLE_PLAIN
     * @see com.emobtech.uime.util.ui.UIUtil#STYLE_GRADIENT
	 */
	public static final void fillRect(Graphics g, int x, int y, int w, int h,
		int color, int style) {
		if ((style & STYLE_PIPE) != 0) {
			fillPipeStyleRect(g, x, y, w, h, color, style);
		} else if ((style & STYLE_GRADIENT) != 0) {
			fillGradientStyleRect(g, x, y, w, h, color, style);
		} else {
			g.setColor(color);
			g.fillRect(x, y, w, h);
		}
	}
	
	/**
	 * <p>
	 * Draws a vertical scrollbar.
	 * </p>
	 * @param g Graphics object.
	 * @param x X coordinate.
	 * @param y Y coordinate.
	 * @param w Width.
	 * @param h Height.
	 * @param linesCount Lines count.
	 * @param linesPerPage Lines count per page.
	 * @param firstLine First visible line.
	 * @param squareRect Scrollbar square rect.
	 * @param color Color.
	 * @param style Style.
	 */
	public static final void drawVerticalScrollbar(Graphics g, int x, int y,
		int w, int h, int linesCount, int linesPerPage, int firstLine, 
		Rect squareRect, int color, int style) {
	    int pxs = linesCount - linesPerPage;
	    if (pxs > 0) { // there is no line to scroll.
	    	if (squareRect == null) {
	    		squareRect = new Rect();
	    	}
	    	//
    		squareRect.x = x;
    		squareRect.w = w;
    		squareRect.h = (100 * (linesPerPage)) / linesCount;
	        if (squareRect.h < SCROLLBAR_MIN_SIZE) {
	        	squareRect.h = SCROLLBAR_MIN_SIZE;
	        }
	        //
	    	int step = 0; // amount of pixel to go up or down during scrolling.
	    	//
	        if (firstLine > 0) {
	        	int p = (100 * (firstLine + linesPerPage)) / linesCount;
	        	//
	        	step = (p * (h - squareRect.h)) / 100;
	        }
	        //
	        //draw the scrollbar background.
	        UIUtil.fillRect(
	        	g,
	        	x,
	        	y,
	        	w,
	        	h,
	        	UIUtil.getHexColor(255, 255, 255),
	        	UIUtil.STYLE_GRADIENT | UIUtil.STYLE_HORIZONTAL |
	        		UIUtil.STYLE_RIGHT | UIUtil.STYLE_TRANS_SPEED_9);
	        //
	        //drawing scrollbar background border.
	        g.setColor(0, 0, 0); //black color.
	        g.drawRect(x, y, w -1, h -1);
	        //
	        //draw the square.
	        squareRect.y = y + step;
	        final int boundY = y + h - squareRect.h;
	        if (squareRect.y > boundY) {
	        	squareRect.y = boundY; //in case of the square crosses the scrollbar area.
	        }
	        //
	        UIUtil.fillRect(g, x, squareRect.y, w, squareRect.h, color, style);
	        g.setColor(0, 0, 0); //black color.
	        g.drawRect(x, squareRect.y, w -1, squareRect.h -1);
	    }
	}
	
	/**
	 * <p>
	 * Draws a horizontal scrollbar.
	 * </p>
	 * @param g Graphics object.
	 * @param x X coordinate.
	 * @param y Y coordinate.
	 * @param w Width.
	 * @param h Height.
	 * @param linesCount Lines count.
	 * @param linesPerPage Lines count per page.
	 * @param firstLine First visible line.
	 * @param squareRect Scrollbar square rect.
	 * @param color Color.
	 * @param style Style.
	 */
	public static final void drawHorizontalScrollbar(Graphics g, int x, int y,
		int w, int h, int linesCount, int linesPerPage, int firstLine, 
		Rect squareRect, int color, int style) {
	    int pxs = linesCount - linesPerPage;
	    if (pxs > 0) { // there is no line to scroll.
	    	if (squareRect == null) {
	    		squareRect = new Rect();
	    	}
	    	//
	    	squareRect.x = x;
	    	squareRect.y = y;
	    	squareRect.h = h;
	    	squareRect.w = (100 * (linesPerPage)) / linesCount;
	        if (squareRect.w < SCROLLBAR_MIN_SIZE) {
	        	squareRect.w = SCROLLBAR_MIN_SIZE;
	        }
	        //
	    	int step = 0; // amount of pixel to go up or down during scrolling.
	    	//
	        if (firstLine > 0) {
	        	int p = (100 * (firstLine + linesPerPage)) / linesCount;
	        	//
	        	step = (p * (w - squareRect.w)) / 100;
	        }
	        //
	        //draw the scrollbar background.
	        UIUtil.fillRect(
	        	g,
	        	x,
	        	y,
	        	w,
	        	h,
	        	UIUtil.getHexColor(255, 255, 255),
	        	UIUtil.STYLE_GRADIENT | UIUtil.STYLE_VERTICAL |
	        		UIUtil.STYLE_BOTTOM | UIUtil.STYLE_TRANS_SPEED_9);
	        //drawing scrollbar background border.
	        g.setColor(0, 0, 0); //black color.
	        g.drawRect(x, y, w -1, h);
	        //draw the square.
	        squareRect.x = x + step;
	        //
	        UIUtil.fillRect(g, squareRect.x, y, squareRect.w, h, color, style);
	        g.setColor(0, 0, 0); //black color.
	        g.drawRect(squareRect.x, y, squareRect.w -1, h -1);
	    }
	}
	
	/**
	 * <p>
	 * Gets the best scrollbar size according to some criteria.
	 * </p>
	 * @param w Screen width.
	 * @param h Screen height.
	 * @param hasPointerEvents Has pointer events.
	 * @return Size.
	 */
	public static final int getScrollbarSize(int w, int h, 
		boolean hasPointerEvents) {
		return hasPointerEvents ? 7 : 5;
	}
	
	/**
	 * <p>
	 * Draws a given string.
	 * </p>
	 * @param g Graphic object.
	 * @param x X coordinate.
	 * @param y Y coordinate.
	 * @param w Width.
	 * @param h Height.
	 * @param text String to draw.
	 * @param font Font.
	 * @param align Alignment constants.
	 * @param color Color.
	 * @param tail Use tail ".." if string does not fit.
	 */
	public static final void drawString(Graphics g, int x, int y, int w, int h,
		String text, Font font, int align, int color, boolean tail) {
		drawString(
			g, x, y, w, h, text, 0, text.length(), font, align, color, tail);
	}
	
	/**
	 * <p>
	 * Draws a given string.
	 * </p>
	 * @param g Graphic object.
	 * @param x X coordinate.
	 * @param y Y coordinate.
	 * @param w Width.
	 * @param h Height.
	 * @param text String to draw.
	 * @param offset String offset.
	 * @param length String length.
	 * @param font Font.
	 * @param align Alignment constants.
	 * @param color Color.
	 * @param tail Use tail ".." if string does not fit.
	 */
	public static final void drawString(Graphics g, int x, int y, int w, int h,
		String text, int offset, int length, Font font, int align, int color,
		boolean tail) {
		g.setColor(color);
		g.setFont(font);
		//
		if (offset != 0 || length != text.length()) {
			text = text.substring(offset, offset + length);
		}
		//
		final char[] chars = text.toCharArray();
		final int lc = font.charWidth('0'); //largest char.
		final int fh = font.getHeight() + OFFSET; //font height.
		final int lineCount = h / fh; //visible line count.
		//
		if (lineCount == 0) {
			throw new IllegalArgumentException(
				"The given height (" + h + ") does not fit the font height.");
		}
		//
		int[] lines = new int[h / fh +1]; 
		offset = 0; //start of the line.
		int ww = 0; //line width.
		int ls = -1; //last space.
		int c = 0; //line count.
		int cw = 0;
		//
		for (int i = 0; i < chars.length; i++) {
			cw = font.charWidth(chars[i]);
			//
			if (ww + cw >= w) {
				if (ls != -1 && c +1 < lineCount) { //is there space in this line?
					i = ls +1;
				}
				//
				lines[c++] = offset; //starting the line.
				//
				offset = i;
				ww = 0;
				ls = -1;
				//
				if (c == lineCount) { //is there space for one more line?
					lines[c] = i;
					ww = 0;
					break;
				}
			}
			//
			ls = StringUtil.isSpaceChar(chars[i]) ? i : ls;
			ww += cw;
		}
		//
		if (ww > 0) {
			//reach the end of the text, but the last line is not inserted.
			lines[c++] = offset;
		}
		if (lines[c] == 0) {
			lines[c] = text.length(); //limit of the last line, in case of this line be reach.
		}
		//
		y += UIUtil.align(-1, fh * c, h, align);
		//
		//drawing the lines.
		for (int i = 0; i < c; i++) {
			offset = lines[i];
			length = lines[i +1] - offset;
			ww = font.charsWidth(chars, offset, length);
			//
			if (tail && i +1 == c && lines[c] != text.length()) {
				//remove last char to put the ".."
				length--;
				ww -= lc;
			}
			//
			g.drawChars(
				chars,
				offset,
				length,
				x + UIUtil.align(ww, -1, w, align),
				y,
				Graphics.TOP | Graphics.LEFT);
			//
			y += fh;
		}
		//
		if (tail && lines[c] != text.length()) {
			g.drawString(
				"..",
				x + ww + UIUtil.align(ww, -1, w, align),
				y - fh,
				Graphics.TOP | Graphics.LEFT);
		}
	}
	
	/**
	 * <p>
	 * Get a brighter color from a given RGB.
	 * </p>
	 * @param r Red.
	 * @param g Green.
	 * @param b Blue.
	 */
	private static final int brighterColor(int r, int g, int b) {
	    final int fadeStep = 255; //colored display.  
		return getHexColor(
			Math.min(r + fadeStep, 255),
			Math.min(g + fadeStep, 255),
			Math.min(b + fadeStep, 255));
	}

	/**
	 * <p>
	 * Get a darker color from a given RGB.
	 * </p>
	 * @param r Red.
	 * @param g Green.
	 * @param b Blue.
	 */
	private static final int darkerColor(int r, int g, int b) {
	    final int fadeStep = -255; //colored display.  
		return getHexColor(
			Math.max(r + fadeStep, 0),
			Math.max(g + fadeStep, 0),
			Math.max(b + fadeStep, 0));
	}
	
	/**
	 * <p>
	 * Fill a given rectangle using the "Pipe" style in a given graphics.
	 * </p>
	 * @param g Graphic object.
	 * @param x X coordinate.
	 * @param y Y coordinate.
	 * @param w Width.
	 * @param h Height.
	 * @param color Color.
	 * @param style Style.
	 */
	private static final void fillPipeStyleRect(Graphics g, int x, int y, int w,
		int h, int color, int style) {
		g.setColor(color);
		//
        final int rr = g.getRedComponent();
        final int gg = g.getGreenComponent();
        final int bb = g.getBlueComponent();
        //
        final int step = 30;
        final boolean isHor = (style & STYLE_HORIZONTAL) != 0;
        final int l = isHor ? w : h;
        int brightness = -120;
        int crh = 0; //central rectangle height.
        int perc;
        int newColor;
        //
        for (int i = 0; i < l; i++) {
            perc = (i / l) * 100; //TODO: testar devido a mudanca de float para int.
            //
            if (perc >= 0 && perc <= 10) {
            	brightness += step;
            } else if (perc >= 90) {
            	if (crh != 0) {
            		g.setColor(color);
            		if (isHor) {
	    	            g.fillRect(x - crh, y, crh, h);
            		} else {
	    	            g.fillRect(x, y - crh, w, crh);
            		}
    	            crh = 0;
            	}
            	brightness -= step;
            } else {
            	crh++;
            }
            //
            if (crh == 0) {
	            newColor = UIUtil.getHexColor(rr, gg, bb, brightness);
	            g.setColor(newColor < color ? newColor : color);
	            //
	            if (isHor) {
		            g.drawLine(x, y, x, y + h);
	            } else {
		            g.drawLine(x, y, x + w, y);
	            }
            }
            if (isHor) {
	            x++;
            } else {
	            y++;
            }
        }
        //
        if (crh != 0) {
    		if (isHor) {
	            g.fillRect(x - crh, y, crh, h);
    		} else {
	            g.fillRect(x, y - crh, w, crh);
    		}
        }
	}
	
	/**
	 * <p>
	 * Fill a given rectangle using the "Gradient" style in a given graphics.
	 * </p>
	 * @param g Graphic object.
	 * @param x X coordinate.
	 * @param y Y coordinate.
	 * @param w Width.
	 * @param h Height.
	 * @param color Color.
	 * @param style Style.
	 */
	private static final void fillGradientStyleRect(Graphics g, int x, int y,
		int w, int h, int color, int style) {
		g.setColor(color);
		//
		int step;
		if ((style & STYLE_TRANS_SPEED_1) != 0) {
			step = 1;
		} else if ((style & STYLE_TRANS_SPEED_2) != 0) {
			step = 3;
		} else if ((style & STYLE_TRANS_SPEED_3) != 0) {
			step = 5;
		} else if ((style & STYLE_TRANS_SPEED_4) != 0) {
			step = 8;
		} else if ((style & STYLE_TRANS_SPEED_5) != 0) {
			step = 10;
		} else if ((style & STYLE_TRANS_SPEED_6) != 0) {
			step = 13;
		} else if ((style & STYLE_TRANS_SPEED_7) != 0) {
			step = 15;
		} else if ((style & STYLE_TRANS_SPEED_8) != 0) {
			step = 17;
		} else if ((style & STYLE_TRANS_SPEED_9) != 0) {
			step = 20;
		} else if ((style & STYLE_TRANS_SPEED_10) != 0) {
			step = 25;
		} else {
			step = 5;
		}
		//
        final int rr = g.getRedComponent();
        final int gg = g.getGreenComponent();
        final int bb = g.getBlueComponent();
        final boolean darker = (style & STYLE_BRIGTH) == 0;
        //
        int brightness = 0;
        //
        if ((style & STYLE_HORIZONTAL) != 0) {
            final boolean fromRight = (style & STYLE_RIGHT) != 0;
            if (fromRight) {
            	//step from the darker towards to the brighter.
            	brightness = (int)(w * step * (darker ? -1 : 1));
            	if (darker) {
                	step = -step;
            	}
            } else {
            	if (!darker) {
            		step = -step;
            	}
            }
            //
    		for (int i = 0; i < w; i++) {
    			g.setColor(getHexColor(rr, gg, bb, brightness));
    			g.drawLine(x + i, y, x + i, y + h);
    			//
    			brightness -= step;
    		}
        } else {
            final boolean fromBottom = (style & STYLE_BOTTOM) != 0;
            if (fromBottom) {
            	//step from the darker towards to the brighter.
            	brightness = (int)(h * step * (darker ? -1 : 1));
            	if (darker) {
                	step = -step;
            	}
            } else {
            	if (!darker) {
            		step = -step;
            	}
            }
            //
    		for (int i = 0; i < h; i++) {
    			g.setColor(getHexColor(rr, gg, bb, brightness));
    			g.drawLine(x, y + i, x + w, y + i);
    			//
       			brightness -= step;
    		}
        }
	}

	/**
	 * <p>
	 * Avoid to create instances.
	 * </p>
	 */
	private UIUtil() {
	}
}
