/*
* RSSItem.java
* May 10, 2008
* JME Framework
* Copyright(c) Ernandes Mourao Junior (ernandes@gmail.com)
* All rights reserved
*/
package com.emobtech.uime.feed;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import com.emobtech.uime.util.rms.SerializableRecord;


/**
 * <p>
 * </p>
 * 
 * @author Ernandes Mourao Junior (ernandes@gmail.com)
 * @version 1.0
 * @since 1.0
 */
public class FeedItem implements SerializableRecord {
    /**
     * 
     */
    private String title;
    
    /**
     * 
     */
    private String desc;
    
    /**
     * 
     */
    private String link;
    
    /**
     * 
     */
    private int recordID = -1;

    /**
     * 
     */
    public FeedItem() {
    	//do not remove it.
    }
    
    /**
     * @param title
     * @param link
     * @param desc
     */
    public FeedItem(String title, String desc, String link) {
    	this.title = title;
    	this.desc  = desc;
    	this.link  = link;
    }
    
    /**
     * @return
     */
    public String getTitle() {
        return title;
    }
 
    /**
     * 
     * @return
     */
    public String getDescription() {
        return desc;
    }

    /**
     * 
     * @return
     */
    public String getLink() {
        return link;
    }
    
	/**
	 * @see com.emobtech.uime.util.rms.SerializableRecord#getRecordID()
	 */
	public int getRecordID() {
		return recordID;
	}

	/**
	 * @param title
	 */
	public void setTitle(String title) {
		this.title = title;
	}

	/**
	 * @param desc
	 */
	public void setDescription(String desc) {
		this.desc = desc;
	}

	/**
	 * @param link
	 */
	public void setLink(String link) {
		this.link = link;
	}

	/**
	 * @see com.emobtech.uime.util.rms.SerializableRecord#setRecordID(int)
	 */
	public void setRecordID(int id) {
		recordID = id;
	}

	/**
	 * @see com.emobtech.uime.io.Serializable#deserialize(byte[])
	 */
	public Object deserialize(byte[] stream) {
		DataInputStream dis =
			new DataInputStream(new ByteArrayInputStream(stream));
		//
		try {
			title = dis.readUTF();
			title = "".equals(title) ? null : title;
			desc = dis.readUTF();
			desc = "".equals(desc) ? null : desc;
			link = dis.readUTF();
			link = "".equals(link) ? null : link;
		} catch (IOException e) {
			throw new IllegalStateException(
				"Invalid stream values: " + getClass().getName());
		}
		//
		return this;
	}

	/**
	 * @see com.emobtech.uime.io.Serializable#serialize()
	 */
	public byte[] serialize() {
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		DataOutputStream dos = new DataOutputStream(bos);
		//
		try {
			dos.writeUTF(title == null? "" : title);
			dos.writeUTF(desc == null? "" : desc);
			dos.writeUTF(link == null? "" : link);
			//
			return bos.toByteArray();
		} catch (IOException e) {
		}
		//
		throw new IllegalStateException(
			"Invalid bean values: " + getClass().getName());
	}
	
	/**
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		return title;
	}
}