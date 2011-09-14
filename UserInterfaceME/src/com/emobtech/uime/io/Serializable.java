/*
* Serializable.java
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
public interface Serializable {
	/**
	 * 
	 * @return
	 */
	public byte[] serialize();
	
	/**
	 * 
	 * @param stream
	 * @return
	 */
	public Object deserialize(byte[] stream);
}