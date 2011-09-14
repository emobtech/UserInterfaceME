/*
 * RSSListSample.java
 */
package com.emobtech.uime.sample.ui;

import java.io.IOException;

import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.Image;

import com.emobtech.uime.ui.RSSList;

/**
 * 
 * @author Main
 */
public class RSSListSample extends RSSList implements CommandListener {
	private Command cmdDone = new Command("Done", Command.OK, 0);

	/**
	 * 
	 * @param title
	 */
	public RSSListSample(String title) {
		super(title);
		//#ifdef MIDP20
		setFullScreenMode(true);
		//#endif
		addCommand(cmdDone);
		setCommandListener(this);
        //
		setItemTitleBackgroundColor(140, 198, 255);
		//
		Image img = null;
		try {
			img = Image.createImage("/etc/images/chart_result_icon.png");
		} catch (IOException e) {
			e.printStackTrace();
		}
		img = null;
		//
		for (int i = 0; i < 20; i++) {
			if (i % 3 == 0) {
				append(i + " Setor de wireless bate recorde e ATT eleva ganhos no quarto trimestre de 2007",  i + " Lucro líquido sobre para US$ 3,14 bilhões e receita fica em US$ 30,3 bilhões, abaixo da expectativa dos analistas de US$ 30,5 bilhões.", img);
			} else if (i % 2 == 0) {
				append(i + " Setor de wireless bate recorde e ATT eleva ganhos no quarto trimestre de 2007",  i + " Lucro líquido sobre para US$ 3,14.", img);
			} else {
				System.out.println("...........");
				append(i + " Setor de wireless bate recorde e ATT eleva ganhos no quarto trimestre de 2007",  "No description", img);
			}
		}
	}

    public void commandAction(Command c, Displayable d) {
    }
}
