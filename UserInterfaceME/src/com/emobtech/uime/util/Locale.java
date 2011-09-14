/*
 * Locale.java
 * 24/03/2007
 * JME Framework
 * Copyright(c) Ernandes Mourao Junior (ernandes@gmail.com)
 * All rights reserved
 */
package com.emobtech.uime.util;

import com.emobtech.uime.midlet.MIDlet;

/**
 * <p>
 * This class is reponsible for managing the framework information regarding
 * locale. All default information is retrieved from the mobile device.
 * </p>
 * 
 * @author Ernandes Mourao Junior (ernandes@gmail.com)
 * @version 1.0
 * @since 1.0
 */
public class Locale {
	/**
	 * <p>
	 * Current locale code.
	 * </p>
	 */
	static String localeCode;
	
	/**
	 * <p>
	 * Gets the current locale code.
	 * </p>
	 * @return Code.
	 */
	public static final String getLocaleCode() {
		return localeCode != null 
			? localeCode 
			: System.getProperty("microedition.locale");
	}
	
	/**
	 * <p>
	 * Sets the current locale code. When this method is called, all the 
	 * i18n resources are reloaded according to the new locale code.
	 * </p>
	 * <p>
	 * If <code>null</code> is passed as argument, the default locale code
	 * from the device is used.
	 * </p>
	 */
	public static final void setLocaleCode(String code) {
		if (code != null && !code.equals(localeCode)) {
			localeCode = code;
			//
			I18N.loadProperties(code);
		}
	}

	/**
     * <p>
     * Returns all the locale codes supported by the application. These codes
     * are specified in the application descriptor property
     * "br-framework-jme-util-supported-locale-codes", separated by comma.
     * </p> 
     * @return Codes.
     */
    public static final String[] getSupportedLocaleCodes() {
    	String locales = 
    		MIDlet.getMIDletInstance().getAppProperty(
    			"br-framework-jme-util-supported-locale-codes");
    	//
        if (locales != null && !locales.trim().equals("")) {
            return StringUtil.split(locales, ',');
        } else {
            return new String[0];
        }
    }
    
    /**
     * <p>
     * Returns the closest locale code to that one passed as parameter. This
     * code is compared to those ones returned by the method 
     * getSupportedLocaleCodes(). Otherwise, if itself is in the list, it will
     * be returned. If no match is found, <code>null</code> will be returned.
     * </p>
     * @param locale Locale code. Ex: en-GB, pt-BR, es-ES.
     * @return Code.
     */
    public static final String getClosestSupportedLocaleCode(String locale) {
        String[] supLocales = getSupportedLocaleCodes();
        String pref = locale.substring(0, 2); //locale prefix.
        String closest = null;
        //
        for (int i = 0; i < supLocales.length; i++) {
            if (StringUtil.equalsIgnoreCase(supLocales[i], locale)) {
                return locale;
            } else if (supLocales[i].startsWith(pref)) {
                closest = supLocales[i];
            }
        }
        //
        return closest;
    }
	
	/**
	 * <p>
	 * Get the default date format.
	 * </p>
	 * @return Format.
	 */
	public static final String getDateFormat() {
    	String localeCode = Locale.getLocaleCode();
    	//
    	if (StringUtil.equalsIgnoreCase(localeCode, "pt-BR")) {
    		return "dd/MM/yyyy";
    	} else {
    		return "MM/dd/yyyy";
    	}
	}

	/**
	 * <p>
	 * Gets the default date separator.
	 * </p>
	 * @return Separator.
	 */
	public static final char getDateSeparator() {
		return '/';
	}
	
	/**
	 * <p>
	 * Gets the default date separator.
	 * </p>
	 * @return Separator.
	 */
	public static final char getDecimalSeparator() {
    	if (StringUtil.equalsIgnoreCase(Locale.getLocaleCode(), "pt-BR")) {
    		return ',';
    	} else {
    		return '.';
    	}
	}
}