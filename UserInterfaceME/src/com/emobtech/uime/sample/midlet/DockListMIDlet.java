package com.emobtech.uime.sample.midlet;

import javax.microedition.lcdui.Display;

import com.emobtech.uime.midlet.MIDlet;
import com.emobtech.uime.sample.ui.DockListSample;

public class DockListMIDlet extends MIDlet {
	/**
	 * @see javax.microedition.midlet.MIDlet#startApp()
	 */
	public void startApp() {
		Display display = Display.getDisplay(this);
		display.setCurrent(new DockListSample("DocList"));
	}
}