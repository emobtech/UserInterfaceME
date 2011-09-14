/*
 * ImageViewerSample.java
 */
package com.emobtech.uime.sample.ui;

import java.io.IOException;

import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.Image;

import com.emobtech.uime.midlet.MIDlet;
import com.emobtech.uime.ui.ImageViewer;
import com.emobtech.uime.util.ui.UIUtil;

/**
 * 
 * @author Main
 */
public class ImageViewerSample extends ImageViewer implements CommandListener {
	private Command cmdStretch = new Command("Stretch", Command.OK, 0);
	private Command cmdFull = new Command("Full", Command.OK, 0);
	private Command cmdFit = new Command("Fit", Command.OK, 0);
	private Command cmdExit = new Command("Exit", Command.EXIT, 0);

	/**
	 * 
	 * @param title
	 */
	public ImageViewerSample(String title) {
		super(title);
		addCommand(cmdFit);
		addCommand(cmdStretch);
		addCommand(cmdFull);
		addCommand(cmdExit);
        setCommandListener(this);
        //
        setImageAlignment(UIUtil.ALIGN_CENTER | UIUtil.ALIGN_MIDDLE);
        //
        Image img = null;
        try {
            img = Image.createImage("/etc/images/map-google.png");
        } catch (IOException e) {
            e.printStackTrace();
        }
        setImage(img);
	}

    public void commandAction(Command c, Displayable d) {
    	if (c == cmdStretch) {
    		stretch();
    	} else if (c == cmdFull) {
    		full();
    	} else if (c == cmdExit) {
   	    	MIDlet.getMIDletInstance().exitApp(false);
    	} else {
    		fit();
    	}
    }
}