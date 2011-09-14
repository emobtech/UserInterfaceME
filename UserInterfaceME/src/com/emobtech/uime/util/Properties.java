/*
 * Properties.java
 * 14/09/2007
 * JME Framework
 * Copyright(c) Ernandes Mourao Junior (ernandes@gmail.com)
 * All rights reserved
 */
package com.emobtech.uime.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.Enumeration;
import java.util.Hashtable;

import com.emobtech.uime.midlet.MIDlet;


/**
 * <p>
 * This class is used to load the content of properties resources.
 * </p>
 * 
 * @author Ernandes Mourao Junior (ernandes@gmail.com)
 * @version 1.0
 * @since 1.0
 */
public class Properties {
	/**
	 * <p>
	 * Properties resources' source.
	 * </p>
	 * 
	 */
	InputStream source;
	
	/**
	 * <p>
	 * Encode.
	 * </p>
	 */
	String encode;
	
	/**
	 * <p>
	 * Hash that holds all the keys-values loaded from the properties resource.
	 * </p>
	 */
	Hashtable keyValueMap;
	
	/**
	 * <p>
	 * Constructor.
	 * </p>
	 * @param resourcePath Resource path.
	 */
	public Properties(String resourcePath) {
		this(
			MIDlet.getMIDletInstance().getClass().getResourceAsStream(
				resourcePath));
	}
	
	/**
	 * <p>
	 * Constructor.
	 * </p>
	 * @param input Resource's source.
	 */
	public Properties(InputStream input) {
		source = input;
		encode = "ISO-8859-1"; //UTF8
		keyValueMap = new Hashtable(20);
		//
		load();
	}
	
	/**
	 * <p>
	 * Gets the value that is associated to the given key. If the key does not
	 * exist, <code>null</code> will be returned.
	 * </p>
	 * @param key Key.
	 * @return Value.
	 */
	public String getProperty(String key) {
        return getProperty(key, null);
	}
	
	/**
	 * <p>
	 * Gets the value that is associated to the given key. If the key does not
	 * exist, <code>null</code> will be returned.
	 * </p>
     * <p>
     * The parameters in the returned value, specified by the mask {0-n}, will 
     * be replaced by the array parameters, as passed at the second argument, 
     * according their order in the array.
     * </p>
	 * @param key Key.
	 * @param params Parameters.
	 * @return Value.
	 */
	public String getProperty(String key, Object[] params) {
		return 
			StringUtil.replaceParameters(
				(String)keyValueMap.get(key.toUpperCase()), params);
	}

	/**
	 * <p>
	 * Returns all the key names of the resource.
	 * </p>
	 * @return Keys.
	 */
	public Enumeration getPropertyNames() {
		return keyValueMap.keys();
	}
	
	/**
	 * <p>
	 * Returns the count of keys in the resource.
	 * </p>
	 * @return Count.
	 */
	public int size() {
		return keyValueMap.size();
	}
	
	/**
	 * <p>
	 * Loads the properties.
	 * </p>
	 */
	void load() {
		if (source == null) {
			return;
		}
        //
        String line;
        String key;
        String value;
        int i;
        //
        while ((line = readLine()) != null) {
        	line = line.trim();
            //
            i = line.indexOf('=');
            if (!line.startsWith("#") && i != -1) {
                key = line.substring(0, i);
                value = line.substring(i +1, line.length());
                keyValueMap.put(key.trim().toUpperCase(), value.trim());
            }
        }
        //
        try {
			source.close();
		} catch (IOException e) {
		}
		source = null;
	}
	
	/**
	 * <p>
	 * Reads a line from the resource. If the end of the resouce is reached,
	 * <code>null</code> will be returned.
	 * </p>
     * @return
     */
    String readLine() {
        int size = 100; //line size.
        byte[] b = new byte[size];
        int offset = 0;
        int c;
        //
        try {
            while ((c = source.read()) != '\n' && c != -1) {
                if (offset == size) {
                    size *= 2; //grows the line size.
                    byte[] a = new byte[size];
                    System.arraycopy(b, 0, a, 0, offset);
                    b = a;
                }
                b[offset++] = (byte) c;
            }
        } catch (IOException e) {
            return null;
        }
        //
        if (c == -1 && offset == 0) {
            return null;
        }
        //
        try {
            return new String(b, 0, offset, encode);
        } catch (UnsupportedEncodingException e) {
            return null;
        }
    }
}