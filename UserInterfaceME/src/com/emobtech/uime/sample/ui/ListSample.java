/*
 * ListSample.java
 */
package com.emobtech.uime.sample.ui;

import java.io.IOException;

import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.Image;

import com.emobtech.uime.midlet.MIDlet;
import com.emobtech.uime.ui.List;

/**
 * 
 * @author Main
 */
public class ListSample extends List implements CommandListener {
	private Command cmdSelect = new Command("Select", Command.OK, 0);
	private Command cmdBack = new Command("Back", Command.BACK, 0);
	private Command cmdExit = new Command("Exit", Command.EXIT, 0);
	
	/**
	 * 
	 * @param title
	 */
	public ListSample(String title) {
		super(title);
		addCommand(cmdSelect);
		addCommand(cmdExit);
        setCommandListener(this);
        //
        append("Implicit List", null);
        append("Multiple List", null);
        append("Exclusive List", null);
	}

    public void commandAction(Command c, Displayable d) {
        if (cmdBack == c) {
        	MIDlet.getDisplayInstance().setCurrent(this);
        } else if (cmdSelect == c) {
        	final String s = get(getSelectedIndex()).toString();
        	//
        	if (s.equals("Implicit List")) {
        		showList("Implicit List", IMPLICIT);
        	} else if (s.equals("Multiple List")) {
        		showList("Multiple List", MULTIPLE);
        	} else if (s.equals("Exclusive List")) {
        		showList("Exclusive List", EXCLUSIVE);
        	}
        } else if (c == cmdExit) {
	    	MIDlet.getMIDletInstance().exitApp(false);
	    }
    }
    
    /**
     * @param list
     */
    void populateList(List list) {
    	Image img = null;
    	try {
			img = Image.createImage("/etc/images/chart_result_icon.png");
		} catch (IOException e) {
			e.printStackTrace();
		}
		//
		for (int i = 1; i <= 20; i++) {
			list.append("Item " + i, img);
		}
    }
    
    /**
     * @param name
     * @param type
     */
    void showList(String name, int type) {
    	List l = new List(name, type);
    	l.addCommand(cmdBack);
    	l.setCommandListener(this);
    	//
    	populateList(l);
    	//
    	MIDlet.getDisplayInstance().setCurrent(l);
    }
}