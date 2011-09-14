/*
 * CheckBox.java
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
 * This class implements a checkbox control.
 * </p>
 * 
 * @author Ernandes Mourao Junior (ernandes@gmail.com)
 * @version 1.0
 * @since 1.0
 */
public class CheckBox extends Control {
	/**
	 * <p>
	 * Marker color.
	 * </p>
	 */
	int markerColor;
    
    /**
	 * <p>
	 * Box size.
	 * </p>
     */
    int boxSize;
    
    /**
	 * <p>
	 * Check state.
	 * </p>
	 */
	boolean checked;

	/**
	 * <p>
	 * Contructor.
	 * </p>
	 * @param label Label.
	 */
	public CheckBox(String label) {
		super(label);
        //
        skin.applyCheckBoxSkin(this);
	}
	
	/**
	 * <p>
	 * Verify if the state is checked or not.
	 * </p>
	 * @return Checked or not.
	 */
	public synchronized boolean isChecked() {
		return checked;
	}
	
	/**
	 * <p>
	 * Set the state checked or not.
	 * </p>
	 * @param checked Checked or not.
	 */
	public synchronized void setChecked(boolean checked) {
		this.checked = checked;
		//
		if (parent != null) {
			parent.notifyStateChanged(this);
		}
		//
		repaint();
	}
    
    /**
     * <p>
     * Set the marker color.
     * </p>
     * @param r Red.
     * @param g Green.
     * @param b Blue.
     */
    public void setMarkerColor(int r, int g, int b) {
        markerColor = UIUtil.getHexColor(r, g, b);
    }
    
    /**
     * @see br.framework.jme.ui.Control#edit()
     */
    public synchronized void edit() {
        super.edit();
        //
        setChecked(!isChecked());
    }

	/**
	 * @inheritDoc
	 * @see br.framework.jme.ui.Control#drawBody(javax.microedition.lcdui.Graphics, int, int, int, int)
	 */
	protected synchronized void drawBody(Graphics g, int x, int y, int w,
        int h) {
        x += OFFSET * 2;
        //
		drawMarker(g, x, y + (h - boxSize) / 2, boxSize, boxSize);
        //
        if (super.isLabelEntered()) {
            g.setColor(bodyFontColor);
            g.setFont(bodyFont);
            x += boxSize + OFFSET * 2;
            //
            g.drawString(
                UIUtil.shrinkString(
                    label, w - (boxSize + OFFSET * 4), bodyFont),
                x,
                y + UIUtil.alignString(
                        label, h, UIUtil.ALIGN_MIDDLE, bodyFont),
                Graphics.TOP | Graphics.LEFT);
        }
	}
	
	/**
	 * <p>
	 * Draw the marker.
	 * </p>
	 * @param g Graphics object.
	 * @param x X coordinate.
	 * @param y Y coordinate.
	 * @param w Width.
	 * @param h Height.
	 */
	void drawMarker(Graphics g, int x, int y, int w, int h) {
        //drawing the background of the box.
        g.setColor(255, 255, 255); //white color.
        g.fillRect(x, y, w, h);
        //
        if (checked) {
            final int borderCount = 2;
            g.setColor(markerColor);
            //
            for (int i = 0; i < borderCount; i++) {
                g.drawLine(x + i, y, x + w, y + h - i);
                g.drawLine(x + i, y + h, x + w, y + i);
                //
                if (i > 0) {
                    //used to make the "X" larger.
                    g.drawLine(x, y + i, x + w - i, y + h);
                    g.drawLine(x, y + h - i, x + w - i, y);
                }
            }
        }
        //
        //drawing the border.
        g.setColor(bodyColor); //brighter gray color.
        g.drawRect(x, y, w, h);
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
				setHeight(bodyFont.getHeight() + OFFSET * 2);
			}
            boxSize = (bodyFont.getHeight() * 75) / 100; //75%
		}
	}
}