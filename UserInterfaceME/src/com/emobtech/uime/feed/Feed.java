/*
* RSSFeed.java
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
import java.util.Vector;

import com.emobtech.uime.util.rms.SerializableRecord;


/**
 * <p>
 * </p>
 * 
 * @author Ernandes Mourao Junior (ernandes@gmail.com)
 * @version 1.0
 * @since 1.0
 */
public class Feed implements SerializableRecord  {
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
    private Vector items = new Vector();
    
    /**
     * 
     */
    private int recordID = -1;
    
    /**
     * 
     */
    private long refreshDate;
    
    /**
     * 
     */
    public Feed() {
    	//do not remove it.
    }

    /**
     * 
     * @param title
     * @param desc
     * @param link
     */
    public Feed(String title, String desc, String link) {
        this.title = title;
        this.desc = desc;
        this.link = link;
    }
    
    /**
     * 
     * @param feed
     */
    public Feed(Feed feed) {
        title = feed.title;
        desc = feed.desc;
        link = feed.link;
        items = feed.items;
        recordID = feed.recordID;
        refreshDate = feed.refreshDate;
    }

    /**
     * 
     * @param item
     */
    public void addItem(FeedItem item) {
    	items.addElement(item);
    }
    
    /**
     * 
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
     * 
     * @return
     */
    public FeedItem[] getItems() {
    	FeedItem[] aItems = new FeedItem[items.size()];
    	items.copyInto(aItems);
    	//
        return aItems;
    }
    
    /**
     * @return
     */
    public long getRefreshDate() {
    	return refreshDate;
    }
    
    /**
     * @param items
     */
    public void setItems(FeedItem[] items) {
    	this.items.removeAllElements();
    	//
    	for (int i = items.length -1; i >= 0; i--) {
    		this.items.addElement(items[i]);
		}
    }

	/**
	 * 
	 * @param title
	 */
	public void setTitle(String title) {
		this.title = title;
	}

	/**
	 * 
	 * @param desc
	 */
	public void setDescription(String desc) {
		this.desc = desc;
	}

	/**
	 * 
	 * @param link
	 */
	public void setLink(String link) {
		this.link = link;
	}

	/**
	 * 
	 * @param date
	 */
	public void setRefreshDate(long date) {
		refreshDate = date;
	}

	/**
	 * @see com.emobtech.uime.util.rms.SerializableRecord#getRecordID()
	 */
	public int getRecordID() {
		return recordID;
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
			link = dis.readUTF();
			link = "".equals(link) ? null : link;
			title = dis.readUTF();
			title = "".equals(title) ? null : title;
			desc = dis.readUTF();
			desc = "".equals(desc) ? null : desc;
			refreshDate = dis.readLong();
			//
			int itemsCount = dis.readInt(); //items count;
			int itemLen;
			byte[] buf = new byte[1024];
			//
			for (int i = 0; i < itemsCount; i++) {
				itemLen = dis.readInt(); //item data length.
				if (itemLen > buf.length) {
					buf = new byte[itemLen];
				}
				//
				dis.read(buf, 0, itemLen);
				items.addElement(new FeedItem().deserialize(buf));
			}
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
		ByteArrayOutputStream bos = new ByteArrayOutputStream(1024);
		DataOutputStream dos = new DataOutputStream(bos);
		//
		try {
			dos.writeUTF(link == null? "" : link);
			dos.writeUTF(title == null? "" : title);
			dos.writeUTF(desc == null? "" : desc);
			dos.writeLong(refreshDate);
			//
			FeedItem[] items = getItems();
			byte[] buf;
			//
			dos.writeInt(items.length); //items count.
			//
			for (int i = 0; i < items.length; i++) {
				buf = items[i].serialize();
				//
				dos.writeInt(buf.length); //buffer length.
				dos.write(buf, 0, buf.length);
			}
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