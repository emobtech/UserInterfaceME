/*
 * GridSample.java
 */
package com.emobtech.uime.sample.ui;

import java.util.Date;

import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Displayable;

import com.emobtech.uime.midlet.MIDlet;
import com.emobtech.uime.ui.Grid;
import com.emobtech.uime.util.ui.UIUtil;

/**
 * 
 * @author Main
 */
public class GridSample extends Grid implements CommandListener {
	Command cmdMax = new Command("Maximizar", Command.BACK, 1);
	Command cmdMin = new Command("Minimizar", Command.BACK, 2);
	Command cmdSort = new Command("Ordenar", Command.BACK, 0);
	Command cmdEdit = new Command("Editar", Command.OK, 0);
	private Command cmdExit = new Command("Exit", Command.EXIT, 0);
	
	/**
	 * 
	 * @param title
	 */
	public GridSample(String title) {
		super(title);
		
		addCommand(cmdMax);
		addCommand(cmdMin);
		addCommand(cmdSort);
		addCommand(cmdEdit);
		addCommand(cmdExit);
		setCommandListener(this);
		
		addColumn("REF", 15, UIUtil.ALIGN_CENTER, NUMERIC);
		addColumn("Nome", 25, UIUtil.ALIGN_LEFT, STRING);
		addColumn("Data Compra", 40, UIUtil.ALIGN_CENTER, DATE);
		addColumn("Qtde", 20, UIUtil.ALIGN_CENTER, NUMERIC);
		
		for (int i = 1; i <= 25; i++) {
			Object[] item = new Object[4];
			item[0] = new Integer(i);
			item[1] = "Produto " + i;
			item[2] = new Date(System.currentTimeMillis() + 86400000 * i);
			item[3] = new Integer(i * 10);
			append(item, null);
		}
		
		setColumnAsEditable(0, true);
		setColumnAsEditable(1, true);
		setColumnAsEditable(2, true);
		setColumnAsEditable(3, true);
	}
	
	protected void onSelectionChange() {
	}

	public void commandAction(Command c, Displayable d) {
		if (c == cmdMax) {
			maximizeColumn(getSelectedColumn());
		} else if (c == cmdMin) {
			minimizeColumn(getSelectedColumn());
		} else if (c == cmdSort) {
			sort(getSelectedColumn());
		} else if (c == cmdEdit) {
			editSelectedCell();
		} else if (c == cmdExit) {
	    	MIDlet.getMIDletInstance().exitApp(false);
	    }

	}
}