/*
 * Skin.java
 * 26/05/2007
 * JME Framework
 * Copyright(c) Ernandes Mourao Junior (ernandes@gmail.com)
 * All rights reserved
 */
package com.emobtech.uime.ui;

import com.emobtech.uime.midlet.MIDlet;
import com.emobtech.uime.ui.skin.DefaultSkin;
import com.emobtech.uime.util.ui.UIUtil;

/**
 * <p>
 * This class defines a basic class for a Skin.
 * </p>
 * 
 * @author Ernandes Mourao Junior (ernandes@gmail.com)
 * @version 1.0
 * @since 1.0
 */
public abstract class Skin {
	/**
	 * <p>
	 * Get the default skin to be used by all the screens created. A default
	 * skin class can be defined in the JAD file. It is set using the property 
	 * br.framework.jme.ui.default.skin.class. If this property is not defined
	 * a framework default skin class will be returned.
	 * </p>
	 * @return Default screen.
	 * @throws IllegalArgumentException Invalid default skin class.
	 */
	public static Skin getDefault() {
		final String defaultSkin = 
			MIDlet.getMIDletInstance().getAppProperty(
				"br-framework-jme-ui-default-skin-class");
		//
		if (defaultSkin != null) {
			try {
				return (Skin)Class.forName(defaultSkin).newInstance();
			} catch (Exception e) {
				throw new IllegalArgumentException(
					"Invalid default skin class: " + defaultSkin);
			}
		} else {
			return new DefaultSkin();
		}
	}
	
	/**
	 * <p>
	 * This method is used to apply the skin looking feel in a given screen.
	 * </p>
	 * @param screen Screen.
	 */
	public void applyScreenSkin(Screen screen) {
	}
	
	/**
	 * <p>
	 * This method is used to apply the skin looking feel in a given List.
	 * </p>
	 * @param list Screen.
	 */
	public void applyListSkin(List list) {
	}
	
	/**
	 * <p>
	 * This method is used to apply the skin looking feel in a given Grid.
	 * </p>
	 * @param grid Screen.
	 */
	public void applyGridSkin(Grid grid) {
	}
	
	/**
	 * <p>
	 * This method is used to apply the skin looking feel in a given Detail
	 * List.
	 * </p>
	 * @param dList Screen.
	 */
	public void applyDetailListSkin(DetailList dList) {
	}

//	/**
//	 * <p>
//	 * This method is used to apply the skin looking feel in a given Grouped
//	 * List.
//	 * </p>
//	 * @param gList Screen.
//	 */
//	public void applyGroupedListSkin(GroupedList gList) {
//	}
	
	/**
	 * <p>
	 * This method is used to apply the skin looking feel in a given TextBox.
	 * </p>
	 * @param tBox Screen.
	 */
	public void applyTextBoxSkin(TextBox tBox) {
	}

	/**
	 * <p>
	 * This method is used to apply the skin looking feel in a given Calendar.
	 * </p>
	 * @param calendar Calendar.
	 */
	public void applyCalendarSkin(Calendar calendar) {
	}

	/**
	 * <p>
	 * This method is used to apply the skin looking feel in a given 
	 * ImageViewer.
	 * </p>
	 * @param viewer ImageViewer.
	 */
	public void applyImageViewerSkin(ImageViewer viewer) {
	}

	/**
	 * <p>
	 * This method is used to apply the skin looking feel in a given Form.
	 * </p>
	 * @param form Screen.
	 */
	public void applyFormSkin(Form form) {
	}

	/**
	 * <p>
	 * This method is used to apply the skin looking feel in a given OptionPane.
	 * </p>
	 * @param optPane OptionPane.
	 */
	public void applyOptionPaneSkin(OptionPane optPane) {
	}

	/**
	 * <p>
	 * This method is used to apply the skin looking feel in a given DocList.
	 * </p>
	 * @param dList DocList.
	 */
	public void applyDocListSkin(DockList dList) {
	}

	/**
	 * <p>
	 * This method is used to apply the skin looking feel in a given RSSList.
	 * </p>
	 * @param rssList RSSList.
	 */
	public void applyRSSListSkin(RSSList rssList) {
	}

	/**
	 * <p>
	 * This method is used to apply the skin looking feel in a given Control.
	 * </p>
	 * @param control Control.
	 */
	public void applyControlSkin(Control control) {
	}

	/**
	 * <p>
	 * This method is used to apply the skin looking feel in a given TextField.
	 * </p>
	 * @param textField TextField.
	 */
	public void applyTextFieldSkin(TextField textField) {
	}

	/**
	 * <p>
	 * This method is used to apply the skin looking feel in a given
	 * MultiLineTextField.
	 * </p>
	 * @param textArea TextArea.
	 */
	public void applyTextAreaSkin(TextArea textArea) {
	}

	/**
     * <p>
     * This method is used to apply the skin looking feel in a given CheckBox.
     * </p>
     * @param checkBox CheckBox.
     */
    public void applyCheckBoxSkin(CheckBox checkBox) {
    }

    /**
     * <p>
     * This method is used to apply the skin looking feel in a given
     * RadioButton.
     * </p>
     * @param radioButton RadioButton.
     */
    public void applyRadioButtonSkin(RadioButton radioButton) {
    }

    /**
     * <p>
     * This method is used to apply the skin looking feel in a given Spacer.
     * </p>
     * @param spacer Spacer.
     */
    public void applySpacerSkin(Spacer spacer) {
    }

    /**
     * <p>
     * This method is used to apply the skin looking feel in a given
     * StringControl.
     * </p>
     * @param strControl StringControl.
     */
    public void applyStringControlSkin(StringControl strControl) {
    }

    /**
     * <p>
     * This method is used to apply the skin looking feel in a given ListBox.
     * </p>
     * @param listBox ListBox.
     */
    public void applyListBoxSkin(ListBox listBox) {
    }

    /**
     * <p>
     * This method is used to apply the skin looking feel in a given ComboBox.
     * </p>
     * @param comboBox ComboBox.
     */
    public void applyComboBoxSkin(ComboBox comboBox) {
    }

    /**
     * <p>
     * This method is used to apply the skin looking feel in a given 
     * ImageControl.
     * </p>
     * @param imgControl ImageControl.
     */
    public void applyImageControlSkin(ImageControl imgControl) {
    }

    /**
     * <p>
     * This method is used to apply the skin looking feel in a given DateField.
     * </p>
     * @param dField DateField.
     */
    public void applyDateFieldSkin(DateField dField) {
    }

    /**
     * <p>
     * This method is used to apply the skin looking feel in a given
     * ScreenToControlAdapter.
     * </p>
     * @param stocoa ScreenToControlAdapter.
     */
    public void applyScreenToControlAdapterSkin(ScreenToControlAdapter stocoa) {
    }

    /**
	 * <p>
	 * Get the style to be applyed on the title bar.
	 * </p>
	 * @return Style.
	 */
	public int getTitleStyle() {
		return UIUtil.STYLE_PLAIN;
	}
	
	/**
	 * <p>
	 * Get the style to be applyed on the selection bar.
	 * </p>
	 * @return Style.
	 */
	public int getSelectionStyle() {
		return UIUtil.STYLE_PLAIN;
	}
	
	/**
	 * <p>
	 * Get the style to be applyed on the vertical scrollbar.
	 * </p>
	 * @return Style.
	 */
	public int getVerticalScrollBarStyle() {
		return UIUtil.STYLE_PLAIN;
	}

	/**
	 * <p>
	 * Get the style to be applyed on the horizontal scrollbar.
	 * </p>
	 * @return Style.
	 */
	public int getHorizontalScrollBarStyle() {
		return UIUtil.STYLE_PLAIN;
	}
	
	/**
	 * <p>
	 * Get the style to be applyed on the command menu bar.
	 * </p>
	 * @return Style.
	 */
	public int getCommandMenuBarStyle() {
		return UIUtil.STYLE_PLAIN;
	}
	
	/**
	 * <p>
	 * Get the style to be applyed on the command menu list.
	 * </p>
	 * @return Style.
	 */
	public int getCommandMenuListStyle() {
		return UIUtil.STYLE_PLAIN;
	}
}