package com.echoleaf.frame.utils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

/**
 * 日期时间工具类
 * 
 * @author 何常平
 * @version 1.0
 */
public class DateUtils {

	public static final long MILLISECOND = 1;
	public static final long SECOND = MILLISECOND * 1000;
	public static final long MINUTE = SECOND * 60;
	public static final long HOUR = MINUTE * 60;
	public static final long DAY = HOUR * 24;

	public static final String TIME_OF_DATE_DEFAULT = "yyyy-MM-dd";
	public static final String TIME_OF_DATE_ZH = "yyyy年MM月dd日";
	public static final String TIME_OF_TIME_ZH = "yyyy年MM月dd日 HH时mm分 ";
	public static final String TIME_OF_HOUR_DEFAULT = "HH:mm:ss";
	public static final String TIME_OF_HOUR_HALFDAY = "HH:mm a";
	public static final String TIME_OF_FULL_DEFAULT = "yyyy-MM-dd HH:mm:ss";
	// 精确到毫秒 用于日志
	public static final String TIME_OF_FULL = "yyyy-MM-dd HH:mm:ss ssssss";
	public static final String TIME_OF_FULL_NULL = "yyyyMMddHHmmss";

	private static final String[] WEEK = { "天", "一", "二", "三", "四", "五", "六" };
	public static final String XING_QI = "星期";

	/**
	 * 将秒格式化为中国时间，最大单位为“天”
	 * 
	 * @param millis 毫秒
	 * @return 格式化为中文单位的秒时间数
	 */
	public static String formartMillisForZH_cn(long millis ) {
		StringBuilder fmtTime = new StringBuilder();
		long second = millis / SECOND;
		if ( second < 60 )
			fmtTime.append ( second + "秒钟" );
		else if ( second < 60 * 60 ) {
			fmtTime.append ( second / 60 + "分钟" );
			if ( second % 60 != 0 )
				fmtTime.append ( second % 60 + "秒钟" );
		} else if ( second < 60 * 60 * 24 ) { // 处理小时
			fmtTime.append ( second / ( 60 * 60 ) + "小时" );
			long mod = second % ( 60 * 60 );
			if ( mod != 0 ) {
				fmtTime.append ( mod / 60 + "分钟" );
				mod %= 60;
				if ( mod != 0 )
					fmtTime.append ( mod + "秒钟" );
			}
		} else {
			fmtTime.append ( second / ( 60 * 60 * 24 ) + "天" );
			long dmod = second % ( 60 * 60 * 24 );
			if ( dmod != 0 ) {
				fmtTime.append ( dmod / ( 60 * 60 ) + "小时" );
				long mod = second % ( 60 * 60 );
				if ( mod != 0 ) {
					fmtTime.append ( mod / 60 + "分钟" );
					mod %= 60;
					if ( mod != 0 )
						fmtTime.append ( mod + "秒钟" );
				}
			}
		}
		return fmtTime.toString ();
	}

	/**
	 * 解析日期String并转换为Date
	 * 
	 * @param date 字符串时间
	 *        <p>
	 *        支持大多数常用时间格式，暂不支持yyyyMMddHHmmss、HH:mm:ss、HH:mm a
	 *        </p>
	 * @return
	 */
	public static Date transformDate(String date ) {
		if ( date == null || date.trim ().isEmpty () )
			return null;
		Date result = null;
		String parse = date;
		parse = parse.replaceFirst ( "^[0-9]{4}([^0-9]?)", "yyyy$1" );
		parse = parse.replaceFirst ( "^[0-9]{2}([^0-9]?)", "yy$1" );
		parse = parse.replaceFirst ( "([^0-9]?)[0-9]{1,2}([^0-9]?)", "$1MM$2" );
		parse = parse.replaceFirst ( "([^0-9]?)[0-9]{1,2}( ?)", "$1dd$2" );
		parse = parse.replaceFirst ( "( )[0-9]{1,2}([^0-9]?)", "$1HH$2" );
		parse = parse.replaceFirst ( "([^0-9]?)[0-9]{1,2}([^0-9]?)", "$1mm$2" );
		parse = parse.replaceFirst ( "([^0-9]?)[0-9]{1,2}([^0-9]?)", "$1ss$2" );

		SimpleDateFormat format = new SimpleDateFormat( parse );

		try {
			result = format.parse ( date );
		} catch ( ParseException e ) {
			e.printStackTrace ();
		}

		return result;
	}

	/**
	 * long型日期转String日期
	 * 
	 * @param pattern 格式化字符串
	 * @param milliseconds long型毫秒数
	 * @return
	 */
	public static String transformDate(String pattern, long milliseconds ) {
		if ( pattern == null ) {
			throw new IllegalArgumentException( " pattern must can not be null " );
		}
		return transformDate ( pattern, new Date( milliseconds ) );
	}

	/**
	 * Date按格式转String
	 * 
	 * @param pattern 格式化字符串
	 * @param date Date型时间
	 * @return
	 */
	public static String transformDate(String pattern, Date date ) {
		if ( pattern == null ) {
			throw new IllegalArgumentException( " pattern must can not be null" );
		}
		if ( date == null ) {
			return "";
		}
		DateFormat df = new SimpleDateFormat( pattern );
		return df.format ( date );
	}

	/**
	 * 日期格式转换
	 * 
	 * @param pattern 字符串时间的格式
	 * @param outPattern 输出的格式化字符串
	 * @param dateStr 时间字符串
	 * @return
	 */
	public static String transformDate(String pattern, String outPattern, String dateStr ) {
		if ( dateStr == null || dateStr.trim ().isEmpty () )
			return null;
		DateFormat dd = new SimpleDateFormat( pattern );
		try {
			Date parse = dd.parse ( dateStr );
			dd = new SimpleDateFormat( outPattern );
			return dd.format ( parse );
		} catch ( ParseException e ) {
			e.printStackTrace ();
		}
		return null;
	}

	/**
	 * String转Date
	 * 
	 * @param pattern 格式化字符串
	 * @param dateStr 时间字符串
	 * @return
	 */
	public static Date transformDate(String pattern, String dateStr ) {
		DateFormat dd = new SimpleDateFormat( pattern );
		try {
			return dd.parse ( dateStr );
		} catch ( ParseException e ) {
			e.printStackTrace ();
		}
		return null;
	}

	/**
	 * 获取当前时间
	 * 
	 * @param format 格式化字符串
	 * @return
	 */
	public static String getCurrentTime(String format ) {
		return transformDate ( format, getCurrentTime () );
	}

	/**
	 * 获取当前时间
	 * 
	 * @return 默认格式为 yyyyMMddHHmmss
	 */
	public static Date getCurrentTime() {
		return new Date( System.currentTimeMillis () );
	}

	/**
	 * 获取星期
	 * 
	 * @param num 星期数
	 * @return
	 */
	public static String getWeek(int num ) {
		final Calendar c = Calendar.getInstance ();
		c.setTimeZone ( TimeZone.getTimeZone ( "GMT+8:00" ) );
		int weekNum = c.get ( Calendar.DAY_OF_WEEK ) + num;
		if ( weekNum > 7 )
			weekNum = weekNum - 7;
		return WEEK[weekNum - 1];
	}

	/**
	 * 获取两个日期的相隔天数
	 * <p>
	 * 若date1较早，则返回正数，若date2较早，则返回负数
	 * </p>
	 * <p>
	 * 若相隔不足24小时，但日期上已经过了一天，则按一天计算返回一天。如 2015-05-01 23:59:59和2015-05-02
	 * 00:00:01之间则相隔一天
	 * </p>
	 * 
	 * @since 1.2
	 * @param date1 通常为较早的一个日期
	 * @param date2 通常为较晚的一个日期
	 * @return 相隔天数
	 */
	@SuppressWarnings("deprecation")
	public static int daysBetween(Date date1, Date date2 ) {
		Calendar cal = Calendar.getInstance ();
		cal.setTime ( date1 );
		long time1 = cal.getTimeInMillis ();
		cal.setTime ( date2 );
		long time2 = cal.getTimeInMillis ();
		long between_days = ( time2 - time1 ) / DAY;
		if ( between_days == 0 ) {
			int day1 = date1.getDay ();
			int day2 = date2.getDay ();
			between_days = day2 - day1;
		}
		return Integer.parseInt ( String.valueOf ( between_days ) );
	}

	/**
	 * 获取两个日期相隔的毫秒数
	 * <p>
	 * 若date1较早，则返回正数，若date2较早，则返回负数
	 * </p>
	 * 
	 * @since 1.2
	 * @param date1 通常为较早的一个日期
	 * @param date2 通常为较晚的一个日期
	 * @return 相隔毫秒数
	 */
	public static long millisBetween(Date date1, Date date2 ) {
		Calendar cal = Calendar.getInstance ();
		cal.setTime ( date1 );
		long time1 = cal.getTimeInMillis ();
		cal.setTime ( date2 );
		long time2 = cal.getTimeInMillis ();
		return time2 - time1;
	}

	/**
	 * 日期单位
	 * 
	 * @author 何常平
	 * @version 1.3
	 */
	public enum DateUnit {
		YEAR( Calendar.YEAR), MONTH( Calendar.MONTH), DAY( Calendar.DAY_OF_YEAR), HOUR( Calendar.HOUR), MINUTE( Calendar.MINUTE), SECOND( Calendar.SECOND), MILLISECOND(
				Calendar.MILLISECOND);

		private int code;

		private DateUnit( int code ) {
			this.code = code;
		}

		public int getCode() {
			return code;
		}
	}

	/**
	 * 日期加法计算
	 * 
	 * @since 1.3
	 * @param date 参与计算的日期
	 * @param value 计算值（负数则为减法）
	 * @param dateUnit 计算单位
	 * @return
	 */
	public static Date additionDate(Date date, int value, DateUnit dateUnit ) {
		Calendar cal = Calendar.getInstance ();
		cal.setTime ( date );
		int values = cal.get ( dateUnit.getCode () );
		cal.set ( dateUnit.getCode (), values + value );
		return cal.getTime ();
	}

	/**
	 * 日期减法计算
	 * 
	 * @since 1.3
	 * @param date 参与计算的日期
	 * @param value 计算值（负数则为加法）
	 * @param dateUnit 计算单位
	 * @return
	 */
	public static Date subtraction(Date date, int value, DateUnit dateUnit ) {
		return additionDate ( date, value * ( -1 ), dateUnit );
	}

}
