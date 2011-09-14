/*
 * RSSListSampleMIDlet.java
 */
package com.emobtech.uime.sample.midlet;

import javax.microedition.lcdui.Display;

import com.emobtech.uime.midlet.MIDlet;
import com.emobtech.uime.sample.ui.RSSListSample;

/**
 * 
 * @author Main
 */
public class RSSListSampleMIDlet extends MIDlet {
	/**
	 * 
	 */
	public RSSListSampleMIDlet() {
	}

	/**
	 * @see javax.microedition.midlet.MIDlet#startApp()
	 */
	public void startApp() {
		Display display = Display.getDisplay(this);
		display.setCurrent(new RSSListSample("RSSList"));
	}

	/**
	 * @see javax.microedition.midlet.MIDlet#pauseApp()
	 */
	public void pauseApp() {
	}

	/**
	 * @see javax.microedition.midlet.MIDlet#destroyApp(boolean)
	 */
	public void destroyApp(boolean arg0) {
	}
}