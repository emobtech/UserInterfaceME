/*
 * I18N.java
 * 03/10/2007
 * JME Framework
 * Copyright(c) Ernandes Mourao Junior (ernandes@gmail.com)
 * All rights reserved
 */
package com.emobtech.uime.util;


/**
 * <p>
 * This class implements a mechanism to access the application resources 
 * that are internationalized.
 * </p>
 * <p>
 * If an application wants to provide a resource file to hold all the strings
 * that are internationalized, it must put all these string in the file
 * application-resources-<locale-code>.properties at the root directory.
 * </p>
 * <p>
 * The file to be loaded will depend on the current application locale. For
 * example, if the current locale is en-US, the following file will be loaded,
 * application-resources-en-US.properties.
 * </p>
 * 
 * @author Ernandes Mourao Junior (ernandes@gmail.com)
 * @version 1.0
 * @since 1.0
 */
public class I18N {
	/**
	 * <p>
	 * Framework's default properties resource.
	 * </p>
	 */
	static Properties frameworkProperties;

	/**
	 * <p>
	 * Application's default properties resource.
	 * </p>
	 */
	static Properties appProperties;
	
	static {
		String code = 
			Locale.getClosestSupportedLocaleCode(Locale.getLocaleCode());
		//
		if (code == null) {
			code = "pt-BR";
		}
		//
		Locale.setLocaleCode(code);
	}

	/**
	 * Gets the value that is associated to the given key in the framework 
	 * properties file. If the key does not exist, <code>null</code> will be 
	 * returned.
	 * </p>
	 * @param key Key.
	 * @return Value.
	 */
	public static final String getFramework(String key) {
		return get(key, frameworkProperties, null);
	}

	/**
	 * Gets the value that is associated to the given key in the application 
	 * properties file. If the key does not exist, <code>null</code> will be 
	 * returned.
	 * </p>
	 * @param key Key.
	 * @return Value.
	 */
	public static final String getApp(String key) {
		return get(key, appProperties, null);
	}
	
	/**
	 * Gets the value that is associated to the given key in the application 
	 * properties file. If the key does not exist, <code>null</code> will be 
	 * returned.
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
	public static final String getApp(String key, Object[] params) {
		return get(key, appProperties, params);
	}

	/**
	 * <p>
	 * Gets the value that is associated to the given key in the given 
	 * properties object. If the key does not exist, <code>null</code> will be 
	 * returned.
	 * </p>
	 * @param key Key.
	 * @param prop Properties object.
	 * @param params Parameters.
	 * @return Value.
	 */
	static final synchronized String get(String key, Properties prop,
		Object[] params) {
		return prop != null ? prop.getProperty(key, params) : null;
	}
	
	/**
	 * <p>
	 * Loads the framework and application properties according to the given
	 * locale code.
	 * </p>
	 * @param localeCode Code.
	 */
	static final synchronized void loadProperties(String localeCode) {
		frameworkProperties = 
			new Properties(
				"/i18n/framework-resources-" + localeCode + ".properties");
		appProperties = 
			new Properties(
				"/i18n/application-resources-" + localeCode + ".properties");
		//
		if (frameworkProperties.size() == 0) {
			frameworkProperties = null;
		}
		if (appProperties.size() == 0) {
			appProperties = null;
		}
	}
}