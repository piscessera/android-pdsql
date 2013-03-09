package com.piscessera.pdsql.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtil {
	public static String convertFormat2StringFromString(SimpleDateFormat fmt,
			String dateString) {
		String result = "";
		try {
			Date date = fmt.parse(dateString);
			result = String.valueOf(date.getTime());
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return result;
	}

	public static long convertFormat2LongFromString(SimpleDateFormat fmt,
			String dateString) {
		long result = 0L;
		try {
			Date date = fmt.parse(dateString);
			result = date.getTime();
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return result;
	}

	public static long convertFormat2LongFromString(SimpleDateFormat fmt,
			Date date) {
		return convertFormat2LongFromString(fmt, String.valueOf(date.getTime()));
	}

	public static String convertFormat2StringFromString(SimpleDateFormat fmt,
			Date date) {
		return convertFormat2StringFromString(fmt,
				String.valueOf(date.getTime()));
	}
}
