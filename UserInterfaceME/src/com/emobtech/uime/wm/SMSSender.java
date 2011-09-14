/*
 * SMSSender.java
 * 05/12/2006
 * JME Framework
 * Copyright(c) Ernandes Mourao Junior (ernandes@gmail.com)
 * All rights reserved
 */
package com.emobtech.uime.wm;

import java.io.IOException;
import java.util.Vector;

import javax.microedition.io.Connector;
import javax.wireless.messaging.MessageConnection;
import javax.wireless.messaging.TextMessage;

import com.emobtech.uime.io.ConnectionListener;


/**
 * <p>
 * </p>
 * 
 * @author Ernandes Mourao Junior (ernandes@gmail.com)
 * @version 1.0
 * @since 1.0
 */
public class SMSSender implements Runnable {
	/**
	 * 
	 */
	protected Vector addressList;
	
	/**
	 * 
	 */
	protected String messageText;
	
	/**
	 * 
	 */
	protected ConnectionListener connListener;
	
	/**
	 * 
	 */
	public SMSSender() {
		addressList = new Vector();
	}
	
	/**
	 * 
	 * @param address
	 */
	public void addAddress(String address) {
		if (address == null || (address = address.trim()).length() == 0) {
			throw new IllegalArgumentException(
				"Address cannot be null or empty.");
		}
		//
		if (!address.startsWith("sms://")) {
			address = "sms://" + address;
		}
		//
		addressList.addElement(address);
	}
	
	/**
	 * 
	 * @param text
	 */
	public void setText(String text) {
		if (text == null || (text = text.trim()).length() == 0) {
			throw new IllegalArgumentException(
				"Message text cannot be null or empty.");
		}
		//
		messageText = text;
	}
	
	/**
	 * 
	 * @param connListener
	 */
	public void setConnectionListener(ConnectionListener listener) {
		connListener = listener;
	}
	
	/**
	 * 
	 */
	public void send() {
		if (addressList.size() == 0) {
			throw new IllegalStateException(
				"The message has no recipient address assigned.");
		}
		if (messageText == null) {
			throw new IllegalStateException(
				"The message has no content assigned.");
		}
		//
		Thread t = new Thread(this);
		t.start();
	}
	
	/**
	 * 
	 */
	public void clear() {
		addressList.removeAllElements();
		messageText = null;
	}
	
	/**
	 * @see java.lang.Runnable#run()
	 */
	public void run() {
		MessageConnection conn;
		String address;
		TextMessage txtMsg;
		//
		for (int i = addressList.size() -1; i >= 0; i--) {
			conn = null;
			address = addressList.elementAt(i).toString();
			//
			try {
				conn = (MessageConnection)Connector.open(address);
				txtMsg =
					(TextMessage)conn.newMessage(
						MessageConnection.TEXT_MESSAGE);
				//
		    	txtMsg.setAddress(address);
		    	txtMsg.setPayloadText(messageText);
		    	//
		    	conn.send(txtMsg);
		        //
		        if (connListener != null) {
		        	connListener.onSuccess(address);
		        }
			} catch (IOException e) {
	            if (connListener != null) {
	            	connListener.onFail(address, e);
	            }
			} catch (SecurityException e) {
	            if (connListener != null) {
	            	connListener.onFail(address, e);
	            }
			} catch (OutOfMemoryError e) {
	            if (connListener != null) {
	            	connListener.onFail(address, null);
	            }
			} finally {
				if (conn != null) {
					try {
						conn.close();
					} catch (IOException e) {
					}
				}
			}
		}
	}
}