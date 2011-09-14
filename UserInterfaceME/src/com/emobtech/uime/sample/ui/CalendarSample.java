/*
 * CalendarSample.java
 */
package com.emobtech.uime.sample.ui;

import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Displayable;

import com.emobtech.uime.midlet.MIDlet;
import com.emobtech.uime.ui.Calendar;

/**
 * 
 * @author Main
 */
public class CalendarSample extends Calendar implements CommandListener {
	private Command cmdOK = new Command("Change View", Command.OK, 0);
	private Command cmdExit = new Command("Exit", Command.EXIT, 0);
	private boolean chageView = false;

	/**
	 * 
	 * @param title
	 */
	public CalendarSample(String title) {
		super(title);
		addCommand(cmdOK);
		addCommand(cmdExit);
        setCommandListener(this);
	}

    public void commandAction(Command c, Displayable d) {
    	if (c == cmdOK) {
    		chageView = !chageView;
        	setSimpleDateMode(chageView);
    	} else if (c == cmdExit) {
    		MIDlet.getMIDletInstance().exitApp(false);
    	}
    }
}