package util;

import java.util.Date;
import java.util.concurrent.TimeUnit;

public class DateUtils {

   /**
    * Get the number of days between two dates
    * NOT completely accurate but ... it's OK for our need, don't want to use a library like Joda
    * @param date1 the oldest date
    * @param date2 the newest date
    * @return the number of days between the 2 dates
    */
    public static long getDaysBetweenDates(Date oldest, Date newest) {
        long diffInMillies = newest.getTime() - oldest.getTime();
        return TimeUnit.DAYS.convert(diffInMillies, TimeUnit.MILLISECONDS);
    }

}
