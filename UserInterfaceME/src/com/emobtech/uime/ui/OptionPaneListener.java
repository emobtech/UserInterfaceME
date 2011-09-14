/*
 * OptionPaneListener.java
 * 20/03/2008
 * JME Framework
 * Copyright(c) Ernandes Mourao Junior (ernandes@gmail.com)
 * All rights reserved
 */
package br.framework.jme.ui;

/**
 * <p>
 * Listener used to notify some states related to the usage of OptionPane class.
 * </p>
 * 
 * @author Ernandes Mourao Junior (ernandes@gmail.com)
 * @version 1.0
 * @since 1.0
 */
public interface OptionPaneListener {
	/**
	 * <p>
	 * Method called when a OptionPane message dialog is closed by pressing the
	 * command OK.
	 * </p>
	 * @param id OptionPane ID.
	 */
	public void messageDialogAction(String id);
	
	/**
	 * <p>
	 * Method called when a OptionPane confirmation dialog is closed by pressing
	 * either command YES or NO.
	 * </p>
	 * @param id OptionPane ID.
	 * @param result Dialog's result (YES: true / NO: false)
	 */
	public void confirmationDialogAction(String id, boolean result);
}