/*
 * RollerName.java
 * 08/12/2006
 * JME Framework
 * Copyright(c) Ernandes Mourao Junior (ernandes@gmail.com)
 * All rights reserved
 */
package br.framework.jme.ui;

import javax.microedition.lcdui.Font;

/**
 * <p>
 * Class responsible to roll a given string value and draw it on a given parent
 * screen, if this one does not fit a given width.
 * </p>
 * 
 * @author Ernandes Mourao Junior (ernandes@gmail.com)
 * @version 1.0
 * @since 1.0
 */
class RollerName implements Runnable {
    /**
     * <p>
     * Jump an iteration of the thread main loop.
     * </p>
     */
    private boolean jump;

    /**
     * <p>
     * Time to sleep before next refresh.
     * </p>
     */
    private int sleepTime;

    /**
     * <p>
	 * Means the thread must keep running, otherwise, it ends.
	 * </p>
	 */
	private boolean keepRunning;

	/**
	 * <p>
     * Text to be rolled.
     * </p>
     */
    private String text;
    
    /**
     * <p>
     * Current rolled name.
     * </p>
     */
    private String rolledName;
    
    /**
     * <p>
     * Start point for the rolling.
     * </p>
     */
    private int start;

	/**
	 * <p>
	 * Width of the are so that the text must fit.
	 * </p>
	 */
	private int width;

	/**
	 * <p>
	 * Font used to draw the text on the screen.
	 * </p>
	 */
	private Font font;

	/**
	 * <p>
     * Means if the text need to be rolled or not. If it does not need
     * to be rolled, it means that the text width fits in a given width.
     * </p>
     */
    private boolean needRooler;
    
    /**
     * <p>
	 * Parent screen which the text will be drawn.
	 * </p>
	 */
	private Screen screen;

	/**
     * <p>
     * Constructor.
     * </p>
     * @param screen Parent screen.
     */
    public RollerName(Screen screen) {
        this.screen = screen;
        keepRunning = true;
    }
    
    /**
     * @inheritDoc
     * @see java.lang.Runnable#run()
     */
    public void run() {
    	while (keepRunning) {
            try {
                Thread.sleep(sleepTime);
                //
                synchronized (this) {
                    if (jump) {
                        jump = false;
                        continue;
                    }
                    rollText();
                }
            } catch (InterruptedException e) {
            }
        }
    }
    
    /**
     * <p>
     * Set the value to be rolled.
     * </p>
     * @param text Text.
     * @param font Font.
     * @param w Width.
     */
    public synchronized void setText(String text, Font font, int w) {
    	if (this.text == null || !this.text.equals(text) || width != w ||
    		this.font != font) {
    		start = 0;
    		jump = true;
            this.text = text;
            this.font = font;
            rolledName = text;
            width = w;
            needRooler = font.stringWidth(text) > w;
            sleepTime = 0;
            //
            rollText();
    	}
    }
    
    /**
     * <p>
     * Get the rolled value.
     * </p>
     * @return Value.
     */
    public synchronized String getText() {
    	return rolledName;
    }
    
    /**
     * <p>
     * Stop the thread.
     * </p>
     */
    public void stop() {
    	keepRunning = false;
    }
    
    /**
     * <p>
     * Rolls the string value.
     * </p>
     */
    private synchronized void rollText() {
    	if (needRooler) {
    		if (start == 0) {
    			sleepTime = 2000;
    		} else {
    			sleepTime = 1000;
    		}
    		//
    		rolledName = text.substring(start, text.length());
    		//
    		if (font.stringWidth(rolledName) > width) {
    			int c = text.indexOf(' ', start); //move ticker's cursor.
    			//
    			if (c != -1) {
    				c++; //next char after white space.
    			} else {
    				c = text.length() -1; //end of the string.
    			}
    			//
    			if (font.stringWidth(text.substring(start, c)) > width) {
    				start++; //single word is still larger than screen width.
    			} else {
    				start = c;
    			}
    		} else {
    			start = 0;
    		}
			//
            screen.requestRepaint();
    	}
    }
}