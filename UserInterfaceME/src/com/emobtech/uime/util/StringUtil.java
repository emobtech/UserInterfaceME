/*
 * UIUtil.java
 * 15/03/2007
 * JME Framework
 * Copyright(c) Ernandes Mourao Junior (ernandes@gmail.com)
 * All rights reserved
 */
package com.emobtech.uime.util;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.Calendar;
import java.util.Date;
import java.util.Vector;

import com.emobtech.uime.midlet.MIDlet;


/**
 * <p>
 * Utility class responsible to provide some useful functions related to work
 * with strings.
 * </p>
 * 
 * @author Ernandes Mourao Junior (ernandes@gmail.com)
 * @version 1.0
 * @since 1.0
 */
public class StringUtil {
	/**
	 * <p>
	 * Format a given date to a string representation, which format will be
	 * according to the current locale code.
	 * </p>
     * @param date Date.
     * @return Formatted date.
     */
    public static final String formatDate(Date date) {
    	String localeCode = Locale.getLocaleCode();
    	Calendar c = Calendar.getInstance();
    	c.setTime(date);
    	//
    	if (StringUtil.equalsIgnoreCase(localeCode, "pt-BR")) {
        	return
        		c.get(Calendar.DAY_OF_MONTH) + "/" +
        	   (c.get(Calendar.MONTH) +1) + "/" +
        	    c.get(Calendar.YEAR);
    	} else {
        	return
        	   (c.get(Calendar.MONTH) +1) + "/" +
        	    c.get(Calendar.DAY_OF_MONTH) + "/" +
	    	    c.get(Calendar.YEAR);
    	}
    }

	/**
	 * <p>
	 * Format a given date string to a formatted date string representation, 
	 * which format will be according to the current locale code.
	 * </p>
     * @param date Date string.
     * @param format Date string format (e.g. dd/MM/yyyy, MM/dd/yyyy or yyyy/MM/dd).
     * @return Formatted date string.
     * @throws IllegalArgumentException Invalid date or format.
     */
    public static final String formatDate(String date, String format) {
    	final char dateSep = Locale.getDateSeparator();
    	String[] dateParts = StringUtil.split(date, dateSep);
    	String[] dateFormatParts = StringUtil.split(format, dateSep);
    	//
    	if (dateParts.length != dateFormatParts.length ||
    		dateParts.length != 3) {
    		throw new IllegalArgumentException(
    			"Invalid date or format. e.g. 01/01/2008 and dd/MM/yyyy.");
    	}
    	//
    	String dd = null;
    	String mm = null;
    	String yyyy = null;
    	String part = null;
    	//
    	for (int i = dateFormatParts.length -1; i >= 0; i--) {
			part = dateFormatParts[i].toLowerCase();
			//
			if ("dd".equals(part)) {
				dd = dateParts[i];
			} else if ("mm".equals(part)) {
				mm = dateParts[i];
			} else { //if ("yyyy".equals(part)) {
				yyyy = dateParts[i];
			}
		}
    	//
    	String localeCode = Locale.getLocaleCode();
    	//
    	if (StringUtil.equalsIgnoreCase(localeCode, "pt-BR")) {
        	return 
        		StringUtil.zeroPad(Integer.parseInt(dd), 2) +
        		dateSep +
        		StringUtil.zeroPad(Integer.parseInt(mm), 2) +
        		dateSep +
        		yyyy;
    	} else {
    		return
	    		StringUtil.zeroPad(Integer.parseInt(mm), 2) +
	    		dateSep +
	    		StringUtil.zeroPad(Integer.parseInt(dd), 2) +
	    		dateSep +
	    		yyyy;
    	}
    }
    
    /**
     * <p>
     * Format a given number string to a number string representation, which
     * format will be according to the current locale code.
     * </p>
     * @param number Number string. (e.g. 52189,90 or 890.99)
     * @return Formatted number string.
     */
    public static final String formatNumber(String number) {
    	char[] numChars = number.toCharArray();
    	//
    	for (int i = numChars.length -1; i >= 0; i--) {
			if (numChars[i] == '.' || numChars[i] == ',') {
				numChars[i] = Locale.getDecimalSeparator();
		    	//
		    	return new String(numChars);
			}
		}
    	//
    	return number;
    }
    
    /**
     * <p>
     * Format a given number string to a abbreviated number string 
     * representation, which format will be according to the current locale
     * code.
     * </p>
     * @param number Number string. (e.g. 52189,90 or 1890.99)
     * @return Formatted number string. (e.g. 52,18K or 1.89K)
     */
    public static final String formatAbbreviatedNumber(String number) {
    	number = formatNumber(number);
    	//
    	final char dSep = Locale.getDecimalSeparator();
    	//
    	int n = number.indexOf(dSep);
    	n = n == -1 ? number.length() : n;
    	int r = n % 3;
    	n = n / 3;
    	//
    	if (r != 0 && n > 0) {
    		String s = number.substring(r, r + 2);
    		//
    		number = number.substring(0, r);
        	//
    		if (!s.equals("00")) {
    			number += dSep + s;
    		}
    	} else if (n > 1) {
    		String s = number.substring(3, 3 + 2);
    		//
    		number = number.substring(0, 3);
        	//
    		if (!s.equals("00")) {
    			number += dSep + s;
    		}
    	}
    	//
    	n -= r == 0 ? 1 : 0;
    	//
    	switch (n) {
			case 1: return number + "K";
			case 2: return number + "M";
			case 3: return number + "B";
			case 4: return number + "T";
			default: return number;
		}
    }

    /**
     * <p>
     * Split a string based on a given delimiter.
     * </p>
     * @param str String.
     * @param delimiter Delimiter.
     * @return String tokens.
     */
    public static final String[] split(String str, char delimiter) {
        Vector v = new Vector();
        int start = 0;
        int iof;
        while ((iof = str.indexOf(delimiter, start)) != -1) {
            v.addElement(str.substring(start, iof).trim());
            start = iof +1;
        }
        v.addElement(str.substring(start, str.length()).trim());
        String[] codes = new String[v.size()];
        v.copyInto(codes);
        return codes;
    }
    
    /**
     * <p>
     * Get the string of a given resource.
     * </p>
     * @param resourcePath Resource path;
     * @param encode Resource encode.
     * @return String.
     */
    public static final String getStringFromResource(String resourcePath,
    	String encode) {
        String text = null;
        InputStream in = 
        	MIDlet.getMIDletInstance().getClass().getResourceAsStream(
        		resourcePath);
        //
        if (in != null) {
        	try {
        		int c;
        		StringBuffer str = new StringBuffer();
        		while ((c = in.read()) != -1) {
        			str.append((char)c);
        		}
				text = str.toString();
			} catch (IOException e) {
				text = e.getMessage();
			} finally {
				try {
					in.close();
				} catch (IOException e) {
				}
			}
        }
        //
        return text;
    }
    
    /**
     * <p>
     * Return a padded string with zeroes at left. 
     * </p>
     * @param n Number.
     * @param len String length.
     * @return Padded string.
     */
    public static final String zeroPad(int n, int len) {
    	String s = n + "";
    	//
    	for (int i = len - s.length(); i > 0; i--) {
            s = '0' + s;
    	}
    	//
        return s;
    }
    
    /**
     * <p>
     * Compares this String to another String, ignoring case considerations. 
     * Two strings are considered equal ignoring case if they are of the same 
     * length, and corresponding characters in the two strings are equal 
     * ignoring case.
     * </p>
     * @param str1 String 1.
     * @param str2 String to compare this String 1 against.
     * @return true if the argument 2 is not null and the Strings are equal, 
     *              ignoring case; false otherwise.
     */
    public static final boolean equalsIgnoreCase(String str1, String str2) {
    	return str1 != null && str2 != null &&
    		str1.toLowerCase().equals(str2.toLowerCase());
    }
    
    /**
     * <p>
     * Replace the parameters in the text. The parameters in the text, specified
     * by the mask {0-n}, will be replaced by the array items, according their
     * order in the array.
     * </p>
     * @param text Text.
     * @param params Parameters.
     * @return Text with all the parameters replaced.
     */
    public static final String replaceParameters(String text, Object[] params) {
        if (text == null) {
            return null;
        }
        if (params == null || params.length == 0) {
            return text;
        }
        //
        StringBuffer newMsg = new StringBuffer();
        char[] msgChars = text.toCharArray();
        int start = 0;
        int iof;
        //
        while ((iof = text.indexOf('{', start)) != -1) {
            newMsg.append(msgChars, start, iof - start);
            newMsg.append(params[Character.digit(msgChars[iof +1], 10)]);
            start = text.indexOf('}', start);
            start++;
        }
        //
        if (start < msgChars.length) {
            newMsg.append(msgChars, start, msgChars.length - start);
        }
        //
        return newMsg.toString();
    }
    
	/**
	 * <p>
	 * Verify if a given char is a space, tab, line return, etc.
	 * </p> 
	 * @param c Char.
	 * @return is space char or not.
	 */
	public static final boolean isSpaceChar(char c) {
		//\u0020
		return c == ' ' || c == '\t' || c == '\n' || c == '\r';
	}
	
    /**
     * 
     * @param s
     * @return
     */
	public static final String formatSpaceChars(String s) {
    	if (s == null) {
    		return null;
    	}
    	//
    	s = s.trim();
    	//
    	StringBuffer b = new StringBuffer();
    	char[] c = s.toCharArray();
    	boolean space = false;
    	//
    	for (int i = 0; i < c.length; i++) {
			if (isSpaceChar(c[i])) {
				if (!space) {
					b.append(' ');
					space = true;
				}
			} else {
				b.append(c[i]);
				space = false;
			}
		}
    	//
    	return b.toString();
    }
	
	/**
	 * <p>
	 * Verify if a given string is empty or null.
	 * </p>
	 * @param s String.
	 * @return Null or not.
	 */
	public static final boolean isEmptyString(String s) {
		return s == null || s.trim().length() == 0;
	}
	
	/**
	 * <p>
	 * Remove any tag occurrence from the given string.
	 * </p>
	 * @param str String to be parsed.
	 * @return String with no tags.
	 */
	public static final String removeTags(String str) {
		if (str == null) {
			return null;
		}
		//
		StringBuffer out = new StringBuffer();
		char cs[] = str.toCharArray();
		boolean tagFound = false;
		int i1 = 0;
		int l = 0;
		//
		for (int i = 0; i < cs.length; i++) {
			if (cs[i] == '<' && !tagFound) {
				out.append(cs, i1, l);
				//
				i1 = i;
				l = 0;
				tagFound = true;
				l++;
			} else if (cs[i] == '>' && tagFound) {
				i1 = i +1;
				l = 0;
				tagFound = false;
			} else {
				l++;
			}
		}
		if (l > 0) {
			out.append(cs, i1, l);
		}
		//
		return out.toString().trim();
	}
	
    /**
	 * <p>
	 * Encode a given string. If encode type is not informed, UTF-8 is
	 * considered.
	 * </p>
	 * @param s String to encode.
	 * @param enc Encode.
	 * @return Encoded string.
	 * @throws IllegalArgumentException If string is empty or null.
	 */
	public static String encode(String s, String enc) {
		if (s == null) {
			throw new IllegalArgumentException("String must not be null");
		}
		if (enc == null) {
			enc = "UTF-8";
		}
		//
		ByteArrayInputStream bIn;
		//
		try {
			bIn = new ByteArrayInputStream(s.getBytes(enc));
		} catch (UnsupportedEncodingException e) {
			bIn = new ByteArrayInputStream(s.getBytes());
		}
		//
		int c = bIn.read();
		StringBuffer ret = new StringBuffer();
		//
		while (c >= 0) {
			if ((c >= 'a' && c <= 'z')
					|| (c >= 'A' && c <= 'Z')
					|| (c >= '0' && c <= '9')
					|| c == '.'
					|| c == '-'
					|| c == '*'
					|| c == '_') {
				ret.append((char) c);
			} else if (c == ' ') {
				ret.append("%20");
			} else {
				if (c < 128) {
					ret.append(getHexChar(c));
				} else if (c < 224) {
					ret.append(getHexChar(c));
					ret.append(getHexChar(bIn.read()));
				} else if (c < 240) {
					ret.append(getHexChar(c));
					ret.append(getHexChar(bIn.read()));
					ret.append(getHexChar(bIn.read()));
				}
			}
			//
			c = bIn.read();
		}
		//
		return ret.toString();
	}
	
	/**
	 * <p>
	 * Replaces each substring of this string that matches the given search 
	 * substring with the given replacement.
	 * </p>
	 * @param text The string.
	 * @param searchStr Search string.
	 * @param replacementStr Replacement string.
	 * @return String replaced.
	 */
	public static final String replace(String text, String searchStr,
		String replacementStr) {
		StringBuffer sb = new StringBuffer();
		int searchStringPos = text.indexOf(searchStr);
		int startPos = 0;
		int searchStringLength = searchStr.length();
		//
		while (searchStringPos != -1) {
			sb.append(
				text.substring(startPos, searchStringPos)).append(
					replacementStr);
			startPos = searchStringPos + searchStringLength;
			searchStringPos = text.indexOf(searchStr, startPos);
		}
		//
		sb.append(text.substring(startPos, text.length()));
		//
		return sb.toString();
	}

	/**
	 * <p>
	 * Replaces each substring of this string that matches the given search 
	 * substring with the given replacement.
	 * </p>
	 * @param text The string.
	 * @param searchStr Search string.
	 * @param replacementStr Replacement string.
	 * @param casesensitive Case sensitive.
	 * @return String replaced.
	 */
	public static final String replaceString(String text, String searchStr,
		String replacementStr, boolean casesensitive) {
		if ((text == null) || (searchStr == null) || (replacementStr == null)) {
			return text;
		}
		//
		if (searchStr.length() == 0) {
			return text;
		}
		if (casesensitive) {
			if (searchStr.equals(replacementStr)) {
				return text;
			}
		} else {
			if (StringUtil.equalsIgnoreCase(searchStr, replacementStr)) {
				return text;
			}
		}
		int rLen = searchStr.length();
		int rwLen = replacementStr.length();
		int loc;
		//
		if (casesensitive) {
			loc = text.indexOf(searchStr);
		} else {
			loc = text.toLowerCase().indexOf(searchStr.toLowerCase());
		}
		//
		while (loc != -1) {
			text =
				text.substring(0, loc) +
				replacementStr +
				text.substring(loc + rLen);
			//
			if (casesensitive) {
				loc = text.indexOf(searchStr, loc + rwLen);
			} else {
				loc =
					text.toLowerCase().indexOf(
						searchStr.toLowerCase(),
						loc + rwLen);
			}
		}
		//
		return text;
	}
	
	/**
	 * <p>
	 * Get a hex value of a char.
	 * </p>
	 * @param c Char.
	 */
	private static String getHexChar(int c) {
		return (c < 16 ? "%0" : "%") + Integer.toHexString(c).toUpperCase();
	}

	/**
	 * <p>
     * Avoid to create instances.
     * </p>
     */
    private StringUtil() {}
}