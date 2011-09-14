/*
 * FormSample.java
 */
package com.emobtech.uime.sample.ui;

import java.io.IOException;
import java.util.Date;

import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.Image;

import com.emobtech.uime.midlet.MIDlet;
import com.emobtech.uime.ui.CheckBox;
import com.emobtech.uime.ui.ComboBox;
import com.emobtech.uime.ui.Control;
import com.emobtech.uime.ui.ControlStateListener;
import com.emobtech.uime.ui.DateField;
import com.emobtech.uime.ui.Form;
import com.emobtech.uime.ui.Grid;
import com.emobtech.uime.ui.ImageControl;
import com.emobtech.uime.ui.List;
import com.emobtech.uime.ui.ListBox;
import com.emobtech.uime.ui.RadioButton;
import com.emobtech.uime.ui.Spacer;
import com.emobtech.uime.ui.StringControl;
import com.emobtech.uime.ui.TextArea;
import com.emobtech.uime.ui.TextField;
import com.emobtech.uime.util.ui.UIUtil;

/**
 * 
 * @author Main
 */
public class FormSample extends Form implements CommandListener, ControlStateListener {
	private Command cmdExit = new Command("Exit", Command.EXIT, 0);

	/**
	 * 
	 * @param title
	 */
	public FormSample(String title) {
		super(title);
		addCommand(cmdExit);
		setCommandListener(this);
		setControlStateListener(this);
        enableEditCommand(true);
        //
        StringControl s = new StringControl("Label:", "http://www.wikihow.com/Make-a-Quilt-from-Tee-Shirts", StringControl.PLAIN);
        append(s);
		//
        DateField df = new DateField("Date Field:", new Date());
        append(df);
        //
        CheckBox c1 = new CheckBox("Receive news");
        append(c1);
        //
        RadioButton r1 = new RadioButton("Morning");
        append(r1);
        //
        TextField t1 = new TextField("Name:", "Java Enterprise Edition");
        append(t1);
        //
        TextArea m1 = new TextArea("Address:", "Rua Chico Lemos, 946, Cidade dos Funcionários.", 2);
        append(m1);
        //
        Spacer s1 = new Spacer(Spacer.SOLID_LINE);
        append(s1);
        //
        ListBox lb = new ListBox("ListBox", new String[] {"Belém", "Maceio", "Blumenau", "Sao Paulo", "Rio de Janeiro", "Fortaleza", "Belo Horizonte", "Salvador", "Porto Alegre"}, null, List.IMPLICIT);
        append(lb);
        //
        ComboBox cb = new ComboBox("ComboBox", new String[] {"Punto", "Polo Hatch", "Polo Sedan", "Frontier Attack"}, null);
        append(cb);
        //
		Image img = null;
		try {
			img = Image.createImage("/etc/images/symbian_pro.png");
		} catch (IOException e) {
			e.printStackTrace();
		}
        ImageControl imgc = new ImageControl("Map", img);
        append(imgc);
        //
        append("Framework JME by Ernandes Jr.");
        //
		Grid grid = new Grid("Grid");
		grid.addColumn("Col1", 50, UIUtil.ALIGN_LEFT, Grid.STRING);
		grid.addColumn("Col2", 50, UIUtil.ALIGN_LEFT, Grid.STRING);
		//
		grid.append(new Object[] {"Last Price:", "10,00"}, null);
		grid.append(new Object[] {"Date:", "02/10/2007"}, null);
		grid.append(new Object[] {"Hour:", "20:18"}, null);
		grid.append(new Object[] {"Change:", "-0,85"}, null);
		grid.append(new Object[] {"Open:", "55,35"}, null);
		grid.append(new Object[] {"Min:", "51,25"}, null);
		grid.append(new Object[] {"Max:", "58,78"}, null);
		grid.append(new Object[] {"Volume:", "568M"}, null);
		append(grid);



//		TextField t1 = new TextField("Name:", "Java Enterprise Edition");
//		MultiLineTextField m1 = new MultiLineTextField("Address:", "Rua Chico Lemos, 946, Cidade dos Funcionários.", 2);
//		Spacer s1 = new Spacer(Spacer.SOLID_LINE);
//		TextField t2 = new TextField("E-mail:", "joaosilva@gmail.com");
//		CheckBox c1 = new CheckBox("Receive news");
//        
//        StringControl str1 = new StringControl("Sun Microsystems", StringControl.HYPERLINK);
//        str1.setDefaultCommand(new Command("default", Command.OK, -1));
//        t1.setDefaultCommand(new Command("t1", Command.OK, -1));
//        
//        RadioButton r1 = new RadioButton("Morning");
//        RadioButton r2 = new RadioButton("Afternoon");
//        RadioButton r3 = new RadioButton("Evening");
//        RadioButton r4 = new RadioButton("Summer");
//        
//        ScreenToControlAdapter ac = new ScreenToControlAdapter(new Calendar("Calendar"));
//        
//        List list = new List("Colors", List.IMPLICIT);
//        list.append("Red", null);
//        list.append("Green", null);
//        list.append("Yellow", null);
//        list.append("White", null);
//        list.append("Black", null);
//        list.append("Blue", null);
//        list.append("Brown", null);
//        list.append("Pink", null);
//        
//        list.addCommand(new Command("Inserir", Command.OK, 0));
//        list.addCommand(new Command("Editar", Command.OK, 0));
//        list.addCommand(new Command("Deletar", Command.OK, 0));
//        list.setCommandListener(new CommandListener() {
//            public void commandAction(Command c, Displayable d) {
//                System.out.println("triggering: " + c.getLabel());
//            }
//        });
//        
//        ScreenToControlAdapter sa1 = new ScreenToControlAdapter(list);
//        sa1.setPopUpMode(true);
//        
//        ListBox lb = new ListBox("ListBox", new String[] {"Belém", "Maceio", "Blumenau", "Sao Paulo", "Rio de Janeiro", "Fortaleza", "Belo Horizonte", "Salvador", "Porto Alegre"}, null);
//        
//        ComboBox cb = new ComboBox("ComboBox", new String[] {"Punto", "Polo Hatch", "Polo Sedan", "Frontier Attack"}, null);
//        
//        Image img = null;
//        try {
//            img = Image.createImage("/etc/images/map-google.png");
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        
//        ImageControl imgc = new ImageControl("Map", img);
////        imgc.fit();
//        
//        
//		//
	}
    
    protected void onFocusChange() {
    }
	
	public void commandAction(Command c, Displayable d) {
		if (c == cmdExit) {
	    	MIDlet.getMIDletInstance().exitApp(false);
	    }
    }

	public void controlStateChanged(Control control) {
	}
}