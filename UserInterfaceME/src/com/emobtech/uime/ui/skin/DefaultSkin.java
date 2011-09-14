/*
 * DefaultSkin.java
 * 07/06/2007
 * JME Framework
 * Copyright(c) Ernandes Mourao Junior (ernandes@gmail.com)
 * All rights reserved
 */
package br.framework.jme.ui.skin;

import javax.microedition.lcdui.Font;

import com.emobtech.uime.util.ui.UIUtil;

import br.framework.jme.ui.Calendar;
import br.framework.jme.ui.CheckBox;
import br.framework.jme.ui.ComboBox;
import br.framework.jme.ui.CommandMenu;
import br.framework.jme.ui.Control;
import br.framework.jme.ui.DateField;
import br.framework.jme.ui.Form;
import br.framework.jme.ui.Grid;
import br.framework.jme.ui.ImageViewer;
import br.framework.jme.ui.List;
import br.framework.jme.ui.Screen;
import br.framework.jme.ui.Skin;
import br.framework.jme.ui.TextArea;
import br.framework.jme.ui.TextBox;
import br.framework.jme.ui.TextField;

/**
 * <p>
 * This class defines a default skin looking feel.
 * </p>
 * 
 * @author Ernandes Mourao Junior (ernandes@gmail.com)
 * @version 1.0
 * @since 1.0
 */
public class DefaultSkin extends Skin {
	/**
	 * @inheritDoc
	 * @see br.framework.jme.ui.Skin#applyScreenSkin(br.framework.jme.ui.Screen)
	 */
	public void applyScreenSkin(Screen screen) {
		Font font =
			Font.getFont(Font.FACE_SYSTEM, Font.STYLE_BOLD, Font.SIZE_SMALL);
		//
		screen.setTitleFont(font);
		screen.setTitleColor(161, 199, 238);
		screen.setTitleFontColor(255, 255, 255);
		screen.setBodyFont(font);
        screen.setBodyFontColor(0, 0, 0); //black color.
		screen.setBackgroundColor(252, 252, 252);
		//
		CommandMenu menu = screen.getCommandMenu();
		menu.setFont(font);
		menu.setSelectionColor(255, 150, 45);
		menu.setBarColor(161, 199, 238);
		menu.setListColor(86, 136, 186);
		menu.setFontColor(255, 255, 255);
	}

	/**
	 * @inheritDoc
	 * @see br.framework.jme.ui.Skin#applyListSkin(br.framework.jme.ui.List)
	 */
	public void applyListSkin(List list) {
		list.setScrollBarColor(88, 145, 222);
		list.setSelectionColor(255, 150, 45);
	}

	/**
	 * @inheritDoc
	 * @see br.framework.jme.ui.Skin#applyGridSkin(br.framework.jme.ui.Grid)
	 */
	public void applyGridSkin(Grid grid) {
		grid.setGradeColor(181, 189, 189);
		grid.setColumnsHeaderFontColor(255, 255, 255);
		grid.setColumnsHeaderColor(30, 96, 182);
		grid.setOddLineColor(146, 185, 237);
		grid.setEvenLineColor(255, 255, 255);
		grid.setLiveColumnResizing(true);
	}

//	/**
//	 * @inheritDoc
//	 * @see br.framework.jme.ui.Skin#applyGroupedListSkin(br.framework.jme.ui.GroupedList)
//	 */
//	public void applyGroupedListSkin(GroupedList gList) {
//		Font font =
//			Font.getFont(Font.FACE_SYSTEM, Font.STYLE_BOLD, Font.SIZE_SMALL);
//		//
//		gList.setHeaderFont(font);
//		gList.setBodyFontColor(255, 255, 255);
//		gList.setHeaderColor(204, 222, 244);
//		gList.setHeaderFontColor(30, 96, 182);
//		gList.setScrollBarColor(88, 145, 222);
//		gList.setSelectionColor(255, 150, 45);
//		gList.setDetailFontColor(255, 255, 255);
//		gList.setDepthColor(1, 88, 145, 222);
//		gList.setDepthColor(2, 140, 180, 232);
//		gList.setDepthColor(3, 180, 205, 239);
//	}
	
	/**
	 * @inheritDoc
	 * @see br.framework.jme.ui.Skin#applyTextBoxSkin(br.framework.jme.ui.TextBox)
	 */
	public void applyTextBoxSkin(TextBox tBox) {
		tBox.setScrollBarColor(88, 145, 222);
	}
	
	/**
	 * @inheritDoc
	 * @see br.framework.jme.ui.Skin#applyCalendarSkin(br.framework.jme.ui.Calendar)
	 */
	public void applyCalendarSkin(Calendar calendar) {
		calendar.setFrameColor(181, 189, 189);
		calendar.setFrameHeaderColor(30, 96, 182);
		calendar.setFrameHeaderFontColor(255, 255, 255);
		calendar.setHeaderFontColor(0, 0, 0);
		calendar.setBodyFontColor(0, 0, 0);
		calendar.setSelectionColor(255, 150, 45);
	}
	
	/**
	 * @inheritDoc
	 * @see br.framework.jme.ui.Skin#applyImageViewerSkin(br.framework.jme.ui.ImageViewer)
	 */
	public void applyImageViewerSkin(ImageViewer viewer) {
		viewer.setScrollBarColor(88, 145, 222);
	}
	
	/**
	 * @inheritDoc
	 * @see br.framework.jme.ui.Skin#applyFormSkin(br.framework.jme.ui.Form)
	 */
	public void applyFormSkin(Form form) {
		form.setScrollBarColor(88, 145, 222);
		form.enableEditCommand(true);
	}
	
	/**
	 * @inheritDoc
	 * @see br.framework.jme.ui.Skin#applyControlSkin(br.framework.jme.ui.Control)
	 */
	public void applyControlSkin(Control control) {
		control.setLayout(UIUtil.ALIGN_CENTER);
		control.setBodyFontColor(0, 0, 0);
		control.setBodyColor(218, 218, 218);
		control.setFocusColor(0, 223, 118);
		control.setLabelColor(0, 0, 0);
		control.setDisabledColor(240, 240, 240);
	}
	
	/**
	 * @inheritDoc
	 * @see br.framework.jme.ui.Skin#applyTextFieldSkin(br.framework.jme.ui.TextField)
	 */
	public void applyTextFieldSkin(TextField textField) {
		textField.setBackgroundColor(249, 249, 249);
	}
	
	/**
	 * @see br.framework.jme.ui.Skin#applyTextAreaSkin(br.framework.jme.ui.TextArea)
	 */
	public void applyTextAreaSkin(TextArea textArea) {
		textArea.setScrollBarColor(88, 145, 222);
	}
    
    /**
     * @inheritDoc
     * @see br.framework.jme.ui.Skin#applyCheckBoxSkin(br.framework.jme.ui.CheckBox)
     */
    public void applyCheckBoxSkin(CheckBox checkBox) {
        checkBox.setMarkerColor(98, 176, 255);
    }
    
    /**
     * @inheritDoc
     * @see br.framework.jme.ui.Skin#applyComboBoxSkin(br.framework.jme.ui.ComboBox)
     */
    public void applyComboBoxSkin(ComboBox comboBox) {
        comboBox.setBackgroundColor(249, 249, 249);
    }
    
    /**
     * @inheritDoc
     * @see br.framework.jme.ui.Skin#applyDateFieldSkin(br.framework.jme.ui.DateField)
     */
    public void applyDateFieldSkin(DateField dField) {
    	dField.setBackgroundColor(249, 249, 249);
    }
	
	/**
	 * @inheritDoc
	 * @see br.framework.jme.ui.Skin#getTitleStyle()
	 */
	public int getTitleStyle() {
		return UIUtil.STYLE_GRADIENT | UIUtil.STYLE_TRANS_SPEED_4;
	}
	
	/**
	 * @inheritDoc
	 * @see br.framework.jme.ui.Skin#getSelectionStyle()
	 */
	public int getSelectionStyle() {
		return UIUtil.STYLE_GRADIENT | UIUtil.STYLE_TOP | UIUtil.STYLE_BRIGTH |
			UIUtil.STYLE_TRANS_SPEED_2;
	}
	
	/**
	 * @inheritDoc
	 * @see br.framework.jme.ui.Skin#getVerticalScrollBarStyle()
	 */
	public int getVerticalScrollBarStyle() {
		return UIUtil.STYLE_GRADIENT | UIUtil.STYLE_HORIZONTAL |
			UIUtil.STYLE_TRANS_SPEED_8;
	}
	
	/**
	 * @inheritDoc
	 * @see br.framework.jme.ui.Skin#getHorizontalScrollBarStyle()
	 */
	public int getHorizontalScrollBarStyle() {
		return UIUtil.STYLE_GRADIENT | UIUtil.STYLE_VERTICAL |
			UIUtil.STYLE_TRANS_SPEED_8;
	}
	
	/**
	 * @inheritDoc
	 * @see br.framework.jme.ui.Skin#getCommandMenuBarStyle()
	 */
	public int getCommandMenuBarStyle() {
		return UIUtil.STYLE_GRADIENT | UIUtil.STYLE_TRANS_SPEED_4;
	}
	
	/**
	 * @inheritDoc
	 * @see br.framework.jme.ui.Skin#getCommandMenuListStyle()
	 */
	public int getCommandMenuListStyle() {
		return UIUtil.STYLE_GRADIENT | UIUtil.STYLE_BOTTOM |
			UIUtil.STYLE_TRANS_SPEED_1;
	}
}