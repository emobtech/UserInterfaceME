///*
// * GroupedListSample.java
// */
//package br.framework.jme.sample.ui;
//
//import javax.microedition.lcdui.Command;
//import javax.microedition.lcdui.CommandListener;
//import javax.microedition.lcdui.Displayable;
//
//import br.framework.jme.ui.GroupedItem;
//import br.framework.jme.ui.GroupedList;
//import br.framework.jme.util.ui.UIUtil;
//
///**
// * 
// * @author Main
// */
//public class GroupedListSample extends GroupedList implements CommandListener {
//	/**
//	 * 
//	 * @param title
//	 */
//	public GroupedListSample(String title) {
//		super(title, "Produto:Ano:Cidade");
//		Command cmdOK = new Command("OK", Command.OK, 0);
//		Command cmdAdd = new Command("Back", Command.BACK, 0);
//		Command cmdRemove = new Command("Remove", Command.ITEM, 2);
//		Command cmdInsert = new Command("Insert", Command.ITEM, -1);
//		Command cmdSearch = new Command("Search", Command.ITEM, 0);
//		Command cmdExit = new Command("Exit", Command.EXIT, 0);
//		
//		addCommand(cmdOK);
//		addCommand(cmdExit);
//		addCommand(cmdAdd);
//		addCommand(cmdRemove);
//		addCommand(cmdInsert);
//		addCommand(cmdSearch);
//		
//		setCommandListener(this);
//		
////		try {
////			setBackgroundImage(Image.createImage("/etc/images/symbian_pro.png"));
////		} catch (IOException e) {
////			e.printStackTrace();
////		}
//		
//		GroupedItem item1 = new GroupedItem("Café Santa Clara a Vácuo 200.000,00g", "R$");
//		GroupedItem item11 = new GroupedItem("Café:2003", "R$", 10.5f);
//		GroupedItem item12 = new GroupedItem("Café: 2004", "R$", 20);
//		GroupedItem item13 = new GroupedItem("Café:2005", "R$", 8f);
//		GroupedItem item14 = new GroupedItem("Café: 2006", "R$", 50);
//		
//		item1.add(item11);
//		item1.add(item12);
//		item1.add(item13);
//		item1.add(item14);
//		
//		GroupedItem item2 = new GroupedItem("Arroz", "R$");
//		GroupedItem item21 = new GroupedItem("Arroz:2004", "R$");
//		GroupedItem item211 = new GroupedItem("Arroz:2004:Fortaleza/CE", "R$", 50);
//		GroupedItem item212 = new GroupedItem("Arroz:2004:Iguatu/CE", "R$", 25);
//		GroupedItem item213 = new GroupedItem("Arroz:2004:Sobral/CE", "R$", 50);
//		GroupedItem item214 = new GroupedItem("Arroz:2004:Guaramiranga/CE", "R$", 25);
//
//		GroupedItem item22 = new GroupedItem("Arroz:2005", "R$", 30);
//
//		item2.add(item21);
//		item21.add(item211);
//		item21.add(item212);
//		item21.add(item213);
//		item21.add(item214);
//		item2.add(item22);
//		
//		setItemOpacityLevel(UIUtil.FULLY_OPAQUE);
//		setSelectionOpacityLevel(UIUtil.FULLY_OPAQUE);
//		
//		append(item1);
//		append(item2);
//	}
//
//	/**
//	 * @see javax.microedition.lcdui.CommandListener#commandAction(javax.microedition.lcdui.Command, javax.microedition.lcdui.Displayable)
//	 */
//	public void commandAction(Command c, Displayable d) {
//	}
//}