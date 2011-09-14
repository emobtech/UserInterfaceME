/*
 * PropertiesRecordStore.java
 * 02/02/2008
 * JME Framework
 * Copyright(c) Ernandes Mourao Junior (ernandes@gmail.com)
 * All rights reserved
 */
package com.emobtech.uime.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Enumeration;
import java.util.Hashtable;

import javax.microedition.rms.RecordEnumeration;
import javax.microedition.rms.RecordStore;
import javax.microedition.rms.RecordStoreException;

/**
 * <p>
 * This class implements a properties file stored in a given Record Store.
 * </p>
 * 
 * @author Ernandes Mourao Junior (ernandes@gmail.com)
 * @version 1.0
 * @since 1.0
 */
public class PropertiesRecordStore {
	/**
	 * <p>
	 * Properties hash.
	 * </p>
	 */
	Hashtable props;
	
	/**
	 * <p>
	 * Record Store in which the properties are stored.
	 * </p>
	 */
	RecordStore rsProps; 
	
	/**
	 * <p>
	 * Record's ID in which the properties data are stored.
	 * </p>
	 */
	int recordID;
	
	/**
	 * <p>
	 * Opens a given properties.
	 * </p>
	 * @param propName Record Store's name.
	 * @param createIfNecessary Create the Record Store if necessary.
	 * @return Properties object.
	 * @throws RecordStoreException Error to open the Record Store.
	 */
	public static PropertiesRecordStore open(String propName,
		boolean createIfNecessary) throws RecordStoreException {
		RecordStore rs =
			RecordStore.openRecordStore(propName, createIfNecessary);
		//
		PropertiesRecordStore p = new PropertiesRecordStore(rs);
		p.open();
		//
		return p;
	}
	
	/**
	 * <p>
	 * Constructor.
	 * </p>
	 * @param rs Record Store object.
	 */
	PropertiesRecordStore(RecordStore rs) {
		rsProps = rs;
		props = new Hashtable(5);
	}
	
	/**
	 * <p>
	 * Opens the Record Store in order to load the properties.
	 * </p>
	 * @throws PropertiesRecordStore Error to access the Record Store.
	 */
	void open() throws RecordStoreException {
        if (rsProps.getNumRecords() == 1) {
            RecordEnumeration re = rsProps.enumerateRecords(null, null, false);
            recordID = re.nextRecordId();
            byte[] record = rsProps.getRecord(recordID);
            //
            String key = null;
            String type = null;
            DataInputStream dis =
            	new DataInputStream(new ByteArrayInputStream(record));
            //
            try {
				while (!(key = dis.readUTF()).equals("eof")) {
					type = dis.readUTF();
					//
					if (type.equals("string")) {
						props.put(key, dis.readUTF());
					} else if (type.equals("string[]")) {
						String[] values = new String[dis.readInt()];
						//
						for (int i = 0; i < values.length; i++) {
							values[i] = dis.readUTF();
						}
						//
						props.put(key, values);
					} else if (type.equals("integer")) {
						props.put(key, new Integer(dis.readInt()));
					} else if (type.equals("long")) {
						props.put(key, new Long(dis.readLong()));
					} else if (type.equals("boolean")) {
						props.put(key, new Boolean(dis.readBoolean()));
					}
				}
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				re.destroy();
			}
        }
	}
	
	/**
	 * <p>
	 * Saves the changes on the properties in the Record Store.
	 * </p>
	 * @throws RecordStoreException Error to save in the Record Store.
	 */
	public void save() throws RecordStoreException {
		Enumeration keys = props.keys();
		String key = null;
		Object value = null;
		//
		ByteArrayOutputStream baos = new ByteArrayOutputStream(2048);
		DataOutputStream dos = new DataOutputStream(baos);
		//
		while (keys.hasMoreElements()) {
			key = keys.nextElement().toString();
			value = props.get(key);
			//
			try {
				dos.writeUTF(key.toString());
				//
				if (value instanceof String) {
					dos.writeUTF("string");
					dos.writeUTF(value.toString());
				} else if (value instanceof String[]) {
					String[] values = (String[])value;
					//
					dos.writeUTF("string[]");
					dos.writeInt(values.length);
					for (int i = 0; i < values.length; i++) {
						dos.writeUTF(values[i]);
					}
				}else if (value instanceof Integer) {
					dos.writeUTF("integer");
					dos.writeInt(((Integer)value).intValue());
				} else if (value instanceof Long) {
					dos.writeUTF("long");
					dos.writeLong(((Long)value).longValue());
				} else if (value instanceof Boolean) {
					dos.writeUTF("boolean");
					dos.writeBoolean(((Boolean)value).booleanValue());
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		//
		try {
			dos.writeUTF("eof");
		} catch (IOException e) {
			e.printStackTrace();
		}
		//
		byte[] data = baos.toByteArray();
		//
		if (rsProps.getNumRecords() == 0) {
			recordID = rsProps.addRecord(data, 0, data.length);
		} else {
			rsProps.setRecord(recordID, data, 0, data.length);
		}
	}
	
	/**
	 * <p>
	 * Closes the Record Store.
	 * </p>
	 */
	public void close() {
		if (rsProps != null) {
			try {
				rsProps.closeRecordStore();
			} catch (RecordStoreException e) {
			}
		}
	}
	
	/**
	 * <p>
	 * Number of properties stored.
	 * </p>
	 * @return Size.
	 */
	public int size() {
		return props.size();
	}
	
	/**
	 * <p>
	 * Puts a given string in the properties file.
	 * </p>
	 * @param key Value's key.
	 * @param value Value.
	 */
	public void putString(String key, String value) {
		props.put(key, value);
	}

	/**
	 * <p>
	 * Puts a given int in the properties file.
	 * </p>
	 * @param key Value's key.
	 * @param value Value.
	 */
	public void putInt(String key, int value) {
		props.put(key, new Integer(value));
	}
	
	/**
	 * <p>
	 * Puts a given boolean in the properties file.
	 * </p>
	 * @param key Value's key.
	 * @param value Value.
	 */
	public void putBoolean(String key, boolean value) {
		props.put(key, new Boolean(value));
	}
	
	/**
	 * <p>
	 * Puts a given long in the properties file.
	 * </p>
	 * @param key Value's key.
	 * @param value Value.
	 */
	public void putLong(String key, long value) {
		props.put(key, new Long(value));
	}
	
	/**
	 * <p>
	 * Puts a given string array in the properties file.
	 * </p>
	 * @param key Value's key.
	 * @param value Value.
	 */
	public void putStringArray(String key, String[] value) {
		props.put(key, value);
	}

	/**
	 * <p>
	 * Gets a string from the given key. If the key is not found
	 * <code>null</code> is returned.
	 * </p>
	 * @param key Value's key.
	 * @return Value.
	 */
	public String getString(String key) {
		return (String)props.get(key);
	}
	
	/**
	 * <p>
	 * Gets a int from the given key.
	 * </p>
	 * @param key Value's key.
	 * @return Value.
	 * @throws NullPointerException If the key is not found.
	 */
	public int getInt(String key) {
		return ((Integer)props.get(key)).intValue();
	}

	/**
	 * <p>
	 * Gets a boolean from the given key.
	 * </p>
	 * @param key Value's key.
	 * @return Value.
	 * @throws NullPointerException If the key is not found.
	 */
	public boolean getBoolean(String key) {
		return ((Boolean)props.get(key)).booleanValue();
	}

	/**
	 * <p>
	 * Gets a long from the given key.
	 * </p>
	 * @param key Value's key.
	 * @return Value.
	 * @throws NullPointerException If the key is not found.
	 */
	public long getLong(String key) {
		return ((Long)props.get(key)).longValue();
	}
	
	/**
	 * <p>
	 * Gets a string array from the given key. If the key is not found
	 * <code>null</code> is returned.
	 * </p>
	 * @param key Value's key.
	 * @return Value.
	 */
	public String[] getStringArray(String key) {
		return (String[])props.get(key);
	}
}