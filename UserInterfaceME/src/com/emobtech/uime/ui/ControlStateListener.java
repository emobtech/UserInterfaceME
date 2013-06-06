/*
 * ControlStateListener.java
 * 02/09/2007
 * JME Framework
 * Copyright(c) Ernandes Mourao Junior (ernandes@gmail.com)
 * All rights reserved
 */
package com.emobtech.uime.ui;

/**
 * <p>
 * This interface is used by applications which need to receive events that 
 * indicate changes in the internal state of the controls within a Form screen.
 * </p>
 * 
 * @author Ernandes Mourao Junior (ernandes@gmail.com)
 * @version 1.0
 * @since 1.0
 */
public interface ControlStateListener {
	/**
     * <p>
     * This method is called when internal state of an control has been changed
     * by the user.
     * </p>
	 * @param control Control.
	 */
	public void controlStateChanged(Control control);
}