package com.github.programmerr47.vkdiscussionviewer.utils;

import android.text.format.DateFormat;

import com.github.programmerr47.vkdiscussionviewer.R;

import java.util.Calendar;
import java.util.Date;

import static com.github.programmerr47.vkdiscussionviewer.utils.AndroidUtils.string;
import static java.util.Calendar.DAY_OF_YEAR;
import static java.util.Calendar.YEAR;

/**
 * @author Michael Spitsin
 * @since 2016-08-01
 */
public class DateUtils {

    private static final String TIME_FORMAT = "k:mm";
    private static final String DAY_IN_YEAR_FORMAT = "EEE, d MMMM, k:mm";
    private static final String DEFAULT_FORMAT = "dd MMMM yyyy, k:mm";

    private static Calendar source = Calendar.getInstance();

    //TODO make formats for english
//    private static final String TIME_FORMAT = "h:mm aa";
//    private static final String DAY_IN_YEAR_FORMAT = "EEE, MMMM d, h:mm aa";
//    private static final String DEFAULT_FORMAT = "MMMM dd yyyy, h:mm aa";

    private static final String SPACE = " ";

    private DateUtils() {}

    public static String formatDate(long dateSeconds) {
        return formatDateMillis(dateSeconds * 1000);
    }

    public static String formatDateMillis(long dateMillis) {
        source.setTimeInMillis(dateMillis);
        Calendar now = Calendar.getInstance();

        return capitalizeFistLetter(formatDateCompareTo(source, now));
    }

    private static String formatDateCompareTo(Calendar sourceDate, Calendar comparedDate) {
        if (sourceDate.get(YEAR) == comparedDate.get(YEAR)) {
            if (sourceDate.get(DAY_OF_YEAR) == comparedDate.get(DAY_OF_YEAR)) {
                return string(R.string.today) + SPACE + DateFormat.format(TIME_FORMAT, sourceDate);
            } else if (comparedDate.get(DAY_OF_YEAR) - sourceDate.get(DAY_OF_YEAR) == 1) {
                return string(R.string.yesterday) + SPACE + DateFormat.format(TIME_FORMAT, sourceDate);
            } else if (sourceDate.get(DAY_OF_YEAR) - comparedDate.get(DAY_OF_YEAR) == 1) {
                return string(R.string.tomorrow) + SPACE + DateFormat.format(TIME_FORMAT, sourceDate);
            } else {
                return DateFormat.format(DAY_IN_YEAR_FORMAT, sourceDate).toString();
            }
        } else {
            return DateFormat.format(DEFAULT_FORMAT, sourceDate).toString();
        }
    }

    private static String capitalizeFistLetter(String sentence) {
        return sentence.substring(0, 1).toUpperCase() + sentence.substring(1);
    }

    public static int compareDatesByDay(long previousInSeconds, long currentInSeconds) {
        long previous = previousInSeconds * 1000;
        long current = currentInSeconds * 1000;

        source.setTimeInMillis(previous);
        int day1 = source.get(Calendar.DAY_OF_YEAR);
        int year1 = source.get(Calendar.YEAR);
        source.setTimeInMillis(current);
        int day2 = source.get(Calendar.DAY_OF_YEAR);
        int year2 = source.get(Calendar.YEAR);

        if (year1 != year2) {
            return year1 > year2 ? 1 : -1;
        } else if (day1 != day2) {
            return day1 > day2 ? 1 : -1;
        }

        return 0;
    }
}
