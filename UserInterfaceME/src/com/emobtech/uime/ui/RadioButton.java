/*
 * RadioButton.java
 * 30/08/2007
 * JME Framework
 * Copyright(c) Ernandes Mourao Junior (ernandes@gmail.com)
 * All rights reserved
 */
package com.emobtech.uime.ui;

import javax.microedition.lcdui.Graphics;

/**
 * <p>
 * This class implements a radio button.
 * </p>
 * 
 * @author Ernandes Mourao Junior (ernandes@gmail.com)
 * @version 1.0
 * @since 1.0
 */
public class RadioButton extends CheckBox {
	/**
	 * <p>
	 * Radio button manager.
	 * </p>
	 */
	RadioManager manager;
	
	/**
	 * <p>
	 * Constructor.
	 * </p>
	 * @param label Label.
	 */
	public RadioButton(String label) {
		super(label);
        //
        skin.applyRadioButtonSkin(this);
	}
	
	/**
	 * @inheritDoc
	 * @see br.framework.jme.ui.CheckBox#setChecked(boolean)
	 */
	public synchronized void setChecked(boolean checked) {
		super.setChecked(checked);
		//
		if (manager != null && checked) {
			manager.mark(this);
		}
	}
    
    /**
     * @inheritDoc
     * @see br.framework.jme.ui.CheckBox#drawMarker(javax.microedition.lcdui.Graphics, int, int, int, int)
     */
    void drawMarker(Graphics g, int x, int y, int w, int h) {
        //drawing the background of the circle.
        g.setColor(255, 255, 255); //white color.
        g.fillArc(x, y, w, h, 0, 360);
        g.setColor(bodyColor); //drawing the border.
        g.drawArc(x, y, w, h, 0, 360);
        //
        if (checked) {
            x += OFFSET * 2;
            y += OFFSET * 2;
            w -= OFFSET * 4;
            h -= OFFSET * 4;
            //drawing the internal circle.
            g.setColor(markerColor);
            g.fillArc(x, y, w, h, 0, 360);
        }
    }
    
    /**
     * <p>
     * Gets radio button manager.
     * </p>
     * @return Manager.
     */
    RadioManager getManager() {
        return manager;
    }
    
    /**
     * <p>
     * Sets a given radio button manager.
     * </p>
     * @param manager Manager.
     */
    void setManager(RadioManager manager) {
    	this.manager = manager;
    }
}