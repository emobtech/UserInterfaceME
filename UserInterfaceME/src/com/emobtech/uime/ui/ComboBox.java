/*
 * ComboBox.java
 * 31/08/2007
 * JME Framework
 * Copyright(c) Ernandes Mourao Junior (ernandes@gmail.com)
 * All rights reserved
 */
package com.emobtech.uime.ui;

import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;

import com.emobtech.uime.util.ui.UIUtil;


/**
 * <p>
 * This class implements a combobox control.
 * </p>
 * 
 * @author Ernandes Mourao Junior (ernandes@gmail.com)
 * @version 1.0
 * @since 1.0
 */
public class ComboBox extends ListBox {
    /**
     * <p>
     * Expanded state.
     * </p>
     */
    boolean isExpanded;
    
    /**
     * <p>
     * Side size.
     * </p>
     */
    int sideSize;
    
    /**
     * <p>
     * Constructor.
     * </p>
     * @param label Label.
     */
    public ComboBox(String label) {
        this(label, null, null);
    }

    /**
     * <p>
     * Constructor.
     * </p>
     * @param label Label.
     * @param items Items.
     * @param images Images.
     */
    public ComboBox(String label, Object[] items, Image[] images) {
        super(label, items, images, List.IMPLICIT);
        //
        model.setHeight(0);
        //
        skin.applyComboBoxSkin(this);
    }
    
    /**
     * @inheritDoc
     * @see br.framework.jme.ui.Control#setHeight(int)
     */
    public void setHeight(int h) {
    	if (isExpanded) {
        	super.setHeight(h);
        	model.setHeight(h - getBestHeight());
    	} else {
        	super.setHeight(h);
        	model.setHeight(0);
    	}
    }
    
    /**
     * @inheritDoc
     * @see br.framework.jme.ui.Control#edit()
     */
    public synchronized void edit() {
    	super.edit();
    	//
    	isExpanded = true;
        //
    	final int listBestHeight = bodyFont.getHeight() * 5;
    	//
        setHeight(getBestHeight() + listBestHeight);
    	model.setHeight(listBestHeight);
        //
        parent.notifySizeChanged(this);
    }

    /**
     * @inheritDoc
     * @see br.framework.jme.ui.Control#drawBody(javax.microedition.lcdui.Graphics, int, int, int, int)
     */
    protected synchronized void drawBody(Graphics g, int x, int y, int w,
        int h) {
    	if (isExpanded) {
    		h -= model.getHeight();
    	}
    	//
        g.setColor(bodyColor);
        //
        g.drawRect(x, y, w, h); //draw the rectangle;
        //
        drawArrow(g, x + w - sideSize, y, sideSize, h); //draw the arrow.
        //
        if (model.getSelectedIndex() != -1) {
            g.setFont(bodyFont);
            g.setColor(bodyFontColor);
            //
            w -= sideSize + OFFSET * 2; //displayed text area.
            //
            String item = model.get(model.getSelectedIndex()).toString();
            g.drawString(
                UIUtil.shrinkString(item, w, bodyFont),
                x + OFFSET, //left margin.
                y + UIUtil.alignString(
                        item, h, UIUtil.ALIGN_MIDDLE, bodyFont),
                Graphics.TOP | Graphics.LEFT);
        }
        //
    	if (isExpanded) {
    		super.drawBody(g, x, y + h, model.getWidth(), model.getHeight());
    	}
    }
    
    /**
     * @inheritDoc
     * @see br.framework.jme.ui.ListBox#penUp(int, int)
     */
    protected void penUp(int x, int y) {
    	model.penUp(x - rect.x, y - rect.y - getBestHeight());
    }
    
    /**
     * @inheritDoc
     * @see br.framework.jme.ui.ListBox#penDown(int, int)
     */
    protected void penDown(int x, int y) {
    	model.penDown(x - rect.x, y - rect.y - getBestHeight());
    }
    
    /**
     * @inheritDoc
     * @see br.framework.jme.ui.ListBox#penDragged(int, int)
     */
    protected void penDragged(int x, int y) {
    	model.penDragged(x - rect.x, y - rect.y - getBestHeight());
    }
    
    /**
     * <p>
     * Draw the arrow.
     * </p>
	 * @param g Graphics object.
	 * @param x X coordinate.
	 * @param y Y coordinate.
	 * @param w Width.
	 * @param h Height.
     */
    void drawArrow(Graphics g, int x, int y, int w, int h) {
    	y += OFFSET;
    	w -= OFFSET;
    	h -= OFFSET * 2;
    	//
    	g.setColor(bodyColor);
    	//
        g.drawRect(x, y, w, h); //draw the square;
        //
        if (isExpanded) {
        	g.setColor(focusColor);
        } else {
            g.setColor(bodyFontColor);
        }
        //
        UIUtil.drawTriangle(g, x + OFFSET, y + w / 4, w - OFFSET * 2, true); //draw the triangle.
    }
    
    /**
     * @inheritDoc
     * @see br.framework.jme.ui.Control#setParent(br.framework.jme.ui.Form)
     */
    void setParent(Form parent) {
        if (getHeight() == 0) {
            super.setParent(parent);
            setHeight(getBestHeight());
        } else {
            super.setParent(parent);
        }
        //
        sideSize = (parent.getWidth() * 12) / 100; //12%
    }
    
    /**
     * @inheritDoc
     * @see br.framework.jme.ui.ListBox#performCommandAction(javax.microedition.lcdui.Command, javax.microedition.lcdui.Displayable)
     */
    void performCommandAction(Command c, Displayable d) {
        isExpanded = false;
        setHeight(getHeight() - model.getHeight());
    	//
    	super.performCommandAction(c, d);
    }
    
    /**
     * <p>
     * Gets best combobox height.
     * </p>
     * @return Height.
     */
    private int getBestHeight() {
    	return bodyFont.getHeight() + OFFSET * 2;
    }
}