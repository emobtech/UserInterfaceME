/*
* ConnectionListener.java
* Feb 24, 2008
* JME Framework
* Copyright(c) Ernandes Mourao Junior (ernandes@gmail.com)
* All rights reserved
*/
package com.emobtech.uime.io;

/**
 * <p>
 * </p>
 * 
 * @author Ernandes Mourao Junior (ernandes@gmail.com)
 * @version 1.0
 * @since 1.0
 */
public interface ConnectionListener {
	/**
	 *
	 * @param url
	 * @param exception
	 */
	public void onFail(String url, Exception exception);

	/**
	 * 
	 * @param url
	 */
	public void onSuccess(String url);
}