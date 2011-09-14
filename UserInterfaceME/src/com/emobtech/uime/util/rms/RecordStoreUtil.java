/*
 * RMSUtil.java
 * Feb 24, 2008
 * JME Framework
 * Copyright(c) Ernandes Mourao Junior (ernandes@gmail.com)
 * All rights reserved
 */
package com.emobtech.uime.util.rms;

import java.util.Vector;

import javax.microedition.rms.RecordComparator;
import javax.microedition.rms.RecordEnumeration;
import javax.microedition.rms.RecordFilter;
import javax.microedition.rms.RecordStore;
import javax.microedition.rms.RecordStoreException;

/**
* <p>
* </p>
* 
* @author Ernandes Mourao Junior (ernandes@gmail.com)
* @version 1.0
* @since 1.0
*/
public class RecordStoreUtil {
	/**
	 * 
	 */
	private Class entityType;
	
	/**
	 * 
	 */
	protected RecordStore rstore;
	
	/**
	 * 
	 * @param recordStoreName
	 * @param createIfNecessary
	 * @param serializableRecordEntityClass
	 * @return
	 * @throws RecordStoreException
	 */
	public static RecordStoreUtil open(String recordStoreName,
		boolean createIfNecessary, Class serializableRecordEntityClass)
			throws RecordStoreException {
//		if (!SerializableRecord.class.isAssignableFrom(
//				serializableRecordEntityClass)) {
//			throw new IllegalArgumentException(
//				"serializableRecordEntityClass argument must be a valid " +
//				"SerializableRecord type.");
//		}
		//
		return new RecordStoreUtil(
			RecordStore.openRecordStore(recordStoreName, createIfNecessary),
			serializableRecordEntityClass);
	}
	
	/**
	 * <p>
	 * </p>
	 * @param rstore
	 * @param serializableRecordEntityClass
	 */
	protected RecordStoreUtil(RecordStore rstore,
		Class serializableRecordEntityClass) {
		this.rstore = rstore;
		this.entityType = serializableRecordEntityClass;
	}
	
	/**
	 * <p>
	 * </p>
	 * @throws RecordStoreException
	 */
	public void close() throws RecordStoreException {
		rstore.closeRecordStore();
	}
	
	/**
	 * <p>
	 * </p>
	 * @param comparator
	 * @return
	 * @throws RecordStoreException
	 */
	public Object[] getAllRecords(RecordComparator comparator)
		throws RecordStoreException {
		return populateArray(rstore.enumerateRecords(null, comparator, false));
	}
	
	/**
	 * <p>
	 * </p>
	 * @param filter
	 * @param comparator
	 * @return
	 * @throws RecordStoreException
	 */
	public Object[] findRecords(RecordFilter filter,
		RecordComparator comparator)throws RecordStoreException {
		return populateArray(rstore.enumerateRecords(filter, comparator,false));
	}
	
	/**
	 * <p>
	 * </p>
	 * @param entity
	 * @throws RecordStoreException
	 */
	public void addRecord(SerializableRecord entity)
		throws RecordStoreException {
		byte[] data = entity.serialize();
		//
		entity.setRecordID(rstore.addRecord(data, 0, data.length));
	}
	
	/**
	 * <p>
	 * </p>
	 * @param entity
	 * @throws RecordStoreException 
	 */
	public void setRecord(SerializableRecord entity)
		throws RecordStoreException {
		byte[] data = entity.serialize();
		//
		rstore.setRecord(entity.getRecordID(), data, 0, data.length);
	}

	/**
	 * <p>
	 * </p>
	 * @param entity
	 * @throws RecordStoreException 
	 */
	public void deleteRecord(SerializableRecord entity)
		throws RecordStoreException {
		rstore.deleteRecord(entity.getRecordID());
	}
	
	/**
	 * <p>
	 * </p>
	 * @param records
	 * @return
	 */
	protected Object[] populateArray(RecordEnumeration records)
		throws RecordStoreException {
		Vector recsVec = new Vector(rstore.getNumRecords());
		//
		SerializableRecord entity;
		int recordID;
		//
		while (records.hasNextElement()) {
			try {
				entity = (SerializableRecord)entityType.newInstance();
				//
				recordID = records.nextRecordId();
				entity.deserialize(rstore.getRecord(recordID));
				entity.setRecordID(recordID);
				//
				recsVec.addElement(entity);
			} catch (InstantiationException e) {
			} catch (IllegalAccessException e) {
			}
		}
		//
		Object[] recsArray = new Object[recsVec.size()];
		recsVec.copyInto(recsArray);
		//
		return recsArray;
	}
}