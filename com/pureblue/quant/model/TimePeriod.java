/*******************************************************************************
 * Copyright (c) 2013 Luigi Sgro. All rights reserved. This
 * program and the accompanying materials are made available under the terms of
 * the Eclipse Public License v1.0 which accompanies this distribution, and is
 * available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Luigi Sgro - initial API and implementation
 ******************************************************************************/
package com.pureblue.quant.model;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Value type used to describe a period of time, in terms of time unit and time amount.
 */
public class TimePeriod {

	private final UnitOfTime unitOfTime;
	private final int amount;

	/**
	 * @param unitOfTime the unit in which this period is measured
	 * @param amount the amount of time units
	 */
	public TimePeriod(UnitOfTime unitOfTime, int amount) {
		super();
		this.unitOfTime = unitOfTime;
		this.amount = amount;
	}

	public UnitOfTime getUnitOfTime() {
		return unitOfTime;
	}

	public int getAmount() {
		return amount;
	}

	@Override
    public String toString() {
		return amount + " " + unitOfTime.name();
	}

	/**
	 * Adds a time period to a specific date
	 * @param date original date
	 * @param period period to be added
	 * @return a {@link java.util.Date} result
	 */
	public static Date addPeriodToDate(Date date, TimePeriod period) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		UnitOfTime unitOfTime = period.getUnitOfTime();
		cal.add(unitOfTime.getCalendarConstant(), period.getAmount() * unitOfTime.getCalendarAmount());
		return cal.getTime();
	}

	/**
	 * Subtracts a time period to a specific date
	 * @param date original date
	 * @param period period to be subtracted
	 * @return a {@link java.util.Date} result
	 */
	public static Date subtractPeriodFromDate(Date date, TimePeriod period) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		UnitOfTime unitOfTime = period.getUnitOfTime();
		cal.add(unitOfTime.getCalendarConstant(), -period.getAmount() * unitOfTime.getCalendarAmount());
		return cal.getTime();
	}

	/**
	 * Utility method to find the best approximation of a time period expressed in milliseconds,
	 * in terms of TimePeriod
	 * @param periodInMs original time period expressed in milliseconds
	 * @return a TimePeriod instance that approximates the input parameter
	 */
	public static TimePeriod findApproxPeriod(long periodInMs) {
		TimePeriod timePeriod = null;
		for (int i = UnitOfTime.values().length - 1; i >= 0; i--) {
			UnitOfTime u = UnitOfTime.values()[i];
			long quot = periodInMs / u.getDurationInMs();
			if (quot > 0) {
				timePeriod = new TimePeriod(u, (int)quot);
				break;
			}
		}
		return timePeriod;
	}
	public static Date formatStringToDate(String dateString) {
		Date wantDate = null;
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		try{
			wantDate = sdf.parse(dateString);
		}catch (Exception e) {
            e.printStackTrace();
		}
		return wantDate;
	}
	public static String formatDateToString(Date wantDate) {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		String wantDateString = formatter.format(wantDate);
		return wantDateString;
	}

	public static int getWeekOfDate(Date date) {
	    Calendar calendar = Calendar.getInstance();
	    if(date != null){
	         calendar.setTime(date);
	    }
	    int w = calendar.get(Calendar.DAY_OF_WEEK) - 1;
	    if (w < 0){
	        w = 0;
	    }
	    return w;
	}

	public static String currentYearToAbbr(){
		return new SimpleDateFormat("yyyy",Locale.CHINESE).format(Calendar.getInstance().getTime());
	}

	public static String yearToAbbr(String year){
		return year.substring(2);
	}
}

