/*
* RSSFeedParser.java
* May 10, 2008
* JME Framework
* Copyright(c) Ernandes Mourao Junior (ernandes@gmail.com)
* All rights reserved
*/
package com.emobtech.uime.feed;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.kxml.Attribute;
import org.kxml.Xml;
import org.kxml.parser.ParseEvent;
import org.kxml.parser.XmlParser;

import com.emobtech.uime.util.I18N;
import com.emobtech.uime.util.StringUtil;


/**
 * <p>
 * </p>
 * 
 * @author Ernandes Mourao Junior (ernandes@gmail.com)
 * @version 1.0
 * @since 1.0
 */
public class FeedParser {
	/**
	 * 
	 */
	static final String RSS_TAG = "rss";

	/**
	 * 
	 */
	static final String ATOM_TAG = "feed";
	
	/**
	 * 
	 */
	static final String ENTRY_TAG = "entry";

	/**
	 * 
	 */
	static final String ITEM_TAG = "item";

	/**
	 * 
	 */
	static final String TITLE_TAG = "title";
	
	/**
	 * 
	 */
	static final String LINK_TAG = "link";

	/**
	 * 
	 */
	static final String SUMMARY_TAG = "summary";
	
	/**
	 * 
	 */
	static final String DESCRIPTION_TAG = "description";
	
	/**
	 * 
	 */
	static final String SUB_TITLE_TAG = "subtitle";

	/**
	 * 
     * @param is
     * @return
     * @throws IOException
     */
    public Feed parse(InputStream is) throws IOException {
        try {
            XmlParser parser = new XmlParser(new InputStreamReader(is));
            ParseEvent event;
            String tagName = "";
            int tagType;
            //
            boolean entryOpen = false;
            Feed feed = new Feed();
            FeedItem item = null;
            //
            parser.skip();
            //
            do {
            	event = parser.read();
            	if (event == null) {
            		continue;
            	}
            	//
            	if (event.getName() != null) {
            		tagName = event.getName().toLowerCase();
            	}
            	tagType = event.getType();
            	//
            	if (tagType == Xml.START_TAG) {
            		if (tagName.equals(ENTRY_TAG) || tagName.equals(ITEM_TAG)) {
            			item = new FeedItem();
            			feed.addItem(item);
            			entryOpen = true;
            		} else if (tagName.equals(LINK_TAG)) {
            			Attribute attr = event.getAttribute("href");
            			if (attr != null) {
                    		if (entryOpen) {
                    			item.setLink(attr.getValue().trim());
                    		} else {
                    			feed.setLink(attr.getValue().trim());
                    		}
            			}
                	}
            	} else if (tagType == Xml.TEXT) {
            		if (tagName.equals(TITLE_TAG)) {
                		if (entryOpen) {
                			item.setTitle(
                				StringUtil.removeTags(event.getText().trim()));
                		} else {
                			feed.setTitle(
                				StringUtil.removeTags(event.getText().trim()));
                		}
                	} else if (tagName.equals(LINK_TAG)) {
                		if (entryOpen) {
                			item.setLink(event.getText().trim());
                		} else {
                			feed.setLink(event.getText().trim());
                		}
                	} else if (tagName.equals(SUB_TITLE_TAG)) {
                		feed.setDescription(event.getText().trim());
                	} else if (tagName.equals(DESCRIPTION_TAG)) {
                		if (entryOpen) {
                    		item.setDescription(
                    			StringUtil.removeTags(event.getText().trim()));
                		} else {
                    		feed.setDescription(
                    			StringUtil.removeTags(event.getText().trim()));
                		}
                	} else if (tagName.equals(SUMMARY_TAG)) {
                    	item.setDescription(
                    		StringUtil.removeTags(event.getText().trim()));
                	}
            	} else if (tagType == Xml.END_TAG) {
            		if (tagName.equals(ATOM_TAG) || tagName.equals(RSS_TAG)) {
            			break;
            		} else if (tagName.equals(ENTRY_TAG)
            			|| tagName.equals(ITEM_TAG)) {
            			entryOpen = false;
            		}
            	}
            } while (true);
            //
            feed.setRefreshDate(System.currentTimeMillis());
            //
            return feed;
        } catch (IOException e) {
           	throw new IOException(
           		I18N.getFramework("framework.jme.rss.error.io"));
        } catch (Exception e) {
        	throw new IOException(
           		I18N.getFramework("framework.jme.rss.invalidrssfeed"));
		} catch (OutOfMemoryError e) {
			throw new IOException(
				I18N.getFramework("framework.jme.rss.outofmemory"));
		}
    }
}