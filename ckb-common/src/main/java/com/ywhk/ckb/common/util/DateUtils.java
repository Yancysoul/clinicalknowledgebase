package com.ywhk.ckb.common.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

public final class DateUtils {

	public static LocalDate UDateToLocalDate(Date date) {
	    Instant instant = date.toInstant();
	    ZoneId zone = ZoneId.systemDefault();
	    LocalDateTime localDateTime = LocalDateTime.ofInstant(instant, zone);
	    return localDateTime.toLocalDate();
	}
	public static String getDateStr(Date date,String format){
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);
		return simpleDateFormat.format(date);
	}
	public static boolean isSameDay(Date date1,Date date2){
		return getDateStr(date1,"yyyyMMdd").equals(getDateStr(date2,"yyyyMMdd"));
	}
	public static String timeToString(Date date) {
        DateFormat bf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//多态
        return bf.format(date);
	}
	
	public static String DateToString(Date date) {
        DateFormat bf = new SimpleDateFormat("yyyy-MM-dd");//多态
        return bf.format(date);
	}
	
	public static Date StringToDate(String dateString) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        return sdf.parse(dateString);
	}
	
	public static Date StringToDate(String dateString,String format) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        return sdf.parse(dateString);
	}
	
	
	public static Date StringToTime(String dateString) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sdf.parse(dateString);
	}
	public static Date StringToTime(String dateString ,String format) throws ParseException {
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		return sdf.parse(dateString);
	}
}
