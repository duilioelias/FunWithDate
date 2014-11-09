package br.com.luiszeni.funwithdateoperation;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;

/** This class contains basic date operations to sum a value to a date, subtract a value to a date, convert a string date to long and convert a long to string date.
 * @author Luis Zeni
 * @version 1.2
 */
public class DateOperations {

	//Some constants to help in the calculations
	public static final int NUMBER_OF_MINUTES_IN_HOUR = 60;
	public static final int NUMBER_OF_HOURS_IN_DAY = 24;
	public static final int NUMBER_OF_DAYS_IN_YEAR = 365;
	public static final int NUMBER_OF_MONTHS_IN_YEAR = 12;
	public static final int NUMBER_OF_MINUTES_IN_DAY = NUMBER_OF_HOURS_IN_DAY * NUMBER_OF_MINUTES_IN_HOUR;
	public static final int NUMBER_OF_MINUTES_IN_YEAR = NUMBER_OF_DAYS_IN_YEAR * NUMBER_OF_MINUTES_IN_DAY;
	public static final int [] NUMBER_DAYS_IN_EACH_MONTH  = {31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};

	////values of the nonlinear functions in the date. It will help calculate the month number and day number in the month
	private int daysInAYearByMonthFunction [] = new int[NUMBER_OF_DAYS_IN_YEAR]; 
	private int daysInAYearByDayFunction [] = new int[NUMBER_OF_DAYS_IN_YEAR]; 
	private int cumulatedDaysInEachMonth [] = new int[12]; 
	
	/**
	 * This constructor only calculates the arrays that help to calculate the nonlinear functions in the date. This will reduce redundant calculation.
	 * As it has always the same value, is better to put it as a constant, but i'll do after.
	 */
	public DateOperations() {
		int k = 0, m =0;
		for (int i = 0; i < NUMBER_OF_MONTHS_IN_YEAR; i++) {
			cumulatedDaysInEachMonth[i] = k;
			for (int j = 0; j < NUMBER_DAYS_IN_EACH_MONTH[i]; j++) {
				daysInAYearByMonthFunction[k] = i+1;
				daysInAYearByDayFunction[k] = m+1;
				k++;
				m++;
			}
			m = 0;
		}
	}
	
	/**
	 * This Method add or subtract a long value in minutes from a date.
	 *     Example:
	 *         changeDate("01/03/2010 23:00", '+', 4000) = "04/03/2010 17:40"
	 *         
	 * @param date An date as String in the format dd/MM/yyyy HH24:mi
	 * @param op Can be only + | -
	 * @param value the value that should be incremented/decremented. It will be expressed in minutes
	 * @return the new date in String format “d/MM/yyyy HH24:mi
	 * @throws DateOperationException if the operation is wrong
	 * @throws DateFormatException if something wrong with dd/MM/yyy hh:mm format
	 */
	public String changeDate(String date, char op, long value) throws DateOperationException, DateFormatException{

		//Validate if op has a valid character (+ or -).
		if(!(op == '+' || op == '-')){ //If not, it throws an exception informing the wrong character and the valid ones.
			throw(new DateOperationException(op));
		}
		
		//Convert the date to long in minutes to add or sub the informed value.
		long dateAsLong = convertStringDateToLongInMinutes(date);

		//Add or sub the value to the date and return it in String format.
		if(op == '+')
			return convertLongInMinutesToStringDate(dateAsLong + value);
		else
			return convertLongInMinutesToStringDate(dateAsLong - value);
	}
	
	/**
	 * This method converts a long to date in String format 01/01/1970 00:00
	 * @param dateInMinutes the date as long
	 * @return date in String format 01/01/1970 00:00
	 */
	public String convertLongInMinutesToStringDate(long dateInMinutes){
		
		//year, hour and minutes are all linear equations and solve then problem with them with following equations
		int year =  (int) Math.floor((double)dateInMinutes / NUMBER_OF_MINUTES_IN_YEAR) + 1970;//normalizes with 1970
		int hour = (int)  floorMod(((double)dateInMinutes)/NUMBER_OF_MINUTES_IN_HOUR,NUMBER_OF_HOURS_IN_DAY);
		int minute = (int) floorMod(dateInMinutes,NUMBER_OF_MINUTES_IN_HOUR);
		
		//As day and the month are nonlinear equations, first i calculate the number of days in a year. After, i use this value to recovery the real value of the month and day.
		int dayOfYear = (int) floorMod(((double)dateInMinutes)/NUMBER_OF_MINUTES_IN_DAY,NUMBER_OF_DAYS_IN_YEAR);
		//recover the day and month values from the pre-calculated functions.
		int day = daysInAYearByDayFunction[dayOfYear];
		int month = daysInAYearByMonthFunction[dayOfYear];
		
		//Thats it, now the values are formated and the date can be returned as String.
		DecimalFormat decimalFormat = new DecimalFormat("00");
		DecimalFormat decimalFormat2 = new DecimalFormat("0000");
		
		return decimalFormat.format(day) + "/" +decimalFormat.format(month) + "/" + decimalFormat2.format(year) + " " + decimalFormat.format(hour) + ":" + decimalFormat.format(minute) ;	
	}
		
	
	
	/**
	 * This method converts a String date to long value. As in the java time, i'm assuming that the long value 0 is referent to 01/01/1970 00:00
	 * @param date a String in dd/MM/yyy hh:mm format
	 * @return a date in long format, where each unit is expressed in minutes
	 * @throws DateFormatException if something wrong with dd/MM/yyy hh:mm format
	 */
	public long convertStringDateToLongInMinutes(String date) throws DateFormatException{
		
		Map<String, Integer> dateVals = validateDateFormatAndParseValues(date);
		
		int normalizedYear = dateVals.get("year") - 1970; //subtracts 1970 from year of the date, this operation is made to normalize the date to 01/01/1970 00:00.
		
		// Calculates the total number of minutes in the normalized year.
		long numberOfMinutesYear = normalizedYear * NUMBER_OF_MINUTES_IN_YEAR;
	
		//As each month can have 28, 30 or 31 days, first is necessary calculate the total of days from the beginning of the year to the current month.
		//To Recovery this value I use the values from the pre-calculated functions.
		//Calculates the total number of minutes in the current month using  the total of days from the beginning of the year to the current month.
		long numberOfMinutesmonth  = cumulatedDaysInEachMonth[dateVals.get("month")-1] *NUMBER_OF_MINUTES_IN_DAY;
		
		//Calculates the total number of minutes in the current day.
		long numberOfMinutesDay = (dateVals.get("day")-1)*NUMBER_OF_MINUTES_IN_DAY;
		
		//Calculates the total number of minutes in the current hour.
		long numberOfMinutesHour = dateVals.get("hour")*NUMBER_OF_MINUTES_IN_HOUR;
		
		//Finally all the calculated minutes are summed, and this is the final long value of the date.
		return numberOfMinutesYear + numberOfMinutesmonth  + numberOfMinutesDay + numberOfMinutesHour + dateVals.get("minute");
	}
	
	/**
	 * 	This method verify if a string date in dd/MM/yyy hh:mm format is correct and convert it to a Map with the splitted values 
	 * @param date a String in dd/MM/yyy hh:mm format
	 * @return a Map<String, Integer> where the keys can recover the day, month , year, hour and minute from the date. 
	 * @throws DateFormatException if something wrong with dd/MM/yyy hh:mm format
	 */
	private Map<String, Integer> validateDateFormatAndParseValues(String date) throws DateFormatException{

		//assuming the dd/MM/yyy hh:mm format, the date is split in two pieces using the blank space (" ") in the date. 
		String [] value = date.split(" ");

		if(value.length != 2) // if there are not two pieces, it's an invalid date.
			throw(new DateFormatException("Invalid date format, use dd/MM/yyy hh:mm format"));
			
		//now the date piece and the time piece are splitted.
		String [] dateValues = value[0].split("/");
		String [] timeValues = value[1].split(":");
		
		if(dateValues.length != 3)// if the date has less than 3 pieces, it is an invalid date.
			throw(new DateFormatException("Invalid date format, use dd/MM/yyy"));
		
		if(timeValues.length != 2) // if the time has less than 2 pieces, it is an invalid date.
			throw(new DateFormatException("Invalid time format, use hh:mm"));
		
		//in this map the date values will be stored and returned.
		Map<String, Integer> dateVals = new HashMap<String, Integer>();
		
		try {
			//Now the values of date and time are parsed to int and sotred each one in the map with its correspondent key.
			dateVals.put("day", Integer.parseInt(dateValues[0]));
			dateVals.put("month", Integer.parseInt(dateValues[1]));
			dateVals.put("year", Integer.parseInt(dateValues[2]));
				
			dateVals.put("hour", Integer.parseInt(timeValues[0]));
			dateVals.put("minute", Integer.parseInt(timeValues[1]));
		} catch (NumberFormatException e) {//If a NumberFormatException is throw, some of the values isn't a number.
			throw(new DateFormatException("Some of the values informed in the date isn't a number"));
		}	
				
		//Verify if the values of the date are inside the correct thresholds
		if(dateVals.get("month") < 1 || dateVals.get("month") > NUMBER_OF_MONTHS_IN_YEAR)
			throw(new DateFormatException("the month value it's out of the bounds [1 12]"));
		
		//the total of days in a month can vary from 28,30 to 31, so i use the array NUMBER DAYS IN EACH MONTH to help in the task of getting the total number of days in a month.
		if(dateVals.get("day") < 1 || dateVals.get("day") > NUMBER_DAYS_IN_EACH_MONTH [dateVals.get("month")-1])
			throw(new DateFormatException("the day value it's out of the bounds [1 " + NUMBER_DAYS_IN_EACH_MONTH [dateVals.get("month")-1] +  "]"));
		
		if(dateVals.get("hour") < 0 || dateVals.get("hour") > NUMBER_OF_HOURS_IN_DAY -1)
			throw(new DateFormatException("the hour value it's out of the bounds [00 23]"));
		
		if(dateVals.get("minute")  < 0 || dateVals.get("minute")  > NUMBER_OF_MINUTES_IN_HOUR-1)
			throw(new DateFormatException("the minute value it's out of the bounds [00 59]"));

		return dateVals;
	}
		
	/**
	 * Calculates the floor mod from x and y. It was implemented in java8 only: https://docs.oracle.com/javase/8/docs/api/java/lang/Math.html#floorMod-int-int- .
	 * But i'm using java7. So I implemented my own function. If you want more information floored mod read this article: https://en.wikipedia.org/wiki/Modulo_operation
	 * @param x
	 * @param y
	 * @return
	 */
	public int floorMod(double x, double y){
		return (int)Math.floor(((x % y) + y) % y);
	}
	
}
