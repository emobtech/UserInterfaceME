/*
 * TextBoxSample.java
 */
package com.emobtech.uime.sample.ui;

import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Displayable;

import com.emobtech.uime.midlet.MIDlet;
import com.emobtech.uime.ui.TextBox;
import com.emobtech.uime.util.StringUtil;
import com.emobtech.uime.util.ui.UIUtil;

/**
 * 
 * @author Main
 */
public class TextBoxSample extends TextBox implements CommandListener {
	private Command cmdPL = new Command("Prt Layout", Command.OK, 0);
	private Command cmdNL = new Command("Nrm Layoyt", Command.OK, 0);
	private Command cmdAL = new Command("Align Lft", Command.OK, 0);
	private Command cmdAR = new Command("Align Rgt", Command.OK, 0);
	private Command cmdAC = new Command("Align Cnt", Command.OK, 0);
	private Command cmdAJ = new Command("Align Jst", Command.OK, 0);
	private Command cmdExit = new Command("Exit", Command.EXIT, 0);

	/**
	 * 
	 * @param title
	 */
	public TextBoxSample(String title) {
		super(title);
		addCommand(cmdPL);
		addCommand(cmdNL);
		addCommand(cmdAL);
		addCommand(cmdAR);
		addCommand(cmdAC);
		addCommand(cmdAJ);
		addCommand(cmdExit);
		setCommandListener(this);
        //
        String text =
        	StringUtil.getStringFromResource("/etc/textbox-sample.txt", null);
        setText(text);
	}

    public void commandAction(Command c, Displayable d) {
        if (cmdPL == c) {
        	setLayout(PRINTING_LAYOUT);
        } else if (cmdNL == c) {
        	setLayout(NORMAL_LAYOUT);
        } else if (cmdAL == c) {
        	setTextAlignment(UIUtil.ALIGN_LEFT);
        } else if (cmdAC == c) {
        	setTextAlignment(UIUtil.ALIGN_CENTER);
        } else if (cmdAR == c) {
        	setTextAlignment(UIUtil.ALIGN_RIGHT);
        } else if (cmdAJ == c) {
        	setTextAlignment(UIUtil.ALIGN_JUSTIFIED);
        } else if (c == cmdExit) {
	    	MIDlet.getMIDletInstance().exitApp(false);
	    }
    }
}