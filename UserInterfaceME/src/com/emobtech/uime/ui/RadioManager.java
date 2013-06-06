/*
 * RadioManager.java
 * 03/09/2007
 * JME Framework
 * Copyright(c) Ernandes Mourao Junior (ernandes@gmail.com)
 * All rights reserved
 */
package com.emobtech.uime.ui;

import java.util.Vector;

/**
 * <p>
 * This class is resposible for controlling the state of all the radio button
 * controls that are on its command.
 * </p>
 * 
 * @author Ernandes Mourao Junior (ernandes@gmail.com)
 * @version 1.0
 * @since 1.0
 */
class RadioManager {
	/**
	 * <p>
	 * Radio button list.
	 * </p>
	 */
	Vector radioList = new Vector(5);
    
    /**
	 * <p>
	 * Manager name.
	 * </p>
     */
    String name;
    
    /**
	 * <p>
	 * Constructor.
	 * </p>
     * @param name Name.
     */
    public RadioManager(String name) {
        this.name = name;
    }
    
    /**
	 * <p>
	 * Gets the name.
	 * </p>
     * @return Name.
     */
    public String getName() {
        return name;
    }
	
    /**
	 * <p>
	 * Adds a given radio button into the manager and assign it to the radio 
	 * button.
	 * </p>
	 * @param radio Radio button.
	 */
	public synchronized void addRadio(RadioButton radio) {
		radioList.addElement(radio);
        radio.setManager(this);
	}
	
    /**
	 * <p>
	 * Remoes a given radio button from the manger and unset manager from the
	 * radio button.
	 * </p>
	 * @param radio Radio button.
	 * @return Radio button removed or not.
	 */
	public synchronized boolean removeRadio(RadioButton radio) {
        if (radioList.removeElement(radio)) {
            radio.setManager(null);
            return true;
        } else {
            return false;
        }
	}
	
    /**
	 * <p>
	 * Removes all the radio buttons from the manager and unset the manager from
	 * all of them.
	 * </p>
	 */
	public synchronized void removeAllRadios() {
		RadioButton r = null;
		//
		for (int i = radioList.size() -1; i >= 0; i--) {
			r = (RadioButton)radioList.elementAt(i);
			//
			r.setManager(null);
		}
		//
		radioList.removeAllElements();
	}
	
    /**
	 * <p>
	 * Gets the size of radio button list.
	 * </p>
	 * @return Size.
	 */
	public synchronized int size() {
		return radioList.size();
	}
 	
    /**
	 * <p>
	 * Changes the state of the given radio button and adjusts the state of the
	 * others contained in the list. 
	 * </p>
	 * @param radio Radio button.
	 */
	public synchronized void mark(RadioButton radio) {
		RadioButton r = null;
		//
		for (int i = radioList.size() -1; i >= 0; i--) {
			r = (RadioButton)radioList.elementAt(i);
			if (r != radio) {
				r.setChecked(false);
			}
		}
	}
}