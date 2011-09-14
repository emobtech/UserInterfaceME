/*
 * DateUtil.java
 * 15/11/2007
 * JME Framework
 * Copyright(c) Ernandes Mourao Junior (ernandes@gmail.com)
 * All rights reserved
 */
package com.emobtech.uime.util;

import java.util.Date;

/**
 * <p>
 * Utility class responsible to provide some useful functions related to work
 * with dates.
 * </p>
 * 
 * @author Ernandes Mourao Junior (ernandes@gmail.com)
 * @version 1.0
 * @since 1.0
 */
public class DateUtil {
	/**
	 * <p>
	 * Calculates the difference between two dates in days.
	 * </p>
	 * @param start Start date.
	 * @param end End date.
	 * @return Days.
	 */
	public static final long calcDateDiffInDays(Date start, Date end) {
		return (end.getTime() - start.getTime()) / (1000 * 60 * 60 * 24);
	}
	
	/**
	 * <p>
	 * Verify if a given year is leap.
	 * </p>
	 * @param year Year.
	 * @return Leap or not.
	 */
	public static final boolean isLeapYear(int year) {
		if (year % 4 == 0) {
			return year % 100 != 0 ? true : year % 400 == 0;
		}
		//
		return false;
	}

	/**
	 * <p>
	 * Verify if the given date is the last day in the month.
	 * </p>
	 * @param day Day.
	 * @param month Month.
	 * @param year Year.
	 * @return Last day or not.
	 */
	public static final boolean isLastDayOfMonth(int day, int month, int year) {
		if (month == 1) { // february
			return isLeapYear(year) ? day == 29 : day == 28;
		} else {
			return is31DaysMonth(month) ? day == 31 : day == 30;
		}
	}

	/**
	 * <p>
	 * Verify if a given month has 31 days or not.
	 * </p>
	 * @param month Month.
	 * @return Has 31 days or not.
	 */
	public static final boolean is31DaysMonth(int month) {
		int[] months31 = { 0, 2, 4, 6, 7, 9, 11 };
		//
		for (int i = months31.length - 1; i >= 0; i--) {
			if (months31[i] == month) {
				return true;
			}
		}
		//
		return false;
	}


}
