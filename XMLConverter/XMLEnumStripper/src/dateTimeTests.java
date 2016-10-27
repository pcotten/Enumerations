import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.joda.time.DateTime;
import org.joda.time.Days;
import org.joda.time.Months;

public class dateTimeTests {

	public static void main(String[] args) throws ParseException {
		SimpleDateFormat dateFormat = new SimpleDateFormat("MM-dd-yyyy");
		DateTime date = new DateTime(dateFormat.parse("2-28-2016"));
		int difBetweenDays = Months.monthsBetween(date, new DateTime()).getMonths();
		int difBetweenDaysRev = Months.monthsBetween(new DateTime(), date).getMonths();
		System.out.println(difBetweenDays + ", " + difBetweenDaysRev);
	}

}
