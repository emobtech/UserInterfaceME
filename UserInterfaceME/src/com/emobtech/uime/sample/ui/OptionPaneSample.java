/*
 * OptionPaneSample.java
 */
package com.emobtech.uime.sample.ui;

import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Displayable;

import com.emobtech.uime.midlet.MIDlet;
import com.emobtech.uime.ui.Form;
import com.emobtech.uime.ui.OptionPane;

/**
 * 
 * @author Main
 */
public class OptionPaneSample extends Form implements CommandListener {
	private Command cmdInfo = new Command("Info", Command.OK, 0);
	private Command cmdWarning = new Command("Warning", Command.OK, 0);
	private Command cmdError = new Command("Error", Command.OK, 0);
	private Command cmdConf = new Command("Confirmation", Command.OK, 0);
	private Command cmdExit = new Command("Exit", Command.EXIT, 0);

	/**
	 * 
	 * @param title
	 */
	public OptionPaneSample(String title) {
		super(title);
		//
		addCommand(cmdInfo);
		addCommand(cmdWarning);
		addCommand(cmdError);
		addCommand(cmdConf);
		addCommand(cmdExit);
        setCommandListener(this);
        //
        append("Teste as mensagens clicando nos comandos.");
	}

    public void commandAction(Command c, Displayable d) {
    	if (c == cmdInfo) {
    		OptionPane.showMessageDialog(null, "Info", "Info Message", OptionPane.INFO_MESSAGE, 5000, null, null);
    	} else if (c == cmdWarning) {
    		OptionPane.showMessageDialog(null, "Warning", "Warning Message", OptionPane.WARNING_MESSAGE, 5000, null, null);
    	} else if (c == cmdError) {
    		OptionPane.showMessageDialog(null, "Error", "Error Message", OptionPane.ERROR_MESSAGE, 5000, null, null);
    	} else if (c == cmdConf) {
   	    	OptionPane.showConfirmationDialog(null, "Confirmation", "Confirmation Message?", null, null);
    	} else if (c == cmdExit) {
	    	MIDlet.getMIDletInstance().exitApp(false);
	    }

    }
}