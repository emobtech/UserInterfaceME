/*
 * MIDlet.java
 * 01/10/2007
 * JME Framework
 * Copyright(c) Ernandes Mourao Junior (ernandes@gmail.com)
 * All rights reserved
 */
package com.emobtech.uime.midlet;

import javax.microedition.lcdui.Display;


/**
 * <p>
 * This class implements a MIDlet app. It is recommended to extend this class
 * instead of javax.microedition.midlet.MIDlet, because all the necessary
 * settings for the framework will be already set.
 * </p>
 * 
 * @author Ernandes Mourao Junior (ernandes@gmail.com)
 * @version 1.0
 * @since 1.0
 */
public class MIDlet extends javax.microedition.midlet.MIDlet {
	/**
	 * <p>
	 * MIDlet instance.
	 * </p>
	 */
	static MIDlet midlet;

	{
		MIDlet.setMIDletInstance(this);
	}
	
	/**
	 * <p>
	 * Sets the MIDlet instance of the current application.
	 * </p>
	 * @param midlet Instance.
	 */
	public static final void setMIDletInstance(MIDlet midlet) {
		MIDlet.midlet = midlet;
	}
	
	/**
	 * <p>
	 * Gets the MIDlet instance of the current application.
	 * </p>
	 * @return Instance.
	 * @throws NullPointerException MIDlet instance is not set.
	 */
	public static final MIDlet getMIDletInstance() {
		if (MIDlet.midlet == null) {
			throw new NullPointerException(
				"The MIDlet instance was not set through the method " +
					"br.framework.jme.midlet.MIDlet.setMIDletInstance().");
		}
		//
		return MIDlet.midlet;
	}
	
	/**
	 * <p>
	 * Gets the display instance.
	 * </p>
	 * @return Instance.
	 * @throws NullPointerException MIDlet instance is not set.
	 */
	public static final Display getDisplayInstance() {
		if (MIDlet.midlet == null) {
			throw new NullPointerException(
				"The MIDlet instance was not set through the method " +
					"br.framework.jme.midlet.MIDlet.setMIDletInstance().");
		}
		//
		return Display.getDisplay(MIDlet.midlet);
	}

	/**
	 * @inheritDoc
	 * @see javax.microedition.midlet.MIDlet#startApp()
	 */
	public void startApp() {
	}

	/**
	 * @inheritDoc
	 * @see javax.microedition.midlet.MIDlet#pauseApp()
	 */
	public void pauseApp() {
	}

	/**
	 * @inheritDoc
	 * @see javax.microedition.midlet.MIDlet#destroyApp(boolean)
	 */
	public void destroyApp(boolean unconditional) {
	}
	
	/**
	 * <p>
	 * Closes the application. This method automatically calls the methods
	 * destroyApp(boolean) and notifyDestroyed().
	 * </p>
	 * @param unconditional If true when this method is called, the MIDlet must 
	 *                      cleanup and release all resources. If false the 
	 *                      MIDlet may throw MIDletStateChangeException to 
	 *                      indicate it does not want to be destroyed at this 
	 *                      time.
	 */
	public void exitApp(boolean unconditional) {
		destroyApp(unconditional);
		notifyDestroyed();
	}
}