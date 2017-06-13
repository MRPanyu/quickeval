package mrpanyu.quickeval;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.ParseException;

import groovy.lang.Script;

/**
 * Base class for groovy script evaluation. Contains custom functions usable in
 * script.
 */
public abstract class QuickEvalScriptBase extends Script {

	/* ---------- Math Functions ---------- */
	public Number round(Number num, int scale) {
		BigDecimal newNum = new BigDecimal(num.doubleValue());
		newNum = newNum.setScale(scale, RoundingMode.HALF_UP);
		return newNum;
	}

	public Number round(Number num) {
		return round(num, 0);
	}

	public Number ceiling(Number num, int scale) {
		BigDecimal newNum = new BigDecimal(num.doubleValue());
		newNum = newNum.setScale(scale, RoundingMode.CEILING);
		return newNum;
	}

	public Number ceiling(Number num) {
		return ceiling(num, 0).longValue();
	}

	public Number floor(Number num, int scale) {
		BigDecimal newNum = new BigDecimal(num.doubleValue());
		newNum = newNum.setScale(scale, RoundingMode.FLOOR);
		return newNum;
	}

	public Number floor(Number num) {
		return floor(num, 0).longValue();
	}

	public Number sqrt(Number num) {
		return Math.sqrt(num.doubleValue());
	}

	public Number pow(Number a, Number b) {
		return Math.pow(a.doubleValue(), b.doubleValue());
	}

	public Number power(Number a, Number b) {
		return pow(a, b);
	}

	public Number abs(Number num) {
		return Math.abs(num.doubleValue());
	}

	public Number max(Number... numbers) {
		Double max = Double.MIN_VALUE;
		for (Number n : numbers) {
			if (n.doubleValue() > max) {
				max = n.doubleValue();
			}
		}
		return max;
	}

	public Number min(Number... numbers) {
		Double min = Double.MAX_VALUE;
		for (Number n : numbers) {
			if (n.doubleValue() < min) {
				min = n.doubleValue();
			}
		}
		return min;
	}

	/* ---------- Date Functions ---------- */
	public Date date(String dateStr) throws ParseException {
		return new Date(dateStr);
	}

	public Duration year(int num) {
		return new Duration(num, 0, 0);
	}

	public Duration years(int num) {
		return year(num);
	}

	public Duration month(int num) {
		return new Duration(0, num, 0);
	}

	public Duration months(int num) {
		return month(num);
	}

	public Duration day(int num) {
		return new Duration(0, 0, num);
	}

	public Duration days(int num) {
		return day(num);
	}

	public Number day(Duration d) {
		return BigDecimal.valueOf(d.getDurationInMs() / 86400000d);
	}

	public Number day(DateDuration d) {
		return day(d.toDurationDHMS());
	}

	public Number days(Duration d) {
		return day(d);
	}

	public Number days(DateDuration d) {
		return day(d);
	}

	public Duration dhms(DateDuration d) {
		return d.toDurationDHMS();
	}

	public Duration hour(int num) {
		return new Duration(0, 0, 0, num, 0, 0, 0);
	}

	public Duration hours(int num) {
		return hour(num);
	}

	public Duration minute(int num) {
		return new Duration(0, 0, 0, 0, num, 0, 0);
	}

	public Duration minutes(int num) {
		return minute(num);
	}

	public Duration second(Number num) {
		int s = BigDecimal.valueOf(num.doubleValue()).setScale(0, RoundingMode.FLOOR).intValue();
		int ms = BigDecimal.valueOf(num.doubleValue() * 1000 - s * 1000).setScale(0, RoundingMode.FLOOR).intValue();
		return new Duration(0, 0, 0, 0, 0, s, ms);
	}

	public Duration seconds(Number num) {
		return second(num);
	}

	public Number second(Duration d) {
		return BigDecimal.valueOf(d.getDurationInMs() / 1000d).setScale(3, RoundingMode.HALF_UP);
	}

	public Number second(DateDuration d) {
		return second(d.toDurationDHMS());
	}

	public Number seconds(Duration d) {
		return second(d);
	}

	public Number seconds(DateDuration d) {
		return second(d);
	}

}
