/*
 * DetailListSample.java
 */
package com.emobtech.uime.sample.ui;

import java.io.IOException;

import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.Image;

import com.emobtech.uime.midlet.MIDlet;
import com.emobtech.uime.ui.DetailList;

/**
 * 
 * @author Main
 */
public class DetailListSample extends DetailList implements CommandListener {
	private Command cmdOK = new Command("OK", Command.OK, 0);
	private Command cmdExit = new Command("Exit", Command.EXIT, 0);

	/**
	 * 
	 * @param title
	 */
	public DetailListSample(String title) {
		super(title, IMPLICIT);
		addCommand(cmdOK);
		addCommand(cmdExit);
		setCommandListener(this);
		
		Image img1 = null;
		Image img2 = null;
		Image img3 = null;

		try {
			img1 = Image.createImage("/etc/images/chart_result_icon.png");
			img2 = Image.createImage("/etc/images/hot_icon.png");
			img3 = Image.createImage("/etc/images/info_icon.png");
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		for (int i = 1; i <= 20; i++) {
			append(
				new String[] {"Full name " + i, "Number address zipcode country " + i, "E-mail@internet.com " + i}, 
				new Image[] {img1, img2, img3});
		}
	}

	/**
	 * @see javax.microedition.lcdui.CommandListener#commandAction(javax.microedition.lcdui.Command, javax.microedition.lcdui.Displayable)
	 */
	public void commandAction(Command c, Displayable d) {
		if (c == cmdExit) {
	    	MIDlet.getMIDletInstance().exitApp(false);
	    }
	}
}