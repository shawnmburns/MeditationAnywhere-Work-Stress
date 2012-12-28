package com.vl.common;

import java.util.Date;
import java.util.TimeZone;

public class VLDate implements Comparable<VLDate> {
	
	private long _ticks = 0;

	public VLDate() {
	}
	
	public VLDate(long ticks) {
		_ticks = ticks;
	}
	
	public VLDate(int year, int mon, int day) {
		_ticks = VLDate.getTicksFromYear(year, mon, day);
	}
	
	public VLDate(int year, int mon, int day, int hours, int mins) {
		_ticks = VLDate.getTicksFromYear(year, mon, day);
		_ticks += hours * kTicksPerHour + mins * kTicksPerMinute;
	}
	
	public VLDate(int year, int mon, int day, int hours, int mins, int seconds) {
		_ticks = VLDate.getTicksFromYear(year, mon, day);
		_ticks += hours * kTicksPerHour + mins * kTicksPerMinute + seconds * kTicksPerSecond;
	}
	
	public VLDate(int year, int mon, int day, int hours, int mins, int seconds, TimeZone tz) {
		_ticks = VLDate.getTicksFromYear(year, mon, day);
		_ticks += hours * kTicksPerHour + mins * kTicksPerMinute + seconds * kTicksPerSecond;
		if(tz != null) {
			int offsetMS = tz.getOffset((_ticks - kTicksUntil1970) / kTicksPerMillisecond);
			_ticks -= offsetMS * kTicksPerMillisecond;
		}
	}
	
	public VLDate(int year, int mon, int day, TimeZone tz) {
		_ticks = VLDate.getTicksFromYear(year, mon, day);
		if(tz != null) {
			int offsetMS = tz.getOffset((_ticks - kTicksUntil1970) / kTicksPerMillisecond);
			_ticks -= offsetMS * kTicksPerMillisecond;
		}
	}
	
	public VLDate(Date other) {
		_ticks = (long)other.getTime()*kTicksPerMillisecond + kTicksUntil1970;
	}
	
	private static final int _daysToMonth365[] = new int[]{ 0, 0x1f, 0x3b, 90, 120, 0x97, 0xb5, 0xd4, 0xf3, 0x111, 0x130, 0x14e, 0x16d };
	private static final int _daysToMonth366[] = new int[]{ 0, 0x1f, 60, 0x5b, 0x79, 0x98, 0xb6, 0xd5, 0xf4, 0x112, 0x131, 0x14f, 0x16e };
	private static final int _daysPerYear365 = 365;
	@SuppressWarnings("unused")
	private static final int _daysPerYear366 = 366;
	
	private int getDatePart(int part, TimeZone tz) {
		long ticks = _ticks;
		if(tz != null) {
			int offsetMS = tz.getOffset((_ticks - kTicksUntil1970) / kTicksPerMillisecond);
			ticks += offsetMS * kTicksPerMillisecond;
		}
		int num = (int) (ticks / kTicksPerDay);
		int num2 = num / 0x23ab1;
		num -= num2 * 0x23ab1;
		int num3 = num / 0x8eac;
		if (num3 == 4) {
			num3 = 3;
		}
		num -= num3 * 0x8eac;
		int num4 = num / 0x5b5;
		num -= num4 * 0x5b5;
		int num5 = num / 0x16d;
		if (num5 == 4) {
			num5 = 3;
		}
		if (part == 0) {
			return (((((num2 * 400) + (num3 * 100)) + (num4 * 4)) + num5) + 1);
		}
		num -= num5 * 0x16d;
		if (part == 1) {
			return (num + 1);
		}
		final int[] numArray = ((num5 == 3) && ((num4 != 0x18) || (num3 == 3))) ? _daysToMonth366 : _daysToMonth365;
		int index = num >> 6;
		while (num >= numArray[index]) {
			index++;
		}
		if (part == 2) {
			return index;
		}
		return ((num - numArray[index - 1]) + 1);
	}
	
	@SuppressWarnings("unused")
	private int getDatePart(int part) {
		return this.getDatePart(part, null);
	}
	
	public static final long kTicksPerMillisecond = ((long)10000);
	public static final long kTicksPerSecond = ((long)10000000);
	public static final long kTicksPerMinute = ((long)(kTicksPerSecond * 60));
	public static final long kTicksUntil1970 = ((long)(kTicksPerMinute * 1035593280));

	public static final long kTicksPerHour = ((long)(kTicksPerMinute * 60));
	public static final long kTicksPerDay = ((long)(kTicksPerHour * 24));
	public static final long kHoursPerDay = 24;
	public static final long kMinutesPerHour = 60;
	public static final long kSecondsPerMinute = 60;
	public static final long kSecondsPerHour = 3600;
	public static final long kMillisecondsPerSecond = 1000;
	public static final long kMillisecondsPerHour = (kMillisecondsPerSecond*kSecondsPerHour);
	private static final String kWeekDaysNames[] = new String[] {"Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday"};
	private static final String kMonthsNames[] = new String[] {"January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"};
	
	public long getTicks() {
		return _ticks;
	}
	
	public void setTicks(long ticks) {
		_ticks = ticks;
	}
	
	public int getYear(TimeZone tz) {
		int res = this.getDatePart(0, tz);
		return res;
	}
	
	public int getYear() {
		return getYear(null);
	}

	public int getMonth(TimeZone tz) {
		int res = this.getDatePart(2, tz);
		return res;
	}
	
	public int getMonth() {
		return getMonth(null);
	}

	public int getDay(TimeZone tz) {
		int res = this.getDatePart(3, tz);
		return res;
	}
	
	public int getDay() {
		return getDay(null);
	}

	public int getHours(TimeZone tz) {
		long ticks = _ticks;
		if(tz != null) {
			int offsetMS = tz.getOffset((_ticks - kTicksUntil1970) / kTicksPerMillisecond);
			ticks += offsetMS * kTicksPerMillisecond;
		}
		return (int) ((ticks / kTicksPerHour) % kHoursPerDay);
	}
	
	public int getHours() {
		return this.getHours(null);
	}

	public int getMinutes(TimeZone tz) {
		long ticks = _ticks;
		if(tz != null) {
			int offsetMS = tz.getOffset((_ticks - kTicksUntil1970) / kTicksPerMillisecond);
			ticks += offsetMS * kTicksPerMillisecond;
		}
		return (int) ((ticks / kTicksPerMinute) % kMinutesPerHour);
	}
	
	public int getMinutes() {
		return this.getMinutes(null);
	}

	public int getSeconds(TimeZone tz) {
		long ticks = _ticks;
		if(tz != null) {
			int offsetMS = tz.getOffset((_ticks - kTicksUntil1970) / kTicksPerMillisecond);
			ticks += offsetMS * kTicksPerMillisecond;
		}
		return (int) ((ticks / kTicksPerSecond) % kSecondsPerMinute);
	}
	
	public int getSeconds() {
		return this.getSeconds(null);
	}

	public int getMilliSeconds(TimeZone tz) {
		long ticks = _ticks;
		if(tz != null) {
			int offsetMS = tz.getOffset((_ticks - kTicksUntil1970) / kTicksPerMillisecond);
			ticks += offsetMS * kTicksPerMillisecond;
		}
		return (int) (ticks / kTicksPerMillisecond % kMillisecondsPerSecond);
	}
	
	public int getMilliSeconds() {
		return this.getMilliSeconds(null);
	}
	
	public long getMilliSecondsSince1970() {
		long res = _ticks - kTicksUntil1970;
		res = res / kTicksPerMillisecond;
		return res;
	}
	
	public void setMilliSecondsSince1970(long value) {
		_ticks = kTicksUntil1970 + value * kTicksPerMillisecond;
	}
	
	public int getDayOfYear() {
		int year = this.getYear();
		int mon = this.getMonth();
		int day = this.getDay();
		boolean isLeap = getIsLeapYear(year);
		int daysPlus = 0;
		if(mon >= 2)
			daysPlus += 31;
		if(mon >= 3)
			daysPlus += isLeap ? 29 : 28;
		if(mon >= 4)
			daysPlus += 31;
		if(mon >= 5)
			daysPlus += 30;
		if(mon >= 6)
			daysPlus += 31;
		if(mon >= 7)
			daysPlus += 30;
		if(mon >= 8)
			daysPlus += 31;
		if(mon >= 9)
			daysPlus += 31;
		if(mon >= 10)
			daysPlus += 30;
		if(mon >= 11)
			daysPlus += 31;
		if(mon >= 12)
			daysPlus += 30;
		int dayOfYear = day + daysPlus;
		return dayOfYear;
	}

	public static boolean getIsLeapYear(int year) {
		if ((year % 4) != 0)
			return false;
		if ((year % 100) == 0)
			return ((year % 400) == 0);
		return true;
	}
	
	public int getDayOfWeek() {
		int day = this.getDay();
		int month = this.getMonth();
		int year = this.getYear();
		int a = (int)Math.floor((double)(14 - month) / 12);
		int y = year - a;
		int m = month + 12 * a - 2;
		int d = (day + y + (int)Math.floor((double)y / 4) - (int)Math.floor((double)y / 100) +
			(int)Math.floor((double)y / 400) + (int)Math.floor((double)(31 * m) / 12)) % 7;
		return d + 1;
	}
	
	public String getDayOfWeekAsString() {
		int weekday = this.getDayOfWeek();
		String res;
		if(weekday <= 0 || weekday > kWeekDaysNames.length)
			res = "";
		else
			res = kWeekDaysNames[weekday - 1];
		return res;
	}
	
	public String getMonthAsString() {
		int month = this.getMonth();
		String res;
		if(month <= 0 || month > kMonthsNames.length)
			res = "";
		else
			res = kMonthsNames[month - 1];
		return res;
	}
	
	public String getDayWithPostfix() {
		int day = this.getDay();
		switch (day)
		{
			case 1: return "1st";
			case 2: return "2nd";
			case 3: return "3rd";
		}
		return day + "th";
	}
	
	public static long getTicksFromYear(int year, int mon, int day) {
		if (((year >= 1) && (year <= 0x270f)) && ((mon >= 1) && (mon <= 12))) {
			final int[] numArray = VLDate.getIsLeapYear(year) ? _daysToMonth366 : _daysToMonth365;
	        if ((day >= 1) && (day <= (numArray[mon] - numArray[mon - 1]))) {
				int num = year - 1;
				int num2 = ((((((num * _daysPerYear365) + (num / 4)) - (num / 100)) + (num / 400)) + numArray[mon - 1]) + day) - 1;
				return (num2 * kTicksPerDay);
			}
		}
		return 0;
	}

	@Override
	public int compareTo(VLDate another) {
		if(_ticks > another.getTicks())
			return 1;
		else if(_ticks < another.getTicks())
			return -1;
		return 0;
	}
	
	public static VLDate getCurrent() {
		Date curDate = new Date();
		VLDate res = new VLDate(curDate);
		return res;
	}
	
	public Date getJavaDate() {
		Date res = new Date();
		res.setTime((_ticks - kTicksUntil1970) / kTicksPerMillisecond);
		return res;
	}

	@Override
	public boolean equals(Object o) {
		if(o == null)
			return false;
		if(o.getClass() != this.getClass())
			return false;
		VLDate otherDate = (VLDate)o;
		return (this.getTicks() == otherDate.getTicks());
	}
	
	@Override
	public String toString() {
		String res = String.format("%04d-%02d-%02d %02d:%02d:%02d.%03d +0000", getYear(), getMonth(), getDay(),
				getHours(), getMinutes(), getSeconds(), getMilliSeconds());
		return res;
	}
}
