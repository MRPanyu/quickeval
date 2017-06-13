package mrpanyu.quickeval;

import java.io.Serializable;

import org.apache.commons.lang.StringUtils;

@SuppressWarnings("serial")
public class Duration implements Serializable {

	protected int months;
	protected long milliseconds;

	public Duration(int years, int months, int days) {
		this(years, months, days, 0, 0, 0, 0);
	}

	public Duration(int years, int months, int days, int hours, int minutes, int seconds, int milliseconds) {
		this.months = years * 12 + months;
		this.milliseconds = days * 86400000L + hours * 3600000L + minutes * 60000L + seconds * 1000L + milliseconds;
	}

	protected Duration(int months, long milliseconds) {
		this.months = months;
		this.milliseconds = milliseconds;
	}

	public Date plus(Date d) {
		return d.plus(this);
	}

	public Duration plus(Duration d1) {
		Duration d2 = new Duration(0, 0, 0);
		d2.months = this.months + d1.months;
		d2.milliseconds = this.milliseconds + d1.milliseconds;
		return d2;
	}

	public Duration plus(DateDuration d1) {
		return plus(d1.toDurationDHMS());
	}

	public Duration minus(Duration d1) {
		Duration d2 = new Duration(0, 0, 0);
		d2.months = this.months - d1.months;
		d2.milliseconds = this.milliseconds - d1.milliseconds;
		return d2;
	}

	public Duration minus(DateDuration d1) {
		return minus(d1.toDurationDHMS());
	}

	public String toString() {
		StringBuilder sb = new StringBuilder();
		if (months > 0) {
			sb.append(getYears()).append("Y ");
			sb.append(getMonths()).append("M ");
		} else if (months < 0) {
			sb.append("-");
			sb.append(-getYears()).append("Y ");
			sb.append(-getMonths()).append("M ");
		}
		if (milliseconds > 0) {
			if (months < 0) {
				sb.append("+");
			}
			sb.append(getDays()).append("D ");
			sb.append(getHours()).append("h ");
			sb.append(getMinutes()).append("m ");
			sb.append(getSeconds());
			int ms = getMilliseconds();
			if (ms != 0) {
				sb.append(".").append(StringUtils.leftPad(String.valueOf(ms), 3, '0'));
			}
			sb.append("s");
		} else if (milliseconds < 0) {
			if (months >= 0) {
				sb.append("-");
			}
			sb.append(-getDays()).append("D ");
			sb.append(-getHours()).append("h ");
			sb.append(-getMinutes()).append("m ");
			sb.append(-getSeconds());
			int ms = -getMilliseconds();
			if (ms != 0) {
				sb.append(".").append(StringUtils.leftPad(String.valueOf(ms), 3, '0'));
			}
			sb.append("s");
		}
		return sb.toString();
	}

	public int getYears() {
		return months / 12;
	}

	public int getMonths() {
		return months % 12;
	}

	public int getDays() {
		return (int) (milliseconds / 86400000);
	}

	public int getHours() {
		return (int) (milliseconds % 86400000 / 3600000);
	}

	public int getMinutes() {
		return (int) (milliseconds % 3600000 / 60000);
	}

	public int getSeconds() {
		return (int) (milliseconds % 60000 / 1000);
	}

	public int getMilliseconds() {
		return (int) (milliseconds % 1000);
	}

	public long getDurationInMs() {
		if (months != 0) {
			throw new IllegalStateException("Cannot get as milliseconds with months");
		}
		return milliseconds;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (milliseconds ^ (milliseconds >>> 32));
		result = prime * result + months;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Duration other = (Duration) obj;
		if (milliseconds != other.milliseconds)
			return false;
		if (months != other.months)
			return false;
		return true;
	}

}
