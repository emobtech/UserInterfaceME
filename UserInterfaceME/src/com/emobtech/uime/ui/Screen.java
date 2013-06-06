/*
 * Screen.java
 * 05/12/2006
 * JME Framework
 * Copyright(c) Ernandes Mourao Junior (ernandes@gmail.com)
 * All rights reserved
 */
package com.emobtech.uime.ui;

import javax.microedition.lcdui.Canvas;
import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.Font;
import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;

import com.emobtech.uime.midlet.MIDlet;
import com.emobtech.uime.util.StringUtil;
import com.emobtech.uime.util.ui.Rect;
import com.emobtech.uime.util.ui.UIUtil;


/**
 * <p>
 * Abstract class that defines a basic structure for a screen. If you intend
 * to create you own custom screens, you start extending this one.
 * </p>
 * 
 * <p>
 * This class supports both native and custom commands and bar title. By default
 * custom ones are used. If you want to use the native ones, you can configure
 * this through the keys "br-framework-jme-ui-use-native-command" and 
 * "br-framework-jme-ui-use-native-title" setting their values as
 * <code>true</code>, in the application descriptor.
 * </p>
 * 
 * @author Ernandes Mourao Junior (ernandes@gmail.com)
 * @version 1.0
 * @since 1.0
 * @see br.framework.jme.ui.Form
 * @see br.framework.jme.ui.List
 * @see br.framework.jme.ui.Grid
 * @see br.framework.jme.ui.DetailList
 * @see br.framework.jme.ui.GroupedList
 * @see br.framework.jme.ui.Calendar
 * @see br.framework.jme.ui.ImageViewer
 * @see br.framework.jme.ui.TextBox
 */
public abstract class Screen extends Canvas {
    /**
     * <p>
     * Skin.
     * </p>
     */
    static final Skin skin = Skin.getDefault();
    
    /**
     * <p>
     * Indicate to use native command. 0 - Not use / 1 - Use.
     * </p>
     */
    private int useNativeCommand = -1;
    
    /**
     * <p>
     * Indicate to use native title. 0 - Not use / 1 - Use.
     * </p>
     */
    private int useNativeTitle = -1;

    /**
	 * <p>
	 * Offset. 
	 * </p>
	 */
    protected final static int OFFSET = UIUtil.OFFSET;

	/**
	 * <p>
	 * Flag that means the screen is top most.
	 * </p>
	 */
	private Boolean isShown;

	/**
     * <p>
	 * Auxiliary image object. This object is used to perform the paint
	 * operation of the screen.
	 * </p>
	 */
	private Image auxImg;

	/**
	 * <p>
	 * Auxiliary graphics object. This object is used to perform the paint
	 * operation of the screen.
	 * </p>
	 */
	private Graphics auxGrap;
	
	/**
	 * <p> Screen width. </p>
	 */
	private int width = -1;

	/**
	 * <p> Screen height. </p>
	 */
	private int height = -1;
	
	/**
	 * <p>
	 * Indicates if the screen is on fullscree mode.
	 * </p>
	 */
	boolean fullScreenMode;
	
	/**
	 * <p>
	 * Back screen.
	 * </p>
	 */
	Screen backScreen;

	/**
	 * <p>
	 * Parent screen.
	 * </p>
	 */
	Screen parent;

	/**
	 * <p>
	 * Hide screen title.
	 * </p>
	 */
	boolean hideTitle;
	
	/**
	 * <p>
	 * Hide commands bar.
	 * </p>
	 */
	boolean hideCommands;
	
	/**
	 * <p>
	 * Background image.
	 * </p>
	 */
	protected Image backImage;
    
    /**
     * <p>
     * Object responsible to manage the commands added into the screen.
     * </p>
     */
    CommandMenu commandBar;
    
    /**
     * <p>
     * Title rectangle.
     * </p>
     */
    Rect titleRect;
    
    /**
     * <p>
     * Body rectangle.
     * </p>
     */
    protected Rect bodyRect;
    
    /**
     * <p>
     * </p>
     */
    boolean fireKeyMainCmd;
    
	/**
	 * <p> Image alignment. </p>
	 */
	protected int backImgAlignment = UIUtil.ALIGN_CENTER | UIUtil.ALIGN_MIDDLE;

	/**
	 * <p> Background style. </p>
	 */
	protected int backStyle = UIUtil.STYLE_PLAIN;

	/**
	 * <p> Background color. </p>
	 */
	protected int backgroundColor;

	/**
	 * <p> Screen title. </p>
	 */
	protected String title;

	/**
	 * <p> Title font. </p>
	 */
	protected Font titleFont;

	/**
	 * <p> Body font. </p>
	 */
	protected Font bodyFont;

	/**
	 * <p> Title color. </p>
	 */
	protected int titleColor;

	/**
	 * <p> Title font color. </p>
	 */
	protected int titleFontColor;

	/**
	 * <p> Body font color. </p>
	 */
	protected int bodyFontColor;
	
	/**
	 * 
	 */
	Image titleImage;

	/**
	 * <p>
	 * Constructor.
	 * </p>
	 * @param title Screen title.
	 */
	public Screen(String title) {
		setTitle(title);
        //
		setTitleFont(
			Font.getFont(Font.FACE_SYSTEM, Font.STYLE_BOLD, Font.SIZE_MEDIUM));
		setBodyFont(
			Font.getFont(Font.FACE_SYSTEM, Font.STYLE_BOLD, Font.SIZE_MEDIUM));
		//
        commandBar = new CommandMenu(this, null);
        //
        titleRect = new Rect();
        bodyRect = new Rect();
        //
        skin.applyScreenSkin(this);
	}
	
	/**
	 * <p>
	 * Sets the screen title.
	 * </p>
	 * @param title Title.
	 */
	public void setTitle(String title) {
		this.title = title;
		//
		//#ifdef MIDP20
		if (useNativeTitle()) {
			super.setTitle(title);
		}
		//#endif
	}
	
	/**
	 * <p>
	 * Gets the screen title.
	 * </p>
	 * @return Title.
	 */
	public String getTitle() {
		return title;
	}
    
    /**
     * <p>
     * Set the screen height.
     * </p>
     * @param height Height.
     */
    public void setHeight(int height) {
    	this.height = height;
    	//
    	sizeChanged(getWidth(), getHeight());
    }
    
    /**
     * @inheritDoc
     * @see javax.microedition.lcdui.Canvas#getHeight()
     */
    public int getHeight() {
    	if (height != -1) {
    		return height;
    	} else {
    		return super.getHeight();
    	}
    }
    
    /**
     * @see javax.microedition.lcdui.Canvas#getGameAction(int)
     */
    public int getGameAction(int keyCode) {
    	try {
        	return super.getGameAction(keyCode);
		} catch (Exception e) {
			return keyCode;
		}
    }

    /**
     * @inheritDoc
     * @see javax.microedition.lcdui.Canvas#getWidth()
     */
    public int getWidth() {
    	if (width != -1) {
    		return width;
    	} else {
        	return super.getWidth();
    	}
    }
    
    /**
     * <p>
     * Set the screen width.
     * </p>
     * @param width Width.
     */
    public void setWidth(int width) {
    	this.width = width;
    	//
    	sizeChanged(getWidth(), getHeight());
    }

    /**
     * <p>
     * Get the object responsible to manage the commands added into the screen.
     * </p>
     * @return Object.
     */
    public CommandMenu getCommandMenu() {
    	return commandBar;
    }
	
	/**
	 * <p>
	 * Set the title color.
	 * </p>
	 * @param r Red.
	 * @param g Green.
	 * @param b Blue.
	 */
	public void setTitleColor(int r, int g, int b) {
		titleColor = UIUtil.getHexColor(r, g, b);
	}
	
	/**
	 * <p>
	 * Set the title font color.
	 * </p>
	 * @param r Red.
	 * @param g Green.
	 * @param b Blue.
	 */
	public void setTitleFontColor(int r, int g, int b) {
		titleFontColor = UIUtil.getHexColor(r, g, b);
	}

	/**
	 * <p>
	 * Set the body font color.
	 * </p>
	 * @param r Red.
	 * @param g Green.
	 * @param b Blue.
	 */
	public void setBodyFontColor(int r, int g, int b) {
		bodyFontColor = UIUtil.getHexColor(r, g, b);
	}

	/**
	 * <p>
	 * Set the background color. When this method is invoked, it overlaps any
	 * background image defined to the screen.
	 * </p>
	 * @param r Red.
	 * @param g Green.
	 * @param b Blue.
	 */
	public void setBackgroundColor(int r, int g, int b) {
		backgroundColor = UIUtil.getHexColor(r, g, b);
		backImage = null;
	}
	
	/**
	 * <p>
	 * Set the background image. When this method is invoked, it overlaps any
	 * background color defined to the screen.
	 * </p>
	 * @param img Image object.
	 */
	public void setBackgroundImage(Image img) {
		backImage = img;
	}
	
	/**
	 * <p>
	 * Set the title image.
	 * </p>
	 * @param img Image object.
	 */
	public void setTitleImage(Image img) {
		titleImage = img;
	}
	
	/**
	 * <p>
	 * Set the title font.
	 * </p>
	 * @param font Font.
	 */
	public void setTitleFont(Font font) {
		titleFont = font;
	}

	/**
	 * <p>
	 * Set the body font.
	 * </p>
	 * @param font Font.
	 */
	public void setBodyFont(Font font) {
		bodyFont = font;
	}
    
    /**
     * @inheritDoc
     * @see javax.microedition.lcdui.Displayable#addCommand(javax.microedition.lcdui.Command)
     */
    public synchronized void addCommand(Command cmd) {
        commandBar.addCommand(cmd);
        //
    	if (useNativeCommand()) {
    		super.addCommand(cmd);
    	}
    }
    
    /**
     * @inheritDoc
     * @see javax.microedition.lcdui.Displayable#removeCommand(javax.microedition.lcdui.Command)
     */
    public synchronized void removeCommand(Command cmd) {
    	commandBar.removeCommand(cmd);
    	//
    	if (useNativeCommand()) {
    		super.removeCommand(cmd);
    	}
    }
    
	/**
	 * <p>
	 * Verifies if a given command is present in the screen.
	 * </p>
	 * @param cmd Command.
	 * @return Present or not.
	 */
    public synchronized boolean hasCommand(Command cmd) {
		return commandBar.hasCommand(cmd);
	}
    
    /**
     * <p>
     * Remove all commands of the screen.
     * </p>
     */
    public synchronized void removeAllCommands() {
    	if (useNativeCommand()) {
        	Command[] cmds = commandBar.getAllCommands();
        	//
        	for (int i = cmds.length -1; i >= 0; i--) {
        		super.removeCommand(cmds[i]);	
    		}
    	}
    	//
    	commandBar.removeAllCommands();
    }
    
    /**
     * <p>
     * Get all the commands.
     * </p>
     * @return Commands.
     */
    public synchronized Command[] getAllCommands() {
        return commandBar.getAllCommands();
    }
    
    /**
     * @inheritDoc
     * @see javax.microedition.lcdui.Displayable#setCommandListener(javax.microedition.lcdui.CommandListener)
     */
    public void setCommandListener(CommandListener listener) {
        commandBar.setCommandListener(listener);
        //
        if (useNativeCommand()) {
        	super.setCommandListener(listener);
        }
    }
    
	/**
	 * <p>
	 * Get the command listener.
	 * </p>
	 * @return Listener.
	 */
	public CommandListener getCommandListenerObject() {
		return commandBar.getCommandListener();
	}
	
	/**
	 * @inheritDoc
	 * @see javax.microedition.lcdui.Displayable#isShown()
	 */
	public boolean isShown() {
		return isShown == null ? super.isShown() : isShown.booleanValue();
	}
	
	/**
	 * @inheritDoc
	 * @see javax.microedition.lcdui.Canvas#paint(javax.microedition.lcdui.Graphics)
	 */
	public final void paint(Graphics g) {
		paint(g, 0, 0);
	}
    
    /**
	 * <p>
	 * Method responsible to draw the screen.
	 * </p>
	 * @param g Graphics object.
	 * @param x X coordinate.
	 * @param y Y coordinate.
	 */
	public final void paint(Graphics g, int x, int y) {
		final int w = getWidth();
		final int h = getHeight();
		//
		if (auxImg == null || auxImg.getWidth() != w || auxImg.getHeight()!=h) {
	        //off-screen buffer strategy.
			auxImg = Image.createImage(w, h);
	        auxGrap = auxImg.getGraphics();
		}
		//
		titleRect.x = 0;
		titleRect.y = 0;
		titleRect.w = w;
		//
		if (useNativeTitle() || hideTitle || title == null) {
			titleRect.h = 0;
		} else {
			titleRect.h = calcBestTitleHeight();
			auxGrap.setClip(titleRect.x, titleRect.y, w, titleRect.h);
			drawTitle(auxGrap, titleRect.x, titleRect.y, w, titleRect.h);
		}
		//
		bodyRect.x = 0;
		bodyRect.y = titleRect.h;
		bodyRect.w = w;
		bodyRect.h = h - titleRect.h;
		//
        //
		if (!useNativeCommand() && !hideCommands &&
			commandBar.getCommandCount() > 0) {
			auxGrap.setClip(bodyRect.x, bodyRect.y, w, bodyRect.h);
			bodyRect.h -= commandBar.getRect().h;
			commandBar.paint(auxGrap, bodyRect.x, bodyRect.y + bodyRect.h);
		}
		//
		if (!commandBar.isExpanded()) {
			auxGrap.setClip(bodyRect.x, bodyRect.y, w, bodyRect.h);
			drawBackground(auxGrap, bodyRect.x, bodyRect.y, w, bodyRect.h);
			drawBody(auxGrap, bodyRect.x, bodyRect.y, w, bodyRect.h);
		}
		//
		g.drawImage(auxImg, x, y, Graphics.TOP | Graphics.LEFT);
	}
	
	/**
	 * <p>
	 * Requests that a given displayable object be made visible on the 
	 * display.
	 * </p>
	 * @param displayable Displayable.
	 */
	public void show(Displayable displayable) {
		MIDlet.getDisplayInstance().setCurrent(displayable);
	}
	
	/**
	 * <p>
	 * Requests that a given screen object be made visible on the display and 
	 * set the caller screen as the back screen of the next screen.
	 * </p>
	 * @param screen Next screen object.
	 */
	public void showNext(Screen screen) {
		screen.backScreen = this; //holds back screen.
		screen.showNotify(this);
		show(screen);
	}
	
	/**
	 * <p>
	 * Requests that the screen's back screen object be made visible on the 
	 * display. After that, the back screen's reference is set as null.
	 * </p>
	 */
	public void showPrevious() {
		if (backScreen != null) {
			backScreen.showNotify(this);
			show(backScreen);
			backScreen = null;
		}
	}
	
	/**
	 * <p>
	 * Gets the previous screen.
	 * </p>
	 * @return Screen.
	 */
	public Screen getPrevious() {
		return backScreen != null ? (Screen)backScreen : null;
	}
	
	/**
	 * <p>
	 * Set enable the trigger of the main command action (left one) when the 
	 * FIRE key is pressed.
	 * </p>
	 * @param enabled Enable or disable.
	 */
	public void setFireKey(boolean enabled) {
		fireKeyMainCmd = enabled;
	}
	
    /**
     * <p>
     * Sets the background image alignment.
     * </p>
	 * @param align Alignment.
     * @see com.emobtech.uime.util.ui.UIUtil#ALIGN_LEFT
     * @see com.emobtech.uime.util.ui.UIUtil#ALIGN_CENTER
     * @see com.emobtech.uime.util.ui.UIUtil#ALIGN_RIGHT
     * @see com.emobtech.uime.util.ui.UIUtil#ALIGN_TOP
     * @see com.emobtech.uime.util.ui.UIUtil#ALIGN_MIDDLE
     * @see com.emobtech.uime.util.ui.UIUtil#ALIGN_BOTTOM
	 */
	public void setBackgroundImageAlignment(int align) {
		backImgAlignment = align;
	}
	
    /**
     * <p>
     * Sets the background style.
     * </p>
	 * @param style Style.
	 * @see com.emobtech.uime.util.ui.UIUtil#STYLE_PIPE
     * @see com.emobtech.uime.util.ui.UIUtil#STYLE_PLAIN
     * @see com.emobtech.uime.util.ui.UIUtil#STYLE_GRADIENT
     * @see com.emobtech.uime.util.ui.UIUtil#STYLE_BOTTOM
     * @see com.emobtech.uime.util.ui.UIUtil#STYLE_BRIGTH
     * @see com.emobtech.uime.util.ui.UIUtil#STYLE_DARK
     * @see com.emobtech.uime.util.ui.UIUtil#STYLE_HORIZONTAL
     * @see com.emobtech.uime.util.ui.UIUtil#STYLE_LEFT
     * @see com.emobtech.uime.util.ui.UIUtil#STYLE_RIGHT
     * @see com.emobtech.uime.util.ui.UIUtil#STYLE_TOP
     * @see com.emobtech.uime.util.ui.UIUtil#STYLE_VERTICAL
     * @see com.emobtech.uime.util.ui.UIUtil#STYLE_TRANS_SPEED_1
     * @see com.emobtech.uime.util.ui.UIUtil#STYLE_TRANS_SPEED_2
     * @see com.emobtech.uime.util.ui.UIUtil#STYLE_TRANS_SPEED_3
     * @see com.emobtech.uime.util.ui.UIUtil#STYLE_TRANS_SPEED_4
     * @see com.emobtech.uime.util.ui.UIUtil#STYLE_TRANS_SPEED_5
     * @see com.emobtech.uime.util.ui.UIUtil#STYLE_TRANS_SPEED_6
     * @see com.emobtech.uime.util.ui.UIUtil#STYLE_TRANS_SPEED_7
     * @see com.emobtech.uime.util.ui.UIUtil#STYLE_TRANS_SPEED_8
     * @see com.emobtech.uime.util.ui.UIUtil#STYLE_TRANS_SPEED_9
     * @see com.emobtech.uime.util.ui.UIUtil#STYLE_TRANS_SPEED_10
	 */
	public void setBackgroundStyle(int style) {
		backStyle = style;
	}
	
	/**
	 * @see javax.microedition.lcdui.Canvas#setFullScreenMode(boolean)
	 */
	public void setFullScreenMode(boolean mode) {
		//#ifdef MIDP20
		super.setFullScreenMode(mode);
		fullScreenMode = mode;
		//#endif
	}
	
	/**
	 * <p>
	 * Event triggered when the screen is request to be in the display by a 
	 * caller screen.
	 * </p>
	 * <p>
	 * This method is only triggered when the methods showNext() and 
	 * showPrevious() are used to display the screens.
	 * </p>
	 * @param caller Caller screen.
	 */
	protected void showNotify(Screen caller) {
	}

	/**
	 * @inheritDoc
	 * @see javax.microedition.lcdui.Canvas#showNotify()
	 */
	protected void showNotify() {
		if (!useNativeCommand()) {
			commandBar.showNotify();
		}
	}
	
	/**
	 * @inheritDoc
	 * @see javax.microedition.lcdui.Canvas#hideNotify()
	 */
	protected void hideNotify() {
		if (!useNativeCommand()) {
			commandBar.hideNotify();
		}
	}
	
	/**
	 * <p>
	 * Called when the drawable area of the Screen has been changed. 
	 * </p>
	 * @param w New width.
	 * @param h New height.
	 */
	protected void sizeChanged(int w, int h) {
		if (!useNativeTitle()) {
			if (!hideTitle) {
				titleRect.h = calcBestTitleHeight();
			}
			titleRect.w = w;
		}
		//
		bodyRect.w = w;
		//
		if (!useNativeCommand() && !hideCommands &&
			commandBar.getCommandCount() > 0) {
			bodyRect.h = h - titleRect.h - commandBar.getRect().h;
		} else if (!useNativeTitle()) {
			bodyRect.h = h - titleRect.h;
		} else {
			bodyRect.h = h;
		}
	}

	/**
	 * <p>
	 * Draw the screen title.
	 * </p>
	 * @param g Graphics object.
	 * @param x X coordinate.
	 * @param y Y coordinate.
	 * @param w Width.
	 * @param h Height.
	 */
	protected void drawTitle(Graphics g, int x, int y, int w, int h) {
		//draw the background.
		UIUtil.fillRect(g, x, y, w, h, titleColor, skin.getTitleStyle());
		g.setColor(190, 190, 190); //dark gray color.
		g.drawLine(x, y + h -1, w, y + h -1); //draw the bottom line.
		//
		if (titleImage != null) {
			final int imgW = (w * 10) / 100;
			final int imgH = h - OFFSET;
			//
			if (titleImage.getWidth() > imgW
					|| titleImage.getHeight() > imgH) {
				titleImage = UIUtil.resizeImage(titleImage, imgW, imgH);
			}
			//
			g.drawImage(
				titleImage,
				x + OFFSET,
				y + UIUtil.align(
						-1, titleImage.getHeight(), h, UIUtil.ALIGN_MIDDLE),
				UIUtil.ANCHOR);
			//
			x += OFFSET + titleImage.getWidth();
			w -= OFFSET + titleImage.getWidth();
		}
		//
		//draw the text.
		final int off_y = (h - titleFont.getHeight()) / 2;
		//
		g.setFont(titleFont);
        g.setColor(titleFontColor);
		g.drawString(title, x + OFFSET, y + off_y, UIUtil.ANCHOR);
	}

	/**
	 * <p>
	 * Draw the screen body. If you intend to extend this class, all your
	 * painting process must start from this method.
	 * </p>
	 * @param g Graphics object.
	 * @param x X coordinate.
	 * @param y Y coordinate.
	 * @param w Width.
	 * @param h Height.
	 */
	protected abstract void drawBody(Graphics g, int x, int y, int w, int h);

	/**
	 * <p>
	 * Draw the screen background.
	 * </p>
	 * @param g Graphics object.
	 * @param x X coordinate.
	 * @param y Y coordinate.
	 * @param w Width.
	 * @param h Height.
	 */
	protected void drawBackground(Graphics g, int x, int y, int w, int h) {
		UIUtil.fillRect(g, x, y, w, h, backgroundColor, backStyle);
		//
		if (backImage != null) {
			int ww;
			int hh;
			boolean needResizing = false;
			//
			if ((ww = backImage.getWidth()) > w) {
				ww = w;
				needResizing = true;
			}
			if ((hh = backImage.getHeight()) > h) {
				hh = h;
				needResizing = true;
			}
			//
			if (needResizing) {
				try {
					backImage =	UIUtil.resizeImage(backImage, ww, hh);
				} catch (OutOfMemoryError e) {
				}
			}
			//
			g.drawImage(
				backImage, 
                x + UIUtil.align(ww, -1, w, backImgAlignment),
                y + UIUtil.align(-1, hh, h, backImgAlignment),
				Graphics.TOP | Graphics.LEFT);
		}
	}
	
	/**
	 * @inheritDoc
	 * @see javax.microedition.lcdui.Canvas#keyPressed(int)
	 */
	protected final void keyPressed(int keyCode) {
		if (useNativeCommand()) {
			keyDown(keyCode);
		} else {
			if (!commandBar.keyPressed(keyCode)) {
				if (fireKeyMainCmd && getGameAction(keyCode) == FIRE) {
					if (commandBar.getCommandCount() > 0) {
						commandBar.triggerLeftCommand();
					}
				} else {
					keyDown(keyCode);
				}
			}
		}
	}
	
	/**
	 * @inheritDoc
	 * @see javax.microedition.lcdui.Canvas#keyReleased(int)
	 */
	protected final void keyReleased(int keyCode) {
		if (useNativeCommand() || !commandBar.keyReleased(keyCode)) {
			keyUp(keyCode);
		}
	}
	
	/**
	 * @inheritDoc
	 * @see javax.microedition.lcdui.Canvas#keyRepeated(int)
	 */
	protected final void keyRepeated(int keyCode) {
		if (useNativeCommand() || !commandBar.keyRepeated(keyCode)) {
			keyHold(keyCode);
		}
	}
	
	/**
	 * @inheritDoc
	 * @see javax.microedition.lcdui.Canvas#pointerDragged(int, int)
	 */
	protected final void pointerDragged(int x, int y) {
		if (useNativeCommand() || !commandBar.pointerDragged(x, y)) {
			penDragged(x, y);
		}
	}

	/**
	 * @inheritDoc
	 * @see javax.microedition.lcdui.Canvas#pointerPressed(int, int)
	 */
	protected final void pointerPressed(int x, int y) {
		if (useNativeCommand() || !commandBar.pointerPressed(x, y)) {
			penDown(x, y);
		}
	}

	/**
	 * @inheritDoc
	 * @see javax.microedition.lcdui.Canvas#pointerReleased(int, int)
	 */
	protected final void pointerReleased(int x, int y) {
		if (useNativeCommand() || !commandBar.pointerReleased(x, y)) {
			penUp(x, y);
		}
	}

	/**
	 * <p>
	 * Method that handles the event when a key is pressed. If you intend to
	 * extend this class, all your key pressed events handling must be done
	 * from this method.
	 * </p>
	 * @param keyCode Key code.
	 */
	protected void keyDown(int keyCode) {
	}

	/**
	 * <p>
	 * Method that handles the event when a key is released. If you intend to
	 * extend this class, all your key released events handling must be done
	 * from this method.
	 * </p>
	 * @param keyCode Key code.
	 */
	protected void keyUp(int keyCode) {
	}
	
	/**
	 * <p>
	 * Method that handles the event when a key is hold. If you intend to
	 * extend this class, all your key hold events handling must be done
	 * from this method.
	 * </p>
	 * @param keyCode Key code.
	 */
	protected void keyHold(int keyCode) {
	}
	
	/**
	 * <p>
	 * Method that handles the event when the pen is pressed. If you intend to
	 * extend this class, all your pen pressed events handling must be done
	 * from this method.
	 * </p>
	 * @param x X coordinate.
	 * @param y Y coordinate.
	 */
	protected void penDown(int x, int y) {
	}

	/**
	 * <p>
	 * Method that handles the event when the pen is released. If you intend to
	 * extend this class, all your pen released events handling must be done
	 * from this method.
	 * </p>
	 * @param x X coordinate.
	 * @param y Y coordinate.
	 */
	protected void penUp(int x, int y) {
	}

	/**
	 * <p>
	 * Method that handles the event when the pen is dragged. If you intend to
	 * extend this class, all your pen dragged events handling must be done
	 * from this method.
	 * </p>
	 * @param x X coordinate.
	 * @param y Y coordinate.
	 */
	protected void penDragged(int x, int y) {
	}
	
	/**
	 * <p>
	 * Requests a repaint for the entire Screen. Use this method instead of
	 * repaint().
	 * </p>
	 */
	protected void requestRepaint() {
		if (parent != null) {
			parent.requestRepaint();
		} else {
			repaint();
		}
	}
	
	/**
	 * @inheritDoc
	 * @see javax.microedition.lcdui.Canvas#hasPointerEvents()
	 */
	public boolean hasPointerEvents() {
		if (parent != null) {
			return parent.hasPointerEvents();
		} else {
			return super.hasPointerEvents();
		}
	}
	
	/**
	 * @inheritDoc
	 * @see javax.microedition.lcdui.Canvas#hasPointerMotionEvents()
	 */
	public boolean hasPointerMotionEvents() {
		if (parent != null) {
			return parent.hasPointerMotionEvents();
		} else {
			return super.hasPointerMotionEvents();
		}
	}
	
	/**
	 * @inheritDoc
	 * @see javax.microedition.lcdui.Canvas#hasRepeatEvents()
	 */
	public boolean hasRepeatEvents() {
		if (parent != null) {
			return parent.hasRepeatEvents();
		} else {
			return super.hasRepeatEvents();
		}
	}
	
	/**
	 * <p>
	 * Set the parent screen.
	 * </p>
	 * @param parent Parent screen.
	 */
	void setParent(Screen parent) {
		this.parent = parent;
	}
	
	/**
	 * <p>
	 * Hide some parts of the screen.
	 * </p>
	 * @param hideTitle Set hide title enabled.
	 * @param hideCommands Set hide commands enabled.
	 */
	void hideScreenParts(boolean hideTitle, boolean hideCommands) {
		this.hideTitle = hideTitle;
		this.hideCommands = hideCommands;
		//
		if (!hideTitle) {
			titleRect.h = calcBestTitleHeight();
		} else {
			titleRect.h = 0;
		}
	}
	
	/**
	 * <p>
	 * Returns an image object that contains the last screen processed.
	 * </p>
	 * @return Image object.
	 */
	Image getLastImageProcessed() {
		return auxImg;
	}

	/**
	 * <p>
	 * Set flag that means the screen is top most. If <code>null</code> is 
	 * passed, the returned value will depende on the platform.
	 * </p>
	 * @param shown Shown or not.
	 */
	void setShown(Boolean shown) {
		isShown = shown;
	}
	
	/**
	 * <p>
	 * Verify if the screen must use native or custom commands.
	 * </p>
	 * @return Use native commands or not.
	 */
	boolean useNativeCommand() {
		if (useNativeCommand != -1) {
			return useNativeCommand == 1;
		}
		//
		String key =
			MIDlet.getMIDletInstance().getAppProperty(
				"br-framework-jme-ui-use-native-command");
		//
		useNativeCommand =
			key != null && StringUtil.equalsIgnoreCase(key.trim(), "true")
				? 1 : 0;
		//
		return useNativeCommand  == 1;
	}

	/**
	 * <p>
	 * Verify if the screen must use native or custom title bar.
	 * </p>
	 * @return Use native commands or not.
	 */
	boolean useNativeTitle() {
		//#ifdef MIDP10
//@		if (true) {
//@			return false;
//@		}
		//#endif
		//
		if (useNativeTitle != -1) {
			return useNativeTitle == 1;
		}
		//
		String key =
			MIDlet.getMIDletInstance().getAppProperty(
				"br-framework-jme-ui-use-native-title");
		//
		useNativeTitle =
			key != null && StringUtil.equalsIgnoreCase(key.trim(), "true")
				? 1 : 0;
		//
		return useNativeTitle  == 1;
	}
	
	/**
	 * <p>
	 * Calculate title height.
	 * </p>
	 * @return Height.
	 */
	private int calcBestTitleHeight() {
		return titleFont.getHeight() + OFFSET * 2;
	}
}
