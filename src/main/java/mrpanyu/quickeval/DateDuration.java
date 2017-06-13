package mrpanyu.quickeval;

import java.io.Serializable;
import java.util.Calendar;

@SuppressWarnings("serial")
public class DateDuration implements Serializable {

	private Date start;
	private Date end;

	public DateDuration(Date start, Date end) {
		this.start = start;
		this.end = end;
	}

	public Duration toDurationDHMS() {
		return new Duration(0, end.getTime() - start.getTime());
	}

	public Duration toDurationYMDHMS() {
		int sign = 1;
		Calendar cstart = Calendar.getInstance();
		cstart.setTime(start);
		Calendar cend = Calendar.getInstance();
		cend.setTime(end);
		if (start.getTime() > end.getTime()) {
			cstart.setTime(end);
			cend.setTime(start);
			sign = -1;
		}
		int months = (cend.get(Calendar.YEAR) - cstart.get(Calendar.YEAR)) * 12
				+ (cend.get(Calendar.MONTH) - cstart.get(Calendar.MONTH));
		cstart.add(Calendar.MONTH, months);
		if (cstart.getTimeInMillis() > cend.getTimeInMillis()) {
			months--;
			cstart.add(Calendar.MONTH, -1);
		}
		long milliseconds = cend.getTimeInMillis() - cstart.getTimeInMillis();
		return new Duration(sign * months, sign * milliseconds);
	}

	public Duration plus(Duration d1) {
		return d1.plus(this);
	}

	public Duration minus(Duration d1) {
		return toDurationDHMS().minus(d1);
	}

	public Duration plus(DateDuration d1) {
		return toDurationDHMS().plus(d1);
	}

	public Duration minus(DateDuration d1) {
		return toDurationDHMS().minus(d1);
	}

	public String toString() {
		return toDurationYMDHMS().toString();
	}

	public Date getStart() {
		return start;
	}

	public void setStart(Date start) {
		this.start = start;
	}

	public Date getEnd() {
		return end;
	}

	public void setEnd(Date end) {
		this.end = end;
	}

	@Override
	public int hashCode() {
		return toDurationDHMS().hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		DateDuration other = (DateDuration) obj;
		return this.toDurationDHMS().equals(other.toDurationDHMS());
	}

}
