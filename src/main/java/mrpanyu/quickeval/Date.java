package mrpanyu.quickeval;

import java.text.ParseException;
import java.util.Calendar;

import org.apache.commons.lang.time.DateFormatUtils;
import org.apache.commons.lang.time.DateUtils;

@SuppressWarnings("serial")
public class Date extends java.util.Date {

	public Date() {
		super();
	}

	public Date(long date) {
		super(date);
	}

	public Date(String dateStr) throws ParseException {
		super();
		java.util.Date d = DateUtils.parseDate(dateStr,
				new String[] { "yyyy-MM-dd HH:mm:ss.SSS", "yyyy-MM-dd HH:mm:ss", "yyyy-MM-dd HH:mm", "yyyy-MM-dd" });
		setTime(d.getTime());
	}

	public Date plus(Duration d) {
		Calendar c = Calendar.getInstance();
		c.setTime(this);
		c.add(Calendar.MONTH, d.months);
		c.add(Calendar.DATE, (int) (d.milliseconds / 86400000));
		c.add(Calendar.MILLISECOND, (int) (d.milliseconds % 86400000));
		return new Date(c.getTimeInMillis());
	}

	public Date plus(DateDuration d) {
		return plus(d.toDurationDHMS());
	}

	public Date minus(Duration d) {
		Duration dr = new Duration(-d.months, -d.milliseconds);
		return plus(dr);
	}

	public DateDuration minus(Date d) {
		return new DateDuration(d, this);
	}

	public String toString() {
		String str = DateFormatUtils.format(this, "yyyy-MM-dd HH:mm:ss.SSS");
		if (str.endsWith(" 00:00:00.000")) {
			str = str.substring(0, 10);
		}
		return str;
	}

}
