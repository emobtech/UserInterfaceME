/*
 * Spacer.java
 * 30/08/2007
 * JME Framework
 * Copyright(c) Ernandes Mourao Junior (ernandes@gmail.com)
 * All rights reserved
 */
package br.framework.jme.ui;

import javax.microedition.lcdui.Graphics;

import com.emobtech.uime.util.ui.UIUtil;


/**
 * <p>
 * This class implements a spacer control.
 * </p>
 * 
 * @author Ernandes Mourao Junior (ernandes@gmail.com)
 * @version 1.0
 * @since 1.0
 */
public class Spacer extends Control {
	/**
	 * <p>
	 * Constant that defines that the spacer will be just an empty space in the 
	 * screen.
	 * </p>
	 */
	public static final int BLANK = -1;
	
	/**
	 * <p>
	 * Constant that defines that the spacer will have solid line drawn in the 
	 * screen.
	 * </p>
	 */
	public static final int SOLID_LINE = Graphics.SOLID;

	/**
	 * <p>
	 * Constant that  defines that the spacer will have dotted line drawn in the
	 * screen.
	 * </p>
	 */
	public static final int DOTTED_LINE = Graphics.DOTTED;
	
	/**
	 * <p>
	 * Type.
	 * </p>
	 */
	int type;

	/**
	 * <p>
	 * Constructor.
	 * </p>
	 * @param type Type.
	 * @see Spacer#BLANK
	 * @see Graphics#SOLID
	 * @see Graphics#DOTTED
	 */
	public Spacer(int type) {
		this(null, type);
	}

	/**
	 * <p>
	 * Constructor.
	 * </p>
	 * @param label Label.
	 * @param type Type.
	 * @see Spacer#BLANK
	 * @see Graphics#SOLID
	 * @see Graphics#DOTTED
	 */
	public Spacer(String label, int type) {
		super(label);
		//
		this.type = type;
		//
		setFocusless(true);
		//
		skin.applySpacerSkin(this);
	}

	/**
	 * @inheritDoc
	 * @see br.framework.jme.ui.Control#drawBody(javax.microedition.lcdui.Graphics, int, int, int, int)
	 */
	protected void drawBody(Graphics g, int x, int y, int w, int h) {
		if (type != BLANK) {
			final int lastStrokeStyle = g.getStrokeStyle();
			//
			g.setStrokeStyle(type);
			g.setColor(bodyColor);
			g.drawLine(x, y + h / 2, x + w, y + h / 2);
			//
			g.setStrokeStyle(lastStrokeStyle); //restoring previous stroke style.
			//
			if (super.isLabelEntered()) {
				g.setColor(bodyFontColor);
				g.setFont(bodyFont);
				g.drawString(
					UIUtil.shrinkString(label, w, bodyFont),
					x + UIUtil.alignString(label, w, layout, bodyFont),
					y,
					Graphics.TOP | Graphics.LEFT);
			}
		}
	}
	
	/**
	 * @inheritDoc
	 * @see br.framework.jme.ui.Control#isEditable()
	 */
	protected boolean isEditable() {
		return false;
	}
	
	/**
	 * @inheritDoc
	 * @see br.framework.jme.ui.Control#isLabelEntered()
	 */
	boolean isLabelEntered() {
		return false;
	}

	/**
	 * @inheritDoc
	 * @see br.framework.jme.ui.Control#setParent(br.framework.jme.ui.Form)
	 */
	void setParent(Form parent) {
		super.setParent(parent);
		//
		if (parent != null) {
			//calculating the text field size.
			if (getWidth() == 0) {
				setWidth((int)((parent.getWidth() * 95) / 100)); //95%
			}
			if (getHeight() == 0) {
				setHeight(bodyFont.getHeight());
			}
		}
	}
}