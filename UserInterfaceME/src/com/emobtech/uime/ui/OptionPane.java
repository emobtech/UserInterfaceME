/*
 * OptionPane.java
 * 08/10/2007
 * JME Framework
 * Copyright(c) Ernandes Mourao Junior (ernandes@gmail.com)
 * All rights reserved
 */
package com.emobtech.uime.ui;

import java.io.IOException;

import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;

import com.emobtech.uime.midlet.MIDlet;
import com.emobtech.uime.util.I18N;
import com.emobtech.uime.util.ui.UIUtil;


/**
 * <p>
 * An option pane is a screen that shows data to the user and waits for a 
 * certain period of time before proceeding to the next Displayable. An option 
 * pane can displays a message that represents INFO, WARNING and ERROR. The 
 * intended use of option pane is to inform the user about errors and other 
 * exceptional conditions.
 * </p>
 * 
 * @author Ernandes Mourao Junior (ernandes@gmail.com)
 * @version 1.0
 * @since 1.0
 */
public class OptionPane extends Screen implements CommandListener {
	/**
	 * <p>
	 * Default OptionPane class full name.
	 * </p> 
	 */
	static String optionPaneClassFullName = "br.framework.jme.ui.OptionPane";
	
	/**
	 * <p>
	 * Contant that indicates that an option pane shows a single message.
	 * </p>
	 */
	protected static final int MESSAGE_DIALOG = 1;
	
	/**
	 * <p>
	 * Contant that indicates that an option pane shows a confirmation message.
	 * </p>
	 */
	protected static final int CONFIRMATION_DIALOG = 2;

	/**
	 * <p>
	 * Contant that indicates that an option pane's message represent an info 
	 * one. 
	 * </p>
	 */
	public static final int INFO_MESSAGE = 4;
	
	/**
	 * <p>
	 * Contant that indicates that an option pane's message represent an warning 
	 * one. 
	 * </p>
	 */
	public static final int WARNING_MESSAGE = 8;
	
	/**
	 * <p>
	 * Contant that indicates that an option pane's message represent an error 
	 * one. 
	 * </p>
	 */
	public static final int ERROR_MESSAGE = 16;
	
	/**
	 * <p>
	 * Contant that indicates that an option pane is kept visible until the user 
	 * dismisses it.
	 * </p>
	 */
	public static final int FOREVER = -1;

	/**
	 * <p>
	 * Info image path.
	 * </p>
	 */
	String infoImagePath = "/images/icon_info_message.png";
	
	/**
	 * <p>
	 * Warning image path.
	 * </p>
	 */
	String warningImagePath = "/images/icon_warning_message.png";
	
	/**
	 * <p>
	 * Error image path.
	 * </p>
	 */
	String errorImagePath = "/images/icon_error_message.png";

	/**
	 * <p>
	 * Confirmation image path.
	 * </p>
	 */
	String confirmationImagePath = "/images/icon_confirmation_message.png";

	/**
	 * <p>
	 * Command OK.
	 * </p>
	 */
	Command cmdOK;
	
	/**
	 * <p>
	 * Command YES.
	 * </p>
	 */
	Command cmdYES;

	/**
	 * <p>
	 * Command NO.
	 * </p>
	 */
	Command cmdNO;

	/**
	 * <p>
	 * Type.
	 * </p>
	 */
	int type;
	
	/**
	 * <p>
	 * OptionPane's ID.
	 * </p>
	 */
	String id;
	
	/**
	 * <p>
	 * Message.
	 * </p>
	 */
	protected String message;
	
	/**
	 * <p>
	 * Message to be displayed splitted in lines.
	 * </p>
	 */
	protected String[] msgLines;
	
	/**
	 * <p>
	 * Time until the screen gets closed.
	 * </p>
	 */
	int timeout;
	
	/**
	 * <p>
	 * Object listener.
	 * </p>
	 */
	OptionPaneListener listener;
	
	/**
	 * <p>
	 * Caller screen.
	 * </p>
	 */
	Displayable prevScreen;

	/**
	 * <p>
	 * Next screen.
	 * </p>
	 */
	Displayable nextScreen;
	
	/**
	 * 
	 */
	protected int textAlignment = UIUtil.ALIGN_CENTER | UIUtil.ALIGN_MIDDLE;

	/**
	 * <p>
	 * Creates an instance of a OptionPane.
	 * </p>
	 * @return Instance.
	 */
	static final OptionPane newInstance() {
		try {
			return (OptionPane)Class.forName(
				optionPaneClassFullName).newInstance();
		} catch (Exception e) {
			throw new IllegalArgumentException(
				"Invalid OptionPane class full name: " +
					optionPaneClassFullName);
		}
	}

	/**
	 * <p>
	 * Displays a option pane with a given message.
	 * </p>
	 * @param id Message ID.
	 * @param title Title.
	 * @param message Message to be displayed.
	 * @param type Message type.
	 * @param timeout Time until the screen closes.
	 * @param listener Object listener.
	 * @param nextScreen Next screen to be displayed when. If <code>null</code>
	 *                   is displays the caller screen.
	 * @see br.framework.jme.ui.OptionPane#INFO_MESSAGE
	 * @see br.framework.jme.ui.OptionPane#WARNING_MESSAGE
	 * @see br.framework.jme.ui.OptionPane#ERROR_MESSAGE
	 */
	public static final void showMessageDialog(String id, String title, 
		String message, int type, int timeout, OptionPaneListener listener,
		Displayable nextScreen) {
		//
		OptionPane opt = newInstance();
		opt.setupInstance(
			id,
			title,
			message,
			MESSAGE_DIALOG | type,
			timeout,
			listener,
			nextScreen);
		//
		opt.show(opt);
	}
	
	/**
	 * <p>
	 * Displays a confirmation option pane with a given message.
	 * </p>
	 * @param id Message ID.
	 * @param title Title.
	 * @param message Message to be displayed.
	 * @param listener Object listener.
	 * @param nextScreen Next screen to be displayed when. If <code>null</code>
	 *                   is displays the caller screen.
	 */
	public static final void showConfirmationDialog(String id, String title,
		String message,	OptionPaneListener listener, Displayable nextScreen) {
		//
		OptionPane opt = newInstance();
		opt.setupInstance(
			id,
			title,
			message,
			CONFIRMATION_DIALOG,
			FOREVER,
			listener,
			nextScreen);
		//
		opt.show(opt);
	}
	
	/**
	 * <p>
	 * Sets the class full name of a OptionPane implementation.
	 * </p>
	 * @param classFullName Class full anem.
	 */
	public static final void setOptionPaneClassFullName(String classFullName) {
		optionPaneClassFullName = classFullName;
	}
	
	/**
	 * 
	 */
	protected OptionPane() {
		super(null);
		//
		skin.applyOptionPaneSkin(this);
	}
	
	/**
	 * <p>
	 * Returns the screen type.
	 * </p>
	 * @return Type.
	 * @see br.framework.jme.ui.OptionPane#INFO_MESSAGE
	 * @see br.framework.jme.ui.OptionPane#WARNING_MESSAGE
	 * @see br.framework.jme.ui.OptionPane#ERROR_MESSAGE
	 */
	public int getType() {
		return type;
	}
	
	/**
	 * <p>
	 * Set the text alignment.
	 * </p>
	 * @param align Alignment type.
	 */
	public void setTextAlignment(int align) {
		textAlignment = align;
	}
	
	/**
	 * <p>
	 * Set info image path.
	 * </p>
	 * @param path Path.
	 */
	public void setInfoImagePath(String path) {
		infoImagePath = path;
	}

	/**
	 * <p>
	 * Set warning image path.
	 * </p>
	 * @param path Path.
	 */
	public void setWarningImagePath(String path) {
		warningImagePath = path;
	}
	
	/**
	 * <p>
	 * Set error image path.
	 * </p>
	 * @param path Path.
	 */
	public void setErrorImagePath(String path) {
		errorImagePath = path;
	}

	/**
	 * <p>
	 * Set confirmation image path.
	 * </p>
	 * @param path Path.
	 */
	public void setConfirmationImagePath(String path) {
		confirmationImagePath = path;
	}

	/**
	 * @inheritDoc
	 * @see javax.microedition.lcdui.CommandListener#commandAction(javax.microedition.lcdui.Command, javax.microedition.lcdui.Displayable)
	 */
	public synchronized void commandAction(Command c, Displayable d) {
		//this checking is necessary, in case of the user hit the OK button at 
		//the same time as the thread closes the screen when the time expires.
		if (MIDlet.getDisplayInstance().getCurrent() == this) {
			if (nextScreen != null) {
				if (prevScreen instanceof Screen
						&& nextScreen == ((Screen)prevScreen).getPrevious()) {
					((Screen)prevScreen).showPrevious();
				} else {
					show(nextScreen);
				}
			} else {
				show(prevScreen);
			}
			//
			if (c == cmdOK) {
				if (listener != null) {
					listener.messageDialogAction(id);
				}
			} else if (c == cmdYES || c == cmdNO) {
				if (listener != null) {
					listener.confirmationDialogAction(id, c == cmdYES);
				}
			}
		}
	}
	
	/**
	 * @inheritDoc
	 * @see br.framework.jme.ui.Screen#drawBody(javax.microedition.lcdui.Graphics, int, int, int, int)
	 */
	protected void drawBody(Graphics g, int x, int y, int w, int h) {
		if (msgLines == null) {
			msgLines = UIUtil.splitString(message, w, bodyFont);
		}
		//
		g.setFont(bodyFont);
		g.setColor(bodyFontColor);
		//
		final int fh = bodyFont.getHeight() + OFFSET;
		y += UIUtil.align(-1, fh * msgLines.length, h, textAlignment); 
		//
		for (int i = 0; i < msgLines.length; i++) {
			g.drawString(
				msgLines[i], 
				x + UIUtil.align(
						bodyFont.stringWidth(msgLines[i]), 
						-1, 
						w, 
						textAlignment), 
				y, 
				Graphics.TOP | Graphics.LEFT);
			//
			y += fh;
		}
	}

	/**
	 * <p>
	 * Load the screen's elements.
	 * </p>
	 */
	void init() {
		String imgName = null;
		//
		setCommandListener(this);
		//
		if ((type & MESSAGE_DIALOG) != 0) {
			cmdOK = 
				new Command(
					I18N.getFramework("framework.jme.ui.optionpane.ok"), 
					Command.OK, 
					0);
			//
			addCommand(cmdOK);
			//
			if ((type & OptionPane.INFO_MESSAGE) != 0) {
				imgName = infoImagePath;
			} else if ((type & OptionPane.WARNING_MESSAGE) != 0) {
				imgName = warningImagePath;
			} else if ((type & OptionPane.ERROR_MESSAGE) != 0) {
				imgName = errorImagePath;
			}
		} else if ((type & CONFIRMATION_DIALOG) != 0) {
			cmdYES = 
				new Command(
					I18N.getFramework("framework.jme.ui.optionpane.yes"), 
					Command.OK, 
					0);
			cmdNO = 
				new Command(
					I18N.getFramework("framework.jme.ui.optionpane.no"), 
					Command.CANCEL, 
					0);
			//
			addCommand(cmdYES);
			addCommand(cmdNO);
			//
			imgName = confirmationImagePath;
		}
		//
		if (imgName != null) {
			try {
				setBackgroundImage(Image.createImage(imgName));
			} catch (IOException e) {
			} catch (OutOfMemoryError e) {
			}
		}
		//
		if (timeout != FOREVER) {
			//thread resposible for closing the screen when the time expires.
			Thread t = new Thread(new Runnable() {
				public void run() {
					try {
						Thread.sleep(timeout);
					} catch (InterruptedException e) {
					}
					//
					if (timeout != FOREVER) {
						commandAction(cmdOK, OptionPane.this);
					}
				}
			});
			t.start();
		}
	}

	/**
	 * <p>
	 * Set up a new option pane.
	 * </p>
	 * @param id OptionPane's ID.
	 * @param title Title.
	 * @param message Message.
	 * @param type Type.
	 * @param timeout Timeout.
	 * @param listener Object listener.
	 * @param nextScreen Next screen to be displayed.
	 */
	protected void setupInstance(String id, String title, String message,
		int type, int timeout, OptionPaneListener listener,
		Displayable nextScreen) {
		setTitle(title);
		//
		this.id = id;
		this.message = message;
		this.type = type;
		this.timeout = timeout;
		this.listener = listener;
		this.nextScreen = nextScreen;
		this.prevScreen = MIDlet.getDisplayInstance().getCurrent();
		//
		init();
	}
}