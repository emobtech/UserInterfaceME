/*
 * SerializableRecord.java
 * 24/02/2008
 * JME Framework
 * Copyright(c) Ernandes Mourao Junior (ernandes@gmail.com)
 * All rights reserved
 */
package com.emobtech.uime.util.rms;

import com.emobtech.uime.io.Serializable;

/**
 * <p>
 * </p>
 * 
 * @author Ernandes Mourao Junior (ernandes@gmail.com)
 * @version 1.0
 * @since 1.0
 */
public interface SerializableRecord extends Serializable {
	/**
	 * 
	 * @return
	 */
	public int getRecordID();
	
	/**
	 * 
	 * @param id
	 */
	public void setRecordID(int id);
}