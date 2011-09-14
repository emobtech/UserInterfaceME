package com.emobtech.uime.sample.ui;

import java.io.IOException;

import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.Image;

import com.emobtech.uime.ui.DockList;

public class DockListSample extends DockList {

	public DockListSample(String title) {
		super(title);
		//
		try {
			append("Confirmation Icon", Image.createImage("/images/icon_confirmation_message.png"));
			append("Error Icon", Image.createImage("/images/icon_error_message.png"));
			append("Info Icon", Image.createImage("/images/icon_info_message.png"));
			append("Warning Icon", Image.createImage("/images/icon_warning_message.png"));
			append("Symbian", Image.createImage("/etc/images/symbian_pro.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		//
		addCommand(new Command("OK", Command.OK, 0));
		sort();
	}
}