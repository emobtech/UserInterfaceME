/*
* DownloadManager.java
* Feb 24, 2008
* JME Framework
* Copyright(c) Ernandes Mourao Junior (ernandes@gmail.com)
* All rights reserved
*/
package com.emobtech.uime.io;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Hashtable;

import javax.microedition.io.Connector;
import javax.microedition.io.HttpConnection;

/**
 * <p>
 * </p>
 * 
 * @author Ernandes Mourao Junior (ernandes@gmail.com)
 * @version 1.0
 * @since 1.0
 */
public class DownloadManager {
	/**
	 * 
	 */
	protected Hashtable downloadData;
	
	/**
	 * 
	 */
	protected ConnectionListener connListener;
	
	/**
	 * 
	 */
	public DownloadManager() {
		downloadData = new Hashtable();
	}
	
	/**
	 * 
	 * @param url
	 */
	public void start(String url) {
		DownloadRunner runner = new DownloadRunner();
		runner.url = url;
		//
		downloadData.put(url, runner);
		//
		Thread t = new Thread(runner);
		t.start();
	}
	
	/**
	 * 
	 * @param url
	 */
	public void stop(String url) {
		((DownloadRunner)downloadData.get(url)).toStop = true;
	}

	/**
	 * 
	 * @param url
	 * @return
	 */
	public byte[] getData(String url) {
		return ((DownloadRunner)downloadData.get(url)).data;
	}
	
	/**
	 * 
	 * @param listener
	 */
	public void setConnectionListener(ConnectionListener listener) {
		this.connListener = listener;
	}
	
	/**
	 * 
	 */
	public void clear() {
		downloadData.clear();
		//
		System.gc();
	}
	
	/**
	 * <p>
	 * </p>
	 * 
	 * @author Ernandes Mourao Junior (ernandes@gmail.com)
	 * @version 1.0
	 * @since 1.0
	 */
	protected class DownloadRunner implements Runnable {
		/**
		 * 
		 */
		public String url;
		
		/**
		 * 
		 */
		public boolean toStop;
		
		/**
		 * 
		 */
		public byte[] data;
			
		/**
		 * @see java.lang.Runnable#run()
		 */
		public void run() {
			HttpConnection conn = null;
			InputStream in = null;
			//
			try {
				conn = (HttpConnection)Connector.open(url);
				conn.setRequestMethod(HttpConnection.GET);
				//
				if (conn.getResponseCode() != HttpConnection.HTTP_OK) {
					//this error mask cannot be changed. it is used to identify
					//the real error during the connection establishing.
					throw new IOException("" + conn.getResponseCode());
				}
				//
				final int length = (int)conn.getLength();
				ByteArrayOutputStream buf =
					new ByteArrayOutputStream(length != -1 ? length : 2048);
	            int ch;
				//
				in = conn.openInputStream();
	            //
	            while ((ch = in.read()) != -1) {
	            	buf.write(ch);
	            	//
	            	if (toStop) {
	            		return; //download stopped...
	            	}
	            }
	            //
	            data = buf.toByteArray();
	            //
				if (connListener != null) {
					connListener.onSuccess(url);
				}
			} catch (IllegalArgumentException e) {
				if (connListener != null) {
					connListener.onFail(url, e);
				}
			} catch (IOException e) {
				if (connListener != null) {
					connListener.onFail(url, e);
				}
			} catch (SecurityException e) {
				if (connListener != null) {
					connListener.onFail(url, e);
				}
			} catch (OutOfMemoryError e) {
				if (connListener != null) {
					connListener.onFail(url, null);
				}
			} finally {
				if (in != null) {
					try {
						in.close();
					} catch (IOException e) {
					}
				}
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